package com.example.lmslite.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "certificates",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_student_course",
                columnNames = {"student_id", "course_id"}))

@Getter
@Setter
@NoArgsConstructor

public class Certificate
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(nullable = false)
    private LocalDate issuedAt;

    @Column(name = "unique_code", nullable = false, unique = true, length = 36)
    private UUID uniqueCode;

    @PrePersist
    void prePersist() {
        if (issuedAt == null) issuedAt = LocalDate.now();
        if (uniqueCode == null) uniqueCode = UUID.randomUUID();
    }
}
