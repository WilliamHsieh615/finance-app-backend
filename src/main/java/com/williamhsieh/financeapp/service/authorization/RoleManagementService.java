package com.williamhsieh.financeapp.service.authorization;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.williamhsieh.financeapp.constant.auth.PermissionCodes;
import com.williamhsieh.financeapp.dto.authorization.RoleSummaryResponse;
import com.williamhsieh.financeapp.entity.auth.Role;
import com.williamhsieh.financeapp.repository.auth.RoleRepository;

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
