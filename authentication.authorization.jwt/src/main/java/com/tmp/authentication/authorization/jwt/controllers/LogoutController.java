package com.tmp.authentication.authorization.jwt.controllers;

import com.tmp.authentication.authorization.jwt.models.RefreshTokenDTO;
import com.tmp.authentication.authorization.jwt.models.TokenDTO;
import com.tmp.authentication.authorization.jwt.models.TokensDTO;
import com.tmp.authentication.authorization.jwt.models.AccessTokenDTO;
import com.tmp.authentication.authorization.jwt.services.LogoutService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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

    @CrossOrigin
    @PostMapping(value = "/validateAccessToken",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccessTokenDTO> validateAccessTokenReq(@RequestBody TokenDTO tokenDTO) {
        log.info("[{}] -> validateAccessTokenReq, tokenDTO: {}", this.getClass().getSimpleName(), tokenDTO);

        return ResponseEntity.status(HttpStatus.OK)
                .body(logoutService.validateAccessTokenReq(tokenDTO));
    }

    @CrossOrigin
    @PostMapping(value = "/validateRefreshToken",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RefreshTokenDTO> validateRefreshTokenReq(@RequestBody TokenDTO tokenDTO) {
        log.info("[{}] -> validateRefreshTokenReq, tokenDTO: {}", this.getClass().getSimpleName(), tokenDTO);

        return ResponseEntity.status(HttpStatus.OK)
                .body(logoutService.validateRefreshTokenReq(tokenDTO));
    }

    @CrossOrigin
    @PostMapping(value = "/logout", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> logoutReq(@RequestBody TokensDTO tokensDTO) {
        log.info("[{}] -> logoutReq, tokenDTO: {}", this.getClass().getSimpleName(), tokensDTO);

        return ResponseEntity.status(HttpStatus.OK)
                .body(logoutService.logoutReq(tokensDTO));
    }
}
