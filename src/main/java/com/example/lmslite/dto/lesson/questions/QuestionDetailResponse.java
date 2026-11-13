package com.example.lmslite.dto.lesson.questions;


import java.util.List;

public record QuestionDetailResponse(
        Long id,

        String text,

        List<String> options,

        Integer correctOptionIndex
) {}
