package com.tmp.assigned.courses.microservice.services;

import com.tmp.assigned.courses.microservice.entities.AssignedCourses;
import com.tmp.assigned.courses.microservice.models.AssignCourseDTO;
import com.tmp.assigned.courses.microservice.models.CoursesStatisticsDTO;
import com.tmp.assigned.courses.microservice.models.CreateAssignCourseDTO;
import com.tmp.assigned.courses.microservice.models.GetAssignCourseDTO;
import com.tmp.assigned.courses.microservice.repositories.AssignedCoursesRepository;
import com.tmp.assigned.courses.microservice.vo.CompactedCourse;
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
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ActiveProfiles(profiles = "local-test")
@RunWith(MockitoJUnitRunner.Silent.class)
class AssignedCoursesServiceTest {

    @Mock
    private AssignedCoursesRepository assignedCoursesRepository;

    @InjectMocks
    private AssignedCoursesService assignedCoursesService;

    private CreateAssignCourseDTO createAssignCourseDTO;
    private AssignedCourses assignedCourses;
    private AssignCourseDTO assignCourseDTO;
    private CompactedCourse compactedCourse;
    private CoursesStatisticsDTO coursesStatisticsDTO;
    private GetAssignCourseDTO getAssignCourseDTO;

    @BeforeEach
    void setUp() {
        createAssignCourseDTO = CreateAssignCourseDTO.builder()
                .idCourse(1L)
                .idEmployee(1L)
                .build();

        assignedCourses = AssignedCourses.builder()
                .id(1L)
                .idCourse(1L)
                .idEmployee(1L)
                .completed(true)
                .date(LocalDateTime.now())
                .build();

        assignCourseDTO = AssignCourseDTO.builder()
                .id(1L)
                .idCourse(1L)
                .idEmployee(1L)
                .completed(true)
                .date(LocalDateTime.now())
                .build();

        compactedCourse = CompactedCourse.builder()
                .id(1L)
                .name("A")
                .description("A")
                .rating(0.0)
                .build();

        coursesStatisticsDTO = CoursesStatisticsDTO.builder()
                .countCompletedCourses(1)
                .countAssignedCourses(1)
                .build();

        getAssignCourseDTO = GetAssignCourseDTO.builder()
                .id(1L)
                .date(LocalDateTime.now())
                .completed(true)
                .build();
    }

    @Test
    void assignUserCourseReq() {
        assignedCoursesRepository.save(assignedCourses);

        Assertions.assertEquals(assignCourseDTO, assignedCoursesService.assignUserCourseReq(createAssignCourseDTO));
    }

    @Test
    void setCompletedCourseReq() {
        assignedCoursesRepository.save(assignedCourses);

        assignedCoursesService.setCompletedCourseReq(createAssignCourseDTO);
    }

    @Test
    void getCompletedCoursesReq() {
        Mockito.when(assignedCoursesRepository.getAssignedCoursesByIdEmployeeAndCompleted(1L, true))
                .thenReturn(List.of(assignedCourses));

        Assertions.assertEquals(compactedCourse, assignedCoursesService.getCompletedCoursesReq(1L));
    }

    @Test
    void getIncompleteCoursesReq() {
        Mockito.when(assignedCoursesRepository.getAssignedCoursesByIdEmployeeAndCompleted(1L, false))
                .thenReturn(List.of(assignedCourses));

        Assertions.assertEquals(compactedCourse, assignedCoursesService.getIncompleteCoursesReq(1L));
    }

    @Test
    void getCoursesStatisticsReq() {
        Mockito.when(assignedCoursesRepository.countAssignedCoursesByIdEmployeeAndCompleted(1L, true)).thenReturn(1);
        Mockito.when(assignedCoursesRepository.countAssignedCoursesByIdEmployeeAndCompleted(1L, false)).thenReturn(1);

        Assertions.assertEquals(coursesStatisticsDTO, assignedCoursesService.getCoursesStatisticsReq(1L));
    }

    @Test
    void getAssignedCoursePropertiesReq() {
        Mockito.when(assignedCoursesRepository.findAssignedCoursesByIdCourseAndIdEmployee(1L, 1L))
                .thenReturn(Optional.of(assignedCourses));

        Assertions.assertEquals(getAssignCourseDTO, assignedCoursesService.getAssignedCoursePropertiesReq(1L, 1L));
    }

    @Test
    void deleteAssignedCoursesByIdCourseReq() {
        assignedCoursesRepository.deleteAllByIdCourse(1L);

        assignedCoursesService.deleteAssignedCoursesByIdCourseReq(1L);
    }
}
