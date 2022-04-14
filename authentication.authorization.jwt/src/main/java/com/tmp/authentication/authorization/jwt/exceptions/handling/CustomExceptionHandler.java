package com.tmp.authentication.authorization.jwt.exceptions.handling;

import com.tmp.authentication.authorization.jwt.exceptions.BadCredentialsException;
import com.tmp.authentication.authorization.jwt.exceptions.RoleAlreadyExistsException;
import com.tmp.authentication.authorization.jwt.exceptions.RoleDoesNotExistException;
import com.tmp.authentication.authorization.jwt.exceptions.TokenInBlackListException;
import com.tmp.authentication.authorization.jwt.exceptions.UnableToDeleteUserException;
import com.tmp.authentication.authorization.jwt.exceptions.UnsupportedRolesSizeException;
import com.tmp.authentication.authorization.jwt.exceptions.UserAlreadyExistsException;
import com.tmp.authentication.authorization.jwt.exceptions.UserNotFoundException;
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

    @ResponseBody
    @ExceptionHandler(TokenInBlackListException.class)
    public ResponseEntity<String> tokenInBlackListExceptionHandler(
            TokenInBlackListException tokenInBlackListException) {
        log.error("thrown TokenInBlackListException");

        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                .body(String.format("Token '%s' in BlackList", tokenInBlackListException.getMessage()));
    }

    @ResponseBody
    @ExceptionHandler(RoleAlreadyExistsException.class)
    public ResponseEntity<String> roleAlreadyExistsExceptionHandler(
            RoleAlreadyExistsException roleAlreadyExistsException) {
        log.error("thrown RoleAlreadyExistsException");

        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                .body(String.format("The role '%s' already exists", roleAlreadyExistsException.getMessage()));
    }

    @ResponseBody
    @ExceptionHandler(UnsupportedRolesSizeException.class)
    public ResponseEntity<String> unsupportedRolesSizeExceptionHandler() {
        log.error("thrown UnsupportedRolesSizeException");

        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                .body("Roles size is unsupported");
    }

    @ResponseBody
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<String> userAlreadyExistsExceptionHandler(
            UserAlreadyExistsException userAlreadyExistsException) {
        log.error("thrown UserAlreadyExistsException");

        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                .body(String.format("The user '%s' already exists in db", userAlreadyExistsException.getMessage()));
    }

    @ResponseBody
    @ExceptionHandler(UnableToDeleteUserException.class)
    public ResponseEntity<String> unableToDeleteUserExceptionHandler() {
        log.error("thrown UnableToDeleteUserException");

        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                .body("This user cannot be deleted because it's just ADMIN");
    }
}