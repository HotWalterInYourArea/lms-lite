package com.example.lmslite.dto.course.update;

public record UpdateCourseResponse(
        Long id,

        String newTitle,

        String newDescription
) {}
