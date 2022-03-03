package com.tmp.authentication.authorization.jwt.repositories;

import com.tmp.authentication.authorization.jwt.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

}
