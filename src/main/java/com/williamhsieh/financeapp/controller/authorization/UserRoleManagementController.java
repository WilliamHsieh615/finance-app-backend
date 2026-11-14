package com.williamhsieh.financeapp.controller.authorization;

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
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RequestParam;

import com.williamhsieh.financeapp.constant.auth.PermissionCodes;
import com.williamhsieh.financeapp.dto.authorization.RoleSummaryResponse;
import com.williamhsieh.financeapp.service.authorization.UserRoleManagementService;
import com.williamhsieh.financeapp.dto.authorization.AssignUserRoleRequest;
import com.williamhsieh.financeapp.dto.authorization.RevokeUserRoleRequest;
import com.williamhsieh.financeapp.dto.authorization.UserRoleAssignmentLogResponse;
import com.williamhsieh.financeapp.dto.common.PageResponse;

@RestController
@RequestMapping(
    "/api/v1/admin/users/{userId}/roles"
)
public class UserRoleManagementController {

    private final UserRoleManagementService
        userRoleManagementService;

    public UserRoleManagementController(
        UserRoleManagementService userRoleManagementService
    ) {
        this.userRoleManagementService =
            userRoleManagementService;
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
            parseAuthenticatedUserId(jwt);

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
            parseAuthenticatedUserId(jwt);

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
            parseAuthenticatedUserId(jwt);

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
                parseAuthenticatedUserId(jwt);

            return userRoleManagementService
                .getUserRoleAssignmentHistory(
                    authenticatedUserId,
                    userId,
                    page,
                    size
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
