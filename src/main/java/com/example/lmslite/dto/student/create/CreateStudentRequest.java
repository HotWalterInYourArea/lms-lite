package com.example.lmslite.dto.student.create;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateStudentRequest(
        @NotBlank(message = "First name is required")
        @Size(max = 255, message = "First name is too big(>255)")
        String firstName,

        @NotBlank(message = "Last name is required")
        @Size(max = 255, message = "Last name is too big(>255)")
        String lastName,

        @NotNull(message = "Email is required")
        @Email(message = "Email must be of correct format")
        @Size(max = 255, message = "Email is too big(>255)")
        String email
){}
