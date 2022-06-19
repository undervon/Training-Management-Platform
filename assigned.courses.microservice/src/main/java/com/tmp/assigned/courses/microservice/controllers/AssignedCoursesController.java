package com.tmp.assigned.courses.microservice.controllers;

import com.tmp.assigned.courses.microservice.models.CreateAssignCourseDTO;
import com.tmp.assigned.courses.microservice.models.SuccessResponseDTO;
import com.tmp.assigned.courses.microservice.services.AssignedCoursesService;
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
public class AssignedCoursesController {

    private final AssignedCoursesService assignedCoursesService;

    @CrossOrigin
    @PostMapping(value = "/assignUserCourse", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessResponseDTO<?>> assignUserCourseReq(
            @RequestBody CreateAssignCourseDTO createAssignCourseDTO) {
        log.info("[ {} ] -> [ {} ] -> [ assignUserCourseReq ] createAssignCourseDTO: {}",
                this.getClass().getSimpleName(), HttpMethod.POST, createAssignCourseDTO);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(SuccessResponseDTO.builder()
                        .data(assignedCoursesService.assignUserCourseReq(createAssignCourseDTO))
                        .build());
    }
}
