package com.example.lmslite.repository;

import com.example.lmslite.dto.enrollment.progress.complete.LessonProgressResponse;
import com.example.lmslite.model.Lesson;
import com.example.lmslite.model.QuizLesson;
import com.example.lmslite.model.TextLesson;
import com.example.lmslite.model.VideoLesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LessonRepository extends JpaRepository<Lesson, Long>
{
    List<Lesson> findByCourseIdOrderByOrderIndexAsc(Long courseId);
    Optional<Lesson> findTopByCourseIdOrderByOrderIndexDesc(Long courseId);
    Optional<QuizLesson> findQuizLessonById(Long id);
    @Query("""
    SELECT new com.example.lmslite.dto.enrollment.progress.complete.LessonProgressResponse(
        les.id,
        les.title,
        CASE WHEN lp.id IS NOT NULL THEN true ELSE false END
    )
    FROM Lesson les
    LEFT JOIN LessonProgress lp ON lp.lesson = les AND lp.enrollment.id = :enrollmentId
    WHERE les.course.id = :courseId
""")
    List<LessonProgressResponse> findLessonProgressResponse(
            @Param("enrollmentId") Long enrollmentId,
            @Param("courseId") Long courseId
    );
    @Query("SELECT DISTINCT ql FROM QuizLesson ql " +
            "LEFT JOIN FETCH ql.questions q " +
            "LEFT JOIN FETCH q.options o " +
            "WHERE ql.id = :quizLessonId")
    Optional<QuizLesson> findQuizLessonWithQuestionsById(@Param("quizLessonId") Long quizLessonId);
    Optional<VideoLesson> findVideoLessonById(Long id);
    Optional<TextLesson> findTextLessonById(Long id);
}
