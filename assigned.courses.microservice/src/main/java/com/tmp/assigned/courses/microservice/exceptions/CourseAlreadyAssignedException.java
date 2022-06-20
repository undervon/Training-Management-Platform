package com.tmp.assigned.courses.microservice.exceptions;

public class CourseAlreadyAssignedException extends RuntimeException {

    public CourseAlreadyAssignedException(String message) {
        super(message);
    }
}
