package com.tmp.courses.microservice.services;

import com.tmp.courses.microservice.entities.Course;
import com.tmp.courses.microservice.entities.Survey;
import com.tmp.courses.microservice.models.AddSurveyDTO;
import com.tmp.courses.microservice.models.SurveyDTO;
import com.tmp.courses.microservice.repositories.SurveyRepository;
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

@ExtendWith(SpringExtension.class)
@ActiveProfiles(profiles = "local-test")
@RunWith(MockitoJUnitRunner.Silent.class)
class SurveyServiceTest {

    @Mock
    private SurveyRepository surveyRepository;

    @Mock
    private CourseService courseService;

    @InjectMocks
    private SurveyService surveyService;

    private AddSurveyDTO addSurveyDTO;
    private Course course;
    private Survey survey;
    private SurveyDTO surveyDTO;

    @BeforeEach
    void setUp() {
        addSurveyDTO = AddSurveyDTO.builder()
                .rating(0.0)
                .courseId(1L)
                .build();

        course = Course.builder()
                .id(1L)
                .name("A")
                .description("A")
                .language("English")
                .requirements("A")
                .category("Programming")
                .rating(0.0)
                .ratedNumber(0)
                .date(LocalDateTime.now())
                .duration("A")
                .timeToMake("A")
                .containsCertificate(true)
                .path("A")
                .build();

        survey = Survey.builder()
                .name("A")
                .rating(0.0)
                .course(course)
                .build();

        surveyDTO = SurveyDTO.builder()
                .id(1L)
                .rating(0.0)
                .name("A")
                .build();
    }

    @Test
    void addSurveyReq() {
        Mockito.when(courseService.findCourseById(addSurveyDTO.getCourseId())).thenReturn(course);
        Mockito.when(courseService.saveCourse(course)).thenReturn(course);

        surveyRepository.save(survey);

        Assertions.assertEquals(surveyDTO, surveyService.addSurveyReq(addSurveyDTO));
    }
}
