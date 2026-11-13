package com.example.lmslite.exception.notFound;

import java.util.UUID;

public class CertificateNotFoundException extends EntityNotFoundException {
    public CertificateNotFoundException(UUID entityUUID) {
        super("Certificate", entityUUID);
    }
    public CertificateNotFoundException(Long entityId) {
        super("Certificate", entityId);
    }
}
