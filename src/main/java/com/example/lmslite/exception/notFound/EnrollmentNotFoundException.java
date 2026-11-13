package com.example.lmslite.exception.notFound;

public class EnrollmentNotFoundException extends EntityNotFoundException
{
    public EnrollmentNotFoundException(Long entityId)
    {
        super("Enrollment", entityId);
    }
}
