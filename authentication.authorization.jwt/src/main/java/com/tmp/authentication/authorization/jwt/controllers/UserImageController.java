package com.tmp.authentication.authorization.jwt.controllers;

import com.tmp.authentication.authorization.jwt.services.UserImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "${api.path}")
public class UserImageController {

    private final UserImageService userImageService;

    @Operation(summary = "Get image by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK - if successful"),
            @ApiResponse(responseCode = "404", description = "NOT_FOUND - if the user image not found in DB",
                    content = @Content)
    })
    @CrossOrigin
    @GetMapping(value = "/image/{id}", produces = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE})
    public ResponseEntity<Resource> getUserImageByIdReq(@PathVariable("id") Long id) {
        log.info("[ {} ] -> [ {} ] -> [ getUserImageByIdReq ] id: {}",
                this.getClass().getSimpleName(), HttpMethod.GET, id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(userImageService.getUserImageByIdReq(id));
    }
}
