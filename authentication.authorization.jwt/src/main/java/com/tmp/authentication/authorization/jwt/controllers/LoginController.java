package com.tmp.authentication.authorization.jwt.controllers;

import com.tmp.authentication.authorization.jwt.models.AuthCredentialsDTO;
import com.tmp.authentication.authorization.jwt.models.TokenDTO;
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
@RequestMapping(path = "/api/1.0/tmp")
public class LoginController {

    private final LoginService loginService;

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TokenDTO> login(@RequestBody AuthCredentialsDTO authCredentialsDTO) {
        log.info("[{}] -> login, authCredentialsDTO: {}", this.getClass().getSimpleName(), authCredentialsDTO);

        TokenDTO tokenDTO = loginService.login(authCredentialsDTO);

        return ResponseEntity.status(HttpStatus.OK)
                .header("Access-Token", tokenDTO.getAccessToken())
                .header("Refresh-Token", tokenDTO.getRefreshToken())
                .body(tokenDTO);
    }
}
