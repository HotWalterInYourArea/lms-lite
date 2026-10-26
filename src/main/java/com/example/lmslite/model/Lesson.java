package com.example.lmslite.model;


import com.example.lmslite.enums.LessonType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
@Entity
@Getter
@Setter
@Table(name = "lessons")
@DiscriminatorColumn(name = "lesson_type")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)

public abstract class Lesson
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private int orderIndex;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    abstract protected LessonType getType();

    abstract protected int getEstimatedMinutes();

}
