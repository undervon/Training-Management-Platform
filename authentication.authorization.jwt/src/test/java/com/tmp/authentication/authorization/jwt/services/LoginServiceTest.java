package com.tmp.authentication.authorization.jwt.services;

import com.tmp.authentication.authorization.jwt.entities.User;
import com.tmp.authentication.authorization.jwt.models.RefreshTokenDTO;
import com.tmp.authentication.authorization.jwt.models.TokenDTO;
import com.tmp.authentication.authorization.jwt.models.TokensDTO;
import com.tmp.authentication.authorization.jwt.models.UserCredentialsDTO;
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
import java.util.Date;

@ExtendWith(SpringExtension.class)
@ActiveProfiles(profiles = "local-test")
@RunWith(MockitoJUnitRunner.Silent.class)
class LoginServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private LogoutService logoutService;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @InjectMocks
    private LoginService loginService;

    private User user;
    private UserCredentialsDTO userCredentialsDTO;
    private TokensDTO tokensDTO;
    private TokenDTO tokenDTO;
    private RefreshTokenDTO refreshTokenDTO;

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

        userCredentialsDTO = UserCredentialsDTO.builder()
                .username("A")
                .password("A")
                .build();

        tokensDTO = TokensDTO.builder()
                .accessToken("A")
                .refreshToken("A")
                .build();

        tokenDTO = TokenDTO.builder()
                .token("A")
                .build();

        refreshTokenDTO = RefreshTokenDTO.builder()
                .username("A")
                .creationDate(new Date())
                .expirationDate(new Date())
                .build();
    }

    @Test
    void loginReq() {
        Mockito.when(userService.findUserByUsername(user.getEmail())).thenReturn(user);

        userService.checkPassword(user.getPassword(), userCredentialsDTO.getPassword());

        Mockito.when(jwtTokenUtil.generateAccessToken(user)).thenReturn(tokensDTO.getAccessToken());
        Mockito.when(jwtTokenUtil.generateRefreshToken(user)).thenReturn(tokensDTO.getRefreshToken());

        Assertions.assertEquals(tokensDTO, loginService.loginReq(userCredentialsDTO));
    }

    @Test
    void generateAccessTokenReq() {
        Mockito.when(logoutService.validateRefreshTokenReq(tokenDTO)).thenReturn(refreshTokenDTO);

        Mockito.when(userService.getUserByUsername(refreshTokenDTO.getUsername())).thenReturn(user);
        Mockito.when(jwtTokenUtil.generateAccessToken(user)).thenReturn(tokensDTO.getAccessToken());

        Assertions.assertEquals(tokenDTO, loginService.generateAccessTokenReq(tokenDTO));
    }
}
