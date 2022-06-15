package com.tmp.authentication.authorization.jwt.repositories;

import com.tmp.authentication.authorization.jwt.entities.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CertificateRepository extends JpaRepository<Certificate, Long> {

}
