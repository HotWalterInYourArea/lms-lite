package com.example.lmslite.controller;
import com.example.lmslite.dto.course.list.CourseResponse;
import com.example.lmslite.dto.enrollment.detail.EnrollmentDetailResponse;
import com.example.lmslite.dto.enrollment.list.EnrollmentResponse;
import com.example.lmslite.dto.enrollment.progress.complete.ProgressResponse;
import com.example.lmslite.dto.enrollment.progress.quiz_results.QuizResultList;
import com.example.lmslite.service.EnrollmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/enrollments")
@RequiredArgsConstructor
@Tag(name = "Зачисления", description = "API для управления записью студентов на курсы")
public class EnrollmentController
{
    private final EnrollmentService enrollmentService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @Operation(
            summary = "Записать студента на курс",
            description = "Создаёт новое зачисление (enrollment) для указанного студента и курса. " +
                    "Возвращает информацию о курсе."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Студент успешно записан на курс"),
            @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса"),
            @ApiResponse(responseCode = "404", description = "Студент или курс не найдены"),
            @ApiResponse(responseCode = "409", description = "Студент уже записан на этот курс")
    })
    public CourseResponse createEnrollment(
            @Parameter(description = "ID студента", required = true, example = "1")
            @RequestParam Long studentId,
            @Parameter(description = "ID курса", required = true, example = "5")
            @RequestParam Long courseId)
    {
        return enrollmentService.enrollStudent(studentId, courseId);
    }

    @GetMapping("/{enrollmentId}/progress")
    @Operation(
            summary = "Получить прогресс по зачислению",
            description = "Возвращает текущий прогресс студента: количество завершённых уроков, процент и т.д."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Прогресс получен"),
            @ApiResponse(responseCode = "404", description = "Зачисление не найдено")
    })
    public ProgressResponse getProgress(
            @Parameter(description = "ID зачисления", required = true, example = "42")
            @PathVariable Long enrollmentId)
    {
        return enrollmentService.getProgress(enrollmentId);
    }

    @GetMapping("/{enrollmentId}")
    @Operation(
            summary = "Получить зачисление по ID",
            description = "Возвращает детальную информацию о зачислении: студент, курс, статус, даты."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Зачисление найдено"),
            @ApiResponse(responseCode = "404", description = "Зачисление не найдено")
    })
    public EnrollmentDetailResponse getById(
            @Parameter(description = "ID зачисления", required = true, example = "42")
            @PathVariable Long enrollmentId)
    {
        return enrollmentService.getById(enrollmentId);
    }


    @GetMapping("/{enrollmentId}/quiz-results")
    @Operation(
            summary = "Получить результаты тестов",
            description = "Возвращает список всех результатов тестов (квизов), сданных в рамках данного зачисления."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список результатов получен"),
            @ApiResponse(responseCode = "404", description = "Зачисление не найдено")
    })
    public QuizResultList getQuizResults(
            @Parameter(description = "ID зачисления", required = true, example = "42")
            @PathVariable Long enrollmentId)
    {
        return enrollmentService.getQuizResults(enrollmentId);
    }

    @PostMapping("/{enrollmentId}/lessons/{lessonId}/complete")
    @Operation(
            summary = "Завершить урок",
            description = "Отмечает урок как завершённый для указанного зачисления и возвращает обновлённый прогресс. " +
                    "Если это был последний урок, зачисление переводится в статус COMPLETED, и генерируется сертификат."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Урок завершён, прогресс обновлён"),
            @ApiResponse(responseCode = "404", description = "Зачисление или урок не найдены"),
            @ApiResponse(responseCode = "409", description = "Урок уже был завершён")
    })
    public ProgressResponse completeLesson(
            @Parameter(description = "ID зачисления", required = true, example = "42")
            @PathVariable Long enrollmentId,
            @Parameter(description = "ID урока", required = true, example = "7")
            @PathVariable Long lessonId)
    {
        return enrollmentService.completeLesson(enrollmentId, lessonId);
    }


    @DeleteMapping("/{enrollmentId}")
    @Operation(
            summary = "Отменить зачисление (отчислить)",
            description = "Переводит зачисление в статус DROPPED. Студент теряет доступ к курсу, " +
                    "но история прогресса и результаты тестов сохраняются."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Зачисление отменено"),
            @ApiResponse(responseCode = "404", description = "Зачисление не найдено"),
            @ApiResponse(responseCode = "409", description = "Нельзя отменить зачисление с текущим статусом")
    })
    public EnrollmentResponse dropEnrollment(
            @Parameter(description = "ID зачисления", required = true, example = "42")
            @PathVariable Long enrollmentId)
    {
        return enrollmentService.dropEnrollment(enrollmentId);
    }

}
