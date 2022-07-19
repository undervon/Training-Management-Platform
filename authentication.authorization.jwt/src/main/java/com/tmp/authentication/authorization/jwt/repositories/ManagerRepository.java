package com.tmp.authentication.authorization.jwt.repositories;

import com.tmp.authentication.authorization.jwt.entities.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ManagerRepository extends JpaRepository<Manager, Long> {

    Optional<Manager> findManagerByEmail(String email);

    Manager getManagerByEmail(String email);

    Boolean existsManagerByEmail(String email);

    Optional<Manager> findManagerById(Long id);
}
