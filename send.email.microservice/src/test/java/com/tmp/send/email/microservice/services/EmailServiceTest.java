package com.tmp.send.email.microservice.services;

import com.tmp.send.email.microservice.models.EmailAssignedCourseEmployeeDTO;
import com.tmp.send.email.microservice.models.EmailAssignedCourseManagerDTO;
import com.tmp.send.email.microservice.models.EmailCourseCompletedEmployeeDTO;
import com.tmp.send.email.microservice.models.EmailCourseCompletedManagerDTO;
import com.tmp.send.email.microservice.models.EmailCreateCourseManagerDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

@ExtendWith(SpringExtension.class)
@ActiveProfiles(profiles = "local-test")
@RunWith(MockitoJUnitRunner.Silent.class)
class EmailServiceTest {

    @InjectMocks
    private EmailService emailService;

    private EmailAssignedCourseManagerDTO emailAssignedCourseManagerDTO;
    private EmailAssignedCourseEmployeeDTO emailAssignedCourseEmployeeDTO;
    private EmailCourseCompletedManagerDTO emailCourseCompletedManagerDTO;
    private EmailCourseCompletedEmployeeDTO emailCourseCompletedEmployeeDTO;
    private EmailCreateCourseManagerDTO emailCreateCourseManagerDTO;

    @BeforeEach
    void setUp() {
        emailAssignedCourseManagerDTO = EmailAssignedCourseManagerDTO.builder()
                .managerEmail("A")
                .managerUsername("A")
                .employeeUsername("A")
                .employeeEmail("A")
                .courseName("A")
                .courseId(1L)
                .courseCategory("A")
                .timeToMakeCourse("A")
                .build();

        emailAssignedCourseEmployeeDTO = EmailAssignedCourseEmployeeDTO.builder()
                .employeeUsername("A")
                .employeeEmail("A")
                .courseName("A")
                .courseId(1L)
                .courseCategory("A")
                .timeToMakeCourse("A")
                .build();

        emailCourseCompletedManagerDTO = EmailCourseCompletedManagerDTO.builder()
                .managerEmail("A")
                .managerUsername("A")
                .employeeUsername("A")
                .employeeEmail("A")
                .courseName("A")
                .courseId(1L)
                .courseCategory("A")
                .courseStartDate(LocalDateTime.now())
                .build();

        emailCourseCompletedEmployeeDTO = EmailCourseCompletedEmployeeDTO.builder()
                .employeeUsername("A")
                .employeeEmail("A")
                .courseName("A")
                .courseId(1L)
                .courseCategory("A")
                .courseStartDate(LocalDateTime.now())
                .build();

        emailCreateCourseManagerDTO = EmailCreateCourseManagerDTO.builder()
                .managerEmail("A")
                .managerUsername("A")
                .employeeUsername("A")
                .employeeEmail("A")
                .courseName("A")
                .courseId(1L)
                .courseCategory("A")
                .build();
    }

    @Test
    void sendEmailAssignedCourseManagerReq() {
        emailService.sendEmailAssignedCourseManagerReq(emailAssignedCourseManagerDTO, "A");
    }

    @Test
    void sendEmailAssignedCourseEmployeeReq() {
        emailService.sendEmailAssignedCourseEmployeeReq(emailAssignedCourseEmployeeDTO, "A");
    }

    @Test
    void sendEmailCourseCompletedManagerReq() {
        emailService.sendEmailCourseCompletedManagerReq(emailCourseCompletedManagerDTO, "A");
    }

    @Test
    void sendEmailCourseCompletedEmployeeReq() {
        emailService.sendEmailCourseCompletedEmployeeReq(emailCourseCompletedEmployeeDTO, "A");
    }

    @Test
    void sendEmailCreateCourseManagerReq() {
        emailService.sendEmailCreateCourseManagerReq(emailCreateCourseManagerDTO, "A");
    }
}
