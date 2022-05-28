package com.github.manerajona.auth.dto;

import javax.validation.constraints.NotBlank;

public record AuthenticationRequest(
        @NotBlank(message = "The username must not be empty") String username,
        @NotBlank(message = "The password must not be empty") String password) {
}
