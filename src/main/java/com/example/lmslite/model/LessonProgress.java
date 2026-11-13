package com.example.lmslite.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "lessons_progress",
        uniqueConstraints =
            @UniqueConstraint(
                    name = "uk_lesson_enrollment",
                    columnNames = {"lesson_id", "enrollment_id"})
)
@Getter
@Setter
@NoArgsConstructor
public class LessonProgress
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "lesson_id", nullable = false)
    private Lesson lesson;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "enrollment_id", nullable = false)
    private Enrollment enrollment;


    @Column(nullable = false, updatable = false)
    private LocalDateTime completedAt;

    private Integer quizScore;


    @PrePersist
    protected void onCreate() {
        if (completedAt == null)
        {
            completedAt = LocalDateTime.now();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        LessonProgress that = (LessonProgress) o;
        return getLesson() != null && getEnrollment() != null
                && Objects.equals(getLesson().getId(), that.getLesson().getId())
                && Objects.equals(getEnrollment().getId(), that.getEnrollment().getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                getLesson() != null ? getLesson().getId() : null,
                getEnrollment() != null ? getEnrollment().getId() : null
        );
    }


}
