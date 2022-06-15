package com.tmp.authentication.authorization.jwt.repositories;

import com.tmp.authentication.authorization.jwt.entities.UserRole;
import com.tmp.authentication.authorization.jwt.entities.UserRoleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleId> {

}
