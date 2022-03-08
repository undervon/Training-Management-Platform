package com.tmp.authentication.authorization.jwt.services;

import com.tmp.authentication.authorization.jwt.exceptions.TokenInBlackListException;
import com.tmp.authentication.authorization.jwt.models.ERole;
import com.tmp.authentication.authorization.jwt.models.TokenDTO;
import com.tmp.authentication.authorization.jwt.models.TokensDTO;
import com.tmp.authentication.authorization.jwt.models.UserCharacteristicsDTO;
import com.tmp.authentication.authorization.jwt.models.UsernameDTO;
import com.tmp.authentication.authorization.jwt.security.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class LogoutService {

    private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil;

    private static final List<String> accessTokenBlackList = new ArrayList<>();
    private static final List<String> refreshTokenBlackList = new ArrayList<>();

    public UserCharacteristicsDTO validateAccessToken(TokenDTO tokenDTO) {
        log.info("[{}] -> validateAccessToken, tokenDTO: {}", this.getClass().getSimpleName(), tokenDTO);

        String accessToken = tokenDTO.getToken();

        if (accessTokenBlackList.contains(accessToken)) {
            throw new TokenInBlackListException(accessToken);
        }

        // Validate accessToken; if it has an exception, intercept it
        jwtTokenUtil.validate(accessToken);

        String username = jwtTokenUtil.getUsernameFromToken(accessToken);
        List<ERole> roles = jwtTokenUtil.getRolesFromToken(accessToken);

        // Check user by username if exist in DB
        userService.findByUsername(username);

        return UserCharacteristicsDTO.builder()
                .username(username)
                .roles(roles)
                .build();
    }

    public UsernameDTO validateRefreshToken(TokenDTO tokenDTO) {
        log.info("[{}] -> validateRefreshToken, tokenDTO: {}", this.getClass().getSimpleName(), tokenDTO);

        String refreshToken = tokenDTO.getToken();

        if (refreshTokenBlackList.contains(refreshToken)) {
            throw new TokenInBlackListException(refreshToken);
        }

        // Validate refreshToken; if it has an exception, intercept it
        jwtTokenUtil.validate(refreshToken);

        String username = jwtTokenUtil.getUsernameFromToken(refreshToken);

        // Check user by username if exist in DB
        userService.findByUsername(username);

        return UsernameDTO.builder()
                .username(username)
                .build();
    }

    public String logout(TokensDTO tokensDTO) {
        log.info("[{}] -> logout, tokensDTO: {}", this.getClass().getSimpleName(), tokensDTO);

        String accessToken = tokensDTO.getAccessToken();
        String refreshToken = tokensDTO.getRefreshToken();

        if (!accessTokenBlackList.contains(accessToken)) {
            log.info("Add accessToken in blackList");

            accessTokenBlackList.add(accessToken);
        }

        if (!refreshTokenBlackList.contains(refreshToken)) {
            log.info("Add refreshToken in blackList");

            refreshTokenBlackList.add(refreshToken);
        }

        return "Tokens successfully destroyed";
    }
}
