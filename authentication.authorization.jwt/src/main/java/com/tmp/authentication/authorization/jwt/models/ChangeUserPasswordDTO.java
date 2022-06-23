package com.tmp.authentication.authorization.jwt.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangeUserPasswordDTO {

    private String email;
    private String oldPassword;
    private String newPassword;
    private String confirmNewPassword;
}
