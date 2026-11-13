package com.example.lmslite.dto.lesson.create;

import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.URL;

public record CreateVideoLessonRequest(
        @NotBlank(message = "Title is required")
        @Size(max = 255, message = "Title is too big(>255)")
        String title,
        @NotBlank(message = "Video URL is required")
        @Size(max = 255, message = "Video URL is too big(>255)")
        @URL(protocol = "https", message = "Must be a valid HTTPS URL")
        String videoUrl,
        @NotNull(message = "Video duration is required")
        @Positive(message = "Video duration must be positive")
        Integer durationMinutes
) implements CreateLessonRequest {}
