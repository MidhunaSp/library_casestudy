package com.springboot.library.service;

import com.springboot.library.dto.request.UserDto;
import com.springboot.library.enums.Role;
import com.springboot.library.exception.DuplicateResourceException;
import com.springboot.library.model.User;
import com.springboot.library.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void addUser(UserDto dto) {
        saveUser(dto, Role.USER);
    }

    public void addAdmin(UserDto dto) {
        saveUser(dto, Role.ADMIN);
    }

    private void saveUser(UserDto dto, Role role) {
        if (userRepository.findByUsername(dto.username()).isPresent())
            throw new DuplicateResourceException("Username already taken");

        User user = new User(
                dto.username(),
                passwordEncoder.encode(dto.password()),
                role
        );
        userRepository.save(user);
    }
}
