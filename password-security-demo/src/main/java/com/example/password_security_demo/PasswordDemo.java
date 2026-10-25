
package com.example.password_security_demo;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordDemo {

    public static void main(String[] args) {

        PasswordEncoder encoder = new BCryptPasswordEncoder();

        String rawPassword = "Secure@123";

        String hashedPassword = encoder.encode(rawPassword);

        System.out.println("Original password: " + rawPassword);
        System.out.println("BCrypt hash: " + hashedPassword);

        boolean result = encoder.matches(rawPassword, hashedPassword);

        System.out.println("Password matches: " + result);
    }
}
