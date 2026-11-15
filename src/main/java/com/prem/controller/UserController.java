
package com.prem.controller;

import com.prem.model.Users;
import com.prem.service.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    // User Registration
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Users user) {

        Users savedUser = service.register(user);

        Map<String, Object> response = new LinkedHashMap<>();

        response.put("message", "Registration successful");
        response.put("username", savedUser.getUsername());
        response.put("provider", savedUser.getProvider());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }
    @GetMapping("/dashboard")
    public String dashboard() {
        return "Welcome! JWT Authentication Successful.";
    }

    // User Login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Users user) {

        try {

            String token = service.verify(user);

            if ("fail".equals(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("message", "Invalid credentials"));
            }

            return ResponseEntity.ok(
                    Map.of(
                            "message", "Login successful",
                            "token", token
                    )
            );

        } catch (AuthenticationException e) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid username or password"));
        }
    }
}
