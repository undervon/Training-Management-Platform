package com.tmp.authentication.authorization.jwt.services;

import com.tmp.authentication.authorization.jwt.entities.Role;
import com.tmp.authentication.authorization.jwt.entities.User;
import com.tmp.authentication.authorization.jwt.entities.UserRole;
import com.tmp.authentication.authorization.jwt.entities.UserRoleId;
import com.tmp.authentication.authorization.jwt.exceptions.BadCredentialsException;
import com.tmp.authentication.authorization.jwt.exceptions.RoleAlreadyExistsException;
import com.tmp.authentication.authorization.jwt.exceptions.RoleDoesNotExistException;
import com.tmp.authentication.authorization.jwt.exceptions.UnsupportedRolesSizeException;
import com.tmp.authentication.authorization.jwt.exceptions.UserNotFoundException;
import com.tmp.authentication.authorization.jwt.models.EditRoleDTO;
import com.tmp.authentication.authorization.jwt.models.Roles;
import com.tmp.authentication.authorization.jwt.repositories.RoleRepository;
import com.tmp.authentication.authorization.jwt.repositories.UserRepository;
import com.tmp.authentication.authorization.jwt.repositories.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;

    User findByUsername(String username) {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UserNotFoundException(username));
    }

    public void checkPassword(String dbPassword, String inPassword) {
        if (!dbPassword.equals(inPassword)) {
            throw new BadCredentialsException("password");
        }
    }

    User getByUsername(String username) {
        return userRepository.getByEmail(username);
    }

    Role findByRoles(Roles roles) {
        return roleRepository.findByRoles(roles)
                .orElseThrow(() -> new RoleDoesNotExistException(roles.getAuthority()));
    }

    public void editRole(EditRoleDTO editRoleDTO) {
        log.info("[{}] -> editRole, editRoleDTO: {}", this.getClass().getSimpleName(), editRoleDTO);

        String username = editRoleDTO.getUsername();
        Roles newRole = editRoleDTO.getRole();

        User user = this.findByUsername(username);
        Role role = this.findByRoles(newRole);

        if (user.getRoles().contains(role)) {
            throw new RoleAlreadyExistsException(newRole.getAuthority());
        }

        UserRoleId userRoleId = UserRoleId.builder()
                .idRole(role.getId())
                .idUser(user.getId())
                .build();

        UserRole userRole = UserRole.builder()
                .id(userRoleId)
                .idUser(user)
                .idRole(role)
                .build();

        userRoleRepository.save(userRole);
    }

    public void deleteRole(EditRoleDTO editRoleDTO) {
        log.info("[{}] -> deleteRole, editRoleDTO: {}", this.getClass().getSimpleName(), editRoleDTO);

        String username = editRoleDTO.getUsername();
        Roles newRole = editRoleDTO.getRole();

        User user = this.findByUsername(username);
        Role role = this.findByRoles(newRole);

        if (user.getRoles().size() == 1) {
            throw new UnsupportedRolesSizeException();
        }

        if (!user.getRoles().contains(role)) {
            throw new RoleDoesNotExistException(newRole.getAuthority());
        }

        UserRoleId userRoleId = UserRoleId.builder()
                .idRole(role.getId())
                .idUser(user.getId())
                .build();

        UserRole userRole = UserRole.builder()
                .id(userRoleId)
                .idUser(user)
                .idRole(role)
                .build();

        userRoleRepository.delete(userRole);
    }
}
