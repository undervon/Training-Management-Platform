package com.tmp.authentication.authorization.jwt.services;

import com.tmp.authentication.authorization.jwt.entities.User;
import com.tmp.authentication.authorization.jwt.exceptions.TokenInBlackListException;
import com.tmp.authentication.authorization.jwt.models.AccessTokenDTO;
import com.tmp.authentication.authorization.jwt.models.GenericDTO;
import com.tmp.authentication.authorization.jwt.models.RefreshTokenDTO;
import com.tmp.authentication.authorization.jwt.models.TokenDTO;
import com.tmp.authentication.authorization.jwt.models.TokensDTO;
import com.tmp.authentication.authorization.jwt.security.JwtTokenUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

@ExtendWith(SpringExtension.class)
@ActiveProfiles(profiles = "local-test")
@RunWith(MockitoJUnitRunner.Silent.class)
class LogoutServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @InjectMocks
    private LogoutService logoutService;

    private User user;
    private TokensDTO tokensDTO;
    private TokenDTO tokenDTO;
    private RefreshTokenDTO refreshTokenDTO;
    private AccessTokenDTO accessTokenDTO;
    private GenericDTO genericDTO;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .firstName("A")
                .lastName("A")
                .email("A")
                .password("A")
                .department("A")
                .employeeNumber("A")
                .joinDate(LocalDateTime.now())
                .image(new byte[1])
                .build();

        tokensDTO = TokensDTO.builder()
                .accessToken("A")
                .refreshToken("A")
                .build();

        tokenDTO = TokenDTO.builder()
                .token("A")
                .build();

        refreshTokenDTO = RefreshTokenDTO.builder()
                .username("B")
                .creationDate(new Date())
                .expirationDate(new Date())
                .build();

        accessTokenDTO = AccessTokenDTO.builder()
                .username("B")
                .roleValues(new ArrayList<>())
                .creationDate(new Date())
                .expirationDate(new Date())
                .build();

        genericDTO = GenericDTO.builder()
                .message("Tokens successfully destroyed")
                .build();
    }

    @Test
    void validateAccessTokenReq() {
        tokenDTO.setToken("B");
        tokensDTO.setRefreshToken("B");
        tokensDTO.setAccessToken("B");
        jwtTokenUtil.validate(tokensDTO.getAccessToken());

        Mockito.when(jwtTokenUtil.getUsernameFromToken(tokensDTO.getAccessToken()))
                .thenReturn(accessTokenDTO.getUsername());
        Mockito.when(jwtTokenUtil.getRolesFromToken(tokensDTO.getAccessToken()))
                .thenReturn(accessTokenDTO.getRoleValues());
        Mockito.when(jwtTokenUtil.getCreationDateFromToken(tokensDTO.getAccessToken()))
                .thenReturn(accessTokenDTO.getCreationDate());
        Mockito.when(jwtTokenUtil.getExpirationDateFromToken(tokensDTO.getAccessToken()))
                .thenReturn(accessTokenDTO.getExpirationDate());

        userService.findUserByUsername(user.getEmail());
        Assertions.assertEquals(accessTokenDTO, logoutService.validateAccessTokenReq(tokenDTO));
    }

    @Test
    void validateAccessTokenReqException() {
        logoutService.logoutReq(tokensDTO);

        TokenInBlackListException tokenInBlackListException = Assertions.assertThrows(TokenInBlackListException.class,
                () -> logoutService.validateAccessTokenReq(tokenDTO));

        Assertions.assertEquals("A", tokenInBlackListException.getMessage());
    }

    @Test
    void validateRefreshTokenReq() {
        tokenDTO.setToken("B");
        tokensDTO.setRefreshToken("B");
        tokensDTO.setAccessToken("B");
        jwtTokenUtil.validate(tokensDTO.getRefreshToken());

        Mockito.when(jwtTokenUtil.getUsernameFromToken(tokensDTO.getAccessToken()))
                .thenReturn(refreshTokenDTO.getUsername());
        Mockito.when(jwtTokenUtil.getCreationDateFromToken(tokensDTO.getAccessToken()))
                .thenReturn(refreshTokenDTO.getCreationDate());
        Mockito.when(jwtTokenUtil.getExpirationDateFromToken(tokensDTO.getAccessToken()))
                .thenReturn(refreshTokenDTO.getExpirationDate());

        userService.findUserByUsername(user.getEmail());

        Assertions.assertEquals(refreshTokenDTO, logoutService.validateRefreshTokenReq(tokenDTO));
    }

    @Test
    void validateRefreshTokenReqException() {
        logoutService.logoutReq(tokensDTO);

        TokenInBlackListException tokenInBlackListException = Assertions.assertThrows(TokenInBlackListException.class,
                () -> logoutService.validateRefreshTokenReq(tokenDTO));

        Assertions.assertEquals("A", tokenInBlackListException.getMessage());
    }

    @Test
    void logoutReq() {
        Assertions.assertEquals(genericDTO, logoutService.logoutReq(tokensDTO));
    }
}
