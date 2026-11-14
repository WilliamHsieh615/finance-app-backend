package com.williamhsieh.financeapp.service.authorization;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.williamhsieh.financeapp.service.auth.UserAuthorities;
import com.williamhsieh.financeapp.service.auth.UserAuthorityService;
import com.williamhsieh.financeapp.service.user.UserAccountAccessPolicy;

@Service
public class PermissionAccessPolicy {

    private final UserAccountAccessPolicy
        userAccountAccessPolicy;

    private final UserAuthorityService
        userAuthorityService;

    public PermissionAccessPolicy(
        UserAccountAccessPolicy userAccountAccessPolicy,
        UserAuthorityService userAuthorityService
    ) {
        this.userAccountAccessPolicy =
            userAccountAccessPolicy;

        this.userAuthorityService =
            userAuthorityService;
    }

    @Transactional(readOnly = true)
    public void requirePermission(
        Long userId,
        String permissionCode
    ) {
        if (
            userId == null ||
            permissionCode == null ||
            permissionCode.isBlank()
        ) {
            throw new ResponseStatusException(
                HttpStatus.FORBIDDEN,
                "你沒有執行此操作的權限"
            );
        }

        boolean accountAvailable =
            userAccountAccessPolicy
                .isAvailable(userId);

        if (!accountAvailable) {
            throw new ResponseStatusException(
                HttpStatus.FORBIDDEN,
                "目前登入的使用者帳號無法使用"
            );
        }

        UserAuthorities authorities =
            userAuthorityService
                .getUserAuthorities(userId);

        boolean permissionAvailable =
            authorities
                .permissions()
                .contains(permissionCode);

        if (!permissionAvailable) {
            throw new ResponseStatusException(
                HttpStatus.FORBIDDEN,
                "你沒有執行此操作的權限"
            );
        }
    }
}
