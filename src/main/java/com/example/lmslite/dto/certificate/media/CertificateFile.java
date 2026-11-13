package com.example.lmslite.dto.certificate.media;

import org.springframework.http.MediaType;

public record CertificateFile(
        String filename,
        MediaType contentType,
        byte[] content
) {}