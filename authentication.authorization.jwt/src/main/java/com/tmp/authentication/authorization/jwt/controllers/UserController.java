package com.tmp.authentication.authorization.jwt.controllers;

import com.tmp.authentication.authorization.jwt.models.EditRoleDTO;
import com.tmp.authentication.authorization.jwt.models.UserDTO;
import com.tmp.authentication.authorization.jwt.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "${api.path}")
public class UserController {

    private final UserService userService;

    @PostMapping(value = "/addUser", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> addUser(@RequestBody UserDTO userDTO) {
        log.info("[{}] -> addUser, userDTO: {}", this.getClass().getSimpleName(), userDTO);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.addUser(userDTO));
    }

    @DeleteMapping(value = "/deleteUser/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        log.info("[{}] -> deleteUser, id: {}", this.getClass().getSimpleName(), id);

        userService.deleteUser(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping(value = "/editRole", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> editRole(@RequestBody EditRoleDTO editRoleDTO) {
        log.info("[{}] -> editRole, editRoleDTO: {}", this.getClass().getSimpleName(), editRoleDTO);

        userService.editRole(editRoleDTO);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping(value = "/deleteRole", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteRole(@RequestBody EditRoleDTO editRoleDTO) {
        log.info("[{}] -> deleteRole, editRoleDTO: {}", this.getClass().getSimpleName(), editRoleDTO);

        userService.deleteRole(editRoleDTO);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
