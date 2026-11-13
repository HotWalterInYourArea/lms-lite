package com.example.lmslite.dto.lesson.detail;

import com.example.lmslite.enums.LessonType;

public interface LessonDetailResponse
{
    Long id();

    String title();

    LessonType type();
}
