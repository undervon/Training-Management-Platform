package com.tmp.courses.microservice.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseDTO {

    private Long id;

    private String name;
    private String description;
    private String language;
    private String requirements;
    private String category;

    private Double rating;
    private Integer ratedNumber;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy - HH:mm:ss")
    private LocalDateTime date;

    private String duration;
    private String timeToMake;

    private String path;
    private Boolean containsCertificate;
}
