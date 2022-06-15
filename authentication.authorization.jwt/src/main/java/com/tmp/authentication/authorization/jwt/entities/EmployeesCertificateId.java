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
public class EmployeesCertificateId implements Serializable {

    private static final long serialVersionUID = 6521677269902122107L;

    @Column(name = "id_certificate", nullable = false)
    private Long idCertificate;

    @Column(name = "id_employee", nullable = false)
    private Long idEmployee;
}