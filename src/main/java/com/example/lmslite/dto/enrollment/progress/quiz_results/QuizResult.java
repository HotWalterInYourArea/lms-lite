package com.example.lmslite.dto.enrollment.progress.quiz_results;

import java.time.LocalDateTime;

public record QuizResult(
        Long lessonId,
        String lessonTitle,
        Integer score,
        boolean passed,
        Long attempts,
        LocalDateTime lastAttemptAt
) {}
