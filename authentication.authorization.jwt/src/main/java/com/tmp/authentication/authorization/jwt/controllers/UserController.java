package com.tmp.authentication.authorization.jwt.controllers;

import com.tmp.authentication.authorization.jwt.models.RoleDTO;
import com.tmp.authentication.authorization.jwt.models.UserDTO;
import com.tmp.authentication.authorization.jwt.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @PostMapping(value = "/editRole/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> editRole(@PathVariable("id") Long id, @RequestBody RoleDTO roleDTO) {
        log.info("[{}] -> editRole, id: {}, roleDTO: {}", this.getClass().getSimpleName(), id, roleDTO);

        userService.editRole(id, roleDTO);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping(value = "/deleteRole/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteRole(@PathVariable("id") Long id, @RequestBody RoleDTO roleDTO) {
        log.info("[{}] -> deleteRole, id: {}, roleDTO: {}", this.getClass().getSimpleName(), id, roleDTO);

        userService.deleteRole(id, roleDTO);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping(value = "/getUser/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> getUserById(@PathVariable("id") Long id) {
        log.info("[{}] -> getUserById, id: {}", this.getClass().getSimpleName(), id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.getUserById(id));
    }

    @GetMapping(value = "/getUsers", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserDTO>> getUsers() {
        log.info("[{}] -> getUsers", this.getClass().getSimpleName());

        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.getUsers());
    }
}
