package com.williamhsieh.financeapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import com.williamhsieh.financeapp.security.RestAuthenticationEntryPoint;
import com.williamhsieh.financeapp.security.RestAccessDeniedHandler;
import com.williamhsieh.financeapp.security.JwtAuthorityConverter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(
        HttpSecurity http,
        RestAuthenticationEntryPoint authenticationEntryPoint,
        RestAccessDeniedHandler accessDeniedHandler,
        JwtAuthorityConverter jwtAuthorityConverter
    ) throws Exception {
        return http
            .csrf(csrf -> csrf.disable())

            .cors(Customizer.withDefaults())

            .sessionManagement(session ->
                session.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS
                )
            )

            .exceptionHandling(exception ->
                exception
                    .authenticationEntryPoint(
                        authenticationEntryPoint
                    )
                    .accessDeniedHandler(
                        accessDeniedHandler
                    )
            )

            .authorizeHttpRequests(authorize ->
                authorize
                    .requestMatchers(
                        "/error",
                        "/api/v1/registration/**",
                        "/api/v1/verifications/**",
                        "/api/v1/auth/login",
                        "/api/v1/auth/refresh",
                        "/api/v1/auth/logout"
                    )
                    .permitAll()
                    .anyRequest()
                    .authenticated()
            )

            .oauth2ResourceServer(oauth2 ->
                oauth2
                    .authenticationEntryPoint(
                        authenticationEntryPoint
                    )
                    .jwt(jwt ->
                        jwt.jwtAuthenticationConverter(
                            jwtAuthorityConverter
                        )
                    )
            )

            .build();
    }
}