
package com.example.password_security_demo;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class BCrypt2SaltDemo {

    public static void main(String[] args) {

        PasswordEncoder encoder = new BCryptPasswordEncoder();

        String password = "Secure@123";

        String firstHash = encoder.encode(password);
        String secondHash = encoder.encode(password);

        System.out.println("First hash: " + firstHash);
        System.out.println("Second hash: " + secondHash);

        System.out.println(
                "Hashes are equal: " + firstHash.equals(secondHash)
        );

        System.out.println(
                "First hash matches: " + encoder.matches(password, firstHash)
        );

        System.out.println(
                "Second hash matches: " + encoder.matches(password, secondHash)
        );
    }
}
