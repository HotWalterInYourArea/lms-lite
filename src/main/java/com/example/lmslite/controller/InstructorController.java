package com.example.lmslite.controller;

import com.example.lmslite.dto.course.list.CourseListResponse;
import com.example.lmslite.dto.instructor.create.CreateInstructorRequest;
import com.example.lmslite.dto.instructor.detail.InstructorProfileResponse;
import com.example.lmslite.dto.instructor.stats.InstructorStatisticsResponse;
import com.example.lmslite.service.InstructorService;
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
@RequestMapping("/instructors")
@RequiredArgsConstructor
@Tag(name = "Инструкторы", description = "API для управления инструкторами, их курсами и статистикой")
public class InstructorController {

    private final InstructorService instructorService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Зарегистрировать инструктора",
            description = "Создаёт профиль нового инструктора на основе переданных данных."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Инструктор успешно зарегистрирован"),
            @ApiResponse(responseCode = "400", description = "Некорректные входные данные")
    })
    public InstructorProfileResponse register(
            @Valid @RequestBody CreateInstructorRequest request)
    {
        return instructorService.register(request);
    }


    @GetMapping("/{instructorId}")
    @Operation(
            summary = "Получить профиль инструктора",
            description = "Возвращает детальную информацию о профиле инструктора по его ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Профиль найден"),
            @ApiResponse(responseCode = "404", description = "Инструктор не найден")
    })
    public InstructorProfileResponse getById(
            @Parameter(description = "ID инструктора", required = true, example = "1")
            @PathVariable Long instructorId)
    {
        return instructorService.getById(instructorId);
    }

    @GetMapping("/{instructorId}/courses")
    @Operation(
            summary = "Получить курсы инструктора",
            description = "Возвращает список всех курсов, созданных указанным инструктором."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список курсов получен"),
            @ApiResponse(responseCode = "404", description = "Инструктор не найден")
    })
    public CourseListResponse getCourses(
            @Parameter(description = "ID инструктора", required = true, example = "1")
            @PathVariable Long instructorId)
    {
        return instructorService.getCourses(instructorId);
    }

    @GetMapping("/{instructorId}/statistics")
    @Operation(
            summary = "Статистика инструктора",
            description = "Возвращает агрегированную статистику по всем курсам инструктора: " +
                    "количество курсов, суммарное число зачислений, завершивших, " +
                    "средний прогресс и средний балл по тестам."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Статистика успешно рассчитана"),
            @ApiResponse(responseCode = "404", description = "Инструктор не найден")
    })
    public InstructorStatisticsResponse getInstructorStats(
            @Parameter(description = "ID инструктора", required = true, example = "1")
            @PathVariable Long instructorId) {
        return instructorService.getInstructorStats(instructorId);
    }
}
