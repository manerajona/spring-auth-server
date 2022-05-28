package com.github.manerajona.auth.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class MeResponse {
    Long id;
    String firstName;
    String lastName;
    String email;
    String role;
}
