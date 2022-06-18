package com.tmp.authentication.authorization.jwt.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EditUserDTO {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String department;
    private String employeeNumber;
}
