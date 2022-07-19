package com.tmp.authentication.authorization.jwt.repositories;

import com.tmp.authentication.authorization.jwt.entities.Certificate;
import com.tmp.authentication.authorization.jwt.entities.EmployeesCertificate;
import com.tmp.authentication.authorization.jwt.entities.EmployeesCertificateId;
import com.tmp.authentication.authorization.jwt.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeesCertificateRepository extends JpaRepository<EmployeesCertificate, EmployeesCertificateId> {

    List<EmployeesCertificate> getEmployeesCertificatesByIdEmployee(User idEmployee);

    void deleteEmployeesCertificateByIdCertificateAndIdEmployee(Certificate certificate, User user);
}
