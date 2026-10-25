package com.example.password_security_demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class PasswordSecurityDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(PasswordSecurityDemoApplication.class, args);
	}

	public PasswordSecurityDemoApplication(PasswordEncoder passwordEncoder) {
		String password = "Secure@123";

		String hash = passwordEncoder.encode(password);

		System.out.println("BCrypt hash: " + hash);
		System.out.println(
				"Password matches: " + passwordEncoder.matches(password, hash)
		);
	}
}