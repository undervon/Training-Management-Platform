package com.tmp.authentication.authorization.jwt.services;

import com.tmp.authentication.authorization.jwt.entities.Manager;
import com.tmp.authentication.authorization.jwt.entities.User;
import com.tmp.authentication.authorization.jwt.models.CertificateDTO;
import com.tmp.authentication.authorization.jwt.models.CreateCertificateDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;

@ExtendWith(SpringExtension.class)
@ActiveProfiles(profiles = "local-test")
@RunWith(MockitoJUnitRunner.Silent.class)
class CertificateServiceTest {

    @Mock
    private UserService userService;

    @InjectMocks
    CertificateService certificateService;

    private User user;
    private CertificateDTO certificateDTO;
    private CreateCertificateDTO createCertificateDTO;

    @BeforeEach
    void setUp() {
        Manager manager = Manager.builder()
                .id(1L)
                .email("A")
                .build();

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
                .manager(manager)
                .build();

        certificateDTO = CertificateDTO.builder()
                .id(1L)
                .name("A")
                .path("A")
                .availability(1)
                .releaseDate(LocalDateTime.now())
                .build();

        createCertificateDTO = CreateCertificateDTO.builder()
                .employeeId(1L)
                .courseName("A")
                .courseCategory("A")
                .build();
    }

    @Test
    void createCertificateReq() {
        Mockito.when(userService.findUserById(user.getId())).thenReturn(user);

        Assertions.assertEquals(certificateDTO, certificateService.createCertificateReq(createCertificateDTO, "A"));
    }

    @Test
    void getCertificatesByUserIdReq() {
        Mockito.when(userService.findUserById(user.getId())).thenReturn(user);

        Assertions.assertEquals(List.of(), certificateService.getCertificatesByUserIdReq(user.getId()));
    }

    @Test
    void getPdfReq() {
        Resource resource = new ByteArrayResource(new byte[1]);
        Assertions.assertEquals(resource, certificateService.getPdfReq("A"));
    }
}
