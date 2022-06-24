package com.tmp.authentication.authorization.jwt.exceptions.handling;

import com.tmp.authentication.authorization.jwt.models.ExceptionResponseDTO;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.PrematureJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@Log4j2
@ControllerAdvice
public class InterceptedExceptionHandler {

    @ResponseBody
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ExceptionResponseDTO> expiredJwtExceptionHandler(ExpiredJwtException expiredJwtException) {
        log.error("thrown ExpiredJwtException");

        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                .body(ExceptionResponseDTO.builder()
                        .message(expiredJwtException.getMessage())
                        .build());
    }

    @ResponseBody
    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<ExceptionResponseDTO> malformedJwtExceptionHandler(
            MalformedJwtException malformedJwtException) {
        log.error("thrown MalformedJwtException");

        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                .body(ExceptionResponseDTO.builder()
                        .message(malformedJwtException.getMessage())
                        .build());
    }

    @ResponseBody
    @ExceptionHandler(PrematureJwtException.class)
    public ResponseEntity<ExceptionResponseDTO> prematureJwtExceptionHandler(
            PrematureJwtException prematureJwtException) {
        log.error("thrown PrematureJwtException");

        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                .body(ExceptionResponseDTO.builder()
                        .message(prematureJwtException.getMessage())
                        .build());
    }

    @ResponseBody
    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<ExceptionResponseDTO> signatureExceptionHandler(SignatureException signatureException) {
        log.error("thrown SignatureException");

        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                .body(ExceptionResponseDTO.builder()
                        .message(signatureException.getMessage())
                        .build());
    }

    @ResponseBody
    @ExceptionHandler(UnsupportedJwtException.class)
    public ResponseEntity<ExceptionResponseDTO> unsupportedJwtExceptionHandler(
            UnsupportedJwtException unsupportedJwtException) {
        log.error("thrown UnsupportedJwtException");

        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                .body(ExceptionResponseDTO.builder()
                        .message(unsupportedJwtException.getMessage())
                        .build());
    }

    @ResponseBody
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionResponseDTO> illegalArgumentExceptionHandler(
            IllegalArgumentException illegalArgumentException) {
        log.error("thrown IllegalArgumentException");

        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                .body(ExceptionResponseDTO.builder()
                        .message(illegalArgumentException.getMessage())
                        .build());
    }

    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponseDTO> methodArgumentNotValidExceptionHandler(
            MethodArgumentNotValidException methodArgumentNotValidException) {
        log.error("thrown MethodArgumentNotValidException");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ExceptionResponseDTO.builder()
                        .message(methodArgumentNotValidException.getMessage())
                        .build());
    }
}
