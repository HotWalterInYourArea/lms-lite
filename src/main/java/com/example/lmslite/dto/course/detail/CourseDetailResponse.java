package com.example.lmslite.dto.course.detail;

import com.example.lmslite.dto.lesson.create.CreateLessonResponse;
import com.example.lmslite.enums.CourseStatus;

import java.util.List;

public record CourseDetailResponse(
        Long id,

        String title,

        String description,

        String authorName,

        CourseStatus status,

        int totalLessons,

        int duration,

        List<CreateLessonResponse> lessons
) {}
