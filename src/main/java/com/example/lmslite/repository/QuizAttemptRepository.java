package com.example.lmslite.repository;

import com.example.lmslite.dto.enrollment.progress.quiz_results.QuizResult;
import com.example.lmslite.model.QuizAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface QuizAttemptRepository extends JpaRepository<QuizAttempt,Long>
{
    @Query("""
        SELECT new com.example.lmslite.dto.enrollment.progress.quiz_results.QuizResult(
            qa.quiz.id,
            qa.quiz.title,
            MAX(qa.quizScore),
            CASE WHEN SUM(CASE WHEN qa.passed = true THEN 1 ELSE 0 END) > 0 THEN true ELSE false END,
            COUNT(qa),
            MAX(qa.attemptedAt)
        )
        FROM QuizAttempt qa
        WHERE qa.enrollment.id = :enrollmentId
        GROUP BY qa.quiz.id, qa.quiz.title
        ORDER BY MAX(qa.attemptedAt) DESC
        """)
    List<QuizResult> findQuizResultsByEnrollmentId(@Param("enrollmentId") Long enrollmentId);
}
