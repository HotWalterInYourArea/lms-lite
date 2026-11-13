package com.example.lmslite.mapper;

import com.example.lmslite.dto.student.create.CreateStudentRequest;
import com.example.lmslite.dto.student.detail.StudentProfileResponse;
import com.example.lmslite.model.Student;

public class StudentMapper {
    public static StudentProfileResponse toProfileResponse(Student student)
    {
        return new StudentProfileResponse(
                student.getId(),
                student.getFullName(),
                student.getEmail());
    }

    public static Student toEntity(CreateStudentRequest request)
    {
        Student newStudent = new Student();
        newStudent.setFirstName(request.firstName());
        newStudent.setLastName(request.lastName());
        newStudent.setEmail(request.email());
        return newStudent;
    }
}
