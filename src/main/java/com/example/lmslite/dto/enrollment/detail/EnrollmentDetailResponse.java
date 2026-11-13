package com.example.lmslite.dto.enrollment.detail;

import com.example.lmslite.enums.EnrollmentStatus;

import java.time.LocalDateTime;

public record EnrollmentDetailResponse(
        Long id,

        String courseTitle,

        String courseDescription,

        EnrollmentStatus status,

        LocalDateTime enrolledAt,

        LocalDateTime completedAt,

        Integer ProgressPercent
) {}
