package com.tmp.authentication.authorization.jwt.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleId implements Serializable {

    private static final long serialVersionUID = 6587945476847710317L;

    @Column(name = "id_employee", nullable = false)
    private Long idUser;

    @Column(name = "id_role", nullable = false)
    private Long idRole;
}