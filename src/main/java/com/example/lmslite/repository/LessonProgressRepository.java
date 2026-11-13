package com.example.lmslite.repository;

import com.example.lmslite.model.LessonProgress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LessonProgressRepository extends JpaRepository<LessonProgress, Long>
{
    boolean existsByLessonIdAndEnrollmentId(Long studentId, Long enrollmentId);
}
