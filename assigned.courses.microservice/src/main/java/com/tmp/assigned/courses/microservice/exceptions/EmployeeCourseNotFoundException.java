package com.tmp.assigned.courses.microservice.exceptions;

public class EmployeeCourseNotFoundException extends RuntimeException {

    public EmployeeCourseNotFoundException(String message) {
        super(message);
    }
}
