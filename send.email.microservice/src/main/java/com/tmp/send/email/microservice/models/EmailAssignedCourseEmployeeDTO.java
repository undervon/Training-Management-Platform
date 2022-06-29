package com.tmp.send.email.microservice.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailAssignedCourseEmployeeDTO {

    private String employeeEmail;
    private String employeeUsername;

    private String courseName;
    private Long courseId;
    private String courseCategory;
    private String timeToMakeCourse;
}