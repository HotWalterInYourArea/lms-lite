package com.example.lmslite.dto.course.update;

import com.example.lmslite.enums.CourseStatus;

public record ChangeCourseStatusResponse(
        Long id,

        String title,

        CourseStatus status
) {}
