package com.github.manerajona.auth.domain.usecase;

import com.github.manerajona.auth.domain.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    User getByEmail(String email);
    void deleteById(Long id);
}
