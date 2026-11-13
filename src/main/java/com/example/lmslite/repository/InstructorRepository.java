package com.example.lmslite.repository;

import com.example.lmslite.dto.instructor.stats.InstructorStatisticsResponse;
import com.example.lmslite.model.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface InstructorRepository extends JpaRepository<Instructor, Long>
{
    @Query("""
    SELECT new com.example.lmslite.dto.instructor.stats.InstructorStatisticsResponse(
        i.id,
        i.firstName || ' ' || i.lastName,
        (SELECT COUNT(c) FROM Course c WHERE c.author.id = i.id),
        (SELECT COUNT(c) FROM Course c WHERE c.author.id = i.id AND c.status = 'PUBLISHED'),
        (SELECT COUNT(e) FROM Enrollment e WHERE e.course.author.id = i.id),
        (SELECT COUNT(e) FROM Enrollment e WHERE e.course.author.id = i.id AND e.status = 'COMPLETED'),
        
        (SELECT AVG(
            (CAST((SELECT COUNT(lp) FROM LessonProgress lp WHERE lp.enrollment.id = e2.id) AS double)
             / NULLIF((SELECT COUNT(l) FROM Lesson l WHERE l.course.id = e2.course.id), 0)) * 100.0
         )
         FROM Enrollment e2
         WHERE e2.course.author.id = i.id),
        (SELECT COUNT(ce) FROM Certificate ce WHERE ce.course.author.id = i.id)
    )
    FROM Instructor i
    WHERE i.id = :instructorId
""")
    InstructorStatisticsResponse getInstructorStatistics(@Param("instructorId") Long instructorId);
}

