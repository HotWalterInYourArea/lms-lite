package com.example.lmslite.service;

import com.example.lmslite.dto.course.list.CourseResponse;
import com.example.lmslite.dto.enrollment.detail.EnrollmentDetailResponse;
import com.example.lmslite.dto.enrollment.list.EnrollmentResponse;
import com.example.lmslite.dto.enrollment.list.EnrollmentResponseList;
import com.example.lmslite.dto.enrollment.progress.complete.LessonProgressResponse;
import com.example.lmslite.dto.enrollment.progress.complete.ProgressResponse;
import com.example.lmslite.dto.enrollment.progress.quiz_results.QuizResult;
import com.example.lmslite.dto.enrollment.progress.quiz_results.QuizResultList;
import com.example.lmslite.dto.enrollment.progress.submit.QuizResultResponse;
import com.example.lmslite.dto.enrollment.progress.submit.SubmitQuizRequest;
import com.example.lmslite.enums.EnrollmentStatus;
import com.example.lmslite.exception.conflict.AlreadyEnrolledException;
import com.example.lmslite.exception.conflict.CourseNotPublishedException;
import com.example.lmslite.exception.conflict.EnrollmentNotActiveException;
import com.example.lmslite.exception.notFound.*;
import com.example.lmslite.exception.conflict.QuizAlreadyPassedException;
import com.example.lmslite.exception.unprocessable.LessonNotInCourseException;
import com.example.lmslite.exception.unprocessable.UnsupportedLessonTypeException;
import com.example.lmslite.mapper.*;
import com.example.lmslite.model.*;
import com.example.lmslite.repository.*;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class EnrollmentService
{
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final LessonRepository lessonRepository;
    private final LessonProgressRepository lessonProgressRepository;
    private final QuizAttemptRepository quizAttemptRepository;
    private final CertificateRepository certificateRepository;

    private void addCompletedLesson(Enrollment enrollment, LessonProgress newLessonProgress)
    {
        enrollment.getCompletedLessons().add(newLessonProgress);
        if (enrollment.getProgressPercent() == 100)
        {
            enrollment.setStatus(EnrollmentStatus.COMPLETED);
            enrollment.setCompletedAt(LocalDateTime.now());
            if (certificateRepository.existsByStudentIdAndCourseIdNot(enrollment.getStudent().getId(), enrollment.getCourse().getId()))
            {
                Certificate newCertificate = CertificateMapper.toEntity(enrollment);
                certificateRepository.save(newCertificate);
            }
        }

    }

    @Transactional
    public CourseResponse enrollStudent(Long studentId, Long courseId)
    {
        Course courseToEnroll = courseRepository.findWithLessonsById(courseId).orElseThrow(
                () -> new CourseNotFoundException(courseId));

        if (!courseToEnroll.isAvailableForEnrollment())
        {
            throw new CourseNotPublishedException("Course " + courseId + "is not published");
        }

        Student studentToEnroll = studentRepository.findById(studentId).orElseThrow(
                () -> new StudentNotFoundException(studentId));

        if (enrollmentRepository.existsByStudentIdAndCourseId(studentId, courseId))
        {
            throw new AlreadyEnrolledException("Student " + studentId +
                                               " is already enrolled in course" +
                                               courseId);
        }
        Enrollment newEnrollment = EnrollmentMapper.toEntity(courseToEnroll, studentToEnroll);
        enrollmentRepository.save(newEnrollment);
        return CourseMapper.toResponse(courseToEnroll);
    }

    @Transactional(readOnly = true)
    public EnrollmentResponseList getStudentEnrollments(Long studentId)
    {
        if(!studentRepository.existsById(studentId))
        {
                throw new StudentNotFoundException(studentId);
        }
        List<Enrollment> studentEnrollments = enrollmentRepository.findByStudentId(studentId);
        return EnrollmentMapper.toResponseList(studentEnrollments);
    }

    @Transactional
    public ProgressResponse completeLesson(Long enrollmentId, Long lessonId)
    {
        Enrollment enrollment = enrollmentRepository.findWithCourseAndLessonProgressById(enrollmentId).orElseThrow(
                () -> new EnrollmentNotFoundException(enrollmentId));

        if(enrollment.getStatus() != EnrollmentStatus.ACTIVE)
        {
            throw new EnrollmentNotActiveException("Enrollment " + enrollmentId + " is not active");
        }

        Lesson lessonToComplete = lessonRepository.findById(lessonId).orElseThrow(
                () -> new LessonNotFoundException(lessonId));


        if(!Objects.equals(enrollment.getCourse().getId(), lessonToComplete.getCourse().getId()))
        {
            throw new LessonNotInCourseException("Lesson " + lessonId +
                                                 " does not belong to course" + enrollment.getCourse().getId());
        }

        if (lessonToComplete instanceof QuizLesson)
        {
            throw new UnsupportedLessonTypeException("Quizzes are processed by separate endpoint");
        }

        List<LessonProgressResponse> lessonProgressResponses = lessonRepository.findLessonProgressResponse(enrollmentId, enrollment.getCourse().getId());

        if(lessonProgressRepository.existsByLessonIdAndEnrollmentId(lessonId, enrollmentId))
        {
            return ProgressMapper.toResponse(enrollment, lessonProgressResponses);
        }

        LessonProgress newLessonProgress = ProgressMapper.toEntity(enrollment, lessonToComplete);
        lessonProgressRepository.save(newLessonProgress);
        lessonProgressResponses.add(new LessonProgressResponse(lessonId, lessonToComplete.getTitle(), true));
        addCompletedLesson(enrollment, newLessonProgress);
        return  ProgressMapper.toResponse(enrollment, lessonProgressResponses);
    }

    @Transactional(readOnly = true)
    public EnrollmentDetailResponse getById(Long enrollmentId)
    {
        Enrollment enrollment = enrollmentRepository.findWithCourseAndLessonProgressById(enrollmentId).orElseThrow(
                () -> new EnrollmentNotFoundException(enrollmentId));

        return EnrollmentMapper.toDetailResponse(enrollment);

    }

    @Transactional(readOnly = true)
    public ProgressResponse getProgress(Long enrollmentId)
    {
        Enrollment enrollment = enrollmentRepository.findWithCourseAndLessonProgressById(enrollmentId).orElseThrow(
                () -> new EnrollmentNotFoundException(enrollmentId));

        List<LessonProgressResponse> lessonProgressResponses = lessonRepository.findLessonProgressResponse(enrollmentId, enrollment.getCourse().getId());

        return  ProgressMapper.toResponse(enrollment, lessonProgressResponses);
    }

    @Transactional
    public EnrollmentResponse dropEnrollment(Long enrollmentId)
    {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId).orElseThrow(
                () -> new EnrollmentNotFoundException(enrollmentId));
        enrollment.setStatus(EnrollmentStatus.DROPPED);
        return EnrollmentMapper.toResponse(enrollment);
    }

    @Transactional
    public QuizResultResponse submitQuiz(Long enrollmentId, Long quizId, SubmitQuizRequest answers)
    {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId).orElseThrow(
                () -> new EnrollmentNotFoundException(enrollmentId));

        if (enrollment.getStatus() != EnrollmentStatus.ACTIVE)
        {
            throw new EnrollmentNotActiveException("Enrollment " + enrollmentId + " is not active");
        }

        QuizLesson quizLesson = lessonRepository.findQuizLessonWithQuestionsById(quizId).orElseThrow(
                () -> new LessonNotFoundException(quizId));

        if (!Objects.equals(enrollment.getCourse().getId(), quizLesson.getCourse().getId()))
        {
            throw new LessonNotInCourseException("Lesson " + quizId +
                    " does not belong to course" + enrollment.getCourse().getId());
        }

        if (lessonProgressRepository.existsByLessonIdAndEnrollmentId(quizId,enrollmentId))
        {
            throw new QuizAlreadyPassedException("You have already passed quiz " + quizId);
        }
        QuizAttempt attempt = QuizAttemptMapper.toEntity(answers, quizLesson, enrollment);
        quizLesson.getQuizAttempts().add(attempt);
        quizAttemptRepository.save(attempt);
        if (attempt.isPassed())
        {
            LessonProgress newLessonProgress = ProgressMapper.toEntity(enrollment, quizLesson, attempt.getQuizScore());
            lessonProgressRepository.save(newLessonProgress);
            addCompletedLesson(enrollment, newLessonProgress);
        }
        return ProgressMapper.toQuizResultResponse(attempt, quizLesson.getQuestions().size());
    }

    @Transactional(readOnly = true)
    public QuizResultList getQuizResults(Long enrollmentId)
    {
        if (!enrollmentRepository.existsById(enrollmentId))
        {
            throw new EnrollmentNotFoundException(enrollmentId);
        }
        List<QuizResult> quizResults = quizAttemptRepository.findQuizResultsByEnrollmentId(enrollmentId);
        return QuizAttemptMapper.toResponseList(quizResults);
    }

}
