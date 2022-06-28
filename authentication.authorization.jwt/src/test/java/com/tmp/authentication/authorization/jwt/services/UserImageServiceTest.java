package com.tmp.authentication.authorization.jwt.services;

import com.tmp.authentication.authorization.jwt.entities.User;
import com.tmp.authentication.authorization.jwt.exceptions.UserImageNotFoundException;
import com.tmp.authentication.authorization.jwt.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ActiveProfiles(profiles = "local-test")
@RunWith(MockitoJUnitRunner.Silent.class)
class UserImageServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserImageService userImageService;

    private User user;

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
    }

    @Test
    void getUserImageByIdReq() throws IOException {
        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        Resource resource = userImageService.getUserImageByIdReq(user.getId());
        Assertions.assertEquals(1, resource.contentLength());
    }

    @Test
    void getUserImageByIdReqException() {
        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        user.setImage(null);
        UserImageNotFoundException userImageNotFoundException =
                Assertions.assertThrows(UserImageNotFoundException.class,
                        () -> userImageService.getUserImageByIdReq(user.getId()));

        Assertions.assertEquals("1", userImageNotFoundException.getMessage());
    }
}
