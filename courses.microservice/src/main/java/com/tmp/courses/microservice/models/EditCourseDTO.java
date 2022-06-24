package com.tmp.courses.microservice.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EditCourseDTO {

    private String name;
    private String description;

    @Pattern(regexp = "English|Romanian")
    private String language;
    private String requirements;

    @Pattern(regexp = "Business Operations|Engineering|Management|Programming|Sales and Marketing")
    private String category;

    private String duration;
    private String timeToMake;

    private Boolean containsCertificate;
}
