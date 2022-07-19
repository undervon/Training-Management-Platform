package com.tmp.authentication.authorization.jwt.services;

import com.tmp.authentication.authorization.jwt.entities.Manager;
import com.tmp.authentication.authorization.jwt.entities.Role;
import com.tmp.authentication.authorization.jwt.entities.User;
import com.tmp.authentication.authorization.jwt.entities.UserRole;
import com.tmp.authentication.authorization.jwt.entities.UserRoleId;
import com.tmp.authentication.authorization.jwt.exceptions.PasswordException;
import com.tmp.authentication.authorization.jwt.models.AddUserDTO;
import com.tmp.authentication.authorization.jwt.models.AssignUserDTO;
import com.tmp.authentication.authorization.jwt.models.ChangeUserPasswordDTO;
import com.tmp.authentication.authorization.jwt.models.EditUserDTO;
import com.tmp.authentication.authorization.jwt.models.ManagerDTO;
import com.tmp.authentication.authorization.jwt.models.RoleDTO;
import com.tmp.authentication.authorization.jwt.models.adapters.UserRoleAdapter;
import com.tmp.authentication.authorization.jwt.models.enums.RoleValue;
import com.tmp.authentication.authorization.jwt.repositories.ManagerRepository;
import com.tmp.authentication.authorization.jwt.repositories.RoleRepository;
import com.tmp.authentication.authorization.jwt.repositories.UserRepository;
import com.tmp.authentication.authorization.jwt.repositories.UserRoleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@ExtendWith(SpringExtension.class)
@ActiveProfiles(profiles = "local-test")
@RunWith(MockitoJUnitRunner.Silent.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserRoleRepository userRoleRepository;

    @Mock
    private ManagerRepository managerRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    private UserService userService;

    private User user;
    private Role role;
    private RoleDTO roleDTO;
    private UserRole userRole;
    private ChangeUserPasswordDTO changeUserPasswordDTO;
    private Manager manager;
    private ManagerDTO managerDTO;
    private AssignUserDTO assignUserDTO;
    private EditUserDTO editUserDTO;
    private AddUserDTO addUserDTO;

    @BeforeEach
    void setUp() {
        manager = Manager.builder()
                .id(1L)
                .email("A")
                .build();

        user = User.builder()
                .id(1L)
                .firstName("A")
                .lastName("A")
                .email("A")
                .password(bCryptPasswordEncoder.encode("A"))
                .department("A")
                .employeeNumber("A")
                .joinDate(LocalDateTime.now())
                .image(new byte[1])
                .manager(manager)
                .build();

        role = Role.builder()
                .id(1L)
                .roleValue(RoleValue.MANAGER)
                .build();

        roleDTO = RoleDTO.builder()
                .roleValue(RoleValue.MANAGER)
                .build();

        userRole = UserRole.builder()
                .id(UserRoleId.builder()
                        .idRole(1L)
                        .idUser(1L)
                        .build())
                .idRole(role)
                .idUser(user)
                .build();

        changeUserPasswordDTO = ChangeUserPasswordDTO.builder()
                .email("A")
                .confirmNewPassword("A")
                .newPassword("A")
                .oldPassword(bCryptPasswordEncoder.encode("A"))
                .build();

        managerDTO = ManagerDTO.builder()
                .firstName("A")
                .email("A")
                .id(1L)
                .build();

        assignUserDTO = AssignUserDTO.builder()
                .idUser(1L)
                .idManager(1L)
                .build();

        editUserDTO = EditUserDTO.builder()
                .firstName("A")
                .lastName("A")
                .department("ADAS")
                .email("A")
                .employeeNumber("A")
                .build();

        addUserDTO = AddUserDTO.builder()
                .firstName("A")
                .lastName("A")
                .department("ADAS")
                .email("A")
                .employeeNumber("A")
                .build();
    }

    @Test
    void getUserRolesByIdReq() {
        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        Mockito.when(userRoleRepository.findByIdUser(user)).thenReturn(List.of(userRole));

        Assertions.assertEquals(List.of(roleDTO), userService.getUserRolesByIdReq(user.getId()));
    }

    @Test
    void changePasswordReqException() {
        Mockito.when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        user.setPassword(bCryptPasswordEncoder.encode("A"));
        changeUserPasswordDTO.setOldPassword(bCryptPasswordEncoder.encode("B"));

        PasswordException passwordException = Assertions.assertThrows(PasswordException.class,
                () -> userService.changePasswordReq(changeUserPasswordDTO));

        Assertions.assertEquals("Wrong old password", passwordException.getMessage());
    }

    @Test
    void getUserManagerByIdEmployeeReq() {
        user.setPassword("A");
        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        Mockito.when(managerRepository.findManagerById(user.getManager().getId())).thenReturn(Optional.of(manager));
        Mockito.when(userRepository.getByEmail(user.getEmail())).thenReturn(user);

        Assertions.assertEquals(managerDTO, userService.getUserManagerByIdEmployeeReq(user.getId()));
    }

    @Test
    void assignUserReq() {
        user.setPassword("A");
        Mockito.when(userRepository.findById(assignUserDTO.getIdManager())).thenReturn(Optional.of(user));
        Mockito.when(managerRepository.findManagerByEmail(user.getManager().getEmail()))
                .thenReturn(Optional.of(manager));
        Mockito.when(userRepository.findById(assignUserDTO.getIdUser())).thenReturn(Optional.of(user));

        userRepository.save(user);

        userService.assignUserReq(assignUserDTO);
    }

    @Test
    void getUnassignedUsersReq() {
        user.setPassword("A");
        Mockito.when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        Mockito.when(managerRepository.findManagerByEmail(user.getManager().getEmail()))
                .thenReturn(Optional.of(manager));
        Mockito.when(managerRepository.getManagerByEmail(user.getEmail())).thenReturn(manager);
        Mockito.when(userRepository.getAllByManagerAndDepartment(manager, user.getDepartment()))
                .thenReturn(List.of(user));

        Assertions.assertEquals(List.of(), userService.getUnassignedUsersReq(user.getEmail()));
    }

    @Test
    void getSubordinateUsersReq() {
        Mockito.when(managerRepository.findManagerByEmail(user.getManager().getEmail()))
                .thenReturn(Optional.of(manager));
        Mockito.when(userRepository.findAllByManager(manager)).thenReturn(Optional.of(List.of(user)));

        Assertions.assertEquals(List.of(user), userService.getSubordinateUsersReq(user.getEmail()));
    }

    @Test
    void getUserByUsernameReq() {
        Mockito.when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        Assertions.assertEquals(user, userService.getUserByUsernameReq(user.getEmail()));
    }

    @Test
    void getUsersReq() {
        Mockito.when(userRepository.findAll()).thenReturn(List.of(user));

        Assertions.assertEquals(List.of(user), userService.getUsersReq());
    }

    @Test
    void getUserByIdReq() {
        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        Assertions.assertEquals(user, userService.getUserByIdReq(user.getId()));
    }

    @Test
    void deleteRoleReq() {
        user.setPassword("A");
        user.setRoles(Set.of(role));
        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        Mockito.when(roleRepository.findByRoleValue(role.getRoleValue())).thenReturn(Optional.of(role));

        userRoleRepository.delete(UserRoleAdapter.createUserRoleObject(role, user));

        userService.deleteRoleReq(user.getId(), roleDTO);
    }

    @Test
    void editRoleReq() {
        user.setPassword("A");
        user.setRoles(Set.of(role));
        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        Mockito.when(roleRepository.findByRoleValue(role.getRoleValue())).thenReturn(Optional.of(role));

        userRoleRepository.save(UserRoleAdapter.createUserRoleObject(role, user));

        userService.editRoleReq(user.getId(), roleDTO);
    }

    @Test
    void deleteUserReq() {
        user.setPassword("A");
        user.setRoles(Set.of(role));
        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        userRepository.delete(user);

        userService.deleteUserReq(user.getId());
    }

    @Test
    void editUserReq() {
        user.setPassword("A");
        user.setRoles(Set.of(role));
        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        userRepository.save(user);

        MultipartFile multipartFile = new MockMultipartFile("A", new byte[1]);

        userService.editUserReq(editUserDTO, multipartFile, user.getId());
    }

    @Test
    void addUserReq() {
        Mockito.when(managerRepository.getManagerByEmail(user.getEmail())).thenReturn(manager);

        userRepository.save(user);

        Mockito.when(roleRepository.getByRoleValue(role.getRoleValue())).thenReturn(role);

        userRoleRepository.save(UserRoleAdapter.createUserRoleObject(role, user));

        MultipartFile multipartFile = new MockMultipartFile("A", new byte[1]);

        Assertions.assertEquals(user, userService.addUserReq(addUserDTO, multipartFile));
    }
}
