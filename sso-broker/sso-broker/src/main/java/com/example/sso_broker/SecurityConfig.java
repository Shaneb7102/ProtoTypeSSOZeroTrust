package com.example.sso_broker;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/user").authenticated()
                .anyRequest().permitAll()
            )
            .oauth2Login(); // Enable OAuth2 login for user authentication

        http
            .oauth2ResourceServer(oauth2 -> oauth2.jwt()); // Enforce JWT authentication for API access

        return http.build();
    }
}
