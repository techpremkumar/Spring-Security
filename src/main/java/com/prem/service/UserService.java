
package com.prem.service;

import com.prem.model.Users;
import com.prem.repo.UserRepo;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Service
public class UserService {

    private final JWTService jwtService;
    private final ObjectProvider<AuthenticationManager> authManagerProvider;
    private final UserRepo repo;

    private final BCryptPasswordEncoder encoder =
            new BCryptPasswordEncoder(12);

    public UserService(
            JWTService jwtService,
            ObjectProvider<AuthenticationManager> authManagerProvider,
            UserRepo repo) {

        this.jwtService = jwtService;
        this.authManagerProvider = authManagerProvider;
        this.repo = repo;
    }

    // Local user registration
    public Users register(Users user) {

        user.setPassword(encoder.encode(user.getPassword()));
        user.setProvider("LOCAL");

        return repo.save(user);
    }

    // Local username/password authentication
    public String verify(Users user) {

        AuthenticationManager authManager =
                authManagerProvider.getObject();

        Authentication authentication =
                authManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                user.getUsername(),
                                user.getPassword()
                        )
                );

        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(user.getUsername());
        }

        return "fail";
    }

    // Google OAuth2 registration and login
    public Users processGoogleUser(OAuth2User oauthUser) {

        String email = oauthUser.getAttribute("email");
        String name = oauthUser.getAttribute("name");
        String picture = oauthUser.getAttribute("picture");

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException(
                    "Google account email is missing"
            );
        }

        Users user = repo.findByEmail(email);

        if (user == null) {

            user = new Users();

            user.setEmail(email);
            user.setUsername(name);
            user.setProfilePicture(picture);
            user.setProvider("GOOGLE");

            // Google users do not need a local password.
        } else {

            user.setUsername(name);
            user.setProfilePicture(picture);
            user.setProvider("GOOGLE");
        }

        return repo.save(user);
    }
}
