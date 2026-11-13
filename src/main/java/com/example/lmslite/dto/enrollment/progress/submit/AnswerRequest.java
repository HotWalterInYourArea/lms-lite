package com.example.lmslite.dto.enrollment.progress.submit;

import jakarta.validation.constraints.NotNull;

public record AnswerRequest(

        @NotNull(message = "Question ID is required")
        Long questionId,

        @NotNull(message = "Selected option ID is required")
        Integer selectedOptionId
) {}
