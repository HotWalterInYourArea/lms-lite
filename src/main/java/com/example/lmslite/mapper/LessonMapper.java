package com.example.lmslite.mapper;

import com.example.lmslite.dto.lesson.create.CreateQuizLessonRequest;
import com.example.lmslite.dto.lesson.create.CreateTextLessonRequest;
import com.example.lmslite.dto.lesson.create.CreateVideoLessonRequest;
import com.example.lmslite.dto.lesson.detail.LessonDetailResponse;
import com.example.lmslite.dto.lesson.detail.QuizLessonDetailResponse;
import com.example.lmslite.dto.lesson.detail.TextLessonDetailResponse;
import com.example.lmslite.dto.lesson.detail.VideoLessonDetailResponse;
import com.example.lmslite.dto.lesson.list.LessonListResponse;
import com.example.lmslite.dto.lesson.list.LessonResponse;
import com.example.lmslite.dto.lesson.create.CreateLessonResponse;
import com.example.lmslite.dto.lesson.update.UpdateLessonResponse;
import com.example.lmslite.dto.lesson.update.UpdateQuizLessonRequest;
import com.example.lmslite.dto.lesson.update.UpdateTextLessonRequest;
import com.example.lmslite.dto.lesson.update.UpdateVideoLessonRequest;
import com.example.lmslite.exception.unprocessable.UnsupportedLessonTypeException;
import com.example.lmslite.model.*;

import java.util.List;


public class LessonMapper {
    public static QuizLesson toEntity(CreateQuizLessonRequest request, Course course, int nextPosition)
    {
        QuizLesson lesson = new QuizLesson();
        lesson.setTitle(request.title());
        lesson.setOrderIndex(nextPosition);
        lesson.setPassingScore(request.passingScore());
        lesson.setTimeLimitMinutes(request.timeLimitMinutes());
        lesson.setQuestions(request.questions().stream().map((questionRequest)->QuestionMapper.toEntity(questionRequest,lesson)).toList());
        lesson.setCourse(course);
        return lesson;
    }

    public static void updateEntity(QuizLesson lesson, UpdateQuizLessonRequest request)
    {
        lesson.setTitle(request.title());
        lesson.setPassingScore(request.passingScore());
        lesson.setTimeLimitMinutes(request.timeLimitMinutes());
        lesson.setQuestions(request.questions().stream().map((questionRequest)->QuestionMapper.toEntity(questionRequest,lesson)).toList());
    }

    public static VideoLesson toEntity(CreateVideoLessonRequest request, Course course, int nextPosition)
    {
        VideoLesson lesson = new VideoLesson();
        lesson.setTitle(request.title());
        lesson.setOrderIndex(nextPosition);
        lesson.setVideoUrl(request.videoUrl());
        lesson.setDurationMinutes(request.durationMinutes());
        lesson.setCourse(course);
        return lesson;
    }
    public static void updateEntity(VideoLesson lesson, UpdateVideoLessonRequest request)
    {
        lesson.setTitle(request.title());
        lesson.setVideoUrl(request.videoUrl());
        lesson.setDurationMinutes(request.durationMinutes());
    }

    public static TextLesson toEntity(CreateTextLessonRequest request, Course course, int nextPosition) {
        TextLesson lesson = new TextLesson();
        lesson.setTitle(request.title());
        lesson.setOrderIndex(nextPosition);
        lesson.setContent(request.content());
        lesson.setReadingTimeMinutes(request.readingTimeMinutes());
        lesson.setCourse(course);
        return lesson;
    }

    public static void updateEntity(TextLesson lesson, UpdateTextLessonRequest request)
    {
        lesson.setTitle(request.title());
        lesson.setContent(request.content());
        lesson.setReadingTimeMinutes(request.readingTimeMinutes());
    }

    public static CreateLessonResponse toCreateResponse(Lesson lesson)
    {
        return  new CreateLessonResponse(
                lesson.getId(),
                lesson.getTitle(),
                lesson.getOrderIndex(),
                lesson.getType());
    }
    public static UpdateLessonResponse toUpdateResponse(Lesson lesson)
    {
        return  new UpdateLessonResponse(
                lesson.getId(),
                lesson.getTitle(),
                lesson.getOrderIndex(),
                lesson.getType());
    }
    public static LessonResponse toResponse(Lesson lesson)
    {
        return  new LessonResponse(
                lesson.getId(),
                lesson.getTitle(),
                lesson.getOrderIndex(),
                lesson.getType());
    }

    public static LessonDetailResponse toDetailResponse(Lesson lesson)
    {
        return switch(lesson)
        {
            case QuizLesson quizLesson -> new QuizLessonDetailResponse(
                    quizLesson.getId(),
                    quizLesson.getTitle(),
                    quizLesson.getOrderIndex(),
                    quizLesson.getType(),
                    quizLesson.getPassingScore(),
                    quizLesson.getEstimatedMinutes(),
                    QuestionMapper.toDetailResponseList(quizLesson.getQuestions()));

            case VideoLesson videoLesson -> new VideoLessonDetailResponse(
                    videoLesson.getId(),
                    videoLesson.getTitle(),
                    videoLesson.getOrderIndex(),
                    videoLesson.getType(),
                    videoLesson.getVideoUrl(),
                    videoLesson.getEstimatedMinutes());

            case TextLesson textLesson -> new TextLessonDetailResponse(
                    textLesson.getId(),
                    textLesson.getTitle(),
                    textLesson.getOrderIndex(),
                    textLesson.getType(),
                    textLesson.getContent(),
                    textLesson.getEstimatedMinutes());

            default -> throw new UnsupportedLessonTypeException("Failed to determine lesson type");
        };
    }
    public static LessonListResponse toListResponse(List<Lesson> lessons)
    {
        return new LessonListResponse(
                lessons.stream()
                .map(LessonMapper::toResponse)
                .toList());
    }
    public static List<CreateLessonResponse> toResponseList(List<Lesson> lessons)
    {
        return lessons.stream()
                .map(LessonMapper::toCreateResponse)
                .toList();
    }
}
