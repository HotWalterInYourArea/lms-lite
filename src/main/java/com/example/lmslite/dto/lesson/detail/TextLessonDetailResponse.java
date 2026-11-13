package com.example.lmslite.dto.lesson.detail;

import com.example.lmslite.enums.LessonType;

public record TextLessonDetailResponse(
        Long id,

        String title,

        Integer orderIndex,

        LessonType type,

        String content,

        Integer estimatedTime
) implements LessonDetailResponse {}
