package com.tmp.authentication.authorization.jwt.repositories;

import com.tmp.authentication.authorization.jwt.entities.EmployeesCertificate;
import com.tmp.authentication.authorization.jwt.entities.EmployeesCertificateId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeesCertificateRepository extends JpaRepository<EmployeesCertificate, EmployeesCertificateId> {

}
