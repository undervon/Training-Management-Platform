package com.tmp.assigned.courses.microservice.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Course {

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

    private List<FileInfo> fileInfo;
}
