package com.example.lmslite.model;

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
@Table(name = "questions")
@NoArgsConstructor
public class Question
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String text;

    @ElementCollection
    @CollectionTable(name = "question_options", joinColumns = @JoinColumn(name = "question_id"))
    @OrderColumn(name = "options_order_index")
    private List<String> options = new ArrayList<>();

    @Column(nullable = false)
    @Check(constraints = "correct_option_index >= 0")
    private int correctOptionIndex;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "quiz_id")
    private QuizLesson quiz;

    public boolean isCorrectAnswer(int index)
    {
        return index == correctOptionIndex;
    }
}
