package com.williamhsieh.financeapp.service.auth;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.williamhsieh.financeapp.repository.auth.RolePermissionRepository;
import com.williamhsieh.financeapp.repository.auth.UserRoleRepository;

@Service
public class UserAuthorityService {

    private final UserRoleRepository userRoleRepository;

    private final RolePermissionRepository
        rolePermissionRepository;

    public UserAuthorityService(
        UserRoleRepository userRoleRepository,
        RolePermissionRepository rolePermissionRepository
    ) {
        this.userRoleRepository = userRoleRepository;
        this.rolePermissionRepository =
            rolePermissionRepository;
    }

    @Transactional(readOnly = true)
    public UserAuthorities getUserAuthorities(
        Long userId
    ) {
        List<String> roleCodes =
            userRoleRepository
                .findActiveRoleCodesByUserId(
                    userId
                );

        List<String> permissionCodes =
            rolePermissionRepository
                .findActivePermissionCodesByUserId(
                    userId
                );

        Set<String> roles =
            new LinkedHashSet<>(roleCodes);

        Set<String> permissions =
            new LinkedHashSet<>(permissionCodes);

        return new UserAuthorities(
            roles,
            permissions
        );
    }
}
