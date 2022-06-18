package com.tmp.assigned.courses.microservice.models.enums;

import org.springframework.security.core.GrantedAuthority;

public enum RoleValue implements GrantedAuthority {
    EMPLOYEE,
    MANAGER,
    ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }
}
