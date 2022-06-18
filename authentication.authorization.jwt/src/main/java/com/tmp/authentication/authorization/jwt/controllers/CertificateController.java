package com.tmp.authentication.authorization.jwt.controllers;

import com.tmp.authentication.authorization.jwt.models.CreateCertificateDTO;
import com.tmp.authentication.authorization.jwt.models.SuccessResponseDTO;
import com.tmp.authentication.authorization.jwt.services.CertificateService;
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
public class CertificateController {

    private final CertificateService certificateService;

    @Operation(summary = "Create new certificate")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "CREATED - if successful", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CreateCertificateDTO.class))
            }),
            @ApiResponse(responseCode = "404",
                    description = "NOT_FOUND - if the user not found in DB", content = @Content),
            @ApiResponse(responseCode = "417",
                    description = "EXPECTATION_FAILED - if [ failed to store empty file ]"
                            + " OR [ failed to store file ]"
                            + " OR [ could not initialize storage ]"
                            + " OR [ File not found on storage certificate or error on create pdf ]",
                    content = @Content)
    })
    @CrossOrigin
    @PostMapping(value = "/createCertificate",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessResponseDTO<?>> createCertificateReq(
            @Valid @RequestBody CreateCertificateDTO createCertificateDTO) {
        log.info("[ {} ] -> [ {} ] -> [ createCertificateReq ] createCertificateDTO: {}",
                this.getClass().getSimpleName(), HttpMethod.POST, createCertificateDTO);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(SuccessResponseDTO.builder()
                        .data(certificateService.createCertificateDTO(createCertificateDTO, "CertificateTemplate.html"))
                        .build());
    }
}
