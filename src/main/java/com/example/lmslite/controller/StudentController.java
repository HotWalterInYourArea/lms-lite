package com.example.lmslite.controller;

import com.example.lmslite.dto.certificate.list.CertificateListResponse;
import com.example.lmslite.dto.enrollment.list.EnrollmentResponseList;
import com.example.lmslite.dto.student.create.CreateStudentRequest;
import com.example.lmslite.dto.student.detail.StudentProfileResponse;
import com.example.lmslite.service.CertificateService;
import com.example.lmslite.service.EnrollmentService;
import com.example.lmslite.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
@Tag(name = "Студенты", description = "API для управления профилями студентов, их зачислениями и сертификатами")
public class StudentController
{
    private final StudentService studentService;
    private final EnrollmentService enrollmentService;
    private final CertificateService certificateService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Зарегистрировать студента",
            description = "Создаёт профиль нового студента на основе переданных данных."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Студент успешно зарегистрирован"),
            @ApiResponse(responseCode = "400", description = "Некорректные входные данные")
    })
    public StudentProfileResponse registerStudent(
            @Valid @RequestBody CreateStudentRequest request)
    {
        return studentService.registerStudent(request);
    }

    @GetMapping("/{studentId}")
    @Operation(
            summary = "Получить профиль студента",
            description = "Возвращает детальную информацию о профиле студента по его ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Профиль найден"),
            @ApiResponse(responseCode = "404", description = "Студент не найден")
    })
    public StudentProfileResponse getStudentById(
            @Parameter(description = "ID студента", required = true, example = "1")
            @PathVariable Long studentId)
    {
        return studentService.getStudentById(studentId);
    }

    @GetMapping("/{studentId}/enrollments")
    @Operation(
            summary = "Получить зачисления студента",
            description = "Возвращает список всех курсов, на которые записан студент, " +
                    "с указанием статуса зачисления и прогресса."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список зачислений получен"),
            @ApiResponse(responseCode = "404", description = "Студент не найден")
    })
    public EnrollmentResponseList getStudentEnrollments(
            @Parameter(description = "ID студента", required = true, example = "1")
            @PathVariable Long studentId)
    {
        return enrollmentService.getStudentEnrollments(studentId);
    }


    @GetMapping("/{studentId}/certificates")
    @Operation(
            summary = "Получить сертификаты студента",
            description = "Возвращает список всех сертификатов, выданных студенту после успешного завершения курсов."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список сертификатов получен"),
            @ApiResponse(responseCode = "404", description = "Студент не найден")
    })
    public CertificateListResponse getStudentCertificates(
            @Parameter(description = "ID студента", required = true, example = "1")
            @PathVariable Long studentId)
    {
        return certificateService.getStudentCerts(studentId);
    }

}
