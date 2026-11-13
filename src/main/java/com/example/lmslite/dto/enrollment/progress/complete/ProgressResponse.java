package com.example.lmslite.dto.enrollment.progress.complete;

import com.example.lmslite.enums.EnrollmentStatus;
import lombok.Builder;

import java.util.List;

@Builder
public record ProgressResponse(
        Long enrollmentId,

        String courseTitle,

        int progressPercent,

        int completedLessons,

        int totalLessons,

        EnrollmentStatus status,

        List<LessonProgressResponse> lessons

) {}
