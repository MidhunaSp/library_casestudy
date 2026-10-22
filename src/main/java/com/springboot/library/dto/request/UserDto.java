package com.springboot.library.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserDto(
        @NotBlank(message = "Username is required")
        @Size(min = 3, max = 15, message = "Username should be 3 to 15 characters")
        String username,

        @NotBlank(message = "Password is required")
        String password
) {
}
