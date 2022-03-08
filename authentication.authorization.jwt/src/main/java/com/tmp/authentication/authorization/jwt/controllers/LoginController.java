package com.tmp.authentication.authorization.jwt.controllers;

import com.tmp.authentication.authorization.jwt.models.AuthCredentialsDTO;
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

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TokensDTO> login(@RequestBody AuthCredentialsDTO authCredentialsDTO) {
        log.info("[{}] -> login, authCredentialsDTO: {}", this.getClass().getSimpleName(), authCredentialsDTO);

        TokensDTO tokensDTO = loginService.login(authCredentialsDTO);

        return ResponseEntity.status(HttpStatus.OK)
                .header("Access-Token", tokensDTO.getAccessToken())
                .header("Refresh-Token", tokensDTO.getRefreshToken())
                .body(tokensDTO);
    }
}
