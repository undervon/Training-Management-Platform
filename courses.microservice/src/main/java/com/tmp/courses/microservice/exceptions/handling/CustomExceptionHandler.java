package com.tmp.courses.microservice.exceptions.handling;

import com.tmp.courses.microservice.exceptions.CourseNotFoundException;
import com.tmp.courses.microservice.exceptions.GenericException;
import com.tmp.courses.microservice.exceptions.RoleDoesNotExistException;
import com.tmp.courses.microservice.exceptions.StorageException;
import com.tmp.courses.microservice.models.ExceptionResponseDTO;
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
    @ExceptionHandler(StorageException.class)
    public ResponseEntity<ExceptionResponseDTO> storageExceptionHandler(StorageException storageException) {
        log.error("thrown StorageException");

        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
                .body(ExceptionResponseDTO.builder()
                        .message(storageException.getMessage())
                        .build());
    }

    @ResponseBody
    @ExceptionHandler(CourseNotFoundException.class)
    public ResponseEntity<ExceptionResponseDTO> courseNotFoundExceptionHandler(
            CourseNotFoundException courseNotFoundException) {
        log.error("thrown CourseNotFoundException");

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ExceptionResponseDTO.builder()
                        .message(String.format("The course '%s' not found in DB", courseNotFoundException.getMessage()))
                        .build());
    }

    @ResponseBody
    @ExceptionHandler(RoleDoesNotExistException.class)
    public ResponseEntity<ExceptionResponseDTO> roleDoesNotExistExceptionHandler(
            RoleDoesNotExistException roleDoesNotExistException) {
        log.error("thrown RoleDoesNotExistException");

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ExceptionResponseDTO.builder()
                        .message(String.format("The role '%s' does not exist", roleDoesNotExistException.getMessage()))
                        .build());
    }

    @ResponseBody
    @ExceptionHandler(GenericException.class)
    public ResponseEntity<ExceptionResponseDTO> genericExceptionHandler() {
        log.error("thrown GenericException");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ExceptionResponseDTO.builder()
                        .message("Something wrong was done")
                        .build());
    }
}
