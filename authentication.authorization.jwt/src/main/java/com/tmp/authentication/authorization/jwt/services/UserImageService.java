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
    private User findUserImageById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserImageNotFoundException(id.toString()));
    }

    /*
        Methods from UserImageController
     */
    public Resource getUserImageByIdReq(Long id) {
        User user = findUserImageById(id);

        byte[] image = user.getImage();
        if (null == image) {
            throw new UserImageNotFoundException(id.toString());
        }

        return new ByteArrayResource(image);
    }
}
