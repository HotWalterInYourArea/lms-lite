package com.example.lmslite.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CorrectOptionIndexValidator.class)
public @interface CorrectOptionIndexValid
{
    String message() default "correctOptionIndex must be a valid index into options";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
