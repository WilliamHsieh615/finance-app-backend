package com.williamhsieh.financeapp.identity.authentication.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.server.ResponseStatusException;

class AuthenticatedUserIdResolverTests {

    private AuthenticatedUserIdResolver resolver;

    @BeforeEach
    void setUp() {
        resolver =
            new AuthenticatedUserIdResolver();
    }

    @Test
    void validNumericSubjectReturnsUserId() {

        Jwt jwt = mock(Jwt.class);

        when(jwt.getSubject())
            .thenReturn("100");

        Long result = resolver.resolve(jwt);

        assertThat(result)
            .isEqualTo(100L);
    }

    @Test
    void nullJwtReturnsUnauthorized() {

        assertThatThrownBy(() ->
            resolver.resolve(null)
        )
            .isInstanceOf(
                ResponseStatusException.class
            )
            .satisfies(exception -> {
                ResponseStatusException responseException =
                    (ResponseStatusException) exception;

                assertThat(
                    responseException
                        .getStatusCode()
                        .value()
                ).isEqualTo(401);

                assertThat(
                    responseException.getReason()
                ).isEqualTo(
                    "Access Token 的使用者資訊無效"
                );
            });
    }

    @Test
    void blankSubjectReturnsUnauthorized() {

        Jwt jwt = mock(Jwt.class);

        when(jwt.getSubject())
            .thenReturn(" ");

        assertThatThrownBy(() ->
            resolver.resolve(jwt)
        )
            .isInstanceOf(
                ResponseStatusException.class
            )
            .satisfies(exception -> {
                ResponseStatusException responseException =
                    (ResponseStatusException) exception;

                assertThat(
                    responseException
                        .getStatusCode()
                        .value()
                ).isEqualTo(401);

                assertThat(
                    responseException.getReason()
                ).isEqualTo(
                    "Access Token 的使用者資訊無效"
                );
            });
    }

    @Test
    void nonNumericSubjectReturnsUnauthorized() {

        Jwt jwt = mock(Jwt.class);

        when(jwt.getSubject())
            .thenReturn("invalid-user-id");

        assertThatThrownBy(() ->
            resolver.resolve(jwt)
        )
            .isInstanceOf(
                ResponseStatusException.class
            )
            .satisfies(exception -> {
                ResponseStatusException responseException =
                    (ResponseStatusException) exception;

                assertThat(
                    responseException
                        .getStatusCode()
                        .value()
                ).isEqualTo(401);

                assertThat(
                    responseException.getReason()
                ).isEqualTo(
                    "Access Token 的使用者資訊無效"
                );
            });
    }
}
