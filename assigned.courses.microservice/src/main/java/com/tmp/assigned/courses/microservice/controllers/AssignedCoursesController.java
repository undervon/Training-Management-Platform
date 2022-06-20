package com.tmp.assigned.courses.microservice.controllers;

import com.tmp.assigned.courses.microservice.models.AssignCourseDTO;
import com.tmp.assigned.courses.microservice.models.CoursesStatisticsDTO;
import com.tmp.assigned.courses.microservice.models.CreateAssignCourseDTO;
import com.tmp.assigned.courses.microservice.models.SuccessResponseDTO;
import com.tmp.assigned.courses.microservice.services.AssignedCoursesService;
import com.tmp.assigned.courses.microservice.vo.CompactedCourse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @Operation(summary = "Create assigned between employee and course")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "CREATED - if successful", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AssignCourseDTO.class))
            }),
            @ApiResponse(responseCode = "400",
                    description = "BAD_REQUEST - if something wrong was done", content = @Content),
            @ApiResponse(responseCode = "406",
                    description = "NOT_ACCEPTABLE - if the course is already assigned", content = @Content)
    })
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

    @Operation(summary = "Set the course completion")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK - if successful", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = String.class))
            }),
            @ApiResponse(responseCode = "400",
                    description = "BAD_REQUEST - if something wrong was done", content = @Content),
    })
    @CrossOrigin
    @PatchMapping(value = "/setCompletedCourse",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessResponseDTO<?>> setCompletedCourseReq(
            @RequestBody CreateAssignCourseDTO createAssignCourseDTO) {
        log.info("[ {} ] -> [ {} ] -> [ setCompletedCourseReq ] createAssignCourseDTO: {}",
                this.getClass().getSimpleName(), HttpMethod.PATCH, createAssignCourseDTO);

        assignedCoursesService.setCompletedCourseReq(createAssignCourseDTO);

        return ResponseEntity.status(HttpStatus.OK)
                .body(SuccessResponseDTO.builder()
                        .data("The course has been completed")
                        .build());
    }

    @Operation(summary = "Get the completed courses by idEmployee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK - if successful", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = CompactedCourse.class)))
            }),
            @ApiResponse(responseCode = "400",
                    description = "BAD_REQUEST - if something wrong was done", content = @Content),
    })
    @CrossOrigin
    @GetMapping(value = "/getCompletedCourses/{idEmployee}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessResponseDTO<?>> getCompletedCoursesReq(
            @PathVariable(value = "idEmployee") Long idEmployee) {
        log.info("[ {} ] -> [ {} ] -> [ getCompletedCoursesReq ] idEmployee: {}",
                this.getClass().getSimpleName(), HttpMethod.GET, idEmployee);

        return ResponseEntity.status(HttpStatus.OK)
                .body(SuccessResponseDTO.builder()
                        .data(assignedCoursesService.getCompletedCoursesReq(idEmployee))
                        .build());
    }

    @Operation(summary = "Get the incomplete courses by idEmployee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK - if successful", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = CompactedCourse.class)))
            }),
            @ApiResponse(responseCode = "400",
                    description = "BAD_REQUEST - if something wrong was done", content = @Content),
    })
    @CrossOrigin
    @GetMapping(value = "/getIncompleteCourses/{idEmployee}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessResponseDTO<?>> getIncompleteCoursesReq(
            @PathVariable(value = "idEmployee") Long idEmployee) {
        log.info("[ {} ] -> [ {} ] -> [ getIncompleteCoursesReq ] idEmployee: {}",
                this.getClass().getSimpleName(), HttpMethod.GET, idEmployee);

        return ResponseEntity.status(HttpStatus.OK)
                .body(SuccessResponseDTO.builder()
                        .data(assignedCoursesService.getIncompleteCoursesReq(idEmployee))
                        .build());
    }

    @Operation(summary = "Get statistics from courses by idEmployee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK - if successful", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CoursesStatisticsDTO.class))
            }),
            @ApiResponse(responseCode = "400",
                    description = "BAD_REQUEST - if something wrong was done", content = @Content),
    })
    @CrossOrigin
    @GetMapping(value = "/getCoursesStatistics/{idEmployee}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessResponseDTO<?>> getCoursesStatisticsReq(
            @PathVariable(value = "idEmployee") Long idEmployee) {
        log.info("[ {} ] -> [ {} ] -> [ getCoursesStatisticsReq ] idEmployee: {}",
                this.getClass().getSimpleName(), HttpMethod.GET, idEmployee);

        return ResponseEntity.status(HttpStatus.OK)
                .body(SuccessResponseDTO.builder()
                        .data(assignedCoursesService.getCoursesStatisticsReq(idEmployee))
                        .build());
    }
}
