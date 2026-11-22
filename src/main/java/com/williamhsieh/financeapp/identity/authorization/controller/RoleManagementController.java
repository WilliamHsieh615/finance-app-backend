package com.williamhsieh.financeapp.identity.authorization.controller;

import java.util.List;
import jakarta.validation.Valid;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;

import com.williamhsieh.financeapp.identity.authorization.constant.PermissionCodes;
import com.williamhsieh.financeapp.identity.authorization.dto.RoleSummaryResponse;
import com.williamhsieh.financeapp.identity.authorization.service.RoleChangeHistoryService;
import com.williamhsieh.financeapp.identity.authorization.service.RoleManagementService;
import com.williamhsieh.financeapp.identity.authentication.security. AuthenticatedUserIdResolver;
import com.williamhsieh.financeapp.shared.dto.PageResponse;
import com.williamhsieh.financeapp.identity.authorization.dto.CreateRoleRequest;
import com.williamhsieh.financeapp.identity.authorization.dto.UpdateRoleRequest;
import com.williamhsieh.financeapp.identity.authorization.dto.RoleDetailResponse;
import com.williamhsieh.financeapp.identity.authorization.dto.RoleChangeLogResponse;

@RestController
@RequestMapping("/api/v1/admin/roles")
public class RoleManagementController {

    private final RoleManagementService roleManagementService;
    private final AuthenticatedUserIdResolver authenticatedUserIdResolver;
    private final RoleChangeHistoryService roleChangeHistoryService;

    public RoleManagementController(
        RoleManagementService roleManagementService,
        AuthenticatedUserIdResolver authenticatedUserIdResolver,
        RoleChangeHistoryService roleChangeHistoryService
    ) {
        this.roleManagementService = roleManagementService;
        this.authenticatedUserIdResolver = authenticatedUserIdResolver;
        this.roleChangeHistoryService = roleChangeHistoryService;
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

    @GetMapping("/deactivated")
    @PreAuthorize(
        "hasAuthority('" +
            PermissionCodes.ROLE_UPDATE +
        "')"
    )
    public List<RoleDetailResponse>
        getDeactivatedRoles(
            @AuthenticationPrincipal
            Jwt jwt
        ) {

        Long authenticatedUserId =
            authenticatedUserIdResolver
                .resolve(jwt);

        return roleManagementService
            .getDeactivatedRoles(
                authenticatedUserId
            );
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize(
        "hasAuthority('" +
            PermissionCodes.ROLE_CREATE +
        "')"
    )
    public RoleSummaryResponse createRole(
        @Valid
        @RequestBody
        CreateRoleRequest request,

        @AuthenticationPrincipal
        Jwt jwt
    ) {
        Long authenticatedUserId =
            authenticatedUserIdResolver
                .resolve(jwt);

        return roleManagementService.createRole(
            authenticatedUserId,
            request
        );
    }

    @PutMapping("/{roleCode}")
    @PreAuthorize(
        "hasAuthority('" +
            PermissionCodes.ROLE_UPDATE +
        "')"
    )
    public RoleSummaryResponse updateRole(
        @PathVariable
        String roleCode,

        @Valid
        @RequestBody
        UpdateRoleRequest request,

        @AuthenticationPrincipal
        Jwt jwt
    ) {
        Long authenticatedUserId =
            authenticatedUserIdResolver
                .resolve(jwt);

        return roleManagementService.updateRole(
            authenticatedUserId,
            roleCode,
            request
        );
    }

    @DeleteMapping("/{roleCode}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize(
        "hasAuthority('" +
            PermissionCodes.ROLE_DELETE +
        "')"
    )
    public void deactivateRole(
        @PathVariable
        String roleCode,

        @AuthenticationPrincipal
        Jwt jwt
    ) {
        Long authenticatedUserId =
            authenticatedUserIdResolver
                .resolve(jwt);

        roleManagementService.deactivateRole(
            authenticatedUserId,
            roleCode
        );
    }

    @PostMapping("/{roleCode}/restore")
    @PreAuthorize(
        "hasAuthority('" +
            PermissionCodes.ROLE_UPDATE +
        "')"
    )
    public RoleSummaryResponse restoreRole(
        @PathVariable
        String roleCode,

        @AuthenticationPrincipal
        Jwt jwt
    ) {
        Long authenticatedUserId =
            authenticatedUserIdResolver
                .resolve(jwt);

        return roleManagementService.restoreRole(
            authenticatedUserId,
            roleCode
        );
    }

    @GetMapping("/{roleCode}/history")
    @PreAuthorize(
        "hasAuthority('" +
            PermissionCodes.ROLE_READ +
        "')"
    )
    public PageResponse<RoleChangeLogResponse>
        getRoleChangeHistory(
            @PathVariable
            String roleCode,

            @RequestParam(
                defaultValue = "0"
            )
            int page,

            @RequestParam(
                defaultValue = "20"
            )
            int size,

            @AuthenticationPrincipal
            Jwt jwt
        ) {

        Long authenticatedUserId =
            authenticatedUserIdResolver
                .resolve(jwt);

        return roleChangeHistoryService
            .getRoleChangeHistory(
                authenticatedUserId,
                roleCode,
                page,
                size
            );
    }
}
