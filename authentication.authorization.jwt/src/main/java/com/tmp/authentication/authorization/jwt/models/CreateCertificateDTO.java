package com.tmp.authentication.authorization.jwt.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCertificateDTO {

    private String employeeFirstName;
    private String employeeLastName;
    private Integer employeeNumber;

    private String courseName;
    private String courseCategory;
}
