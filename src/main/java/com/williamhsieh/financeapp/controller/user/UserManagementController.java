package com.williamhsieh.financeapp.controller.user;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.williamhsieh.financeapp.dto.user.RestoreUserRequest;
import com.williamhsieh.financeapp.constant.auth.PermissionCodes;
import com.williamhsieh.financeapp.dto.user.UserDetailResponse;
import com.williamhsieh.financeapp.dto.common.PageResponse;
import com.williamhsieh.financeapp.dto.user.UserSummaryResponse;
import com.williamhsieh.financeapp.service.user.UserManagementService;
import com.williamhsieh.financeapp.dto.user.UpdateUserAccountStatusRequest;
import com.williamhsieh.financeapp.dto.user.UserAccountStatusLogResponse;
import com.williamhsieh.financeapp.dto.user.DeleteUserRequest;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/admin/users")
public class UserManagementController {

    private final UserManagementService
        userManagementService;

    public UserManagementController(
        UserManagementService userManagementService
    ) {
        this.userManagementService =
            userManagementService;
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
            parseAuthenticatedUserId(jwt);

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
                parseAuthenticatedUserId(jwt);

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
            parseAuthenticatedUserId(jwt);

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
            parseAuthenticatedUserId(jwt);

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
            parseAuthenticatedUserId(jwt);

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
                parseAuthenticatedUserId(jwt);

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
            parseAuthenticatedUserId(jwt);

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
                parseAuthenticatedUserId(jwt);

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
            parseAuthenticatedUserId(jwt);

        return userManagementService
            .restoreUser(
                userId,
                changedByUserId,
                request
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
