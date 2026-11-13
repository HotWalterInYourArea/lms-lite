package com.example.lmslite.dto.lesson.update;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record UpdateTextLessonRequest(
        @NotBlank(message = "Title is required")
        @Size(max = 255, message = "Title is too big(>255)")
        String title,
        @NotBlank(message = "Markdown or text content is required")
        String content,
        @NotNull(message = "Estimated reading time is required")
        @Positive(message = "Estimated reading time must be positive")
        Integer readingTimeMinutes
) implements UpdateLessonRequest {}
