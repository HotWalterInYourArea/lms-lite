package com.example.lmslite.mapper;

import com.example.lmslite.dto.enrollment.detail.EnrollmentDetailResponse;
import com.example.lmslite.dto.enrollment.list.EnrollmentResponse;
import com.example.lmslite.dto.enrollment.list.EnrollmentResponseList;
import com.example.lmslite.model.Course;
import com.example.lmslite.model.Enrollment;
import com.example.lmslite.model.Student;

import java.util.List;

public class EnrollmentMapper
{
    public static EnrollmentResponse toResponse(Enrollment enrollment)
    {
        return  new EnrollmentResponse(
                enrollment.getId(),
                enrollment.getCourse().getTitle(),
                enrollment.getCourse().getDescription(),
                enrollment.getStatus(),
                enrollment.getEnrolledAt(),
                enrollment.getCompletedAt());
    }
    public static Enrollment toEntity(Course courseToEnroll, Student studentToEnroll)
    {
        Enrollment newEnrollment = new Enrollment();
        newEnrollment.setCourse(courseToEnroll);
        newEnrollment.setStudent(studentToEnroll);
        return newEnrollment;
    }

    public static EnrollmentResponseList toResponseList(List<Enrollment> enrollments)
    {
        List<EnrollmentResponse> toResponseList = enrollments.stream()
                .map(EnrollmentMapper::toResponse)
                .toList();
        return new EnrollmentResponseList(toResponseList);
    }

    public static EnrollmentDetailResponse toDetailResponse(Enrollment enrollment)
    {
        return  new EnrollmentDetailResponse(
                enrollment.getId(),
                enrollment.getCourse().getTitle(),
                enrollment.getCourse().getDescription(),
                enrollment.getStatus(),
                enrollment.getEnrolledAt(),
                enrollment.getCompletedAt(),
                enrollment.getProgressPercent());
    }

}
