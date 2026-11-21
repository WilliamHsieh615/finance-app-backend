package com.williamhsieh.financeapp.identity.authorization.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.williamhsieh.financeapp.identity.authentication.security.AuthenticatedUserIdResolver;
import com.williamhsieh.financeapp.identity.authorization.constant.PermissionCodes;
import com.williamhsieh.financeapp.identity.authorization.dto.PermissionTypeSummaryResponse;
import com.williamhsieh.financeapp.identity.authorization.service.PermissionTypeQueryService;

@RestController
@RequestMapping("/api/v1/admin/permission-types")
public class PermissionTypeQueryController {

    private final PermissionTypeQueryService
        permissionTypeQueryService;

    private final AuthenticatedUserIdResolver
        authenticatedUserIdResolver;

    public PermissionTypeQueryController(
        PermissionTypeQueryService
            permissionTypeQueryService,
        AuthenticatedUserIdResolver
            authenticatedUserIdResolver
    ) {
        this.permissionTypeQueryService =
            permissionTypeQueryService;

        this.authenticatedUserIdResolver =
            authenticatedUserIdResolver;
    }

    @GetMapping
    @PreAuthorize(
        "hasAuthority('" +
            PermissionCodes.PERMISSION_READ +
        "')"
    )
    public List<PermissionTypeSummaryResponse>
        getActivePermissionTypes(
            @AuthenticationPrincipal
            Jwt jwt
        ) {

        Long authenticatedUserId =
            authenticatedUserIdResolver
                .resolve(jwt);

        return permissionTypeQueryService
            .getActivePermissionTypes(
                authenticatedUserId
            );
    }
}
