package com.example.lmslite.service;

import com.example.lmslite.dto.course.create.CreateCourseRequest;
import com.example.lmslite.dto.course.create.CreateCourseResponse;
import com.example.lmslite.dto.course.detail.CourseDetailResponse;
import com.example.lmslite.dto.course.list.CourseListResponse;
import com.example.lmslite.dto.course.stats.CourseStatisticsResponse;
import com.example.lmslite.dto.course.stats.TopCourseResponse;
import com.example.lmslite.dto.course.update.ChangeCourseStatusResponse;
import com.example.lmslite.dto.course.update.UpdateCourseRequest;
import com.example.lmslite.dto.course.update.UpdateCourseResponse;
import com.example.lmslite.dto.lesson.create.*;
import com.example.lmslite.dto.lesson.create.CreateLessonResponse;
import com.example.lmslite.dto.lesson.list.LessonListResponse;
import com.example.lmslite.enums.CourseStatus;
import com.example.lmslite.enums.EnrollmentStatus;
import com.example.lmslite.exception.conflict.CourseEnrollmentNotEmptyException;
import com.example.lmslite.exception.conflict.EmptyCourseException;
import com.example.lmslite.exception.conflict.IllegalCourseStatus;
import com.example.lmslite.exception.notFound.CourseNotFoundException;
import com.example.lmslite.exception.notFound.InstructorNotFoundException;
import com.example.lmslite.exception.unprocessable.UnsupportedLessonTypeException;
import com.example.lmslite.mapper.CourseMapper;
import com.example.lmslite.mapper.LessonMapper;
import com.example.lmslite.model.*;
import com.example.lmslite.repository.CourseRepository;
import com.example.lmslite.repository.EnrollmentRepository;
import com.example.lmslite.repository.InstructorRepository;
import com.example.lmslite.repository.LessonRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService
{
    private final CourseRepository courseRepository;
    private final InstructorRepository instructorRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final LessonRepository lessonRepository;

    @Transactional
    public CreateCourseResponse createCourse(CreateCourseRequest request)
    {
        Instructor courseAuthor = instructorRepository.findById(request.authorId()).orElseThrow(
                ()-> new InstructorNotFoundException(request.authorId()));
        Course newCourse = CourseMapper.toEntity(request, courseAuthor);
        courseRepository.save(newCourse);
        return CourseMapper.toCreateResponse(newCourse);
    }

    @Transactional
    public UpdateCourseResponse updateCourseById(UpdateCourseRequest request, Long courseId)
    {
        Course courseToUpdate = courseRepository.findWithLessonsById(courseId).orElseThrow(
                ()-> new CourseNotFoundException(courseId));

       CourseMapper.updateEntity(courseToUpdate, request);

        return CourseMapper.toUpdateResponse(courseToUpdate);
    }

    @Transactional
    public void deleteCourseById(Long courseId)
    {
        Course courseToDelete = courseRepository.findById(courseId).orElseThrow(
                ()-> new CourseNotFoundException(courseId));

        if (courseToDelete.getStatus() != CourseStatus.DRAFT)
        {
            throw new IllegalCourseStatus("Course " + courseId + "must be in DRAFT status to be deleted");
        }
        courseRepository.delete(courseToDelete);

    }

    @Transactional
    public ChangeCourseStatusResponse publishCourse(Long courseId)
    {
        Course courseToPublish = courseRepository.findById(courseId).orElseThrow(
                () -> new CourseNotFoundException(courseId));
        if (courseToPublish.getStatus() == CourseStatus.PUBLISHED)
        {
            return CourseMapper.toStatusChangeResponse(courseToPublish);
        }
        int totalLessons = courseToPublish.getTotalLessons();
        if(totalLessons == 0)
        {
           throw new EmptyCourseException("Course " + courseId + "doesn't have any lessons");
        }
        courseToPublish.publish();
        return CourseMapper.toStatusChangeResponse(courseToPublish);
    }

    @Transactional
    public ChangeCourseStatusResponse archiveCourse(Long courseId)
    {
        Course courseToArchive = courseRepository.findById(courseId).orElseThrow(
                () -> new CourseNotFoundException(courseId));
        if (courseToArchive.getStatus() == CourseStatus.ARCHIVED)
        {
            return CourseMapper.toStatusChangeResponse(courseToArchive);
        }
        if (enrollmentRepository.existsByCourseIdAndStatus(courseId, EnrollmentStatus.ACTIVE))
        {
            throw new CourseEnrollmentNotEmptyException("Cannot archive course " + courseId
                    +" while it still has active enrollments");
        }
        courseToArchive.setStatus(CourseStatus.ARCHIVED);
        return CourseMapper.toStatusChangeResponse(courseToArchive);
    }

    @Transactional(readOnly = true)
    public CourseListResponse getPublishedCourses()
    {
        List<Course> publishedCourses = courseRepository.findWithLessonsByStatus(CourseStatus.PUBLISHED);
        courseRepository.fetchQuestionsForQuizLessonsByStatus(CourseStatus.PUBLISHED);
        return CourseMapper.toListResponse(publishedCourses);
    }

    @Transactional(readOnly = true)
    public CourseDetailResponse getCourseById(Long courseId)
    {
        Course courseById =  courseRepository.findWithLessonsById(courseId)
                .orElseThrow(() -> new CourseNotFoundException(courseId));
        courseRepository.fetchQuestionsForQuizLessonsById(courseId);
        return CourseMapper.toDetailResponse(courseById);
    }

    @Transactional(readOnly = true)
    public LessonListResponse getCourseLessons(Long courseId) {
        if (!courseRepository.existsById(courseId))
        {
                throw new CourseNotFoundException(courseId);
        }
        List<Lesson> courseLessons = lessonRepository.findByCourseIdOrderByOrderIndexAsc(courseId);
        return LessonMapper.toListResponse(courseLessons);
    }

    @Transactional
    public CreateLessonResponse addLesson(Long courseId, CreateLessonRequest request)
    {
        Course courseToGetLessons =  courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException(courseId));
        int nextPosition = lessonRepository.findTopByCourseIdOrderByOrderIndexDesc(courseId).map(l -> l.getOrderIndex() + 1).orElse(0);
        switch (request){
            case CreateQuizLessonRequest quizRequest:
                {
                    QuizLesson newQuizLesson = LessonMapper.toEntity(quizRequest, courseToGetLessons, nextPosition);
                    lessonRepository.save(newQuizLesson);
                    return LessonMapper.toCreateResponse(newQuizLesson);
                }
            case CreateVideoLessonRequest videoRequest:
                {
                    VideoLesson newVideoLesson = LessonMapper.toEntity(videoRequest, courseToGetLessons, nextPosition);
                    lessonRepository.save(newVideoLesson);
                    return LessonMapper.toCreateResponse(newVideoLesson);
                }
            case CreateTextLessonRequest textRequest:
                {
                    TextLesson newTextLesson = LessonMapper.toEntity(textRequest, courseToGetLessons, nextPosition);
                    lessonRepository.save(newTextLesson);
                    return LessonMapper.toCreateResponse(newTextLesson);
                }
            default:
                throw new UnsupportedLessonTypeException("Failed to determine lesson type");
        }
    }

    @Transactional(readOnly = true)
    public CourseStatisticsResponse getCourseStats(Long courseId)
    {
        if(!courseRepository.existsById(courseId)){
            throw new CourseNotFoundException(courseId);
        }
        return courseRepository.getCourseStatistics(courseId);

    }

    @Transactional(readOnly = true)
    public List<TopCourseResponse> getTopCoursesLast30Days(int limit)
    {
        LocalDateTime sinceDate = LocalDateTime.now().minusDays(30);
        return courseRepository.findTopCoursesSince(sinceDate, limit);
    }

}
