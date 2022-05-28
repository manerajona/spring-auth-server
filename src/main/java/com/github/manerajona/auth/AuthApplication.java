package com.github.manerajona.auth;

import com.github.manerajona.auth.domain.model.User;
import com.github.manerajona.auth.domain.usecase.UserService;
import com.github.manerajona.auth.dto.AuthenticationRequest;
import com.github.manerajona.auth.dto.AuthenticationResponse;
import com.github.manerajona.auth.dto.MeResponse;
import com.github.manerajona.auth.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@SpringBootApplication
@RequiredArgsConstructor
public class AuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtils jwtUtils;

    @PostMapping("auth")
    ResponseEntity<?> login(@Valid @RequestBody AuthenticationRequest request) {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.username(), request.password()));

        if (authentication.isAuthenticated() && authentication.getPrincipal() instanceof UserDetails users) {

            final String token = jwtUtils.generateToken(users);

            return ResponseEntity.ok(AuthenticationResponse.builder()
                    .token(token)
                    .expirationDate(jwtUtils.extractExpirationDate(token))
                    .build());
        }
        throw new AccessDeniedException("Error during authentication");
    }

    @GetMapping("auth/me")
    ResponseEntity<?> me(@AuthenticationPrincipal User user) {

        return ResponseEntity.ok(MeResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(user.getRole().getAuthority())
                .build());
    }

    @DeleteMapping("users/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(@NotNull @PathVariable Long id) {
        userService.deleteById(id);
    }
}
