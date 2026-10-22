package com.springboot.library.controller;

import com.springboot.library.dto.request.UserDto;
import com.springboot.library.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/api/user/add")
    public void addUser(@Valid @RequestBody UserDto dto) {
        userService.addUser(dto);
    }

    // register an admin (role ADMIN)
    @PostMapping("/api/admin/add")
    public void addAdmin(@Valid @RequestBody UserDto dto) {
        userService.addAdmin(dto);
    }
}
