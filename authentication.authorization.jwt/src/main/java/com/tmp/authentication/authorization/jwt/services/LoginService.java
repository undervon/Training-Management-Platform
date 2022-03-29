package com.tmp.authentication.authorization.jwt.services;

import com.tmp.authentication.authorization.jwt.entities.User;
import com.tmp.authentication.authorization.jwt.models.AuthCredentialsDTO;
import com.tmp.authentication.authorization.jwt.models.TokenDTO;
import com.tmp.authentication.authorization.jwt.models.TokensDTO;
import com.tmp.authentication.authorization.jwt.security.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserService userService;
    private final LogoutService logoutService;
    private final JwtTokenUtil jwtTokenUtil;

    public TokensDTO login(AuthCredentialsDTO authCredentialsDTO) {
        log.info("[{}] -> login, authCredentialsDTO: {}", this.getClass().getSimpleName(), authCredentialsDTO);

        User user = userService.findByUsername(authCredentialsDTO.getUsername());

        userService.checkPassword(user.getPassword(), authCredentialsDTO.getPassword());

        String accessToken = jwtTokenUtil.generateAccessToken(user);
        String refreshToken = jwtTokenUtil.generateRefreshToken(user);

        return TokensDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public TokenDTO generateAccessToken(TokenDTO tokenDTO) {
        log.info("[{}] -> generateAccessToken, tokenDTO: {}", this.getClass().getSimpleName(), tokenDTO);

        String username = logoutService.validateRefreshToken(tokenDTO);

        User user = userService.getByUsername(username);

        String accessToken = jwtTokenUtil.generateAccessToken(user);

        return TokenDTO.builder()
                .token(accessToken)
                .build();
    }
}
