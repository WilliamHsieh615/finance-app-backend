package com.williamhsieh.financeapp.controller.user;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

import com.williamhsieh.financeapp.constant.auth.PermissionCodes;
import com.williamhsieh.financeapp.dto.user.UserDetailResponse;
import com.williamhsieh.financeapp.dto.common.PageResponse;
import com.williamhsieh.financeapp.dto.user.UserSummaryResponse;
import com.williamhsieh.financeapp.service.user.UserManagementService;

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
        @PathVariable Long userId
    ) {
        return userManagementService
            .getUserById(userId);
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
        String keyword
    ) {
        return userManagementService.getUsers(
            page,
            size,
            keyword
        );
    }
}
