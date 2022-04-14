package com.tmp.authentication.authorization.jwt.models;

import com.tmp.authentication.authorization.jwt.models.enums.RoleValue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EditRoleDTO {

    private String username;
    private RoleValue roleValue;
}
