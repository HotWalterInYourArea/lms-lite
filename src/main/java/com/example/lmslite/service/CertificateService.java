package com.example.lmslite.service;

import com.example.lmslite.dto.certificate.detail.CertificateDetailResponse;
import com.example.lmslite.dto.certificate.list.CertificateListResponse;
import com.example.lmslite.dto.certificate.media.CertificateFile;
import com.example.lmslite.exception.notFound.CertificateNotFoundException;
import com.example.lmslite.exception.notFound.StudentNotFoundException;
import com.example.lmslite.mapper.CertificateMapper;
import com.example.lmslite.model.Certificate;
import com.example.lmslite.repository.CertificateRepository;
import com.example.lmslite.repository.StudentRepository;
import com.example.lmslite.service.util.CertificatePdfRenderer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CertificateService
{
    private final StudentRepository studentRepository;
    private final CertificateRepository certificateRepository;
    private final CertificatePdfRenderer certificatePdfRenderer;

    @Transactional(readOnly = true)
    public CertificateListResponse getStudentCerts(Long studentId)
    {
        if(!studentRepository.existsById(studentId))
        {
            throw  new StudentNotFoundException(studentId);
        }
        List<Certificate> certificatesByStudent = certificateRepository.findByStudentId(studentId);
        return CertificateMapper.toListResponse(certificatesByStudent);
    }

    @Transactional(readOnly = true)
    public CertificateDetailResponse getCertByUUID(UUID certificateUC)
    {
        Certificate certificateByUC = certificateRepository.findByUniqueCode(certificateUC).orElseThrow(
                ()-> new CertificateNotFoundException(certificateUC)
        );
        return CertificateMapper.toDetailResponse(certificateByUC);
    }

    @Transactional(readOnly = true)
    public CertificateFile renderCertificate(Long certificateId) {
        Certificate certificateById = certificateRepository.findWithCourseAndStudentById(certificateId).orElseThrow(
                ()-> new CertificateNotFoundException(certificateId)
        );
        byte[] pdf = certificatePdfRenderer.render(certificateById);
        return new CertificateFile(
                "certificate-" + certificateById.getUniqueCode() + ".pdf",
                MediaType.APPLICATION_PDF,
                pdf
        );
    }

}
