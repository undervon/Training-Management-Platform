package com.tmp.courses.microservice.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CoursesCategoryDTO {

    private Long id;

    private String name;
    private String description;

    private Double rating;
}
