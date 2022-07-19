package com.tmp.courses.microservice.controllers;

import com.tmp.courses.microservice.models.AddSurveyDTO;
import com.tmp.courses.microservice.models.SuccessResponseDTO;
import com.tmp.courses.microservice.services.SurveyService;
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

import javax.validation.Valid;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "${api.path}")
public class SurveyController {

    private final SurveyService surveyService;

    @Operation(summary = "Create new survey")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "CREATED - if successful", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AddSurveyDTO.class))
            }),
            @ApiResponse(responseCode = "404",
                    description = "NOT_FOUND - if the course not found in DB",
                    content = @Content)
    })
    @CrossOrigin
    @PostMapping(value = "/addSurvey",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessResponseDTO<?>> addSurveyReq(@Valid @RequestBody AddSurveyDTO addSurveyDTO) {
        log.info("[ {} ] -> [ {} ] -> [ addSurveyReq ] addSurveyDTO: {}",
                this.getClass().getSimpleName(), HttpMethod.POST, addSurveyDTO);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(SuccessResponseDTO.builder()
                        .data(surveyService.addSurveyReq(addSurveyDTO))
                        .build());
    }
}
