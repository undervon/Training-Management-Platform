package com.tmp.authentication.authorization.jwt.controllers;

import com.tmp.authentication.authorization.jwt.models.EditUserDTO;
import com.tmp.authentication.authorization.jwt.models.RoleDTO;
import com.tmp.authentication.authorization.jwt.models.AddUserDTO;
import com.tmp.authentication.authorization.jwt.models.UserDTO;
import com.tmp.authentication.authorization.jwt.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpMethod;
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

    @Operation(summary = "Create new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "CREATED - if successful"),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST - if something wrong was done",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "NOT_FOUND - if the image is empty",
                    content = @Content),
            @ApiResponse(responseCode = "406", description = "NOT_ACCEPTABLE - "
                    + "if the user already exists in DB or the image has wrong content type",
                    content = @Content)
    })
    @CrossOrigin
    @PostMapping(value = "/addUser",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> addUserReq(
            @RequestPart("addUser") @Parameter(schema = @Schema(type = "string", format = "binary")) AddUserDTO addUserDTO,
            @RequestPart("image") MultipartFile image) {
        log.info("[ {} ] -> [ {} ] -> [ addUserReq ] addUserDTO: {}",
                this.getClass().getSimpleName(), HttpMethod.POST, addUserDTO);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.addUserReq(addUserDTO, image));
    }

    @Operation(summary = "Edit user by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "NO_CONTENT - if successful",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST - if something wrong was done",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "NOT_FOUND - if the user not found in DB",
                    content = @Content),
            @ApiResponse(responseCode = "406", description = "NOT_ACCEPTABLE - if the image has wrong content type",
                    content = @Content)
    })
    @CrossOrigin
    @PutMapping(value = "/editUser/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> editUserReq(
            @RequestPart("editUser") @Parameter(schema = @Schema(type = "string", format = "binary")) EditUserDTO editUserDTO,
            @RequestPart("image") MultipartFile image,
            @PathVariable("id") Long id) {
        log.info("[ {} ] -> [ {} ] -> [ editUserReq ] editUserDTO: {}, id: {}",
                this.getClass().getSimpleName(), HttpMethod.PUT, editUserDTO, id);

        userService.editUserReq(editUserDTO, image, id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Delete user by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "NO_CONTENT - if successful",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "NOT_FOUND - if the user not found in DB",
                    content = @Content),
            @ApiResponse(responseCode = "406", description = "NOT_ACCEPTABLE - if this user it's just ADMIN",
                    content = @Content)
    })
    @CrossOrigin
    @DeleteMapping(value = "/deleteUser/{id}")
    public ResponseEntity<?> deleteUserReq(@PathVariable("id") Long id) {
        log.info("[ {} ] -> [ {} ] -> [ deleteUserReq ] id: {}",
                this.getClass().getSimpleName(), HttpMethod.DELETE, id);

        userService.deleteUserReq(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Edit role by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "NO_CONTENT - if successful",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "NOT_FOUND - if the user not found in DB or "
                    + "the role does not exist",
                    content = @Content),
            @ApiResponse(responseCode = "406", description = "NOT_ACCEPTABLE - if this role already exists",
                    content = @Content)
    })
    @CrossOrigin
    @PutMapping(value = "/editRole/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> editRoleReq(@PathVariable("id") Long id,
            @RequestBody RoleDTO roleDTO) {
        log.info("[ {} ] -> [ {} ] -> [ editRoleReq ] id: {}, roleDTO: {}",
                this.getClass().getSimpleName(), HttpMethod.PUT, id, roleDTO);

        userService.editRoleReq(id, roleDTO);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Delete role by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "NO_CONTENT - if successful",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "NOT_FOUND - if [ the user not found in DB ] OR "
                    + "[ the role does not exist ] OR [ the manager not found in DB ]",
                    content = @Content),
            @ApiResponse(responseCode = "406", description = "NOT_ACCEPTABLE - if the roles size is unsupported",
                    content = @Content)
    })
    @CrossOrigin
    @PutMapping(value = "/deleteRole/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteRoleReq(@PathVariable("id") Long id,
            @RequestBody RoleDTO roleDTO) {
        log.info("[ {} ] -> [ {} ] -> [ deleteRoleReq ] id: {}, roleDTO: {}",
                this.getClass().getSimpleName(), HttpMethod.PUT, id, roleDTO);

        userService.deleteRoleReq(id, roleDTO);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Get user by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK - if successful"),
            @ApiResponse(responseCode = "404", description = "NOT_FOUND - if the user not found in DB",
                    content = @Content)
    })
    @CrossOrigin
    @GetMapping(value = "/getUser/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> getUserByIdReq(@PathVariable("id") Long id) {
        log.info("[ {} ] -> [ {} ] -> [ getUserByIdReq ] id: {}",
                this.getClass().getSimpleName(), HttpMethod.GET, id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.getUserByIdReq(id));
    }

    @Operation(summary = "Get all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK - if successful")
    })
    @CrossOrigin
    @GetMapping(value = "/getUsers", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserDTO>> getUsersReq() {
        log.info("[ {} ] -> [ {} ] -> [ getUsersReq ]",
                this.getClass().getSimpleName(), HttpMethod.GET);

        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.getUsersReq());
    }

    @Operation(summary = "Get user by username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK - if successful"),
            @ApiResponse(responseCode = "404", description = "NOT_FOUND - if the user not found in DB",
                    content = @Content)
    })
    @CrossOrigin
    @GetMapping(value = "/user/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> getUserByUsernameReq(@PathVariable("username") String username) {
        log.info("[ {} ] -> [ {} ] -> [ getUserByUsernameReq ] username: {}",
                this.getClass().getSimpleName(), HttpMethod.GET, username);

        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.getUserByUsernameReq(username));
    }

    @Operation(summary = "Get all subordinate users by username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK - if successful"),
            @ApiResponse(responseCode = "404", description = "NOT_FOUND - "
                    + "if the user not found in DB or the manager not found in DB",
                    content = @Content)
    })
    @CrossOrigin
    @GetMapping(value = "/subordinateUsers/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserDTO>> getSubordinateUsersReq(@PathVariable("username") String username) {
        log.info("[ {} ] -> [ {} ] -> [ getSubordinateUsersReq ] username: {}",
                this.getClass().getSimpleName(), HttpMethod.GET, username);

        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.getSubordinateUsersReq(username));
    }

    @Operation(summary = "Get all unassigned users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK - if successful"),
            @ApiResponse(responseCode = "404", description = "NOT_FOUND - if the user not found in DB",
                    content = @Content)
    })
    @CrossOrigin
    @GetMapping(value = "/unassignedUsers/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserDTO>> getUnassignedUsersReq(@PathVariable("username") String username) {
        log.info("[ {} ] -> [ {} ] -> [ getNotAssignedUsersReq ] username: {}",
                this.getClass().getSimpleName(), HttpMethod.GET, username);

        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.getUnassignedUsersReq(username));
    }
}
