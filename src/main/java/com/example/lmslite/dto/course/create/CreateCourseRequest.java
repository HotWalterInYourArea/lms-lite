package com.example.lmslite.dto.course.create;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateCourseRequest(
        @NotBlank(message = "No title provided for course creation")
        @Size(max = 255, message = "Title is too big(>255)")
        String title,

        @Size(max = 2000, message = "Description is too big(>2000)")
        String description,

        @NotNull(message = "Course author ID is required")
        Long authorId
){}
