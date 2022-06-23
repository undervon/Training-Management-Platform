package com.tmp.authentication.authorization.jwt.services;

import com.tmp.authentication.authorization.jwt.entities.Certificate;
import com.tmp.authentication.authorization.jwt.entities.Manager;
import com.tmp.authentication.authorization.jwt.entities.Role;
import com.tmp.authentication.authorization.jwt.entities.User;
import com.tmp.authentication.authorization.jwt.exceptions.BadCredentialsException;
import com.tmp.authentication.authorization.jwt.exceptions.GenericException;
import com.tmp.authentication.authorization.jwt.exceptions.ImageContentTypeException;
import com.tmp.authentication.authorization.jwt.exceptions.ImageEmptyException;
import com.tmp.authentication.authorization.jwt.exceptions.ManagerNotFoundException;
import com.tmp.authentication.authorization.jwt.exceptions.PasswordException;
import com.tmp.authentication.authorization.jwt.exceptions.RoleAlreadyExistsException;
import com.tmp.authentication.authorization.jwt.exceptions.RoleDoesNotExistException;
import com.tmp.authentication.authorization.jwt.exceptions.UnableToDeleteUserException;
import com.tmp.authentication.authorization.jwt.exceptions.UnsupportedRolesSizeException;
import com.tmp.authentication.authorization.jwt.exceptions.UserAlreadyExistsException;
import com.tmp.authentication.authorization.jwt.exceptions.UserNotFoundException;
import com.tmp.authentication.authorization.jwt.models.AssignUserDTO;
import com.tmp.authentication.authorization.jwt.models.ChangeUserPasswordDTO;
import com.tmp.authentication.authorization.jwt.models.EditUserDTO;
import com.tmp.authentication.authorization.jwt.models.ManagerDTO;
import com.tmp.authentication.authorization.jwt.models.RoleDTO;
import com.tmp.authentication.authorization.jwt.models.UserDTO;
import com.tmp.authentication.authorization.jwt.models.adapters.UserAdapter;
import com.tmp.authentication.authorization.jwt.models.adapters.UserRoleAdapter;
import com.tmp.authentication.authorization.jwt.models.enums.RoleValue;
import com.tmp.authentication.authorization.jwt.models.AddUserDTO;
import com.tmp.authentication.authorization.jwt.repositories.CertificateRepository;
import com.tmp.authentication.authorization.jwt.repositories.ManagerRepository;
import com.tmp.authentication.authorization.jwt.repositories.RoleRepository;
import com.tmp.authentication.authorization.jwt.repositories.UserRepository;
import com.tmp.authentication.authorization.jwt.repositories.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final ManagerRepository managerRepository;
    private final CertificateRepository certificateRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Value("${api.path}")
    private String apiPath;

    @Value("${certificates.path}")
    private String certificatesPath;

    @Value("${user.manager-generic.email}")
    private String managerGenericUsername;

    /*
        UserService methods
     */

    protected void delete(File coursePath) {
        FileSystemUtils.deleteRecursively(Paths.get(coursePath.toString())
                .toFile());
    }

    protected User findUserByUsername(String username) {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UserNotFoundException(username));
    }

    protected User getUserByUsername(String username) {
        return userRepository.getByEmail(username);
    }

    protected User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id.toString()));
    }

    protected List<User> findUsersByManager(Manager manager, String username) {
        return userRepository.findAllByManager(manager)
                .orElseThrow(() -> new UserNotFoundException(username));
    }

    protected List<User> getAllByManagerAndDepartment(Manager manager, String department) {
        return userRepository.getAllByManagerAndDepartment(manager, department);
    }

    protected Role findRoleByRoleValue(RoleValue roleValue) {
        return roleRepository.findByRoleValue(roleValue)
                .orElseThrow(() -> new RoleDoesNotExistException(roleValue.getAuthority()));
    }

    protected Role getRoleByRoleValue(RoleValue roleValue) {
        return roleRepository.getByRoleValue(roleValue);
    }

    protected Manager findManagerByUsername(String username) {
        return managerRepository.findManagerByEmail(username)
                .orElseThrow(() -> new ManagerNotFoundException(username));
    }

    protected Manager getManagerByUsername(String username) {
        return managerRepository.getManagerByEmail(username);
    }

    protected Manager findManagerById(Long id) {
        return managerRepository.findManagerById(id)
                .orElseThrow(() -> new ManagerNotFoundException(id.toString()));
    }

    protected boolean existsManagerByUsername(String username) {
        return managerRepository.existsManagerByEmail(username);
    }

    protected void checkPassword(String dbPassword, String inPassword) {
        if (!bCryptPasswordEncoder.matches(inPassword, dbPassword)) {
            throw new BadCredentialsException();
        }
    }

    private void checkIfImageIsEmpty(MultipartFile image) {
        if (image.isEmpty()) {
            throw new ImageEmptyException();
        }
    }

    private void checkImageContentType(MultipartFile image) {
        List<String> acceptableContentType = Arrays.asList(MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE);

        if (!acceptableContentType.contains(image.getContentType())) {
            throw new ImageContentTypeException();
        }
    }

    private void checkUserAlreadyExistsInDB(String username) {
        try {
            User dbUser = findUserByUsername(username);

            if (dbUser.getEmail().equals(username)) {
                throw new UserAlreadyExistsException(username);
            }
        } catch (UserNotFoundException userNotFoundException) {
            // no-op
        }
    }

    /*
        Methods from UserController
     */
    @Transactional
    public UserDTO addUserReq(AddUserDTO addUserDTO, MultipartFile image) {
        checkUserAlreadyExistsInDB(addUserDTO.getEmail());

        Manager manager = getManagerByUsername(managerGenericUsername);

        User user = User.builder()
                .firstName(addUserDTO.getFirstName())
                .lastName(addUserDTO.getLastName())
                .email(addUserDTO.getEmail())
                .password(bCryptPasswordEncoder.encode(addUserDTO.getEmail()))
                .department(addUserDTO.getDepartment())
                .employeeNumber(addUserDTO.getEmployeeNumber())
                .manager(manager)
                .build();

        checkIfImageIsEmpty(image);
        checkImageContentType(image);

        // Set the image
        try {
            user.setImage(image.getBytes());
        } catch (IOException ioException) {
            throw new GenericException();
        }

        userRepository.save(user);

        Role role = getRoleByRoleValue(RoleValue.EMPLOYEE);

        // Set the EMPLOYEE role
        userRoleRepository.save(UserRoleAdapter.createUserRoleObject(role, user));

        return UserAdapter.userToUserDTO(user, apiPath);
    }

    @Transactional
    public void editUserReq(EditUserDTO editUserDTO, MultipartFile image, Long id) {
        // Check if the user exists in db
        User dbUser = findUserById(id);

        User user = User.builder()
                .id(id)
                .firstName(editUserDTO.getFirstName())
                .lastName(editUserDTO.getLastName())
                .email(editUserDTO.getEmail())
                .password(bCryptPasswordEncoder.encode(editUserDTO.getPassword()))
                .department(editUserDTO.getDepartment())
                .employeeNumber(editUserDTO.getEmployeeNumber())
                .joinDate(dbUser.getJoinDate())
                .roles(dbUser.getRoles())
                .manager(dbUser.getManager())
                .build();

        // If the image is empty set the old image else set the new image
        if (image.isEmpty()) {
            user.setImage(dbUser.getImage());
        } else {
            checkImageContentType(image);

            try {
                user.setImage(image.getBytes());
            } catch (IOException ioException) {
                throw new GenericException();
            }
        }

        userRepository.save(user);
    }

    @Transactional
    public void deleteUserReq(Long id) {
        User user = findUserById(id);

        // Create a role list of roles with user roles
        List<Role> roles = user.getRoles().stream()
                .map(role -> getRoleByRoleValue(role.getRoleValue()))
                .collect(Collectors.toList());

        if (roles.size() == 1 && roles.get(0).getRoleValue() == RoleValue.ADMIN) {
            throw new UnableToDeleteUserException();
        }

        user.setRoles(null);

        List<Certificate> certificateList = certificateRepository.getCertificatesByUsersIn(Set.of(user));
        if (certificateList != null) {
            certificateList.forEach(certificate -> {
                File certificatePath = new File(certificatesPath, certificate.getName() + ".pdf");
                delete(certificatePath);
            });
            certificateRepository.deleteAll(certificateList);
        }

        // Checking if the user is a MANAGER; if it is,
        // for each subordinate user the manager will be set with the generic manager
        String username = user.getEmail();
        if (existsManagerByUsername(username)) {
            final Manager manager = getManagerByUsername(username);
            final List<User> users = findUsersByManager(manager, username);

            final Manager genericManager = getManagerByUsername(managerGenericUsername);

            users.forEach(employee -> employee.setManager(genericManager));

            managerRepository.delete(manager);
        }

        userRepository.delete(user);
    }

    public void editRoleReq(Long id, RoleDTO roleDTO) {
        final RoleValue newRoleValue = roleDTO.getRoleValue();

        final User user = findUserById(id);
        final Role role = findRoleByRoleValue(newRoleValue);

        if (user.getRoles().contains(role)) {
            throw new RoleAlreadyExistsException(newRoleValue.getAuthority());
        }

        // if the input role is MANAGER, create new manager in managers table
        if (newRoleValue.equals(RoleValue.MANAGER)) {
            final Manager newManager = Manager.builder()
                    .email(user.getEmail())
                    .build();

            managerRepository.save(newManager);
        }

        userRoleRepository.save(UserRoleAdapter.createUserRoleObject(role, user));
    }

    @Transactional
    public void deleteRoleReq(Long id, RoleDTO roleDTO) {
        final RoleValue newRoleValue = roleDTO.getRoleValue();

        final User user = findUserById(id);
        final Role role = findRoleByRoleValue(newRoleValue);

        if (user.getRoles().size() == 1) {
            throw new UnsupportedRolesSizeException();
        }

        if (!user.getRoles().contains(role)) {
            throw new RoleDoesNotExistException(newRoleValue.getAuthority());
        }

        final String username = user.getEmail();
        // if the input role is MANAGER, delete from managers table; for each subordinate users
        // the manager will be set with the generic manager
        if (newRoleValue.equals(RoleValue.MANAGER)) {
            final Manager manager = findManagerByUsername(username);
            final List<User> users = findUsersByManager(manager, username);

            final Manager genericManager = getManagerByUsername(managerGenericUsername);

            users.forEach(employee -> employee.setManager(genericManager));

            managerRepository.delete(manager);
        }

        userRoleRepository.delete(UserRoleAdapter.createUserRoleObject(role, user));
    }

    public UserDTO getUserByIdReq(Long id) {
        User user = findUserById(id);

        return UserAdapter.userToUserDTO(user, apiPath);
    }

    public List<UserDTO> getUsersReq() {
        List<User> userList = userRepository.findAll();

        return UserAdapter.userListToUserDTOList(userList, apiPath);
    }

    @Transactional
    public UserDTO getUserByUsernameReq(String username) {
        User user = findUserByUsername(username);

        return UserAdapter.userToUserDTO(user, apiPath);
    }

    @Transactional
    public List<UserDTO> getSubordinateUsersReq(String username) {
        Manager manager = findManagerByUsername(username);

        List<User> users = findUsersByManager(manager, username);

        return UserAdapter.userListToUserDTOList(users, apiPath);
    }

    @Transactional
    public List<UserDTO> getUnassignedUsersReq(String username) {
        final User user = findUserByUsername(username);

        // Check if he is manager
        findManagerByUsername(user.getEmail());

        // Get generic manager from DB
        final Manager manager = getManagerByUsername(managerGenericUsername);

        // Get department by user who create this request
        final String department = user.getDepartment();

        // Get all users by manager and department
        final List<User> users = getAllByManagerAndDepartment(manager, department);

        // Delete the user who created the request from the users list
        users.remove(user);

        return UserAdapter.userListToUserDTOList(users, apiPath);
    }

    public void assignUserReq(AssignUserDTO assignUserDTO) {
        User userWhoWillAssign = findUserById(assignUserDTO.getIdManager());

        Manager manager = findManagerByUsername(userWhoWillAssign.getEmail());

        User userToBeAssigned = findUserById(assignUserDTO.getIdUser());

        userToBeAssigned.setManager(manager);

        userRepository.save(userToBeAssigned);
    }

    @Transactional
    public ManagerDTO getUserManagerByIdEmployeeReq(Long id) {
        User user = findUserById(id);

        Manager manager = findManagerById(user.getManager().getId());

        User managerAttributes = getUserByUsername(manager.getEmail());

        if (managerAttributes == null) {
            return ManagerDTO.builder()
                    .id(0L)
                    .firstName("Manager")
                    .email(managerGenericUsername)
                    .build();
        }

        return ManagerDTO.builder()
                .id(managerAttributes.getId())
                .firstName(managerAttributes.getFirstName())
                .email(managerAttributes.getEmail())
                .build();
    }

    @Transactional
    public void changePasswordReq(ChangeUserPasswordDTO changeUserPasswordDTO) {
        User user = findUserByUsername(changeUserPasswordDTO.getEmail());

        if (!bCryptPasswordEncoder.matches(changeUserPasswordDTO.getOldPassword(), user.getPassword())) {
            throw new PasswordException("Wrong old password");
        }

        if (!changeUserPasswordDTO.getNewPassword().equals(changeUserPasswordDTO.getConfirmNewPassword())) {
            throw new PasswordException("The new password and tha new confirm password are not the same");
        }

        user.setPassword(bCryptPasswordEncoder.encode(changeUserPasswordDTO.getNewPassword()));

        userRepository.save(user);
    }
}
