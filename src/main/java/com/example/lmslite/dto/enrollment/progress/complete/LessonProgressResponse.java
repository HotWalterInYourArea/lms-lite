package com.example.lmslite.dto.enrollment.progress.complete;


public record LessonProgressResponse(
        Long lessonId,

        String lessonTitle,

        boolean completed
) {}
