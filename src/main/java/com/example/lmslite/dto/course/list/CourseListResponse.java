package com.example.lmslite.dto.course.list;


import java.util.List;

public record CourseListResponse(List<CourseResponse> courses)
{
    public CourseListResponse
    {
        if (courses == null)
        {
            courses = List.of();
        }
    }
}
