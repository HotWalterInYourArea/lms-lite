package com.example.lmslite.dto.lesson.create;

import jakarta.validation.constraints.*;

public record CreateTextLessonRequest(
        @NotBlank(message = "Title is required")
        @Size(max = 255, message = "Title is too big(>255)")
        String title,
        @NotBlank(message = "Markdown or text content is required")
        String content,
        @NotNull(message = "Estimated reading time is required")
        @Positive(message = "Estimated reading time must be positive")
        Integer readingTimeMinutes
) implements CreateLessonRequest {}
