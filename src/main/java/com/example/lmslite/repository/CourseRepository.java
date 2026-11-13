package com.example.lmslite.repository;

import com.example.lmslite.dto.course.stats.CourseStatisticsResponse;
import com.example.lmslite.dto.course.stats.TopCourseResponse;
import com.example.lmslite.enums.CourseStatus;
import com.example.lmslite.model.Course;
import com.example.lmslite.model.QuizLesson;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long>
{

    @EntityGraph(
            attributePaths = {
                    "lessons",
                    "author"
            }
    )
    List<Course> findWithLessonsByStatus(CourseStatus status);

    @EntityGraph(
            attributePaths = {
                    "lessons",
                    "author"
            }
    )
    Optional<Course> findWithLessonsById(Long courseId);

    @Query("SELECT c FROM Course c " +
            "JOIN FETCH c.author " +
            "LEFT JOIN FETCH c.lessons l " +
            "WHERE c.author.id = :authorId " +
            "ORDER BY l.orderIndex")
    List<Course> findAllByAuthorIdWithLessons(@Param("authorId") Long authorId);

    @Query("SELECT l FROM QuizLesson l " +
            "LEFT JOIN FETCH l.questions " +
            "WHERE l.course.author.id = :authorId")
    List<QuizLesson> fetchQuestionsForQuizLessonsByAuthor(@Param("authorId") Long authorId);

    @Query("SELECT l FROM QuizLesson l " +
            "LEFT JOIN FETCH l.questions " +
            "WHERE l.course.id = :courseId")
    List<QuizLesson> fetchQuestionsForQuizLessonsById(@Param("courseId") Long courseId);

    @Query("SELECT l FROM QuizLesson l " +
            "LEFT JOIN FETCH l.questions " +
            "WHERE l.course.status = :status")
    List<QuizLesson> fetchQuestionsForQuizLessonsByStatus(@Param("status") CourseStatus status);

    @Query("""
    SELECT new com.example.lmslite.dto.course.stats.CourseStatisticsResponse(
        c.id,
        c.title,
        
        (SELECT COUNT(e) FROM Enrollment e WHERE e.course.id = c.id),
        (SELECT COUNT(e) FROM Enrollment e WHERE e.course.id = c.id AND e.status = 'ACTIVE'),
        (SELECT COUNT(e) FROM Enrollment e WHERE e.course.id = c.id AND e.status = 'COMPLETED'),
        (SELECT COUNT(e) FROM Enrollment e WHERE e.course.id = c.id AND e.status = 'DROPPED'),
        
        (SELECT (CAST(SUM(CASE WHEN e.status = 'COMPLETED' THEN 1 ELSE 0 END) AS double) / NULLIF(COUNT(e), 0)) * 100.0
         FROM Enrollment e WHERE e.course.id = c.id),
         
        (SELECT AVG(
            (CAST((SELECT COUNT(lp) FROM LessonProgress lp WHERE lp.enrollment.id = e2.id) AS double)
             / NULLIF((SELECT COUNT(l) FROM Lesson l WHERE l.course.id = c.id), 0)) * 100.0
         ) 
         FROM Enrollment e2 
         WHERE e2.course.id = c.id),
         
        (SELECT AVG(qa.quizScore) 
         FROM QuizAttempt qa 
         WHERE qa.enrollment.course.id = c.id)
    )
    FROM Course c
    WHERE c.id = :courseId
""")
    CourseStatisticsResponse getCourseStatistics(@Param("courseId") Long courseId);
    @Query("""
    SELECT new com.example.lmslite.dto.course.stats.TopCourseResponse(
        c.id,
        c.title,
        COUNT(e.id)
    )
    FROM Course c
    JOIN Enrollment e ON e.course.id = c.id
    WHERE e.enrolledAt >= :sinceDate
    GROUP BY c.id, c.title
    ORDER BY COUNT(e.id) DESC
    LIMIT :limit
""")
    List<TopCourseResponse> findTopCoursesSince(
            @Param("sinceDate") LocalDateTime sinceDate,
            @Param("limit") int limit
    );
    List<Course> findByAuthorId(Long authorId);

    Optional<Course> findByLessonsId(Long lessonId);

    List<Course> findByTitleIgnoreCase(String title);

    List<Course> findByIdIn(Collection<Long> ids);

}
