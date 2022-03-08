package com.tmp.authentication.authorization.jwt.exceptions;

public class TokenInBlackListException extends RuntimeException {

    public TokenInBlackListException(String message) {
        super(message);
    }
}
