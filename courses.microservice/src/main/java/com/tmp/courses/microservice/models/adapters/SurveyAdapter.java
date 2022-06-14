package com.tmp.courses.microservice.models.adapters;

import com.tmp.courses.microservice.entities.Survey;
import com.tmp.courses.microservice.models.SurveyDTO;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class SurveyAdapter {

    public static SurveyDTO surveyToSurveyDTO(Survey survey) {
        return SurveyDTO.builder()
                .id(survey.getId())
                .name(survey.getName())
                .rating(survey.getRating())
                .build();
    }
}
