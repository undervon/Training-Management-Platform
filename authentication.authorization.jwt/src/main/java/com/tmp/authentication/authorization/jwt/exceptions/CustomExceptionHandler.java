package com.tmp.authentication.authorization.jwt.exceptions;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@Log4j2
@ControllerAdvice
public class CustomExceptionHandler {

    @ResponseBody
    @ExceptionHandler(RoleDoesNotExistException.class)
    public ResponseEntity<String> roleDoesNotExistExceptionHandler(
            RoleDoesNotExistException roleDoesNotExistException) {
        log.error("thrown RoleDoesNotExistException");

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(String.format("The role '%s' does not exist", roleDoesNotExistException.getMessage()));
    }

    @ResponseBody
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> badCredentialsExceptionHandler(BadCredentialsException badCredentialsException) {
        log.error("thrown BadCredentialsException");

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(String.format("Bad credentials (%s)", badCredentialsException.getMessage()));
    }

    @ResponseBody
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> userNotFoundExceptionHandler(UserNotFoundException userNotFoundException) {
        log.error("thrown UserNotFoundException");

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(String.format("User '%s' not found in DB", userNotFoundException.getMessage()));
    }
}
