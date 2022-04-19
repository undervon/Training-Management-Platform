package com.tmp.authentication.authorization.jwt.services;

import com.tmp.authentication.authorization.jwt.entities.Role;
import com.tmp.authentication.authorization.jwt.entities.User;
import com.tmp.authentication.authorization.jwt.entities.UserRoleId;
import com.tmp.authentication.authorization.jwt.exceptions.BadCredentialsException;
import com.tmp.authentication.authorization.jwt.exceptions.GenericException;
import com.tmp.authentication.authorization.jwt.exceptions.ImageContentTypeException;
import com.tmp.authentication.authorization.jwt.exceptions.ImageEmptyException;
import com.tmp.authentication.authorization.jwt.exceptions.RoleAlreadyExistsException;
import com.tmp.authentication.authorization.jwt.exceptions.RoleDoesNotExistException;
import com.tmp.authentication.authorization.jwt.exceptions.UnableToDeleteUserException;
import com.tmp.authentication.authorization.jwt.exceptions.UnsupportedRolesSizeException;
import com.tmp.authentication.authorization.jwt.exceptions.UserAlreadyExistsException;
import com.tmp.authentication.authorization.jwt.exceptions.UserNotFoundException;
import com.tmp.authentication.authorization.jwt.models.RoleDTO;
import com.tmp.authentication.authorization.jwt.models.UserDTO;
import com.tmp.authentication.authorization.jwt.models.adapters.UserAdapter;
import com.tmp.authentication.authorization.jwt.models.adapters.UserRoleAdapter;
import com.tmp.authentication.authorization.jwt.models.enums.RoleValue;
import com.tmp.authentication.authorization.jwt.models.AddUserDTO;
import com.tmp.authentication.authorization.jwt.repositories.RoleRepository;
import com.tmp.authentication.authorization.jwt.repositories.UserRepository;
import com.tmp.authentication.authorization.jwt.repositories.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;

    @Value("${api.path}")
    private String apiPath;

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

    public void checkIfImageIsEmpty(MultipartFile image) {
        if (image.isEmpty()) {
            throw new ImageEmptyException();
        }
    }

    public void checkImageContentType(MultipartFile image) {
        List<String> acceptableContentType = Arrays.asList(MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE);

        if (!acceptableContentType.contains(image.getContentType())) {
            throw new ImageContentTypeException();
        }
    }

    @Transactional
    public UserDTO addUser(AddUserDTO addUserDTO, MultipartFile image) {
        log.info("[{}] -> addUser, addUserDTO: {}", this.getClass().getSimpleName(), addUserDTO);

        try {
            User dbUser = this.findUserByUsername(addUserDTO.getEmail());

            if (dbUser.getEmail().equals(addUserDTO.getEmail())) {
                throw new UserAlreadyExistsException(addUserDTO.getEmail());
            }
        } catch (UserNotFoundException userNotFoundException) {
            log.error(userNotFoundException);
        }

        Role role = this.getRoleByRoleValue(RoleValue.EMPLOYEE);

        User user = User.builder()
                .firstName(addUserDTO.getFirstName())
                .lastName(addUserDTO.getLastName())
                .email(addUserDTO.getEmail())
                .password(addUserDTO.getPassword())
                .department(addUserDTO.getDepartment())
                .employeeNumber(addUserDTO.getEmployeeNumber())
                .joinDate(LocalDateTime.now())
                .build();

        this.checkIfImageIsEmpty(image);
        this.checkImageContentType(image);

        try {
            user.setImage(image.getBytes());
        } catch (IOException ioException) {
            log.error(ioException);
            throw new GenericException();
        }

        userRepository.save(user);

        userRoleRepository.save(UserRoleAdapter.createUserRoleObject(role, user));

        return UserAdapter.userToUserDTO(user, apiPath);
    }

    @Transactional
    public void editUser(AddUserDTO addUserDTO, MultipartFile image, Long id) {
        log.info("[{}] -> editUser, addUserDTO: {}, id: {}", this.getClass().getSimpleName(), addUserDTO, id);

        // Check if user exist in db
        User dbUser = this.findUserById(id);

        User user = User.builder()
                .id(id)
                .firstName(addUserDTO.getFirstName())
                .lastName(addUserDTO.getLastName())
                .email(addUserDTO.getEmail())
                .password(addUserDTO.getPassword())
                .department(addUserDTO.getDepartment())
                .employeeNumber(addUserDTO.getEmployeeNumber())
                .joinDate(dbUser.getJoinDate())
                .roles(dbUser.getRoles())
                .build();

        if (image.isEmpty()) {
            user.setImage(dbUser.getImage());
        } else {
            this.checkImageContentType(image);

            try {
                user.setImage(image.getBytes());
            } catch (IOException ioException) {
                log.error(ioException);
                throw new GenericException();
            }
        }

        userRepository.save(user);
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

        roles.forEach(role -> {
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

        userRoleRepository.save(UserRoleAdapter.createUserRoleObject(role, user));
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

        userRoleRepository.delete(UserRoleAdapter.createUserRoleObject(role, user));
    }

    public UserDTO getUserById(Long id) {
        log.info("[{}] -> getUserById, id: {}", this.getClass().getSimpleName(), id);

        User user = this.findUserById(id);

        return UserAdapter.userToUserDTO(user, apiPath);
    }

    public List<UserDTO> getUsers() {
        log.info("[{}] -> getUsers", this.getClass().getSimpleName());

        List<User> userList = userRepository.findAll();

        return UserAdapter.userListToUserDTOList(userList, apiPath);
    }
}
