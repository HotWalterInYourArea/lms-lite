package com.example.lmslite.dto.lesson.create;

import com.example.lmslite.dto.lesson.questions.QuestionRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.List;

public record CreateQuizLessonRequest(
        @NotBlank(message = "Title is required")
        @Size(max = 255, message = "Title is too big(>255)")
        String title,
        @NotNull(message = "Passing score is required")
        @Positive(message = "Passing score must be positive")
        Integer passingScore,
        @Positive(message = "Time limit must be positive")
        Integer timeLimitMinutes,
        @NotNull(message = "Questions are required")
        @NotEmpty(message = "Questions are required")
        List<@Valid QuestionRequest> questions
) implements CreateLessonRequest {}
