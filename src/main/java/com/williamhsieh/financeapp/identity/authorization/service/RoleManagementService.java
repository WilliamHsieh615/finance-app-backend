package com.williamhsieh.financeapp.identity.authorization.service;

import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.williamhsieh.financeapp.identity.authorization.constant.PermissionCodes;
import com.williamhsieh.financeapp.identity.authorization.dto.RoleSummaryResponse;
import com.williamhsieh.financeapp.identity.authorization.entity.Role;
import com.williamhsieh.financeapp.identity.authorization.repository.RoleRepository;
import com.williamhsieh.financeapp.identity.authorization.mapper.RoleResponseMapper;
import com.williamhsieh.financeapp.identity.authorization.dto.CreateRoleRequest;
import com.williamhsieh.financeapp.shared.exception.BusinessException;
import com.williamhsieh.financeapp.identity.authorization.constant.RoleCodes;
import com.williamhsieh.financeapp.identity.authorization.dto.UpdateRoleRequest;
import com.williamhsieh.financeapp.identity.authorization.dto.RoleDetailResponse;
import com.williamhsieh.financeapp.identity.authorization.constant.RoleChangeTypeCodes;

@Service
public class RoleManagementService {

    private final RoleRepository roleRepository;
    private final PermissionAccessPolicy permissionAccessPolicy;
    private final RoleResponseMapper roleResponseMapper;
    private final UserAuthorityService userAuthorityService;
    private final RoleChangeAuditService roleChangeAuditService;

    public RoleManagementService(
        RoleRepository roleRepository,
        PermissionAccessPolicy permissionAccessPolicy,
        RoleResponseMapper roleResponseMapper,
        UserAuthorityService userAuthorityService,
        RoleChangeAuditService roleChangeAuditService
    ) {
        this.roleRepository = roleRepository;
        this.permissionAccessPolicy = permissionAccessPolicy;
        this.roleResponseMapper = roleResponseMapper;
        this.userAuthorityService = userAuthorityService;
        this.roleChangeAuditService = roleChangeAuditService;
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

    @Transactional
    public RoleSummaryResponse createRole(
        Long authenticatedUserId,
        CreateRoleRequest request
    ) {
        permissionAccessPolicy.requirePermission(
            authenticatedUserId,
            PermissionCodes.ROLE_CREATE
        );

        String normalizedCode =
            request.code()
                .trim()
                .toUpperCase(Locale.ROOT);

        String normalizedName =
            request.name().trim();

        String normalizedNote =
            normalizeOptionalNote(
                request.note()
            );

        /*
        * 包含停用與軟刪除資料，
        * 避免重複使用既有角色 code。
        */
        roleRepository
            .findByCodeIgnoreCase(
                normalizedCode
            )
            .ifPresent(existingRole -> {
                throw new BusinessException(
                    HttpStatus.CONFLICT,
                    "ROLE_CODE_ALREADY_EXISTS",
                    "角色代碼已存在"
                );
            });

        Role role =
            new Role(
                normalizedCode,
                normalizedName,
                normalizedNote
            );

        Role savedRole =
            roleRepository.save(role);

        roleChangeAuditService.recordCreated(
            savedRole,
            authenticatedUserId,
            RoleChangeTypeCodes.CREATED
        );

        return roleResponseMapper.toSummary(
            savedRole
        );
    }

    @Transactional
    public RoleSummaryResponse updateRole(
        Long authenticatedUserId,
        String roleCode,
        UpdateRoleRequest request
    ) {
        permissionAccessPolicy.requirePermission(
            authenticatedUserId,
            PermissionCodes.ROLE_UPDATE
        );

        String normalizedRoleCode =
            roleCode
                .trim()
                .toUpperCase(Locale.ROOT);

        String normalizedName =
            request.name().trim();

        String normalizedNote =
            normalizeOptionalNote(
                request.note()
            );

        /*
        * 修改期間鎖定角色資料，
        * 避免同時修改或停用造成競態。
        */
        Role role =
            roleRepository
                .findActiveRoleByCodeForUpdate(
                    normalizedRoleCode
                )
                .orElseThrow(() ->
                    new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "找不到指定的角色"
                    )
                );

        /*
        * 即使一般管理員擁有 ROLE_UPDATE，
        * 仍不能修改 SUPER_ADMIN。
        */
        if (
            RoleCodes.SUPER_ADMIN.equals(
                role.getCode()
            ) &&
            !hasActiveRole(
                authenticatedUserId,
                RoleCodes.SUPER_ADMIN
            )
        ) {
            throw new BusinessException(
                HttpStatus.FORBIDDEN,
                "SUPER_ADMIN_REQUIRED",
                "只有最高系統管理員可以修改 SUPER_ADMIN 角色"
            );
        }

        RoleChangeAuditService.RoleState
            previousState =
                roleChangeAuditService.capture(
                    role
                );

        role.updateDetails(
            normalizedName,
            normalizedNote
        );

        Role savedRole =
            roleRepository.save(role);

        roleChangeAuditService.recordChanged(
            savedRole,
            authenticatedUserId,
            RoleChangeTypeCodes.UPDATED,
            previousState
        );

        return roleResponseMapper.toSummary(
            savedRole
        );
    }

    @Transactional
    public void deactivateRole(
        Long authenticatedUserId,
        String roleCode
    ) {
        permissionAccessPolicy.requirePermission(
            authenticatedUserId,
            PermissionCodes.ROLE_DELETE
        );

        String normalizedRoleCode =
            roleCode
                .trim()
                .toUpperCase(Locale.ROOT);

        Role role =
            roleRepository
                .findActiveRoleByCodeForUpdate(
                    normalizedRoleCode
                )
                .orElseThrow(() ->
                    new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "找不到指定的角色"
                    )
                );

        /*
        * USER 是新會員的預設角色。
        * 停用後會造成註冊與基本授權失效。
        */
        if (
            RoleCodes.USER.equals(
                role.getCode()
            )
        ) {
            throw new BusinessException(
                HttpStatus.CONFLICT,
                "SYSTEM_ROLE_CANNOT_BE_DEACTIVATED",
                "不可停用系統核心角色 USER"
            );
        }

        /*
        * SUPER_ADMIN 是最高權限根。
        * 停用角色本身會讓所有最高管理員失去權限，
        * 因此即使操作者是 SUPER_ADMIN 也不可停用。
        */
        if (
            RoleCodes.SUPER_ADMIN.equals(
                role.getCode()
            )
        ) {
            throw new BusinessException(
                HttpStatus.CONFLICT,
                "SYSTEM_ROLE_CANNOT_BE_DEACTIVATED",
                "不可停用系統核心角色 SUPER_ADMIN"
            );
        }

        RoleChangeAuditService.RoleState
            previousState =
                roleChangeAuditService.capture(
                    role
                );

        role.deactivate();

        Role savedRole =
            roleRepository.save(role);

        roleChangeAuditService.recordChanged(
            savedRole,
            authenticatedUserId,
            RoleChangeTypeCodes.DEACTIVATED,
            previousState
        );
    }

    @Transactional
    public RoleSummaryResponse restoreRole(
        Long authenticatedUserId,
        String roleCode
    ) {
        permissionAccessPolicy.requirePermission(
            authenticatedUserId,
            PermissionCodes.ROLE_UPDATE
        );

        String normalizedRoleCode =
            roleCode
                .trim()
                .toUpperCase(Locale.ROOT);

        /*
        * 恢復時必須查詢所有狀態，
        * 不能只查詢有效角色。
        */
        Role role =
            roleRepository
                .findByCodeIgnoreCase(
                    normalizedRoleCode
                )
                .orElseThrow(() ->
                    new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "找不到指定的角色"
                    )
                );

        if (role.isAvailable()) {
            throw new BusinessException(
                HttpStatus.CONFLICT,
                "ROLE_ALREADY_ACTIVE",
                "角色目前已經是啟用狀態"
            );
        }

        RoleChangeAuditService.RoleState
            previousState =
                roleChangeAuditService.capture(
                    role
                );

        role.restore();

        Role savedRole =
            roleRepository.save(role);

        roleChangeAuditService.recordChanged(
            savedRole,
            authenticatedUserId,
            RoleChangeTypeCodes.RESTORED,
            previousState
        );

        return roleResponseMapper.toSummary(
            savedRole
        );
    }

    @Transactional(readOnly = true)
    public List<RoleDetailResponse>
        getDeactivatedRoles(
            Long authenticatedUserId
        ) {

        permissionAccessPolicy.requirePermission(
            authenticatedUserId,
            PermissionCodes.ROLE_UPDATE
        );

        List<RoleDetailResponse> roles =
            roleRepository
                .findAllByActiveFalseOrDeletedDateIsNotNullOrderByCodeAsc()
                .stream()
                .map(
                    roleResponseMapper::toDetail
                )
                .toList();

        return List.copyOf(roles);
    }

    private String normalizeOptionalNote(
        String note
    ) {
        if (note == null || note.isBlank()) {
            return null;
        }

        return note.trim();
    }

    private boolean hasActiveRole(
        Long userId,
        String roleCode
    ) {
        return userAuthorityService
            .getUserAuthorities(userId)
            .roles()
            .contains(roleCode);
    }
}
