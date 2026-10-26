package com.example.lmslite.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "students")
@Getter
@Setter
@NoArgsConstructor
public class Student extends User {

    //@OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    //private List<Enrollment> enrollments = new ArrayList<>();
}

