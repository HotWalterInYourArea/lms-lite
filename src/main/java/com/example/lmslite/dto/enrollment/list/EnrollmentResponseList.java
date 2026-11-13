package com.example.lmslite.dto.enrollment.list;

import java.util.List;

public record EnrollmentResponseList(List<EnrollmentResponse> enrollments)
{
    public EnrollmentResponseList
    {
        if (enrollments == null)
        {
            enrollments = List.of();
        }
    }
}
