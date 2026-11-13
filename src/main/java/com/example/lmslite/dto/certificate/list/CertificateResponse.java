package com.example.lmslite.dto.certificate.list;

import java.time.LocalDate;
import java.util.UUID;

public record CertificateResponse (
        Long certificateId,
        Long studentId,
        Long courseId,
        UUID uuid,
        LocalDate issuedAt
) {}
