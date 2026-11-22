package com.williamhsieh.financeapp.identity.authorization.controller;

import java.util.List;
import jakarta.validation.Valid;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.williamhsieh.financeapp.identity.authentication.security.AuthenticatedUserIdResolver;
import com.williamhsieh.financeapp.identity.authorization.constant.PermissionCodes;
import com.williamhsieh.financeapp.identity.authorization.dto.PermissionSummaryResponse;
import com.williamhsieh.financeapp.identity.authorization.service.PermissionChangeHistoryService;
import com.williamhsieh.financeapp.identity.authorization.service.PermissionManagementService;
import com.williamhsieh.financeapp.identity.authorization.dto.CreatePermissionRequest;
import com.williamhsieh.financeapp.identity.authorization.dto.UpdatePermissionRequest;
import com.williamhsieh.financeapp.identity.authorization.dto.PermissionDetailResponse;
import com.williamhsieh.financeapp.identity.authorization.dto.PermissionChangeLogResponse;
import com.williamhsieh.financeapp.shared.dto.PageResponse;

@RestController
@RequestMapping("/api/v1/admin/permissions")
public class PermissionManagementController {

    private final PermissionManagementService permissionManagementService;
    private final AuthenticatedUserIdResolver authenticatedUserIdResolver;
    private final PermissionChangeHistoryService permissionChangeHistoryService;

    public PermissionManagementController(
        PermissionManagementService permissionManagementService,
        AuthenticatedUserIdResolver authenticatedUserIdResolver,
        PermissionChangeHistoryService permissionChangeHistoryService
    ) {
        this.permissionManagementService = permissionManagementService;
        this.authenticatedUserIdResolver = authenticatedUserIdResolver;
        this.permissionChangeHistoryService = permissionChangeHistoryService;
    }

    @GetMapping
    @PreAuthorize(
        "hasAuthority('" +
            PermissionCodes.PERMISSION_READ +
        "')"
    )
    public List<PermissionSummaryResponse>
        getActivePermissions(
            @AuthenticationPrincipal
            Jwt jwt
        ) {
            Long authenticatedUserId =
                authenticatedUserIdResolver
                    .resolve(jwt);

            return permissionManagementService
                .getActivePermissions(
                    authenticatedUserId
                );
        }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize(
        "hasAuthority('" +
            PermissionCodes.PERMISSION_MANAGE +
        "')"
    )
    public PermissionSummaryResponse createPermission(
        @Valid
        @RequestBody
        CreatePermissionRequest request,

        @AuthenticationPrincipal
        Jwt jwt
    ) {
        Long authenticatedUserId =
            authenticatedUserIdResolver
                .resolve(jwt);

        return permissionManagementService
            .createPermission(
                authenticatedUserId,
                request
            );
    }

    @PutMapping("/{permissionCode}")
    @PreAuthorize(
        "hasAuthority('" +
            PermissionCodes.PERMISSION_MANAGE +
        "')"
    )
    public PermissionSummaryResponse updatePermission(
        @PathVariable String permissionCode,

        @Valid
        @RequestBody
        UpdatePermissionRequest request,

        @AuthenticationPrincipal
        Jwt jwt
    ) {
        Long authenticatedUserId =
            authenticatedUserIdResolver
                .resolve(jwt);

        return permissionManagementService
            .updatePermission(
                authenticatedUserId,
                permissionCode,
                request
            );
    }

    @DeleteMapping("/{permissionCode}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize(
        "hasAuthority('" +
            PermissionCodes.PERMISSION_MANAGE +
        "')"
    )
    public void deactivatePermission(
        @PathVariable String permissionCode,

        @AuthenticationPrincipal
        Jwt jwt
    ) {
        Long authenticatedUserId =
            authenticatedUserIdResolver
                .resolve(jwt);

        permissionManagementService
            .deactivatePermission(
                authenticatedUserId,
                permissionCode
            );
    }

    @PostMapping("/{permissionCode}/restore")
    @PreAuthorize(
        "hasAuthority('" +
            PermissionCodes.PERMISSION_MANAGE +
        "')"
    )
    public PermissionSummaryResponse restorePermission(
        @PathVariable String permissionCode,

        @AuthenticationPrincipal
        Jwt jwt
    ) {
        Long authenticatedUserId =
            authenticatedUserIdResolver
                .resolve(jwt);

        return permissionManagementService
            .restorePermission(
                authenticatedUserId,
                permissionCode
            );
    }

    @GetMapping("/deactivated")
    @PreAuthorize(
        "hasAuthority('" +
            PermissionCodes.PERMISSION_MANAGE +
        "')"
    )
    public List<PermissionDetailResponse>
        getDeactivatedPermissions(
            @AuthenticationPrincipal
            Jwt jwt
        ) {
            Long authenticatedUserId =
                authenticatedUserIdResolver
                    .resolve(jwt);

            return permissionManagementService
                .getDeactivatedPermissions(
                    authenticatedUserId
                );
    }

    @GetMapping("/{permissionCode}/history")
    @PreAuthorize(
        "hasAuthority('" +
            PermissionCodes.PERMISSION_MANAGE +
        "')"
    )
    public PageResponse<PermissionChangeLogResponse>
        getPermissionChangeHistory(
            @PathVariable String permissionCode,

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

            return permissionChangeHistoryService
                .getPermissionChangeHistory(
                    authenticatedUserId,
                    permissionCode,
                    page,
                    size
                );
        }
}
