package com.example.lmslite.validation;

import com.example.lmslite.dto.lesson.questions.QuestionRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CorrectOptionIndexValidator implements ConstraintValidator<CorrectOptionIndexValid, QuestionRequest>
{
    @Override
    public boolean isValid(QuestionRequest req, ConstraintValidatorContext ctx) {
        if (req == null) return true;
        if (req.options() == null || req.correctOptionIndex() == null) return true;

        int idx = req.correctOptionIndex();
        return idx >= 0 && idx < req.options().size();
    }
}
