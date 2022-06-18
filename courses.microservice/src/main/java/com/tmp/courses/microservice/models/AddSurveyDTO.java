package com.tmp.courses.microservice.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddSurveyDTO {

    private Long courseId;

    @Max(value = 5)
    private Double rating;
}
