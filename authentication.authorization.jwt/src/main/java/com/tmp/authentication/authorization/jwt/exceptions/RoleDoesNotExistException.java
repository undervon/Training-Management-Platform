package com.tmp.authentication.authorization.jwt.exceptions;

public class RoleDoesNotExistException extends RuntimeException {

    public RoleDoesNotExistException(String message) {
        super(message);
    }
}
