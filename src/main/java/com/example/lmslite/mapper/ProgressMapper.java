package com.example.lmslite.mapper;

import com.example.lmslite.dto.enrollment.progress.complete.LessonProgressResponse;
import com.example.lmslite.dto.enrollment.progress.complete.ProgressResponse;
import com.example.lmslite.dto.enrollment.progress.submit.QuizResultResponse;
import com.example.lmslite.model.*;

import java.util.List;

public class ProgressMapper {
    public static ProgressResponse toResponse(Enrollment enrollment, List<LessonProgressResponse> lessons)
    {
        return new ProgressResponse(
                enrollment.getId(),
                enrollment.getCourse().getTitle(),
                enrollment.getProgressPercent(),
                enrollment.getCompletedLessons().size(),
                enrollment.getCourse().getTotalLessons(),
                enrollment.getStatus(),
                lessons);
    }
    public static LessonProgress toEntity(Enrollment enrollment, QuizLesson lesson, Integer scorePercent)
    {
        LessonProgress newLessonProgress = new LessonProgress();
        newLessonProgress.setQuizScore(scorePercent);
        newLessonProgress.setLesson(lesson);
        newLessonProgress.setEnrollment(enrollment);
        return newLessonProgress;
    }
    public static LessonProgress toEntity(Enrollment enrollment,Lesson lesson)
    {
        LessonProgress newLessonProgress = new LessonProgress();
        newLessonProgress.setLesson(lesson);
        newLessonProgress.setEnrollment(enrollment);
        return newLessonProgress;
    }
    public static QuizResultResponse toQuizResultResponse(QuizAttempt quizAttempt, Integer totalQuestions)
    {
        return new QuizResultResponse(
                quizAttempt.getQuiz().getId(),
                quizAttempt.getQuiz().getTitle(),
                quizAttempt.getQuiz().getQuestions().size(),
                quizAttempt.getCorrectAnswers(),
                totalQuestions,
                quizAttempt.isPassed(),
                quizAttempt.getQuizScore());
    }
}
