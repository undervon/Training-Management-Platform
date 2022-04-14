package com.tmp.authentication.authorization.jwt.exceptions.handling;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.PrematureJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@Log4j2
@ControllerAdvice
public class InterceptedExceptionHandler {

    @ResponseBody
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<String> expiredJwtExceptionHandler(ExpiredJwtException expiredJwtException) {
        log.error("thrown ExpiredJwtException");

        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                .body(expiredJwtException.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<String> malformedJwtExceptionHandler(MalformedJwtException malformedJwtException) {
        log.error("thrown MalformedJwtException");

        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                .body(malformedJwtException.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(PrematureJwtException.class)
    public ResponseEntity<String> prematureJwtExceptionHandler(PrematureJwtException prematureJwtException) {
        log.error("thrown PrematureJwtException");

        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                .body(prematureJwtException.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<String> signatureExceptionHandler(SignatureException signatureException) {
        log.error("thrown SignatureException");

        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                .body(signatureException.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(UnsupportedJwtException.class)
    public ResponseEntity<String> unsupportedJwtExceptionHandler(UnsupportedJwtException unsupportedJwtException) {
        log.error("thrown UnsupportedJwtException");

        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                .body(unsupportedJwtException.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> illegalArgumentExceptionHandler(IllegalArgumentException illegalArgumentException) {
        log.error("thrown IllegalArgumentException");

        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                .body(illegalArgumentException.getMessage());
    }
}
