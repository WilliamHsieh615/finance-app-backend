package com.williamhsieh.financeapp.identity.authorization.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.williamhsieh.financeapp.identity.authentication.security.AuthenticatedUserIdResolver;
import com.williamhsieh.financeapp.identity.authorization.constant.PermissionCodes;
import com.williamhsieh.financeapp.identity.authorization.dto.GrantRolePermissionRequest;
import com.williamhsieh.financeapp.identity.authorization.dto.PermissionSummaryResponse;
import com.williamhsieh.financeapp.identity.authorization.dto.RevokeRolePermissionRequest;
import com.williamhsieh.financeapp.identity.authorization.dto.RolePermissionAssignmentLogResponse;
import com.williamhsieh.financeapp.identity.authorization.service.RolePermissionAssignmentHistoryService;
import com.williamhsieh.financeapp.identity.authorization.service.RolePermissionManagementService;
import com.williamhsieh.financeapp.shared.dto.PageResponse;

@RestController
@RequestMapping(
    "/api/v1/admin/roles/{roleCode}/permissions"
)
public class RolePermissionManagementController {

    private final RolePermissionManagementService rolePermissionManagementService;
    private final AuthenticatedUserIdResolver authenticatedUserIdResolver;
    private final RolePermissionAssignmentHistoryService assignmentHistoryService;

    public RolePermissionManagementController(
        RolePermissionManagementService rolePermissionManagementService,
        AuthenticatedUserIdResolver authenticatedUserIdResolver,
        RolePermissionAssignmentHistoryService assignmentHistoryService
    ) {
        this.rolePermissionManagementService = rolePermissionManagementService;
        this.authenticatedUserIdResolver = authenticatedUserIdResolver;
        this.assignmentHistoryService = assignmentHistoryService;
    }

    @GetMapping
    @PreAuthorize(
        "hasAuthority('" +
            PermissionCodes.PERMISSION_READ +
        "')"
    )
    public List<PermissionSummaryResponse>
        getRolePermissions(
            @PathVariable
            String roleCode,

            @AuthenticationPrincipal
            Jwt jwt
        ) {

        Long authenticatedUserId =
            authenticatedUserIdResolver
                .resolve(jwt);

        return rolePermissionManagementService
            .getRolePermissions(
                authenticatedUserId,
                roleCode
            );
    }

    @GetMapping("/history")
    @PreAuthorize(
        "hasAuthority('" +
            PermissionCodes.PERMISSION_READ +
        "')"
    )
    public PageResponse<
        RolePermissionAssignmentLogResponse
    > getRolePermissionAssignmentHistory(
        @PathVariable
        String roleCode,

        @RequestParam(defaultValue = "0")
        int page,

        @RequestParam(defaultValue = "20")
        int size,

        @AuthenticationPrincipal
        Jwt jwt
    ) {
        Long authenticatedUserId =
            authenticatedUserIdResolver
                .resolve(jwt);

        return assignmentHistoryService
            .getRolePermissionAssignmentHistory(
                authenticatedUserId,
                roleCode,
                page,
                size
            );
    }

    @PostMapping
    @PreAuthorize(
        "hasAuthority('" +
            PermissionCodes.ROLE_PERMISSION_MANAGE +
        "')"
    )
    public PermissionSummaryResponse grantPermission(
        @PathVariable
        String roleCode,

        @Valid
        @RequestBody
        GrantRolePermissionRequest request,

        @AuthenticationPrincipal
        Jwt jwt
    ) {
        Long authenticatedUserId =
            authenticatedUserIdResolver
                .resolve(jwt);

        return rolePermissionManagementService
            .grantPermission(
                authenticatedUserId,
                roleCode,
                request.permissionCode(),
                request.note()
            );
    }

    @DeleteMapping("/{permissionCode}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize(
        "hasAuthority('" +
            PermissionCodes.ROLE_PERMISSION_MANAGE +
        "')"
    )
    public void revokePermission(
        @PathVariable
        String roleCode,

        @PathVariable
        String permissionCode,

        @Valid
        @RequestBody
        RevokeRolePermissionRequest request,

        @AuthenticationPrincipal
        Jwt jwt
    ) {
        Long authenticatedUserId =
            authenticatedUserIdResolver
                .resolve(jwt);

        rolePermissionManagementService
            .revokePermission(
                authenticatedUserId,
                roleCode,
                permissionCode,
                request.note()
            );
    }
}
