package com.tmp.authentication.authorization.jwt.exceptions;

public class ManagerNotFoundException extends RuntimeException {

    public ManagerNotFoundException(String message) {
        super(message);
    }
}
