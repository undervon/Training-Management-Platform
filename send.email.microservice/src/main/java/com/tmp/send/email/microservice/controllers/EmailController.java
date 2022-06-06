package com.tmp.send.email.microservice.controllers;

import com.tmp.send.email.microservice.models.EmailAssignedCourseEmployeeDTO;
import com.tmp.send.email.microservice.models.EmailAssignedCourseManagerDTO;
import com.tmp.send.email.microservice.models.EmailCourseCompletedEmployeeDTO;
import com.tmp.send.email.microservice.models.EmailCourseCompletedManagerDTO;
import com.tmp.send.email.microservice.models.SuccessResponseDTO;
import com.tmp.send.email.microservice.services.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "${api.path}")
public class EmailController {

    private final EmailService emailService;

    @Operation(summary = "Send email for assigned course for manager request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK - if successful", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = String.class))
            }),
            @ApiResponse(responseCode = "422",
                    description = "UNPROCESSABLE_ENTITY - if something went wrong when sending the email",
                    content = @Content)
    })
    @CrossOrigin
    @PostMapping(path = "/assignedCourseManager",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessResponseDTO<?>> sendEmailAssignedCourseManagerReq(
            @RequestBody EmailAssignedCourseManagerDTO emailAssignedCourseManagerDTO) {
        log.info("[ {} ] -> [ {} ] -> [ sendEmailAssignedCourseManagerReq ] emailAssignedCourseManagerDTO: {}",
                this.getClass().getSimpleName(), HttpMethod.POST, emailAssignedCourseManagerDTO);

        emailService.sendEmailAssignedCourseManagerReq(emailAssignedCourseManagerDTO,
                "AssignedCourseManagerEmailTemplate.html");

        return ResponseEntity.status(HttpStatus.OK)
                .body(SuccessResponseDTO.builder()
                        .data("The email assigned course for manager sent successfully!")
                        .build());
    }

    @Operation(summary = "Send email for assigned course for employee request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK - if successful", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = String.class))
            }),
            @ApiResponse(responseCode = "422",
                    description = "UNPROCESSABLE_ENTITY - if something went wrong when sending the email",
                    content = @Content)
    })
    @CrossOrigin
    @PostMapping(path = "/assignedCourseEmployee",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessResponseDTO<?>> sendEmailAssignedCourseEmployeeReq(
            @RequestBody EmailAssignedCourseEmployeeDTO emailAssignedCourseEmployeeDTO) {
        log.info("[ {} ] -> [ {} ] -> [ sendEmailAssignedCourseEmployeeReq ] emailAssignedCourseEmployeeDTO: {}",
                this.getClass().getSimpleName(), HttpMethod.POST, emailAssignedCourseEmployeeDTO);

        emailService.sendEmailAssignedCourseEmployeeReq(emailAssignedCourseEmployeeDTO,
                "AssignedCourseEmployeeEmailTemplate.html");

        return ResponseEntity.status(HttpStatus.OK)
                .body(SuccessResponseDTO.builder()
                        .data("The email assigned course for employee sent successfully!")
                        .build());
    }

    @Operation(summary = "Send email for completed the course for manager request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK - if successful", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = String.class))
            }),
            @ApiResponse(responseCode = "422",
                    description = "UNPROCESSABLE_ENTITY - if something went wrong when sending the email",
                    content = @Content)
    })
    @CrossOrigin
    @PostMapping(path = "/courseCompletedManager",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessResponseDTO<?>> sendEmailCourseCompletedManagerReq(
            @RequestBody EmailCourseCompletedManagerDTO emailCourseCompletedManagerDTO) {
        log.info("[ {} ] -> [ {} ] -> [ sendEmailCourseCompletedManagerReq ] emailCourseCompletedManagerDTO: {}",
                this.getClass().getSimpleName(), HttpMethod.POST, emailCourseCompletedManagerDTO);

        emailService.sendEmailCourseCompletedManagerReq(emailCourseCompletedManagerDTO,
                "CourseCompletedManagerEmailTemplate.html");

        return ResponseEntity.status(HttpStatus.OK)
                .body(SuccessResponseDTO.builder()
                        .data("The email course completed for manager sent successfully!")
                        .build());
    }

    @Operation(summary = "Send email for completed the course for employee request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK - if successful", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = String.class))
            }),
            @ApiResponse(responseCode = "422",
                    description = "UNPROCESSABLE_ENTITY - if something went wrong when sending the email",
                    content = @Content)
    })
    @CrossOrigin
    @PostMapping(path = "/courseCompletedEmployee",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessResponseDTO<?>> sendEmailCourseCompletedEmployeeReq(
            @RequestBody EmailCourseCompletedEmployeeDTO emailCourseCompletedEmployeeDTO) {
        log.info("[ {} ] -> [ {} ] -> [ sendEmailCourseCompletedEmployeeReq ] emailCourseCompletedEmployeeDTO: {}",
                this.getClass().getSimpleName(), HttpMethod.POST, emailCourseCompletedEmployeeDTO);

        emailService.sendEmailCourseCompletedEmployeeReq(emailCourseCompletedEmployeeDTO,
                "CourseCompletedEmployeeEmailTemplate.html");

        return ResponseEntity.status(HttpStatus.OK)
                .body(SuccessResponseDTO.builder()
                        .data("The email course completed for employee sent successfully!")
                        .build());
    }
}