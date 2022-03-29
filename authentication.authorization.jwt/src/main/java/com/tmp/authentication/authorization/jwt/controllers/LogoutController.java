package com.tmp.authentication.authorization.jwt.controllers;

import com.tmp.authentication.authorization.jwt.models.TokenDTO;
import com.tmp.authentication.authorization.jwt.models.TokensDTO;
import com.tmp.authentication.authorization.jwt.models.UserCharacteristicsDTO;
import com.tmp.authentication.authorization.jwt.services.LogoutService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping(value = "/validateAccessToken", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserCharacteristicsDTO> validateAccessToken(@RequestBody TokenDTO tokenDTO) {
        log.info("[{}] -> validateAccessToken, tokenDTO: {}", this.getClass().getSimpleName(), tokenDTO);

        return ResponseEntity.status(HttpStatus.OK)
                .body(logoutService.validateAccessToken(tokenDTO));
    }

    @GetMapping(value = "/validateRefreshToken", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> validateRefreshToken(@RequestBody TokenDTO tokenDTO) {
        log.info("[{}] -> validateRefreshToken, tokenDTO: {}", this.getClass().getSimpleName(), tokenDTO);

        return ResponseEntity.status(HttpStatus.OK)
                .body(logoutService.validateRefreshToken(tokenDTO));
    }

    @PostMapping(value = "/logout", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> logout(@RequestBody TokensDTO tokensDTO) {
        log.info("[{}] -> logout, tokenDTO: {}", this.getClass().getSimpleName(), tokensDTO);

        return ResponseEntity.status(HttpStatus.OK)
                .body(logoutService.logout(tokensDTO));
    }
}
