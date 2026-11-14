package com.williamhsieh.financeapp.identity.authentication.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.williamhsieh.financeapp.identity.authentication.dto.CurrentUserResponse;
import com.williamhsieh.financeapp.identity.authentication.service.CurrentUserService;
import com.williamhsieh.financeapp.identity.authentication.security.AuthenticatedUserIdResolver;

@RestController
@RequestMapping("/api/v1/auth")
public class CurrentUserController {

    private final CurrentUserService currentUserService;
    private final AuthenticatedUserIdResolver
        authenticatedUserIdResolver;

    public CurrentUserController(
        CurrentUserService currentUserService,
        AuthenticatedUserIdResolver
            authenticatedUserIdResolver
    ) {
        this.currentUserService =
            currentUserService;
        this.authenticatedUserIdResolver =
            authenticatedUserIdResolver;
    }

    @GetMapping("/me")
    public CurrentUserResponse getCurrentUser(
        @AuthenticationPrincipal Jwt jwt
    ) {
        Long authenticatedUserId =
            authenticatedUserIdResolver.resolve(jwt);

        List<String> roles =
            getStringListClaim(
                jwt,
                "roles"
            );

        List<String> permissions =
            getStringListClaim(
                jwt,
                "permissions"
            );

        return currentUserService.getCurrentUser(
            authenticatedUserId,
            roles,
            permissions
        );
    }

    private List<String> getStringListClaim(
        Jwt jwt,
        String claimName
    ) {
        List<String> values =
            jwt.getClaimAsStringList(
                claimName
            );

        if (values == null) {
            return List.of();
        }

        return List.copyOf(values);
    }
}