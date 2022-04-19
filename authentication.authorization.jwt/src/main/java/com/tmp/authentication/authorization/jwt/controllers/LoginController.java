package com.tmp.authentication.authorization.jwt.controllers;

import com.tmp.authentication.authorization.jwt.models.UserCredentialsDTO;
import com.tmp.authentication.authorization.jwt.models.TokenDTO;
import com.tmp.authentication.authorization.jwt.models.TokensDTO;
import com.tmp.authentication.authorization.jwt.services.LoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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
public class LoginController {

    private final LoginService loginService;

    @PostMapping(value = "/login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TokensDTO> login(@RequestBody UserCredentialsDTO userCredentialsDTO) {
        log.info("[{}] -> login, userCredentialsDTO: {}", this.getClass().getSimpleName(), userCredentialsDTO);

        return ResponseEntity.status(HttpStatus.OK)
                .body(loginService.login(userCredentialsDTO));
    }

    @PostMapping(value = "/generateAccessToken",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TokenDTO> generateAccessToken(@RequestBody TokenDTO tokenDTO) {
        log.info("[{}] -> generateAccessToken, tokenDTO: {}", this.getClass().getSimpleName(), tokenDTO);

        return ResponseEntity.status(HttpStatus.OK)
                .body(loginService.generateAccessToken(tokenDTO));
    }
}
