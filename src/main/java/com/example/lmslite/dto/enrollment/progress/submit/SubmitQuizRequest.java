package com.example.lmslite.dto.enrollment.progress.submit;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;


public record SubmitQuizRequest(
        @NotNull(message = "Cannot send empty request as submission")
        List<@NotNull @Valid AnswerRequest> answers
) {}
