package com.example.lmslite.controller;

import com.example.lmslite.dto.certificate.detail.CertificateDetailResponse;
import com.example.lmslite.dto.certificate.media.CertificateFile;
import com.example.lmslite.service.CertificateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/certificates")
@RequiredArgsConstructor
@Tag(name = "Сертификаты", description = "API для работы с сертификатами студентов")

public class CertificateController {
    private final CertificateService certificateService;
    @Operation(
            summary = "Получить сертификат по уникальному коду (UUID)",
            description = "Возвращает детальную информацию о сертификате, используя его уникальный UUID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Сертификат успешно найден"),
            @ApiResponse(responseCode = "404", description = "Сертификат с указанным UUID не найден"),
            @ApiResponse(responseCode = "400", description = "Некорректный формат UUID")
    })
    @GetMapping("/{certificateUC}")
    public CertificateDetailResponse getCertificateByUC(@Parameter(description = "Уникальный код сертификата (UUID)", required = true) @PathVariable UUID certificateUC)
    {
        return certificateService.getCertByUUID(certificateUC);
    }

    @GetMapping("/{certificateId}/download")
    @Operation(
            summary = "Скачать сертификат в формате PDF",
            description = "Генерирует и возвращает PDF-файл сертификата для скачивания."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "PDF-файл успешно сгенерирован",
                    content = @Content(
                            mediaType = "application/pdf",
                            schema = @Schema(type = "string", format = "binary")
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Сертификат не найден")
    })
    public ResponseEntity<byte[]> downloadCertificate(@Parameter(description = "ID сертификата в базе данных", required = true) @PathVariable Long certificateId)
    {
        CertificateFile file = certificateService.renderCertificate(certificateId);
        return ResponseEntity.ok()
                .contentType(file.contentType())
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment().filename(file.filename()).build().toString())
                .body(file.content());
    }
}
