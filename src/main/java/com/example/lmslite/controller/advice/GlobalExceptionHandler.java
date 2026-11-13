package com.example.lmslite.controller.advice;


import com.example.lmslite.exception.conflict.*;
import com.example.lmslite.exception.notFound.EntityNotFoundException;
import com.example.lmslite.exception.unprocessable.UnprocessableEntityException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail handleConstraintViolation(ConstraintViolationException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "Validation failed for request parameters"
        );

        var errors = ex.getConstraintViolations().stream()
                .map(v -> v.getPropertyPath().toString() + ": " + v.getMessage())
                .collect(Collectors.toList());

        problem.setProperty("errors", errors);
        return problem;
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex) {

        Map<String, String> errors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        fieldError -> fieldError.getDefaultMessage() != null
                                ? fieldError.getDefaultMessage()
                                : "Invalid value",
                        (existing, replacement) -> existing
                ));

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, "Validation failed for one or more fields");

        problem.setTitle("Validation Failed");

        problem.setProperty("errors", errors);

        return problem;
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ProblemDetail handleEntityNotFound(EntityNotFoundException ex) {
        return problem(HttpStatus.NOT_FOUND, "Resource Not Found", ex.getMessage());
    }


    @ExceptionHandler({
            ConflictException.class
    })
    public ProblemDetail handleConflictExceptions(RuntimeException ex) {
        return problem(HttpStatus.CONFLICT, "Operation Conflict",  ex.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail handleDataIntegrity(DataIntegrityViolationException ex) {
        log.warn("Data integrity violation", ex);
        return problem(HttpStatus.CONFLICT, "Data Conflict",
                "The request conflicts with existing data");
    }


    @ExceptionHandler({
            UnprocessableEntityException.class
    })
    public ProblemDetail handleUnprocessableExceptions(RuntimeException ex) {
        return problem(HttpStatus.UNPROCESSABLE_ENTITY, "Invalid Request Semantics", ex.getMessage());
    }

    @ExceptionHandler({
            IllegalStateException.class
    })
    public ProblemDetail handleIllegalState(RuntimeException ex) {
        return problem(HttpStatus.BAD_REQUEST, "Illegal state", ex.getMessage());
    }


    @ExceptionHandler(Exception.class)
    public ProblemDetail handleUnexpected(Exception ex, HttpServletRequest request) {
        if (ex instanceof org.springframework.web.ErrorResponse) {
            return ((org.springframework.web.ErrorResponse) ex).getBody();
        }

        log.error("Unhandled exception at {} {}", request.getMethod(), request.getRequestURI(), ex);
        return problem(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", "An unexpected error occurred");
    }



    private static ProblemDetail problem(HttpStatus status, String title, String detail) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(status, detail);
        pd.setTitle(title);
        return pd;
    }

}