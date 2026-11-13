
package com.prem.service;

import com.prem.model.Users;
import com.prem.repo.UserRepo;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

@Service
public class UserService {

    private final JWTService jwtService;
    private final AuthenticationManager authManager;
    private final UserRepo repo;

    private final BCryptPasswordEncoder encoder =
            new BCryptPasswordEncoder(12);

    public UserService(
            JWTService jwtService,
            AuthenticationManager authManager,
            UserRepo repo) {
        this.jwtService = jwtService;
        this.authManager = authManager;
        this.repo = repo;
    }

    public Users register(Users user) {
        user.setPassword(encoder.encode(user.getPassword()));
        return repo.save(user);
    }

    public String verify(Users user) {

        Authentication authentication =
                authManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                user.getUsername(),
                                user.getPassword()
                        )
                );

        if (authentication.isAuthenticated()) {
            return jwtService.generateToken();
        }

        return "fail";
    }
}
