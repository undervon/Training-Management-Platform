package com.tmp.courses.microservice.services;

import com.tmp.courses.microservice.entities.Course;
import com.tmp.courses.microservice.entities.Survey;
import com.tmp.courses.microservice.models.AddSurveyDTO;
import com.tmp.courses.microservice.models.SurveyDTO;
import com.tmp.courses.microservice.models.adapters.SurveyAdapter;
import com.tmp.courses.microservice.repositories.SurveyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class SurveyService {

    private final SurveyRepository surveyRepository;
    private final CourseService courseService;

    public SurveyDTO addSurveyReq(AddSurveyDTO addSurveyDTO) {
        Course course = courseService.findCourseById(addSurveyDTO.getCourseId());

        course.setRatedNumber(course.getRatedNumber() + 1);
        Course updatedCourse = courseService.saveCourse(course);

        String surveyName = "Survey" + "-" + course.getId();
        Survey newSurvey = Survey.builder()
                .name(surveyName)
                .rating(addSurveyDTO.getRating())
                .course(course)
                .build();

        surveyRepository.save(newSurvey);

        List<Survey> surveys = surveyRepository.getSurveysByName(surveyName);

        Double sumRating = 0.0;

        for (Survey survey : surveys) {
            sumRating += survey.getRating();
        }

        updatedCourse.setRating(sumRating / updatedCourse.getRatedNumber());
        courseService.saveCourse(updatedCourse);

        return SurveyAdapter.surveyToSurveyDTO(newSurvey);
    }
}
