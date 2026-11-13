package com.example.lmslite.dto.enrollment.progress.submit;

public record QuizResultResponse(
        Long quizId,

        String quizTitle,

        Integer totalQuestions,

        Integer correctAnswers,

        Integer scorePercent,

        boolean passed,

        Integer passingScore
) {}
