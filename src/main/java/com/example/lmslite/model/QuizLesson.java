package com.example.lmslite.model;

import com.example.lmslite.enums.LessonType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Check;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@DiscriminatorValue("QUIZ")
@NoArgsConstructor
public class QuizLesson extends Lesson
{

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> questions = new ArrayList<>();

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuizAttempt> quizAttempts = new ArrayList<>();

    @Check(constraints = "passing_score >= 0")
    private int passingScore;

    @Check(constraints = "time_limit_minutes > 0")
    private Integer timeLimitMinutes;

    @Override
    public LessonType getType()
    {
        return LessonType.QUIZ;
    }

    @Override
    public int getEstimatedMinutes()
    {
        if (this.timeLimitMinutes != null) return this.timeLimitMinutes;
        return this.questions.size();
    }
}
