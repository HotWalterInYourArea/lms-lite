package com.example.lmslite.model;

import com.example.lmslite.enums.LessonType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Check;


@Entity
@Getter
@Setter
@DiscriminatorValue("TEXT")
@NoArgsConstructor
public class TextLesson extends Lesson
{

    @Lob
    @Column(columnDefinition = "TEXT")
    private String content;

    @Check(constraints = "reading_time_minutes > 0")
    private int readingTimeMinutes;

    @Override
    public LessonType getType() {
        return LessonType.TEXT;
    }

    @Override
    public int getEstimatedMinutes() {
        return this.readingTimeMinutes;
    }
}
