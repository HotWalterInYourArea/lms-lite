package com.example.lmslite.dto.course.stats;

public record CourseStatisticsResponse(
        Long courseId,
        String courseTitle,
        long totalEnrollments,
        long activeStudents,
        long completedStudents,
        long droppedStudents,
        Double completionRate,
        Double averageProgress,
        Double averageQuizScore
) {
    public CourseStatisticsResponse {
        completionRate  = completionRate  != null ? completionRate  : 0.0;
        averageProgress = averageProgress != null ? averageProgress : 0.0;
        averageQuizScore = averageQuizScore != null ? averageQuizScore : 0.0;
    }
}
