package com.example.lmslite.model;

import com.example.lmslite.enums.LessonType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@DiscriminatorValue("VIDEO")
@NoArgsConstructor
public class VideoLesson extends Lesson
{
    @Column(nullable = false)
    private String videoUrl;

    @Column(nullable = false)
    private int durationMinutes;

    @Override
    protected LessonType getType() {
        return LessonType.VIDEO;
    }

    @Override
    protected int getEstimatedMinutes() {
        return this.durationMinutes;
    }
}
