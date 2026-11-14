package com.williamhsieh.financeapp.identity.authorization.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.williamhsieh.financeapp.identity.authorization.constant.PermissionCodes;
import com.williamhsieh.financeapp.identity.authorization.dto.RoleSummaryResponse;
import com.williamhsieh.financeapp.identity.authorization.service.RoleManagementService;
import com.williamhsieh.financeapp.identity.authentication.security. AuthenticatedUserIdResolver;

@RestController
@RequestMapping("/api/v1/admin/roles")
public class RoleManagementController {

    private final RoleManagementService
        roleManagementService;
    private final AuthenticatedUserIdResolver
        authenticatedUserIdResolver;

    public RoleManagementController(
        RoleManagementService roleManagementService,
        AuthenticatedUserIdResolver
            authenticatedUserIdResolver
    ) {
        this.roleManagementService =
            roleManagementService;
        this.authenticatedUserIdResolver =
            authenticatedUserIdResolver;
    }

    @GetMapping
    @PreAuthorize(
        "hasAuthority('" +
            PermissionCodes.ROLE_READ +
        "')"
    )
    public List<RoleSummaryResponse>
        getAssignableRoles(
            @AuthenticationPrincipal
            Jwt jwt
        ) {
            Long authenticatedUserId =
                authenticatedUserIdResolver.resolve(jwt);

            return roleManagementService
                .getAssignableRoles(
                    authenticatedUserId
                );
        }
}
