package com.williamhsieh.financeapp.identity.authorization.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.williamhsieh.financeapp.identity.authorization.constant.PermissionCodes;
import com.williamhsieh.financeapp.identity.authorization.dto.RoleSummaryResponse;
import com.williamhsieh.financeapp.identity.authorization.entity.Role;
import com.williamhsieh.financeapp.identity.authorization.repository.RoleRepository;

@Service
public class RoleManagementService {

    private final RoleRepository roleRepository;

    private final PermissionAccessPolicy
        permissionAccessPolicy;

    public RoleManagementService(
        RoleRepository roleRepository,
        PermissionAccessPolicy permissionAccessPolicy
    ) {
        this.roleRepository =
            roleRepository;

        this.permissionAccessPolicy =
            permissionAccessPolicy;
    }

    @Transactional(readOnly = true)
    public List<RoleSummaryResponse>
        getAssignableRoles(
            Long authenticatedUserId
        ) {
            permissionAccessPolicy
                .requirePermission(
                    authenticatedUserId,
                    PermissionCodes.ROLE_READ
                );

            List<RoleSummaryResponse> roles =
                roleRepository
                    .findAllByActiveTrueAndDeletedDateIsNullOrderByCodeAsc()
                    .stream()
                    .map(this::toResponse)
                    .toList();

            return List.copyOf(roles);
        }

    private RoleSummaryResponse toResponse(
        Role role
    ) {
        return new RoleSummaryResponse(
            role.getId(),
            role.getCode(),
            role.getName(),
            role.getNote()
        );
    }
}
