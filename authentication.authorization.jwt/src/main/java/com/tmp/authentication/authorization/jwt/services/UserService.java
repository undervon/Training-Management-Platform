package com.tmp.authentication.authorization.jwt.services;

import com.tmp.authentication.authorization.jwt.entities.Role;
import com.tmp.authentication.authorization.jwt.entities.User;
import com.tmp.authentication.authorization.jwt.entities.UserRole;
import com.tmp.authentication.authorization.jwt.entities.UserRoleId;
import com.tmp.authentication.authorization.jwt.exceptions.BadCredentialsException;
import com.tmp.authentication.authorization.jwt.exceptions.RoleAlreadyExistsException;
import com.tmp.authentication.authorization.jwt.exceptions.RoleDoesNotExistException;
import com.tmp.authentication.authorization.jwt.exceptions.UnableToDeleteUserException;
import com.tmp.authentication.authorization.jwt.exceptions.UnsupportedRolesSizeException;
import com.tmp.authentication.authorization.jwt.exceptions.UserAlreadyExistsException;
import com.tmp.authentication.authorization.jwt.exceptions.UserNotFoundException;
import com.tmp.authentication.authorization.jwt.models.RoleDTO;
import com.tmp.authentication.authorization.jwt.models.adapters.UserAdapter;
import com.tmp.authentication.authorization.jwt.models.enums.RoleValue;
import com.tmp.authentication.authorization.jwt.models.UserDTO;
import com.tmp.authentication.authorization.jwt.repositories.RoleRepository;
import com.tmp.authentication.authorization.jwt.repositories.UserRepository;
import com.tmp.authentication.authorization.jwt.repositories.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;

    public User findUserByUsername(String username) {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UserNotFoundException(username));
    }

    public User getUserByUsername(String username) {
        return userRepository.getByEmail(username);
    }

    public User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id.toString()));
    }

    public void checkPassword(String dbPassword, String inPassword) {
        if (!dbPassword.equals(inPassword)) {
            throw new BadCredentialsException("password");
        }
    }

    public Role findRoleByRoleValue(RoleValue roleValue) {
        return roleRepository.findByRoleValue(roleValue)
                .orElseThrow(() -> new RoleDoesNotExistException(roleValue.getAuthority()));
    }

    public Role getRoleByRoleValue(RoleValue roleValue) {
        return roleRepository.getByRoleValue(roleValue);
    }

    public UserDTO addUser(UserDTO userDTO) {
        log.info("[{}] -> addUser, userDTO: {}", this.getClass().getSimpleName(), userDTO);

        try {
            User dbUser = this.findUserByUsername(userDTO.getEmail());

            if (dbUser.getEmail().equals(userDTO.getEmail())) {
                throw new UserAlreadyExistsException(userDTO.getEmail());
            }
        } catch (UserNotFoundException userNotFoundException) {
            log.error(userNotFoundException);
        }

        Role role = this.getRoleByRoleValue(RoleValue.EMPLOYEE);

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

    public void deleteUser(Long id) {
        log.info("[{}] -> deleteUser, id: {}", this.getClass().getSimpleName(), id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id.toString()));

        List<Role> roles = user.getRoles().stream()
                .map(role -> this.getRoleByRoleValue(role.getRoleValue()))
                .collect(Collectors.toList());

        if (roles.size() == 1 && roles.get(0).getRoleValue() == RoleValue.ADMIN) {
            throw new UnableToDeleteUserException();
        }

        roles.stream()
                .forEach(role -> {
                    UserRoleId userRoleId = UserRoleId.builder()
                            .idRole(role.getId())
                            .idUser(user.getId())
                            .build();

                    userRoleRepository.deleteById(userRoleId);
                });

        user.setRoles(null);

        userRepository.delete(user);
    }

    public void editRole(Long id, RoleDTO roleDTO) {
        log.info("[{}] -> editRole, id: {}, roleDTO: {}", this.getClass().getSimpleName(), id, roleDTO);

        RoleValue newRoleValue = roleDTO.getRoleValue();

        User user = this.findUserById(id);
        Role role = this.findRoleByRoleValue(newRoleValue);

        if (user.getRoles().contains(role)) {
            throw new RoleAlreadyExistsException(newRoleValue.getAuthority());
        }

        userRoleRepository.save(this.createUserRoleObject(role, user));
    }

    public void deleteRole(Long id, RoleDTO roleDTO) {
        log.info("[{}] -> deleteRole, roleDTO: {}", this.getClass().getSimpleName(), roleDTO);

        RoleValue newRoleValue = roleDTO.getRoleValue();

        User user = this.findUserById(id);
        Role role = this.findRoleByRoleValue(newRoleValue);

        if (user.getRoles().size() == 1) {
            throw new UnsupportedRolesSizeException();
        }

        if (!user.getRoles().contains(role)) {
            throw new RoleDoesNotExistException(newRoleValue.getAuthority());
        }

        userRoleRepository.delete(this.createUserRoleObject(role, user));
    }

    public UserDTO getUserById(Long id) {
        log.info("[{}] -> getUserById, id: {}", this.getClass().getSimpleName(), id);

        User user = this.findUserById(id);

        return UserAdapter.userToUserDTO(user);
    }

    public List<UserDTO> getUsers() {
        log.info("[{}] -> getUsers", this.getClass().getSimpleName());

        List<User> userList = userRepository.findAll();

        return UserAdapter.userListToUserDTOList(userList);
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
