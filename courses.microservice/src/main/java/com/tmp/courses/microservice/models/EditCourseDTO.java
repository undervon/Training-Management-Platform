package com.tmp.courses.microservice.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EditCourseDTO {

    private String name;
    private String description;
    private String language;
    private String requirements;
    private String category;

    private String duration;
    private String timeToMake;

    private Boolean containsCertificate;
}
