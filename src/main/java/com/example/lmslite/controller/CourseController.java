package com.example.lmslite.controller;


import com.example.lmslite.dto.course.create.CreateCourseRequest;
import com.example.lmslite.dto.course.create.CreateCourseResponse;
import com.example.lmslite.dto.course.detail.CourseDetailResponse;
import com.example.lmslite.dto.course.list.CourseListResponse;
import com.example.lmslite.dto.course.stats.CourseStatisticsResponse;
import com.example.lmslite.dto.course.stats.TopCourseResponse;
import com.example.lmslite.dto.course.update.ChangeCourseStatusResponse;
import com.example.lmslite.dto.course.update.UpdateCourseRequest;
import com.example.lmslite.dto.course.update.UpdateCourseResponse;
import com.example.lmslite.dto.lesson.create.CreateLessonRequest;
import com.example.lmslite.dto.lesson.create.CreateLessonResponse;
import com.example.lmslite.dto.lesson.list.LessonListResponse;
import com.example.lmslite.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
@Validated
@Tag(name = "Курсы", description = "API для управления курсами")
public class CourseController {
    private final CourseService courseService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @Operation(
            summary = "Создать новый курс",
            description = "Создаёт новый курс на основе переданных данных. Возвращает созданный курс."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Курс успешно создан"),
            @ApiResponse(responseCode = "400", description = "Некорректные входные данные")
    })
    public CreateCourseResponse createCourse(
            @Valid @RequestBody CreateCourseRequest request)
    {
        return courseService.createCourse(request);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{courseId}/lessons")
    @Operation(
            summary = "Добавить урок в курс",
            description = "Создаёт новый урок и привязывает его к курсу с указанным ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Урок успешно добавлен"),
            @ApiResponse(responseCode = "400", description = "Некорректные входные данные"),
            @ApiResponse(responseCode = "404", description = "Курс не найден")
    })
    public CreateLessonResponse createCourse(
            @Parameter(description = "ID курса", required = true)
            @PathVariable Long courseId,
            @Valid @RequestBody CreateLessonRequest request)
    {
        return courseService.addLesson(courseId, request);
    }


    @GetMapping("/{courseId}/lessons")
    @Operation(
            summary = "Получить уроки курса",
            description = "Возвращает список всех уроков, привязанных к указанному курсу."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список уроков получен"),
            @ApiResponse(responseCode = "404", description = "Курс не найден")
    })
    public LessonListResponse getCourseLessons(
            @Parameter(description = "ID курса", required = true)
            @PathVariable Long courseId)
    {
        return courseService.getCourseLessons(courseId);
    }

    @PutMapping("/{courseId}")
    @Operation(
            summary = "Обновить курс",
            description = "Обновляет основные поля курса по его ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Курс успешно обновлён"),
            @ApiResponse(responseCode = "400", description = "Некорректные входные данные"),
            @ApiResponse(responseCode = "404", description = "Курс не найден")
    })
    public UpdateCourseResponse updateCourseById(
            @Parameter(description = "ID курса", required = true)
            @PathVariable Long courseId,
            @Valid @RequestBody UpdateCourseRequest request)
    {
        return courseService.updateCourseById(request, courseId);
    }

    @DeleteMapping("/{courseId}")
    @Operation(
            summary = "Удалить курс",
            description = "Удаляет курс по ID. Возвращает 204 No Content при успехе."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Курс успешно удалён"),
            @ApiResponse(responseCode = "404", description = "Курс не найден")
    })
    public ResponseEntity<Void> deleteCourseById(@Parameter(description = "ID курса", required = true) @PathVariable Long courseId)
    {
        courseService.deleteCourseById(courseId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{courseId}/publish")
    @Operation(
            summary = "Опубликовать курс",
            description = "Переводит курс в статус PUBLISHED (если он находится в DRAFT)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Статус успешно изменён"),
            @ApiResponse(responseCode = "404", description = "Курс не найден"),
            @ApiResponse(responseCode = "409", description = "Недопустимый переход статуса")
    })
    public ChangeCourseStatusResponse publishCourse(@Parameter(description = "ID курса", required = true) @PathVariable Long courseId)
    {
        return courseService.publishCourse(courseId);
    }

    @PatchMapping("/{courseId}/archive")
    @Operation(
            summary = "Архивировать курс",
            description = "Переводит курс в статус ARCHIVED."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Статус успешно изменён"),
            @ApiResponse(responseCode = "404", description = "Курс не найден")
    })
    public ChangeCourseStatusResponse archiveCourse(@Parameter(description = "ID курса", required = true) @PathVariable Long courseId)
    {
        return courseService.archiveCourse(courseId);
    }

    @GetMapping("/published")
    @Operation(
            summary = "Получить опубликованные курсы",
            description = "Возвращает список всех курсов со статусом PUBLISHED."
    )
    @ApiResponse(responseCode = "200", description = "Список курсов получен")
    public CourseListResponse getPublishedCourses()
    {
        return courseService.getPublishedCourses();
    }

    @GetMapping("/{courseId}")
    @Operation(
            summary = "Получить курс по ID",
            description = "Возвращает детальную информацию о курсе, включая список уроков."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Курс найден"),
            @ApiResponse(responseCode = "404", description = "Курс не найден")
    })
    public CourseDetailResponse getCourseById(@Parameter(description = "ID курса") @PathVariable Long courseId)
    {
        return courseService.getCourseById(courseId);
    }

    @GetMapping("/{courseId}/statistics")
    @Operation(
            summary = "Статистика курса",
            description = "Возвращает агрегированную статистику по курсу: количество записей, " +
                    "активных/завершивших/бросивших студентов, completion rate, средний прогресс и средний балл тестов."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Статистика успешно рассчитана"),
            @ApiResponse(responseCode = "404", description = "Курс не найден")
    })
    public CourseStatisticsResponse getCourseStats(@Parameter(description = "ID курса") @PathVariable Long courseId)
    {
        return courseService.getCourseStats(courseId);
    }

    @GetMapping("/top-courses")
    @Operation(
            summary = "Топ курсов за последние 30 дней",
            description = "Возвращает список самых популярных курсов (по количеству новых записей) за последние 30 дней."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список топ-курсов получен"),
            @ApiResponse(responseCode = "400", description = "Некорректное значение параметра limit")
    })
    public List<TopCourseResponse> getTopCoursesLast30Days(@RequestParam(defaultValue = "10") @Min(value = 1, message = "limit must be >= 1")
                                                               @Max(value = 100, message = "limit must be <= 100") int limit)
    {
        return courseService.getTopCoursesLast30Days(limit);
    }
}
