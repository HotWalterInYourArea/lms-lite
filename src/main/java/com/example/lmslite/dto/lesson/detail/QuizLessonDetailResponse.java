package com.example.lmslite.dto.lesson.detail;

import com.example.lmslite.dto.lesson.questions.QuestionDetailResponse;
import com.example.lmslite.enums.LessonType;


import java.util.List;

public record QuizLessonDetailResponse(
        Long id,

        String title,

        Integer orderIndex,

        LessonType type,

        Integer passingScore,

        Integer estimatedTime,

        List<QuestionDetailResponse> questions
) implements LessonDetailResponse {}
