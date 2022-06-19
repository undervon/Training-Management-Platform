package com.tmp.assigned.courses.microservice.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssignCourseDTO {

    private Long id;

    private LocalDateTime date;
    private String state;
    private Boolean completed;

    private Long idEmployee;
}