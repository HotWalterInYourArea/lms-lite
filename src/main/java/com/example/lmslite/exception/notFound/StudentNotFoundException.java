package com.example.lmslite.exception.notFound;

public class StudentNotFoundException extends EntityNotFoundException {
    public StudentNotFoundException(Long entityId)
    {
        super("Student", entityId);
    }
}
