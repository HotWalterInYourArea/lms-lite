package com.example.lmslite.exception.unprocessable;

public class LessonNotInCourseException extends RuntimeException
{
    public LessonNotInCourseException(String message)
    {
        super(message);
    }
    public LessonNotInCourseException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
