package com.example.lmslite.dto.lesson.questions;

import java.util.List;

public record QuestionListResponse(List<QuestionResponse> questions)
{
    public QuestionListResponse
    {
        if (questions == null)
        {
            questions = List.of();
        }
    }
}
