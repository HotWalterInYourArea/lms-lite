package com.example.lmslite.exception.unprocessable;


public class UnsupportedLessonTypeException extends UnprocessableEntityException {
    public UnsupportedLessonTypeException(String message) {
        super(message);
    }
}