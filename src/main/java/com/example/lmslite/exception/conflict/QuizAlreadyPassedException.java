package com.example.lmslite.exception.conflict;

import com.example.lmslite.exception.unprocessable.UnprocessableEntityException;

public class QuizAlreadyPassedException extends UnprocessableEntityException {
    public QuizAlreadyPassedException(String message) {
        super(message);
    }
}
