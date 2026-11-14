package com.williamhsieh.financeapp.identity.authentication.security;

import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class AuthenticatedUserIdResolver {

    public Long resolve(
        Jwt jwt
    ) {
        if (
            jwt == null ||
            jwt.getSubject() == null ||
            jwt.getSubject().isBlank()
        ) {
            throw invalidAuthenticatedUser();
        }

        try {
            return Long.valueOf(
                jwt.getSubject()
            );
        } catch (NumberFormatException exception) {
            throw new ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                "Access Token 的使用者資訊無效",
                exception
            );
        }
    }

    private ResponseStatusException
        invalidAuthenticatedUser() {

        return new ResponseStatusException(
            HttpStatus.UNAUTHORIZED,
            "Access Token 的使用者資訊無效"
        );
    }
}
