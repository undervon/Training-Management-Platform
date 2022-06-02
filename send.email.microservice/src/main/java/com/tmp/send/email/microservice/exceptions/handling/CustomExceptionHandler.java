package com.tmp.send.email.microservice.exceptions.handling;

import com.tmp.send.email.microservice.exceptions.SendEmailUnknownException;
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
    @ExceptionHandler(SendEmailUnknownException.class)
    public ResponseEntity<String> sendEmailUnknownExceptionHandler() {
        log.error("thrown SendEmailUnknownException");

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body("Something went wrong when sending the email");
    }
}