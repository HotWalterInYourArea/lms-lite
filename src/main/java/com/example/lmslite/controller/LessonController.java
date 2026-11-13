package com.example.lmslite.controller;


import com.example.lmslite.dto.enrollment.progress.submit.QuizResultResponse;
import com.example.lmslite.dto.enrollment.progress.submit.SubmitQuizRequest;


import com.example.lmslite.dto.lesson.detail.LessonDetailResponse;
import com.example.lmslite.dto.lesson.questions.QuestionListResponse;
import com.example.lmslite.dto.lesson.update.UpdateLessonRequest;
import com.example.lmslite.dto.lesson.update.UpdateLessonResponse;
import com.example.lmslite.service.EnrollmentService;
import com.example.lmslite.service.LessonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/lessons")
@RequiredArgsConstructor
@Tag(name = "Уроки", description = "API для управления уроками, тестами и их прохождением")
public class LessonController
{
    private final LessonService lessonService;
    private final EnrollmentService enrollmentService;


    @PutMapping("{lessonId}")
    @Operation(
            summary = "Обновить урок",
            description = "Обновляет данные урока по его ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Урок успешно обновлён"),
            @ApiResponse(responseCode = "400", description = "Некорректные входные данные"),
            @ApiResponse(responseCode = "404", description = "Урок не найден")
    })
    public UpdateLessonResponse register(
            @Parameter(description = "ID урока", required = true)
            @PathVariable Long lessonId,
            @Valid @RequestBody UpdateLessonRequest request)
    {
        return lessonService.updateById(lessonId, request);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Получить урок по ID",
            description = "Возвращает детальную информацию об уроке."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Урок найден"),
            @ApiResponse(responseCode = "404", description = "Урок не найден")
    })
    public LessonDetailResponse getById(
            @Parameter(description = "ID урока", required = true)
            @PathVariable Long id)
    {
        return lessonService.getById(id);
    }

    @PostMapping("/{quizId}/submit")
    @Operation(
            summary = "Сдать тест",
            description = "Отправляет ответы на вопросы теста и возвращает результат. " +
                    "Требуется enrollmentId — зачисление на курс, к которому относится тест."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Результат теста рассчитан"),
            @ApiResponse(responseCode = "400", description = "Некорректные ответы или отсутствует enrollmentId"),
            @ApiResponse(responseCode = "404", description = "Тест или enrollment не найдены")
    })
    public QuizResultResponse submitQuiz(
            @Parameter(description = "ID зачисления", required = true, example = "42")
            @RequestParam Long enrollmentId,
            @Parameter(description = "ID теста)", required = true)
            @PathVariable Long quizId,
            @Valid @RequestBody SubmitQuizRequest answers)
    {
        return enrollmentService.submitQuiz(enrollmentId, quizId, answers);
    }

    @GetMapping("/{quizId}/questions")
    @Operation(
            summary = "Получить вопросы теста",
            description = "Возвращает список вопросов для указанного теста (без правильных ответов)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список вопросов получен"),
            @ApiResponse(responseCode = "404", description = "Тест не найден")
    })
    public QuestionListResponse getQuizQuestions(
            @Parameter(description = "ID теста", required = true)
            @PathVariable Long quizId)
    {
        return lessonService.getQuestions(quizId);
    }

}
