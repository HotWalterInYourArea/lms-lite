package com.example.lmslite.exception.notFound;

public class CourseNotFoundException extends EntityNotFoundException
{
    public CourseNotFoundException(Long entityId)
    {
        super("Course", entityId);
    }

}
