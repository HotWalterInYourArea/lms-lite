package com.example.lmslite.repository;

import com.example.lmslite.enums.EnrollmentStatus;
import com.example.lmslite.model.Enrollment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface EnrollmentRepository extends JpaRepository<Enrollment, Long>
{
    boolean existsByStudentIdAndCourseId(Long studentId, Long courseId);

    boolean existsByCourseIdAndStatus(Long courseId, EnrollmentStatus status);

    @EntityGraph(attributePaths = {
            "course"
    })
    List<Enrollment> findByStudentId(Long studentId);

    @EntityGraph(attributePaths = {
            "course.lessons",
            "completedLessons"
    })
    Optional<Enrollment> findWithCourseAndLessonProgressById(Long enrollmentId);


}