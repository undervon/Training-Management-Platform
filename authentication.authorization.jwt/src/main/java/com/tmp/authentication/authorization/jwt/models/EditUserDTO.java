package com.tmp.authentication.authorization.jwt.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EditUserDTO {

    private String firstName;
    private String lastName;
    private String email;

    @Pattern(regexp = "HMI|VNI|ADAS|PSS|ADMIN")
    private String department;
    private String employeeNumber;
}
