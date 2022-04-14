package com.tmp.authentication.authorization.jwt.models.adapters;

import com.tmp.authentication.authorization.jwt.entities.User;
import com.tmp.authentication.authorization.jwt.models.UserDTO;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public final class UserAdapter {

    public static UserDTO userToUserDTO(User user) {
        return UserDTO.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .password(user.getPassword())
                .department(user.getDepartment())
                .employeeNumber(user.getEmployeeNumber())
                .build();
    }

    public static List<UserDTO> userListToUserDTOList(List<User> userList) {
        return userList.stream()
                .map(UserAdapter::userToUserDTO)
                .collect(Collectors.toList());
    }
}
