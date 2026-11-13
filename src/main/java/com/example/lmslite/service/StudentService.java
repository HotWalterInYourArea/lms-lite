package com.example.lmslite.service;

import com.example.lmslite.dto.student.create.CreateStudentRequest;
import com.example.lmslite.dto.student.detail.StudentProfileResponse;
import com.example.lmslite.exception.notFound.StudentNotFoundException;
import com.example.lmslite.mapper.StudentMapper;
import com.example.lmslite.model.Student;
import com.example.lmslite.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentService
{
    private final StudentRepository studentRepository;

    public StudentProfileResponse registerStudent(CreateStudentRequest request)
    {
        Student newStudent = StudentMapper.toEntity(request);
        studentRepository.save(newStudent);
        return StudentMapper.toProfileResponse(newStudent);
    }

    public StudentProfileResponse getStudentById(Long studentId)
    {
        Student studentToGet = studentRepository.findById(studentId).orElseThrow(
                () -> new StudentNotFoundException(studentId));

        return StudentMapper.toProfileResponse(studentToGet);
    }
}
