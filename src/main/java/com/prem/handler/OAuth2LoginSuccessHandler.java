package com.prem.handler;

import com.prem.model.Users;
import com.prem.service.JWTService;
import com.prem.service.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuth2LoginSuccessHandler
        implements AuthenticationSuccessHandler {

    private final UserService userService;
    private final JWTService jwtService;

    public OAuth2LoginSuccessHandler(
            UserService userService,
            JWTService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication)
            throws IOException, ServletException {

        OAuth2AuthenticationToken token =
                (OAuth2AuthenticationToken) authentication;

        Users user =
                userService.processGoogleUser(token.getPrincipal());

        String jwt = jwtService.generateToken(user.getUsername());

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        response.getWriter().write(
                "{\"message\":\"Google login successful\",\"token\":\""
                        + jwt + "\"}"
        );
    }
}