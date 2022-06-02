package com.tmp.send.email.microservice.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailAssignedCourseManagerDTO {

    private String managerEmail;
    private String managerUsername;

    private String employeeUsername;
    private String employeeEmail;

    private String courseName;
    private Long courseId;
    private String courseCategory;
    private Integer timeToMakeCourse;
}
