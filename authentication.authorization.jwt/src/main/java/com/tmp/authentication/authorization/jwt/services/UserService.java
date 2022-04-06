package com.tmp.authentication.authorization.jwt.services;

import com.tmp.authentication.authorization.jwt.entities.Role;
import com.tmp.authentication.authorization.jwt.entities.User;
import com.tmp.authentication.authorization.jwt.entities.UserRole;
import com.tmp.authentication.authorization.jwt.entities.UserRoleId;
import com.tmp.authentication.authorization.jwt.exceptions.BadCredentialsException;
import com.tmp.authentication.authorization.jwt.exceptions.RoleAlreadyExistsException;
import com.tmp.authentication.authorization.jwt.exceptions.RoleDoesNotExistException;
import com.tmp.authentication.authorization.jwt.exceptions.UnsupportedRolesSizeException;
import com.tmp.authentication.authorization.jwt.exceptions.UserAlreadyExistsException;
import com.tmp.authentication.authorization.jwt.exceptions.UserNotFoundException;
import com.tmp.authentication.authorization.jwt.models.EditRoleDTO;
import com.tmp.authentication.authorization.jwt.models.Roles;
import com.tmp.authentication.authorization.jwt.models.UserDTO;
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

    public User findByUsername(String username) {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UserNotFoundException(username));
    }

    public void checkPassword(String dbPassword, String inPassword) {
        if (!dbPassword.equals(inPassword)) {
            throw new BadCredentialsException("password");
        }
    }

    public User getByUsername(String username) {
        return userRepository.getByEmail(username);
    }

    public Role findByRoles(Roles roles) {
        return roleRepository.findByRoles(roles)
                .orElseThrow(() -> new RoleDoesNotExistException(roles.getAuthority()));
    }

    Role getByRoles(Roles roles) {
        return roleRepository.getByRoles(roles);
    }

    public UserDTO addUser(UserDTO userDTO) {
        log.info("[{}] -> addUser, userDTO: {}", this.getClass().getSimpleName(), userDTO);

        try {
            User dbUser = this.findByUsername(userDTO.getEmail());

            if (dbUser.getEmail().equals(userDTO.getEmail())) {
                throw new UserAlreadyExistsException(userDTO.getEmail());
            }
        } catch (UserNotFoundException userNotFoundException) {
            log.error(userNotFoundException);
        }

        Role role = this.getByRoles(Roles.EMPLOYEE);

        User user = User.builder()
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .email(userDTO.getEmail())
                .password(userDTO.getPassword())
                .department(userDTO.getDepartment())
                .employeeNumber(userDTO.getEmployeeNumber())
                .build();

        userRepository.save(user);

        userRoleRepository.save(this.createUserRoleObject(role, user));

        return userDTO;
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

        userRoleRepository.save(this.createUserRoleObject(role, user));
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

        userRoleRepository.delete(this.createUserRoleObject(role, user));
    }

    private UserRole createUserRoleObject(Role role, User user) {
        UserRoleId userRoleId = UserRoleId.builder()
                .idRole(role.getId())
                .idUser(user.getId())
                .build();

        return UserRole.builder()
                .id(userRoleId)
                .idUser(user)
                .idRole(role)
                .build();
    }
}
