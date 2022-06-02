package com.tmp.send.email.microservice.controllers;

import com.tmp.send.email.microservice.models.EmailAssignedCourseEmployeeDTO;
import com.tmp.send.email.microservice.models.EmailAssignedCourseManagerDTO;
import com.tmp.send.email.microservice.services.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

    @PostMapping(path = "/assignedCourseManager", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> sendEmailAssignedCourseManagerReq(
            @RequestBody EmailAssignedCourseManagerDTO emailAssignedCourseManagerDTO) {
        log.info("[ {} ] -> [ {} ] -> [ sendEmailAssignedCourseManagerReq ] emailAssignedCourseManagerDTO: {}",
                this.getClass().getSimpleName(), HttpMethod.POST, emailAssignedCourseManagerDTO);

        emailService.sendEmailAssignedCourseManagerReq(emailAssignedCourseManagerDTO,
                "AssignedCourseManagerEmailTemplate.html");

        return ResponseEntity.status(HttpStatus.OK)
                .body("The email sent successfully!");
    }

    @PostMapping(path = "/assignedCourseEmployee", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> sendEmailAssignedCourseEmployeeReq(
            @RequestBody EmailAssignedCourseEmployeeDTO emailAssignedCourseEmployeeDTO) {
        log.info("[ {} ] -> [ {} ] -> [ sendEmailAssignedCourseEmployeeReq ] emailAssignedCourseEmployeeDTO: {}",
                this.getClass().getSimpleName(), HttpMethod.POST, emailAssignedCourseEmployeeDTO);

        emailService.sendEmailAssignedCourseEmployeeReq(emailAssignedCourseEmployeeDTO,
                "AssignedCourseEmployeeEmailTemplate.html");

        return ResponseEntity.status(HttpStatus.OK)
                .body("The email sent successfully!");
    }
}