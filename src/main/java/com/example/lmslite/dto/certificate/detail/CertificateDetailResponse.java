package com.example.lmslite.dto.certificate.detail;

import java.time.LocalDate;
import java.util.UUID;

public record CertificateDetailResponse(
        Long certificateId,
        Long studentId,
        Long courseId,
        UUID uuid,
        LocalDate issuedAt
) {}
