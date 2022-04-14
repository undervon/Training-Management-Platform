package com.tmp.authentication.authorization.jwt.services;

import com.tmp.authentication.authorization.jwt.entities.User;
import com.tmp.authentication.authorization.jwt.models.UserCredentialsDTO;
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

    public TokensDTO login(UserCredentialsDTO userCredentialsDTO) {
        log.info("[{}] -> login, userCredentialsDTO: {}", this.getClass().getSimpleName(), userCredentialsDTO);

        User user = userService.findUserByUsername(userCredentialsDTO.getUsername());

        userService.checkPassword(user.getPassword(), userCredentialsDTO.getPassword());

        String accessToken = jwtTokenUtil.generateAccessToken(user);
        String refreshToken = jwtTokenUtil.generateRefreshToken(user);

        return TokensDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public TokenDTO generateAccessToken(TokenDTO tokenDTO) {
        log.info("[{}] -> generateAccessToken, tokenDTO: {}", this.getClass().getSimpleName(), tokenDTO);

        String username = logoutService.validateRefreshToken(tokenDTO).getUsername();

        User user = userService.getUserByUsername(username);

        String accessToken = jwtTokenUtil.generateAccessToken(user);

        return TokenDTO.builder()
                .token(accessToken)
                .build();
    }
}
