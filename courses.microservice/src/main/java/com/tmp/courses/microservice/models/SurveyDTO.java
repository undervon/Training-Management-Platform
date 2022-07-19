package com.tmp.courses.microservice.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SurveyDTO {

    @NotNull
    private Long id;

    private String name;

    @Max(value = 5)
    private Double rating;
}
