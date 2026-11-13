package com.example.lmslite.exception.notFound;

public class InstructorNotFoundException extends EntityNotFoundException {
    public InstructorNotFoundException(Long entityId)
    {
        super("Instructor", entityId);
    }
}
