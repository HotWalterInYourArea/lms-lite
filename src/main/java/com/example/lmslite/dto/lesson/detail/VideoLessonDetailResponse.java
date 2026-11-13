package com.example.lmslite.dto.lesson.detail;

import com.example.lmslite.enums.LessonType;

public record VideoLessonDetailResponse(
        Long id,

        String title,

        Integer orderIndex,

        LessonType type,

        String videoUrl,

        Integer estimatedTime
) implements LessonDetailResponse{}
