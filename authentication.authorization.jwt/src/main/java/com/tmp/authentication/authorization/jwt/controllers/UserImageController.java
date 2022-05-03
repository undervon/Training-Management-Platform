package com.tmp.authentication.authorization.jwt.controllers;

import com.tmp.authentication.authorization.jwt.services.UserImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "${api.path}")
public class UserImageController {

    private final UserImageService userImageService;

    @CrossOrigin
    @GetMapping(value = "/image/{id}", produces = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE})
    public ResponseEntity<Resource> getUserImageByIdReq(@PathVariable("id") Long id) {
        log.info("[{}] -> getUserImageByIdReq, id: {}", this.getClass().getSimpleName(), id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(userImageService.getUserImageByIdReq(id));
    }
}
