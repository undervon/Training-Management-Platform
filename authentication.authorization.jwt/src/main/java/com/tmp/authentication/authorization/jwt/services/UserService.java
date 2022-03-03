package com.tmp.authentication.authorization.jwt.services;

import com.tmp.authentication.authorization.jwt.entities.User;
import com.tmp.authentication.authorization.jwt.exceptions.BadCredentialsException;
import com.tmp.authentication.authorization.jwt.exceptions.UserNotFoundException;
import com.tmp.authentication.authorization.jwt.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    User findByUsername(String username) {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UserNotFoundException(username));
    }

    public void checkPassword(String dbPassword, String inPassword) {
        if (!dbPassword.equals(inPassword)) {
            throw new BadCredentialsException("password");
        }
    }
}
