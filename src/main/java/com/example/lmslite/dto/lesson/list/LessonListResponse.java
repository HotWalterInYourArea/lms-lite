package com.example.lmslite.dto.lesson.list;

import java.util.List;

public record LessonListResponse(List<LessonResponse> lessons)
{
    public LessonListResponse
    {
        if (lessons == null)
        {
            lessons = List.of();
        }
    }
}
