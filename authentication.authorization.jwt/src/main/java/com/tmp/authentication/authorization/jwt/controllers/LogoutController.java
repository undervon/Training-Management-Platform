package com.tmp.authentication.authorization.jwt.controllers;

import com.tmp.authentication.authorization.jwt.models.GenericDTO;
import com.tmp.authentication.authorization.jwt.models.RefreshTokenDTO;
import com.tmp.authentication.authorization.jwt.models.SuccessResponseDTO;
import com.tmp.authentication.authorization.jwt.models.TokenDTO;
import com.tmp.authentication.authorization.jwt.models.TokensDTO;
import com.tmp.authentication.authorization.jwt.models.AccessTokenDTO;
import com.tmp.authentication.authorization.jwt.services.LogoutService;
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
public class LogoutController {

    private final LogoutService logoutService;

    @Operation(summary = "Validate access token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK - if successful", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AccessTokenDTO.class))
            }),
            @ApiResponse(responseCode = "404", description = "NOT_FOUND - if the user not found in DB or "
                    + "the role does not exist",
                    content = @Content),
            @ApiResponse(responseCode = "406", description = "NOT_ACCEPTABLE - if [ the token is in BlackList ] OR "
                    + "[ Expired JWT token ] OR "
                    + "[ Invalid JWT token ] OR "
                    + "[ Unacceptable JWT token ] OR "
                    + "[ Invalid JWT signature ] OR "
                    + "[ Unsupported JWT token ] OR "
                    + "[ JWT claims string is empty ]",
                    content = @Content)
    })
    @CrossOrigin
    @PostMapping(value = "/validateAccessToken",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessResponseDTO<?>> validateAccessTokenReq(@RequestBody TokenDTO tokenDTO) {
        log.info("[ {} ] -> [ {} ] -> [ validateAccessTokenReq ] tokenDTO: {}",
                this.getClass().getSimpleName(), HttpMethod.POST, tokenDTO);

        return ResponseEntity.status(HttpStatus.OK)
                .body(SuccessResponseDTO.builder()
                        .data(logoutService.validateAccessTokenReq(tokenDTO))
                        .build());
    }

    @Operation(summary = "Validate refresh token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK - if successful", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RefreshTokenDTO.class))
            }),
            @ApiResponse(responseCode = "404", description = "NOT_FOUND - if the user not found in DB",
                    content = @Content),
            @ApiResponse(responseCode = "406", description = "NOT_ACCEPTABLE - if [ the token is in BlackList ] OR "
                    + "[ Expired JWT token ] OR "
                    + "[ Invalid JWT token ] OR "
                    + "[ Unacceptable JWT token ] OR "
                    + "[ Invalid JWT signature ] OR "
                    + "[ Unsupported JWT token ] OR "
                    + "[ JWT claims string is empty ]",
                    content = @Content)
    })
    @CrossOrigin
    @PostMapping(value = "/validateRefreshToken",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessResponseDTO<?>> validateRefreshTokenReq(@RequestBody TokenDTO tokenDTO) {
        log.info("[ {} ] -> [ {} ] -> [ validateRefreshTokenReq ] tokenDTO: {}",
                this.getClass().getSimpleName(), HttpMethod.POST, tokenDTO);

        return ResponseEntity.status(HttpStatus.OK)
                .body(SuccessResponseDTO.builder()
                        .data(logoutService.validateRefreshTokenReq(tokenDTO))
                        .build());
    }

    @Operation(summary = "Logout request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK - if successful", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = GenericDTO.class))
            }),
    })
    @CrossOrigin
    @PostMapping(value = "/logout", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessResponseDTO<?>> logoutReq(@RequestBody TokensDTO tokensDTO) {
        log.info("[ {} ] -> [ {} ] -> [ logoutReq ] tokensDTO: {}",
                this.getClass().getSimpleName(), HttpMethod.POST, tokensDTO);

        return ResponseEntity.status(HttpStatus.OK)
                .body(SuccessResponseDTO.builder()
                        .data(logoutService.logoutReq(tokensDTO))
                        .build());
    }
}
