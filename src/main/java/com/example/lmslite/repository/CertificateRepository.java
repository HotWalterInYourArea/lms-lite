package com.example.lmslite.repository;

import com.example.lmslite.model.Certificate;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CertificateRepository extends JpaRepository<Certificate, Long>
{
    @EntityGraph(attributePaths =
            {
                  "course.id",
                  "student.id"
            }
    )
    List<Certificate> findByStudentId(Long studentId);

    @EntityGraph(attributePaths =
            {
                    "course.id",
                    "student.id"
            }
    )
    Optional<Certificate> findByUniqueCode(UUID certificateUniqueCode);
    @EntityGraph(attributePaths =
            {
                    "course",
                    "student"
            }
    )
    Optional<Certificate> findWithCourseAndStudentById(Long certificateId);
    boolean existsByStudentIdAndCourseIdNot(Long studentId, Long courseId);

}
