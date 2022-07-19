package com.tmp.assigned.courses.microservice.exceptions;

public class RoleDoesNotExistException extends RuntimeException {

    public RoleDoesNotExistException(String message) {
        super(message);
    }
}
