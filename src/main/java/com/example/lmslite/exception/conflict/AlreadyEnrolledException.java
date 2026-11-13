package com.example.lmslite.exception.conflict;

public class AlreadyEnrolledException extends ConflictException
{
    public AlreadyEnrolledException(String message)
    {
        super(message);
    }
}
