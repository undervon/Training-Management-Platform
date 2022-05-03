package com.tmp.authentication.authorization.jwt.services;

import com.tmp.authentication.authorization.jwt.entities.User;
import com.tmp.authentication.authorization.jwt.exceptions.UserImageNotFoundException;
import com.tmp.authentication.authorization.jwt.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserImageService {

    private final UserRepository userRepository;

    /*
        UserImageService methods
     */
    public User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserImageNotFoundException(id.toString()));
    }

    /*
        Methods from UserImageController
     */
    public Resource getUserImageByIdReq(Long id) {
        log.info("[{}] -> getUserImageByIdReq, id: {}", this.getClass().getSimpleName(), id);

        User user = findUserById(id);

        byte[] image = user.getImage();
        if (null == image) {
            throw new UserImageNotFoundException(id.toString());
        }

        return new ByteArrayResource(image);
    }
}
