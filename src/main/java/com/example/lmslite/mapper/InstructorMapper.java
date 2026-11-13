package com.example.lmslite.mapper;

import com.example.lmslite.dto.instructor.create.CreateInstructorRequest;
import com.example.lmslite.dto.instructor.detail.InstructorProfileResponse;
import com.example.lmslite.model.Instructor;


public class InstructorMapper {
    public static InstructorProfileResponse toProfileResponse(Instructor instructor)
    {
        return new InstructorProfileResponse(
                instructor.getId(),
                instructor.getFullName(),
                instructor.getEmail(),
                instructor.getBio());
    }

    public static Instructor toEntity(CreateInstructorRequest request)
    {
        Instructor newInstructor = new Instructor();
        newInstructor.setFirstName(request.firstName());
        newInstructor.setLastName(request.lastName());
        newInstructor.setEmail(request.email());
        newInstructor.setBio(request.bio());
        return newInstructor;
    }
}
