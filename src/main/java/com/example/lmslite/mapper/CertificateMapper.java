package com.example.lmslite.mapper;

import com.example.lmslite.dto.certificate.detail.CertificateDetailResponse;
import com.example.lmslite.dto.certificate.list.CertificateListResponse;
import com.example.lmslite.dto.certificate.list.CertificateResponse;
import com.example.lmslite.model.Certificate;
import com.example.lmslite.model.Enrollment;

import java.util.List;

public class CertificateMapper
{
    public static CertificateResponse toResponse(Certificate certificate)
    {
        return new CertificateResponse(
                certificate.getId(),
                certificate.getStudent().getId(),
                certificate.getCourse().getId(),
                certificate.getUniqueCode(),
                certificate.getIssuedAt()
        );
    }
    public static Certificate toEntity(Enrollment enrollment)
    {
        Certificate newCertificate = new Certificate();
        newCertificate.setStudent(enrollment.getStudent());
        newCertificate.setCourse(enrollment.getCourse());
        return newCertificate;
    }
    public static CertificateDetailResponse toDetailResponse(Certificate certificate)
    {
        return new CertificateDetailResponse(
                certificate.getId(),
                certificate.getStudent().getId(),
                certificate.getCourse().getId(),
                certificate.getUniqueCode(),
                certificate.getIssuedAt()
        );
    }
    public static CertificateListResponse toListResponse(List<Certificate> certificates)
    {
    List<CertificateResponse> toResponseList = certificates.stream()
            .map(CertificateMapper::toResponse)
            .toList();
        return new CertificateListResponse(toResponseList);
    }
}
