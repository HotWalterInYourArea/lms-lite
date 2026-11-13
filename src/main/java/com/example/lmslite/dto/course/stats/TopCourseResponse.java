package com.example.lmslite.dto.course.stats;

public record TopCourseResponse(
        Long courseId,
        String courseTitle,
        long totalEnrollments
) {}
