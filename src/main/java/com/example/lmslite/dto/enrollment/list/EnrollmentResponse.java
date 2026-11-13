package com.example.lmslite.dto.enrollment.list;

import com.example.lmslite.enums.EnrollmentStatus;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record EnrollmentResponse(
        Long id,

        String courseTitle,

        String courseDescription,

        EnrollmentStatus status,

        LocalDateTime enrolledAt,

        LocalDateTime completedAt
) {}
