
package com.prem.config;

import com.prem.service.MyUserDetailsService;
import com.prem.filter.JwtFilter;
import com.prem.handler.OAuth2LoginSuccessHandler;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final OAuth2LoginSuccessHandler successHandler;

    public SecurityConfig(
            JwtFilter jwtFilter,
            OAuth2LoginSuccessHandler successHandler) {

        this.jwtFilter = jwtFilter;
        this.successHandler = successHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            AuthenticationProvider authenticationProvider
    ) throws Exception {

        return http

                // Disable CSRF for this API-oriented setup
                .csrf(csrf -> csrf.disable())

                // Request authorization
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(
                                "/register",
                                "/login",
                                "/test",
                                "/error",
                                "/oauth2/**",
                                "/login/oauth2/**"
                        ).permitAll()

                        .anyRequest().authenticated()
                )

                // HTTP Basic authentication
                .httpBasic(Customizer.withDefaults())

                // Google OAuth2 login
                .oauth2Login(oauth -> oauth
                        .successHandler(successHandler)
                )

                // Local username/password authentication
                .authenticationProvider(authenticationProvider)

                // JWT filter
                .addFilterBefore(
                        jwtFilter,
                        UsernamePasswordAuthenticationFilter.class
                )

                .build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(
            MyUserDetailsService userDetailsService) {

        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider();

        provider.setPasswordEncoder(
                new BCryptPasswordEncoder(12)
        );

        provider.setUserDetailsService(userDetailsService);

        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {

        return config.getAuthenticationManager();
    }
}
