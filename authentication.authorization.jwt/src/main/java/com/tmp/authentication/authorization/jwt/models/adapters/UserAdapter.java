package com.tmp.authentication.authorization.jwt.models.adapters;

import com.tmp.authentication.authorization.jwt.entities.User;
import com.tmp.authentication.authorization.jwt.models.UserDTO;
import lombok.experimental.UtilityClass;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public final class UserAdapter {

    public static UserDTO userToUserDTO(User user, String apiPath) {
        return UserDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .department(user.getDepartment())
                .employeeNumber(user.getEmployeeNumber())
                .joinDate(user.getJoinDate())
                .imageURL(generateImageURL(user.getId(), apiPath))
                .build();
    }

    public static List<UserDTO> userListToUserDTOList(List<User> userList, String apiPath) {
        return userList.stream()
                .map(user -> userToUserDTO(user, apiPath))
                .collect(Collectors.toList());
    }

    private static String generateImageURL(Long id, String apiPath) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(apiPath + "/image/")
                .path(id.toString())
                .toUriString();
    }
}
