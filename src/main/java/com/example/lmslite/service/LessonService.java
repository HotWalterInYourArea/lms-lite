package com.example.lmslite.service;

import com.example.lmslite.dto.lesson.questions.QuestionListResponse;
import com.example.lmslite.dto.lesson.detail.LessonDetailResponse;
import com.example.lmslite.dto.lesson.update.*;
import com.example.lmslite.exception.notFound.LessonNotFoundException;
import com.example.lmslite.exception.unprocessable.UnsupportedLessonTypeException;
import com.example.lmslite.mapper.LessonMapper;
import com.example.lmslite.mapper.QuestionMapper;
import com.example.lmslite.model.*;
import com.example.lmslite.repository.LessonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LessonService
{
    private final LessonRepository lessonRepository;

    @Transactional
    public UpdateLessonResponse updateById(Long lessonId, UpdateLessonRequest request)
    {
        switch (request){
            case UpdateQuizLessonRequest quizRequest:
                {
                QuizLesson quizLessonToUpdate =  lessonRepository.findQuizLessonById(lessonId)
                        .orElseThrow(() -> new LessonNotFoundException(lessonId));
                LessonMapper.updateEntity(quizLessonToUpdate, quizRequest);
                return LessonMapper.toUpdateResponse(quizLessonToUpdate);
            }
            case UpdateVideoLessonRequest videoRequest:
            {
                VideoLesson videoLessonToUpdate = lessonRepository.findVideoLessonById(lessonId)
                        .orElseThrow(() -> new LessonNotFoundException(lessonId));
                LessonMapper.updateEntity(videoLessonToUpdate, videoRequest);
                return LessonMapper.toUpdateResponse(videoLessonToUpdate);
            }
            case UpdateTextLessonRequest textRequest:
            {
                TextLesson textLessonToUpdate = lessonRepository.findTextLessonById(lessonId)
                        .orElseThrow(() -> new LessonNotFoundException(lessonId));
                LessonMapper.updateEntity(textLessonToUpdate, textRequest);
                return LessonMapper.toUpdateResponse(textLessonToUpdate);
            }
            default:
                throw new UnsupportedLessonTypeException("Failed to determine lesson type");
        }
    }

    @Transactional(readOnly = true)
    public LessonDetailResponse getById(Long lessonId)
    {
        Optional<QuizLesson> quizLesson = lessonRepository.findQuizLessonWithQuestionsById(lessonId);
        Lesson lessonToGet;
        if (quizLesson.isPresent())
        {
            lessonToGet = quizLesson.get();
        }
        else
        {
            lessonToGet = lessonRepository.findById(lessonId)
                    .orElseThrow(() -> new LessonNotFoundException(lessonId));
        }
        return LessonMapper.toDetailResponse(lessonToGet);
    }

    @Transactional(readOnly = true)
    public QuestionListResponse getQuestions(Long lessonId)
    {
        QuizLesson quizLessonToGetQuestions =  lessonRepository.findQuizLessonWithQuestionsById(lessonId)
                .orElseThrow(() -> new LessonNotFoundException(lessonId));
        return QuestionMapper.toListResponse(quizLessonToGetQuestions.getQuestions());
    }
}
