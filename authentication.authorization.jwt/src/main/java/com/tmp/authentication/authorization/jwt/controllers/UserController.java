package com.tmp.authentication.authorization.jwt.controllers;

import com.tmp.authentication.authorization.jwt.models.EditRoleDTO;
import com.tmp.authentication.authorization.jwt.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

    @PutMapping(value = "/editRole", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> editRole(@RequestBody EditRoleDTO editRoleDTO) {
        log.info("[{}] -> editRole, editRoleDTO: {}", this.getClass().getSimpleName(), editRoleDTO);

        userService.editRole(editRoleDTO);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
