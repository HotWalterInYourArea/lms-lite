package com.example.lmslite.model;

import com.example.lmslite.enums.LessonType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@DiscriminatorValue("TEXT")
@NoArgsConstructor
public class TextLesson extends Lesson
{

    @Lob
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(nullable = false)
    private int readingTimeMinutes;

    @Override
    protected LessonType getType() {
        return LessonType.TEXT;
    }

    @Override
    protected int getEstimatedMinutes() {
        return this.readingTimeMinutes;
    }
}
