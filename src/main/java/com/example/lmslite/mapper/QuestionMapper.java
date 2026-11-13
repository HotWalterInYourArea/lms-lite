package com.example.lmslite.mapper;

import com.example.lmslite.dto.lesson.questions.QuestionDetailResponse;
import com.example.lmslite.dto.lesson.questions.QuestionListResponse;
import com.example.lmslite.dto.lesson.questions.QuestionRequest;
import com.example.lmslite.dto.lesson.questions.QuestionResponse;
import com.example.lmslite.model.Question;
import com.example.lmslite.model.QuizLesson;

import java.util.List;

public class QuestionMapper
{
    public static Question toEntity(QuestionRequest request, QuizLesson quizLesson)
    {
        Question question = new Question();
        question.setText(request.text());
        question.setOptions(request.options());
        question.setCorrectOptionIndex(request.correctOptionIndex());
        question.setQuiz(quizLesson);
        return question;
    }
    public static QuestionDetailResponse toDetailResponse(Question question)
    {
        return  new QuestionDetailResponse(
                question.getId(),
                question.getText(),
                question.getOptions(),
                question.getCorrectOptionIndex());
    }
    public static QuestionResponse toResponse(Question question)
    {
        return  new QuestionResponse(
                question.getId(),
                question.getText(),
                question.getOptions());
    }

    public static List<QuestionDetailResponse> toDetailResponseList(List<Question> questions)
    {
        return questions.stream()
                .map(QuestionMapper::toDetailResponse)
                .toList();
    }

    public static QuestionListResponse toListResponse(List<Question> questions)
    {
        return new QuestionListResponse(
                questions.stream()
                .map(QuestionMapper::toResponse)
                .toList());
    }
}
