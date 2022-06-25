package com.tmp.authentication.authorization.jwt.repositories;

import com.tmp.authentication.authorization.jwt.entities.Certificate;
import com.tmp.authentication.authorization.jwt.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface CertificateRepository extends JpaRepository<Certificate, Long> {

    List<Certificate> getCertificatesByUsersIn(Set<User> users);

    void deleteCertificateById(Long id);
}
