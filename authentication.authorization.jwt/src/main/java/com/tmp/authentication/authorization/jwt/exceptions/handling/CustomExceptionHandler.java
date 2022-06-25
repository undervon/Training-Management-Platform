package com.tmp.authentication.authorization.jwt.exceptions.handling;

import com.tmp.authentication.authorization.jwt.exceptions.BadCredentialsException;
import com.tmp.authentication.authorization.jwt.exceptions.GenericException;
import com.tmp.authentication.authorization.jwt.exceptions.ImageContentTypeException;
import com.tmp.authentication.authorization.jwt.exceptions.ImageEmptyException;
import com.tmp.authentication.authorization.jwt.exceptions.ManagerDepartmentException;
import com.tmp.authentication.authorization.jwt.exceptions.ManagerNotFoundException;
import com.tmp.authentication.authorization.jwt.exceptions.PasswordException;
import com.tmp.authentication.authorization.jwt.exceptions.RoleAlreadyExistsException;
import com.tmp.authentication.authorization.jwt.exceptions.RoleDoesNotExistException;
import com.tmp.authentication.authorization.jwt.exceptions.StorageException;
import com.tmp.authentication.authorization.jwt.exceptions.TokenInBlackListException;
import com.tmp.authentication.authorization.jwt.exceptions.UnableToDeleteUserException;
import com.tmp.authentication.authorization.jwt.exceptions.UnsupportedRolesSizeException;
import com.tmp.authentication.authorization.jwt.exceptions.UserImageNotFoundException;
import com.tmp.authentication.authorization.jwt.exceptions.UserNotFoundException;
import com.tmp.authentication.authorization.jwt.models.ExceptionResponseDTO;
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
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ExceptionResponseDTO> badCredentialsExceptionHandler() {
        log.error("thrown BadCredentialsException");

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ExceptionResponseDTO.builder()
                        .message("Wrong username or password")
                        .build());
    }

    @ResponseBody
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ExceptionResponseDTO> userNotFoundExceptionHandler(
            UserNotFoundException userNotFoundException) {
        log.error("thrown UserNotFoundException");

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ExceptionResponseDTO.builder()
                        .message(String.format("The user '%s' not found in DB", userNotFoundException.getMessage()))
                        .build());
    }

    @ResponseBody
    @ExceptionHandler(TokenInBlackListException.class)
    public ResponseEntity<ExceptionResponseDTO> tokenInBlackListExceptionHandler(
            TokenInBlackListException tokenInBlackListException) {
        log.error("thrown TokenInBlackListException");

        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                .body(ExceptionResponseDTO.builder()
                        .message(String.format("Token '%s' in BlackList", tokenInBlackListException.getMessage()))
                        .build());
    }

    @ResponseBody
    @ExceptionHandler(RoleAlreadyExistsException.class)
    public ResponseEntity<ExceptionResponseDTO> roleAlreadyExistsExceptionHandler(
            RoleAlreadyExistsException roleAlreadyExistsException) {
        log.error("thrown RoleAlreadyExistsException");

        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                .body(ExceptionResponseDTO.builder()
                        .message(String.format("The role '%s' already exists", roleAlreadyExistsException.getMessage()))
                        .build());
    }

    @ResponseBody
    @ExceptionHandler(UnsupportedRolesSizeException.class)
    public ResponseEntity<ExceptionResponseDTO> unsupportedRolesSizeExceptionHandler() {
        log.error("thrown UnsupportedRolesSizeException");

        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                .body(ExceptionResponseDTO.builder()
                        .message("The roles size is unsupported")
                        .build());
    }

    @ResponseBody
    @ExceptionHandler(ManagerDepartmentException.class)
    public ResponseEntity<ExceptionResponseDTO> managerDepartmentExceptionHandler() {
        log.error("thrown ManagerDepartmentException");

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(ExceptionResponseDTO.builder()
                        .message("A manager department cannot be edited")
                        .build());
    }

    @ResponseBody
    @ExceptionHandler(UnableToDeleteUserException.class)
    public ResponseEntity<ExceptionResponseDTO> unableToDeleteUserExceptionHandler() {
        log.error("thrown UnableToDeleteUserException");

        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                .body(ExceptionResponseDTO.builder()
                        .message("This user cannot be deleted because it's just ADMIN")
                        .build());
    }

    @ResponseBody
    @ExceptionHandler(UserImageNotFoundException.class)
    public ResponseEntity<ExceptionResponseDTO> userImageNotFoundExceptionHandler(
            UserImageNotFoundException userImageNotFoundException) {
        log.error("thrown UserImageNotFoundException");

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ExceptionResponseDTO.builder()
                        .message(String.format("The user image '%s' not found in DB",
                                userImageNotFoundException.getMessage()))
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
    @ExceptionHandler(ImageEmptyException.class)
    public ResponseEntity<ExceptionResponseDTO> imageEmptyExceptionHandler() {
        log.error("thrown ImageEmptyException");

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ExceptionResponseDTO.builder()
                        .message("The image is empty")
                        .build());
    }

    @ResponseBody
    @ExceptionHandler(ImageContentTypeException.class)
    public ResponseEntity<ExceptionResponseDTO> imageContentTypeExceptionHandler() {
        log.error("thrown ImageContentTypeException");

        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                .body(ExceptionResponseDTO.builder()
                        .message("The image has wrong content type")
                        .build());
    }

    @ResponseBody
    @ExceptionHandler(ManagerNotFoundException.class)
    public ResponseEntity<ExceptionResponseDTO> managerNotFoundExceptionHandler(
            ManagerNotFoundException managerNotFoundException) {
        log.error("thrown ManagerNotFoundException");

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ExceptionResponseDTO.builder()
                        .message(String.format("The manager '%s' not found in DB",
                                managerNotFoundException.getMessage()))
                        .build());
    }

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
    @ExceptionHandler(PasswordException.class)
    public ResponseEntity<ExceptionResponseDTO> passwordExceptionHandler(PasswordException passwordException) {
        log.error("thrown PasswordException");

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ExceptionResponseDTO.builder()
                        .message(passwordException.getMessage())
                        .build());
    }
}
