package com.tmp.authentication.authorization.jwt.models.adapters;

import com.tmp.authentication.authorization.jwt.entities.Role;
import com.tmp.authentication.authorization.jwt.entities.User;
import com.tmp.authentication.authorization.jwt.entities.UserRole;
import com.tmp.authentication.authorization.jwt.entities.UserRoleId;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class UserRoleAdapter {

    public static UserRole createUserRoleObject(Role role, User user) {
        UserRoleId userRoleId = UserRoleId.builder()
                .idRole(role.getId())
                .idUser(user.getId())
                .build();

        return UserRole.builder()
                .id(userRoleId)
                .idUser(user)
                .idRole(role)
                .build();
    }
}
