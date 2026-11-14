package com.williamhsieh.financeapp.identity.user.controller;

import jakarta.validation.Valid;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.williamhsieh.financeapp.identity.authorization.constant.PermissionCodes;
import com.williamhsieh.financeapp.identity.user.dto.DeleteUserRequest;
import com.williamhsieh.financeapp.identity.user.dto.RestoreUserRequest;
import com.williamhsieh.financeapp.identity.user.dto.UpdateUserAccountStatusRequest;
import com.williamhsieh.financeapp.identity.user.dto.UserAccountStatusLogResponse;
import com.williamhsieh.financeapp.identity.user.dto.UserDetailResponse;
import com.williamhsieh.financeapp.identity.user.dto.UserSummaryResponse;
import com.williamhsieh.financeapp.identity.user.service.UserManagementService;
import com.williamhsieh.financeapp.shared.dto.PageResponse;
import com.williamhsieh.financeapp.identity.authentication.security.AuthenticatedUserIdResolver;

@RestController
@RequestMapping("/api/v1/admin/users")
public class UserManagementController {

    private final UserManagementService
        userManagementService;
    private final AuthenticatedUserIdResolver
        authenticatedUserIdResolver;

    public UserManagementController(
        UserManagementService userManagementService,
        AuthenticatedUserIdResolver
            authenticatedUserIdResolver
    ) {
        this.userManagementService =
            userManagementService;
        this.authenticatedUserIdResolver =
            authenticatedUserIdResolver;
    }

    @GetMapping("/{userId}")
    @PreAuthorize(
        "hasAuthority('" +
            PermissionCodes.USER_READ +
        "')"
    )
    public UserDetailResponse getUserById(
        @PathVariable Long userId,

        @AuthenticationPrincipal
        Jwt jwt
    ) {
        Long authenticatedUserId =
            authenticatedUserIdResolver.resolve(jwt);

        return userManagementService
            .getUserById(
                authenticatedUserId,
                userId
            );
    }

    @GetMapping("/{userId}/status-history")
    @PreAuthorize(
        "hasAuthority('" +
            PermissionCodes.USER_READ +
        "')"
    )
    public PageResponse<UserAccountStatusLogResponse>
        getUserAccountStatusHistory(
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

            return userManagementService
                .getUserAccountStatusHistory(
                    authenticatedUserId,
                    userId,
                    page,
                    size
                );
        }

    @PatchMapping("/{userId}/status")
    @PreAuthorize(
        "hasAuthority('" +
            PermissionCodes.USER_STATUS_UPDATE +
        "')"
    )
    public UserDetailResponse updateUserAccountStatus(
        @PathVariable Long userId,

        @Valid
        @RequestBody
        UpdateUserAccountStatusRequest request,

        @AuthenticationPrincipal
        Jwt jwt
    ) {
        Long changedByUserId =
            authenticatedUserIdResolver.resolve(jwt);

        return userManagementService
            .updateUserAccountStatus(
                userId,
                changedByUserId,
                request
            );
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize(
        "hasAuthority('" +
            PermissionCodes.USER_DELETE +
        "')"
    )
    public ResponseEntity<Void> deleteUser(
        @PathVariable Long userId,

        @Valid
        @RequestBody
        DeleteUserRequest request,

        @AuthenticationPrincipal
        Jwt jwt
    ) {
        Long changedByUserId =
            authenticatedUserIdResolver.resolve(jwt);

        userManagementService.softDeleteUser(
            userId,
            changedByUserId,
            request
        );

        return ResponseEntity
            .noContent()
            .build();
    }

    @GetMapping
    @PreAuthorize(
        "hasAuthority('" +
            PermissionCodes.USER_READ +
        "')"
    )
    public PageResponse<UserSummaryResponse> getUsers(
        @RequestParam(defaultValue = "0")
        int page,

        @RequestParam(defaultValue = "20")
        int size,

        @RequestParam(required = false)
        String keyword,

        @RequestParam(required = false)
        String status,

        @AuthenticationPrincipal
        Jwt jwt
    ) {
        Long authenticatedUserId =
            authenticatedUserIdResolver.resolve(jwt);

        return userManagementService.getUsers(
            authenticatedUserId,
            page,
            size,
            keyword,
            status
        );
    }

    @GetMapping("/deleted")
    @PreAuthorize(
        "hasAuthority('" +
            PermissionCodes.USER_READ_DELETED +
        "')"
    )
    public PageResponse<UserSummaryResponse>
        getDeletedUsers(
            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "20")
            int size,

            @RequestParam(required = false)
            String keyword,

            @AuthenticationPrincipal
            Jwt jwt
        ) {
            Long authenticatedUserId =
                authenticatedUserIdResolver.resolve(jwt);

            return userManagementService
                .getDeletedUsers(
                    authenticatedUserId,
                    page,
                    size,
                    keyword
                );
        }

    @GetMapping("/deleted/{userId}")
    @PreAuthorize(
        "hasAuthority('" +
            PermissionCodes.USER_READ_DELETED +
        "')"
    )
    public UserDetailResponse getDeletedUserById(
        @PathVariable Long userId,

        @AuthenticationPrincipal
        Jwt jwt
    ) {
        Long authenticatedUserId =
            authenticatedUserIdResolver.resolve(jwt);

        return userManagementService
            .getDeletedUserById(
                authenticatedUserId,
                userId
            );
    }
    
    @GetMapping(
        "/deleted/{userId}/status-history"
    )
    @PreAuthorize(
        "hasAuthority('" +
            PermissionCodes.USER_READ_DELETED +
        "')"
    )
    public PageResponse<UserAccountStatusLogResponse>
        getDeletedUserAccountStatusHistory(
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

            return userManagementService
                .getDeletedUserAccountStatusHistory(
                    authenticatedUserId,
                    userId,
                    page,
                    size
                );
        }
    
    @PostMapping(
        "/deleted/{userId}/restore"
    )
    @PreAuthorize(
        "hasAuthority('" +
            PermissionCodes.USER_RESTORE +
        "')"
    )
    public UserDetailResponse restoreUser(
        @PathVariable Long userId,

        @Valid
        @RequestBody
        RestoreUserRequest request,

        @AuthenticationPrincipal
        Jwt jwt
    ) {
        Long changedByUserId =
            authenticatedUserIdResolver.resolve(jwt);

        return userManagementService
            .restoreUser(
                userId,
                changedByUserId,
                request
            );
    }
}
