package com.example.sso_broker;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.config.Customizer;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, ClientRegistrationRepository clientRegistrationRepository) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/user").authenticated()
                .anyRequest().permitAll()
            )
            .oauth2Login(login -> login.defaultSuccessUrl("/api/user", true))

            // ✅ Modern way to configure JWT authentication (fixes deprecation)
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));

        // Proper OIDC logout handling
        http.logout(logout -> logout
            .logoutSuccessHandler(oidcLogoutHandler(clientRegistrationRepository))
        );

        return http.build();
    }

    @Bean
    public LogoutSuccessHandler oidcLogoutHandler(ClientRegistrationRepository clientRegistrationRepository) {
        OidcClientInitiatedLogoutSuccessHandler logoutHandler =
            new OidcClientInitiatedLogoutSuccessHandler(clientRegistrationRepository);
        logoutHandler.setPostLogoutRedirectUri("http://localhost:8080/login?logout");
        return logoutHandler;
    }
}
