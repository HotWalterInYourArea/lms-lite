package com.example.lmslite.dto.lesson.questions;

import com.example.lmslite.validation.CorrectOptionIndexValid;
import jakarta.validation.constraints.*;

import java.util.List;

@CorrectOptionIndexValid
public record QuestionRequest(
        @NotBlank String text,

        @NotNull @NotEmpty List<String> options,

        @NotNull @Positive  Integer correctOptionIndex
        ) {}
