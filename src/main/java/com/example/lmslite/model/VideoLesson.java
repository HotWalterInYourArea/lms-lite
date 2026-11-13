package com.example.lmslite.model;

import com.example.lmslite.enums.LessonType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Check;

@Getter
@Setter
@Entity
@DiscriminatorValue("VIDEO")
@NoArgsConstructor
public class VideoLesson extends Lesson
{
    private String videoUrl;

    @Check(constraints = "duration_minutes > 0")
    private int durationMinutes;

    @Override
    public LessonType getType() {
        return LessonType.VIDEO;
    }

    @Override
    public int getEstimatedMinutes() {
        return this.durationMinutes;
    }
}
