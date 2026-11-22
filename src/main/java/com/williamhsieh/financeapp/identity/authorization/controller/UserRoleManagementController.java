package com.williamhsieh.financeapp.identity.authorization.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RequestParam;

import com.williamhsieh.financeapp.shared.dto.PageResponse;
import com.williamhsieh.financeapp.identity.authorization.constant.PermissionCodes;
import com.williamhsieh.financeapp.identity.authorization.dto.AssignUserRoleRequest;
import com.williamhsieh.financeapp.identity.authorization.dto.RevokeUserRoleRequest;
import com.williamhsieh.financeapp.identity.authorization.dto.RoleSummaryResponse;
import com.williamhsieh.financeapp.identity.authorization.dto.UserRoleAssignmentLogResponse;
import com.williamhsieh.financeapp.identity.authorization.service.UserRoleAssignmentHistoryService;
import com.williamhsieh.financeapp.identity.authorization.service.UserRoleManagementService;
import com.williamhsieh.financeapp.identity.authentication.security.AuthenticatedUserIdResolver;

@RestController
@RequestMapping(
    "/api/v1/admin/users/{userId}/roles"
)
public class UserRoleManagementController {

    private final UserRoleManagementService userRoleManagementService;
    private final AuthenticatedUserIdResolver authenticatedUserIdResolver;
    private final UserRoleAssignmentHistoryService assignmentHistoryService;

    public UserRoleManagementController(
        UserRoleManagementService userRoleManagementService,
        AuthenticatedUserIdResolver authenticatedUserIdResolver,
        UserRoleAssignmentHistoryService assignmentHistoryService
    ) {
        this.userRoleManagementService = userRoleManagementService;
        this.authenticatedUserIdResolver = authenticatedUserIdResolver;
        this.assignmentHistoryService = assignmentHistoryService;
    }

    @GetMapping
    @PreAuthorize(
        "hasAuthority('" +
            PermissionCodes.ROLE_READ +
        "')"
    )
    public List<RoleSummaryResponse> getUserRoles(
        @PathVariable Long userId,

        @AuthenticationPrincipal
        Jwt jwt
    ) {
        Long authenticatedUserId =
            authenticatedUserIdResolver.resolve(jwt);

        return userRoleManagementService
            .getUserRoles(
                authenticatedUserId,
                userId
            );
    }

    @PostMapping
    @PreAuthorize(
        "hasAuthority('" +
            PermissionCodes.ROLE_ASSIGN +
        "')"
    )
    public RoleSummaryResponse assignRole(
        @PathVariable Long userId,

        @Valid
        @RequestBody
        AssignUserRoleRequest request,

        @AuthenticationPrincipal
        Jwt jwt
    ) {
        Long authenticatedUserId =
            authenticatedUserIdResolver.resolve(jwt);

        return userRoleManagementService
            .assignRole(
                authenticatedUserId,
                userId,
                request.roleCode(),
                request.note()
            );
    }

    @DeleteMapping("/{roleCode}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize(
        "hasAuthority('" +
            PermissionCodes.ROLE_ASSIGN +
        "')"
    )
    public void revokeRole(
        @PathVariable Long userId,

        @PathVariable String roleCode,

        @Valid
        @RequestBody
        RevokeUserRoleRequest request,

        @AuthenticationPrincipal
        Jwt jwt
    ) {
        Long authenticatedUserId =
            authenticatedUserIdResolver.resolve(jwt);

        userRoleManagementService.revokeRole(
            authenticatedUserId,
            userId,
            roleCode,
            request.note()
        );
    }

    @GetMapping("/history")
    @PreAuthorize(
        "hasAuthority('" +
            PermissionCodes.ROLE_READ +
        "')"
    )
    public PageResponse<UserRoleAssignmentLogResponse>
        getUserRoleAssignmentHistory(
            @PathVariable Long userId,

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "20")
            int size,

            @AuthenticationPrincipal
            Jwt jwt
        ) {
            Long authenticatedUserId =
                authenticatedUserIdResolver.resolve(jwt);

            return assignmentHistoryService
                .getUserRoleAssignmentHistory(
                    authenticatedUserId,
                    userId,
                    page,
                    size
                );
        }
}
