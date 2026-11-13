package com.example.lmslite.dto.lesson.update;

import com.example.lmslite.enums.LessonType;

public record UpdateLessonResponse(
        Long id,

        String title,

        Integer orderIndex,

        LessonType type
) {}
