package com.tmp.authentication.authorization.jwt.controllers;

import com.tmp.authentication.authorization.jwt.models.UserCredentialsDTO;
import com.tmp.authentication.authorization.jwt.models.TokenDTO;
import com.tmp.authentication.authorization.jwt.models.TokensDTO;
import com.tmp.authentication.authorization.jwt.services.LoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
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
public class LoginController {

    private final LoginService loginService;

    @Operation(summary = "Login request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK - if successful"),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED - if the username or the password is wrong",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "NOT_FOUND - if the user not found in DB",
                    content = @Content)
    })
    @CrossOrigin
    @PostMapping(value = "/login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TokensDTO> loginReq(@RequestBody UserCredentialsDTO userCredentialsDTO) {
        log.info("[ {} ] -> [ {} ] -> [ loginReq ] userCredentialsDTO: {}",
                this.getClass().getSimpleName(), HttpMethod.POST, userCredentialsDTO);

        return ResponseEntity.status(HttpStatus.OK)
                .body(loginService.loginReq(userCredentialsDTO));
    }

    @Operation(summary = "Generate access token by refresh token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK - if successful"),
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
    @PostMapping(value = "/generateAccessToken",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TokenDTO> generateAccessTokenReq(@RequestBody TokenDTO tokenDTO) {
        log.info("[ {} ] -> [ {} ] -> [ generateAccessTokenReq ] tokenDTO: {}",
                this.getClass().getSimpleName(), HttpMethod.POST, tokenDTO);

        return ResponseEntity.status(HttpStatus.OK)
                .body(loginService.generateAccessTokenReq(tokenDTO));
    }
}
