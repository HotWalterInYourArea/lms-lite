package com.example.lmslite.mapper;

import com.example.lmslite.dto.enrollment.progress.quiz_results.QuizResult;
import com.example.lmslite.dto.enrollment.progress.quiz_results.QuizResultList;
import com.example.lmslite.dto.enrollment.progress.submit.AnswerRequest;
import com.example.lmslite.dto.enrollment.progress.submit.SubmitQuizRequest;
import com.example.lmslite.model.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class QuizAttemptMapper
{
    public static QuizAttempt toEntity(SubmitQuizRequest answers, QuizLesson quizLesson, Enrollment enrollment)
    {
        Map<Long, Integer> answerMap = answers.answers().stream().collect(Collectors.toMap(AnswerRequest::questionId, AnswerRequest::selectedOptionId,(existingValue, newValue) -> newValue));
        Map<Long, Integer> questionMap = quizLesson.getQuestions().stream().collect(Collectors.toMap(Question::getId, Question::getCorrectOptionIndex,(existingValue, newValue) -> newValue));
        int correctCount = 0;
        for(AnswerRequest answer : answers.answers())
        {
            if (Objects.equals(answerMap.get(answer.questionId()),questionMap.get(answer.questionId())))
            {
                correctCount++;
            }
        }
        int scorePercent = (correctCount * 100) / quizLesson.getQuestions().size();
        boolean passed = scorePercent >= quizLesson.getPassingScore();
        QuizAttempt attempt = new QuizAttempt();
        attempt.setPassed(passed);
        attempt.setQuizScore(scorePercent);
        attempt.setEnrollment(enrollment);
        attempt.setQuiz(quizLesson);
        attempt.setCorrectAnswers(correctCount);
        return attempt;
    }

    public static QuizResultList toResponseList(List<QuizResult> quizResults)
    {
        return new QuizResultList(
                quizResults,
                quizResults.stream().mapToDouble(QuizResult::score).average().orElse(0.0),
                quizResults.stream().filter(QuizResult::passed).count(),
                quizResults.size());
    }
}
