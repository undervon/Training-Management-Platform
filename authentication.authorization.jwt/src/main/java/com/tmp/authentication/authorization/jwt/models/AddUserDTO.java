package com.tmp.authentication.authorization.jwt.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddUserDTO {

    private String firstName;
    private String lastName;
    private String email;
    private String department;
    private String employeeNumber;
}
