package com.tmp.courses.microservice.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetCourseDTO {

    private Long id;

    private String name;
    private String description;

    @Pattern(regexp = "English|Romanian")
    private String language;
    private String requirements;

    @Pattern(regexp = "Business Operations|Engineering|Management|Programming|Sales and Marketing")
    private String category;

    private Double rating;
    private Integer ratedNumber;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy - HH:mm:ss")
    private LocalDateTime date;

    private String duration;
    private String timeToMake;

    private String path;
    private Boolean containsCertificate;

    private List<FileInfoDTO> fileInfo;
}
