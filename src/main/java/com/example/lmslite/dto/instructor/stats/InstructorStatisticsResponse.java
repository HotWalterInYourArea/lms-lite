package com.example.lmslite.dto.instructor.stats;

public record InstructorStatisticsResponse(
        Long instructorId,
        String instructorName,
        long totalCourses,
        long publishedCourses,
        long totalStudents,
        long completedStudents,
        Double averageProgress,
        long issuedCertificates
) {
    public InstructorStatisticsResponse
    {
        averageProgress  = averageProgress  != null ? averageProgress  : 0.0;
    }
}
