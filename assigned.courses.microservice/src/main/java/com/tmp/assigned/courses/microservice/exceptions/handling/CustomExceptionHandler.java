package com.tmp.assigned.courses.microservice.exceptions.handling;

import com.tmp.assigned.courses.microservice.exceptions.CourseAlreadyAssignedException;
import com.tmp.assigned.courses.microservice.exceptions.GenericException;
import com.tmp.assigned.courses.microservice.exceptions.RoleDoesNotExistException;
import com.tmp.assigned.courses.microservice.models.ExceptionResponseDTO;
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

    @ResponseBody
    @ExceptionHandler(CourseAlreadyAssignedException.class)
    public ResponseEntity<ExceptionResponseDTO> courseAlreadyAssignedExceptionHandler(
            CourseAlreadyAssignedException courseAlreadyAssignedException) {
        log.error("thrown CourseAlreadyAssignedException");

        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                .body(ExceptionResponseDTO.builder()
                        .message(String.format("The course '%s' already assigned",
                                courseAlreadyAssignedException.getMessage()))
                        .build());
    }
}