package com.williamhsieh.financeapp.identity.authorization.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.williamhsieh.financeapp.identity.authorization.constant.PermissionCodes;
import com.williamhsieh.financeapp.identity.authorization.dto.RoleSummaryResponse;
import com.williamhsieh.financeapp.identity.authorization.entity.Role;
import com.williamhsieh.financeapp.identity.authorization.repository.RoleRepository;
import com.williamhsieh.financeapp.identity.authorization.dto.PermissionSummaryResponse;
import com.williamhsieh.financeapp.identity.authorization.repository.RolePermissionRepository;
import com.williamhsieh.financeapp.identity.authorization.mapper.PermissionResponseMapper;
import com.williamhsieh.financeapp.identity.authorization.mapper.RoleResponseMapper;

@Service
public class RoleManagementService {

    private final RoleRepository roleRepository;
    private final RolePermissionRepository
        rolePermissionRepository;
    private final PermissionAccessPolicy
        permissionAccessPolicy;
    private final PermissionResponseMapper
        permissionResponseMapper;
    private final RoleResponseMapper
        roleResponseMapper;

    public RoleManagementService(
        RoleRepository roleRepository,
        RolePermissionRepository
            rolePermissionRepository,
        PermissionAccessPolicy permissionAccessPolicy,
        PermissionResponseMapper permissionResponseMapper,
        RoleResponseMapper roleResponseMapper
    ) {
        this.roleRepository =
            roleRepository;
        this.rolePermissionRepository =
            rolePermissionRepository;
        this.permissionAccessPolicy =
            permissionAccessPolicy;
        this.permissionResponseMapper =
            permissionResponseMapper;
        this.roleResponseMapper =
            roleResponseMapper;
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
                    .map(
                        roleResponseMapper::toSummary
                    )
                    .toList();

            return List.copyOf(roles);
        }

    @Transactional(readOnly = true)
    public List<PermissionSummaryResponse>
        getRolePermissions(
            Long authenticatedUserId,
            String roleCode
        ) {
            permissionAccessPolicy
                .requirePermission(
                    authenticatedUserId,
                    PermissionCodes.PERMISSION_READ
                );

            String normalizedRoleCode =
                roleCode.trim();

            Role role = roleRepository
                .findByCodeIgnoreCaseAndActiveTrueAndDeletedDateIsNull(
                    normalizedRoleCode
                )
                .orElseThrow(() ->
                    new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "找不到指定的角色"
                    )
                );

            List<PermissionSummaryResponse> permissions =
                rolePermissionRepository
                    .findActivePermissionsByRoleCode(
                        role.getCode()
                    )
                    .stream()
                    .map(
                        permissionResponseMapper::toSummary
                    )
                    .toList();

            return List.copyOf(permissions);
        }
}
