package com.tmp.send.email.microservice.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailCourseCompletedEmployeeDTO {

    private String employeeEmail;
    private String employeeUsername;

    private String courseName;
    private Long courseId;
    private String courseCategory;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy - HH:mm:ss")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = "dd.MM.yyyy - HH:mm:ss")
    private LocalDateTime courseStartDate;
}
