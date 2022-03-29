package com.tmp.authentication.authorization.jwt.models;

import org.springframework.security.core.GrantedAuthority;

public enum Roles implements GrantedAuthority {
    EMPLOYEE,
    MANAGER,
    ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }
}
