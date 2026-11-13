package com.example.lmslite.dto.instructor.detail;

public record InstructorProfileResponse(
        Long id,

        String fullName,

        String email,

        String bio
) {}
