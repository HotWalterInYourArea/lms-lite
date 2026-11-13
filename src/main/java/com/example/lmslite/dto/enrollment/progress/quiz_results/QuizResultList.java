package com.example.lmslite.dto.enrollment.progress.quiz_results;

import java.util.List;

public record QuizResultList(
        List<QuizResult> quizResults,
        Double averageScore,
        long passedQuizzes,
        long totalQuizzes
) {}
