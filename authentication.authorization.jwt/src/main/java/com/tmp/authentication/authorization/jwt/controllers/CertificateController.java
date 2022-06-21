package com.tmp.authentication.authorization.jwt.controllers;

import com.tmp.authentication.authorization.jwt.models.CertificateDTO;
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
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
                            schema = @Schema(implementation = CertificateDTO.class))
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
            @RequestBody CreateCertificateDTO createCertificateDTO) {
        log.info("[ {} ] -> [ {} ] -> [ createCertificateReq ] createCertificateDTO: {}",
                this.getClass().getSimpleName(), HttpMethod.POST, createCertificateDTO);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(SuccessResponseDTO.builder()
                        .data(certificateService.createCertificateDTO(createCertificateDTO, "CertificateTemplate.html"))
                        .build());
    }

    @Operation(summary = "Get certificates by user id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK - if successful", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CertificateDTO.class))
            }),
            @ApiResponse(responseCode = "404", description = "NOT_FOUND - if the user not found in DB",
                    content = @Content)
    })
    @CrossOrigin
    @GetMapping(value = "/getCertificatesByUserId/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessResponseDTO<?>> getCertificatesByUserIdReq(@PathVariable("id") Long id) {
        log.info("[ {} ] -> [ {} ] -> [ getCertificateByUserIdReq ] id: {}",
                this.getClass().getSimpleName(), HttpMethod.GET, id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(SuccessResponseDTO.builder()
                        .data(certificateService.getCertificatesByUserIdReq(id))
                        .build());
    }

    @Operation(summary = "Get pdf resource")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK - if successful"),
            @ApiResponse(responseCode = "417",
                    description = "EXPECTATION_FAILED - if Could not read file", content = @Content)
    })
    @CrossOrigin
    @GetMapping(value = "/{pdfName}", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<Resource> getPdfReq(@PathVariable(value = "pdfName") String pdfName) {
        log.info("[ {} ] -> [ {} ] -> [ getPdfReq ] pdfName: {}",
                this.getClass().getSimpleName(), HttpMethod.GET, pdfName);

        Resource file = certificateService.getPdfReq(pdfName);

        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }
}
