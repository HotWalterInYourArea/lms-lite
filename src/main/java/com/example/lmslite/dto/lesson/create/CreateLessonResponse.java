package com.example.lmslite.dto.lesson.create;

import com.example.lmslite.enums.LessonType;

public record CreateLessonResponse(
        Long id,

        String title,

        Integer orderIndex,

        LessonType type
) {}
