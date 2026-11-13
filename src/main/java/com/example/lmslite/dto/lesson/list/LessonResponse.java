package com.example.lmslite.dto.lesson.list;

import com.example.lmslite.enums.LessonType;

public record LessonResponse(
        Long id,

        String title,

        Integer orderIndex,

        LessonType type
) {}
