package com.example.lmslite.dto.certificate.list;

import java.util.List;

public record CertificateListResponse(List<CertificateResponse> certificates)
{
    public CertificateListResponse
    {
        if (certificates == null)
        {
            certificates = List.of();
        }
    }
}
