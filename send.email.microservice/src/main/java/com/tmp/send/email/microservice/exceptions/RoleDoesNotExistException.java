package com.tmp.send.email.microservice.exceptions;

public class RoleDoesNotExistException extends RuntimeException {

    public RoleDoesNotExistException(String message) {
        super(message);
    }
}
