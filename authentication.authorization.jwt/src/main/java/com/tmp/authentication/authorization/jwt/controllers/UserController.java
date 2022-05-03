package com.tmp.authentication.authorization.jwt.controllers;

import com.tmp.authentication.authorization.jwt.models.RoleDTO;
import com.tmp.authentication.authorization.jwt.models.AddUserDTO;
import com.tmp.authentication.authorization.jwt.models.UserDTO;
import com.tmp.authentication.authorization.jwt.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "${api.path}")
public class UserController {

    private final UserService userService;

    @CrossOrigin
    @PostMapping(value = "/addUser",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> addUser(@RequestPart("addUser") AddUserDTO addUserDTO,
            @RequestPart("image") MultipartFile image) {
        log.info("[{}] -> addUser, addUserDTO: {}", this.getClass().getSimpleName(), addUserDTO);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.addUser(addUserDTO, image));
    }

    @CrossOrigin
    @PutMapping(value = "/editUser/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> editUser(@RequestPart("addUser") AddUserDTO addUserDTO,
            @RequestPart("image") MultipartFile image, @PathVariable("id") Long id) {
        log.info("[{}] -> editUser, addUserDTO: {}, id: {}", this.getClass().getSimpleName(), addUserDTO, id);

        userService.editUser(addUserDTO, image, id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @CrossOrigin
    @DeleteMapping(value = "/deleteUser/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Long id) {
        log.info("[{}] -> deleteUser, id: {}", this.getClass().getSimpleName(), id);

        userService.deleteUser(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @CrossOrigin
    @PutMapping(value = "/editRole/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> editRole(@PathVariable("id") Long id, @RequestBody RoleDTO roleDTO) {
        log.info("[{}] -> editRole, id: {}, roleDTO: {}", this.getClass().getSimpleName(), id, roleDTO);

        userService.editRole(id, roleDTO);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @CrossOrigin
    @PutMapping(value = "/deleteRole/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteRole(@PathVariable("id") Long id, @RequestBody RoleDTO roleDTO) {
        log.info("[{}] -> deleteRole, id: {}, roleDTO: {}", this.getClass().getSimpleName(), id, roleDTO);

        userService.deleteRole(id, roleDTO);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @CrossOrigin
    @GetMapping(value = "/getUser/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> getUserById(@PathVariable("id") Long id) {
        log.info("[{}] -> getUserById, id: {}", this.getClass().getSimpleName(), id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.getUserById(id));
    }

    @CrossOrigin
    @GetMapping(value = "/getUsers", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserDTO>> getUsers() {
        log.info("[{}] -> getUsers", this.getClass().getSimpleName());

        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.getUsers());
    }

    @CrossOrigin
    @GetMapping(value = "/user/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> getUserByUsernameReq(@PathVariable("username") String username) {
        log.info("[{}] -> getUserByUsernameReq, username: {}", this.getClass().getSimpleName(), username);

        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.getUserByUsernameReq(username));
    }
}
