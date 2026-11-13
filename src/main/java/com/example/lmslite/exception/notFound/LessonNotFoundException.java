package com.example.lmslite.exception.notFound;

public class LessonNotFoundException extends EntityNotFoundException
{
    public LessonNotFoundException(Long entityId)
    {
        super("Lesson", entityId);
    }
}
