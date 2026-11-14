package com.williamhsieh.financeapp.controller.authorization;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.williamhsieh.financeapp.constant.auth.PermissionCodes;
import com.williamhsieh.financeapp.dto.authorization.RoleSummaryResponse;
import com.williamhsieh.financeapp.service.authorization.RoleManagementService;

@RestController
@RequestMapping("/api/v1/admin/roles")
public class RoleManagementController {

    private final RoleManagementService
        roleManagementService;

    public RoleManagementController(
        RoleManagementService roleManagementService
    ) {
        this.roleManagementService =
            roleManagementService;
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
                parseAuthenticatedUserId(jwt);

            return roleManagementService
                .getAssignableRoles(
                    authenticatedUserId
                );
        }

    private Long parseAuthenticatedUserId(
        Jwt jwt
    ) {
        if (
            jwt == null ||
            jwt.getSubject() == null ||
            jwt.getSubject().isBlank()
        ) {
            throw new ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                "Access Token 的使用者資訊無效"
            );
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
}
