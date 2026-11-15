package com.williamhsieh.financeapp.identity.authorization.service;

import java.util.Locale;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.williamhsieh.financeapp.identity.authorization.constant.PermissionCodes;
import com.williamhsieh.financeapp.identity.authorization.constant.RoleCodes;
import com.williamhsieh.financeapp.identity.authorization.constant.RolePermissionAssignmentTypeCodes;
import com.williamhsieh.financeapp.identity.authorization.dto.PermissionSummaryResponse;
import com.williamhsieh.financeapp.identity.authorization.entity.Permission;
import com.williamhsieh.financeapp.identity.authorization.entity.Role;
import com.williamhsieh.financeapp.identity.authorization.entity.RolePermission;
import com.williamhsieh.financeapp.identity.authorization.entity.RolePermissionAssignmentLog;
import com.williamhsieh.financeapp.identity.authorization.entity.RolePermissionAssignmentType;
import com.williamhsieh.financeapp.identity.authorization.mapper.PermissionResponseMapper;
import com.williamhsieh.financeapp.identity.authorization.repository.PermissionRepository;
import com.williamhsieh.financeapp.identity.authorization.repository.RolePermissionAssignmentLogRepository;
import com.williamhsieh.financeapp.identity.authorization.repository.RolePermissionAssignmentTypeRepository;
import com.williamhsieh.financeapp.identity.authorization.repository.RolePermissionRepository;
import com.williamhsieh.financeapp.identity.authorization.repository.RoleRepository;
import com.williamhsieh.financeapp.identity.authorization.repository.UserRoleRepository;
import com.williamhsieh.financeapp.identity.user.entity.User;
import com.williamhsieh.financeapp.identity.user.repository.UserRepository;
import com.williamhsieh.financeapp.shared.exception.BusinessException;
import com.williamhsieh.financeapp
    .identity.authorization.dto
    .RolePermissionAssignmentLogResponse;
import com.williamhsieh.financeapp
    .identity.authorization.dto
    .RolePermissionAssignmentLogResponse
    .AssignmentTypeInfo;
import com.williamhsieh.financeapp
    .identity.authorization.dto
    .RolePermissionAssignmentLogResponse
    .ChangedByUserInfo;
import com.williamhsieh.financeapp.shared.dto.PageResponse;

@Service
public class RolePermissionManagementService {

    private final RoleRepository roleRepository;

    private final PermissionRepository
        permissionRepository;

    private final RolePermissionRepository
        rolePermissionRepository;

    private final RolePermissionAssignmentTypeRepository
        assignmentTypeRepository;

    private final RolePermissionAssignmentLogRepository
        assignmentLogRepository;

    private final UserRoleRepository
        userRoleRepository;

    private final UserRepository userRepository;

    private final PermissionAccessPolicy
        permissionAccessPolicy;

    private final PermissionResponseMapper
        permissionResponseMapper;

    public RolePermissionManagementService(
        RoleRepository roleRepository,
        PermissionRepository permissionRepository,
        RolePermissionRepository rolePermissionRepository,
        RolePermissionAssignmentTypeRepository
            assignmentTypeRepository,
        RolePermissionAssignmentLogRepository
            assignmentLogRepository,
        UserRoleRepository userRoleRepository,
        UserRepository userRepository,
        PermissionAccessPolicy permissionAccessPolicy,
        PermissionResponseMapper
            permissionResponseMapper
    ) {
        this.roleRepository =
            roleRepository;
        this.permissionRepository =
            permissionRepository;
        this.rolePermissionRepository =
            rolePermissionRepository;
        this.assignmentTypeRepository =
            assignmentTypeRepository;
        this.assignmentLogRepository =
            assignmentLogRepository;
        this.userRoleRepository =
            userRoleRepository;
        this.userRepository =
            userRepository;
        this.permissionAccessPolicy =
            permissionAccessPolicy;
        this.permissionResponseMapper =
            permissionResponseMapper;
    }

    @Transactional(readOnly = true)
    public PageResponse<RolePermissionAssignmentLogResponse>
        getRolePermissionAssignmentHistory(
            Long authenticatedUserId,
            String roleCode,
            int page,
            int size
        ) {
            permissionAccessPolicy.requirePermission(
                authenticatedUserId,
                PermissionCodes.PERMISSION_READ
            );

            validatePagination(page, size);

            String normalizedRoleCode =
                roleCode
                    .trim()
                    .toUpperCase(Locale.ROOT);

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

            PageRequest pageRequest =
                PageRequest.of(
                    page,
                    size
                );

            Page<RolePermissionAssignmentLog> logs =
                assignmentLogRepository
                    .findAllByRole_IdOrderByChangedDateDescIdDesc(
                        role.getId(),
                        pageRequest
                    );

            Page<RolePermissionAssignmentLogResponse>
                result = logs.map(
                    this::toAssignmentLogResponse
                );

            return PageResponse.from(result);
        }

    @Transactional
    public PermissionSummaryResponse grantPermission(
        Long authenticatedUserId,
        String roleCode,
        String permissionCode,
        String note
    ) {
        permissionAccessPolicy.requirePermission(
            authenticatedUserId,
            PermissionCodes.ROLE_PERMISSION_MANAGE
        );

        String normalizedRoleCode =
            roleCode
                .trim()
                .toUpperCase(Locale.ROOT);

        Role role = roleRepository
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
         * 一般管理員不能修改 SUPER_ADMIN
         * 角色所擁有的權限。
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
                "只有最高系統管理員可以修改 SUPER_ADMIN 角色權限"
            );
        }

        String normalizedPermissionCode =
            permissionCode
                .trim()
                .toUpperCase(Locale.ROOT);

        Permission permission =
            permissionRepository
                .findActiveByCodeIgnoreCase(
                    normalizedPermissionCode
                )
                .orElseThrow(() ->
                    new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "找不到指定的權限"
                    )
                );

        User changedByUser = userRepository
            .findByIdAndDeletedDateIsNull(
                authenticatedUserId
            )
            .orElseThrow(() ->
                new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "目前登入的使用者帳號無法使用"
                )
            );

        RolePermission rolePermission =
            rolePermissionRepository
                .findByRoleIdAndPermissionIdForUpdate(
                    role.getId(),
                    permission.getId()
                )
                .map(existingRolePermission -> {
                    if (
                        existingRolePermission
                            .isAvailable()
                    ) {
                        throw new BusinessException(
                            HttpStatus.CONFLICT,
                            "PERMISSION_ALREADY_GRANTED",
                            "角色已擁有此權限"
                        );
                    }

                    existingRolePermission.activate();

                    return existingRolePermission;
                })
                .orElseGet(() ->
                    new RolePermission(
                        role,
                        permission
                    )
                );

        rolePermissionRepository.save(
            rolePermission
        );

        RolePermissionAssignmentType assignmentType =
            assignmentTypeRepository
                .findByCodeAndActiveTrueAndDeletedDateIsNull(
                    RolePermissionAssignmentTypeCodes.GRANTED
                )
                .orElseThrow(() ->
                    new IllegalStateException(
                        "找不到啟用中的 GRANTED 角色權限異動類型"
                    )
                );

        RolePermissionAssignmentLog assignmentLog =
            new RolePermissionAssignmentLog(
                role,
                permission,
                assignmentType,
                changedByUser,
                note.trim()
            );

        assignmentLogRepository.save(
            assignmentLog
        );

        return permissionResponseMapper
            .toSummary(permission);
    }

    @Transactional
    public void revokePermission(
        Long authenticatedUserId,
        String roleCode,
        String permissionCode,
        String note
    ) {
        permissionAccessPolicy.requirePermission(
            authenticatedUserId,
            PermissionCodes.ROLE_PERMISSION_MANAGE
        );

        String normalizedRoleCode =
            roleCode
                .trim()
                .toUpperCase(Locale.ROOT);

        Role role = roleRepository
            .findActiveRoleByCodeForUpdate(
                normalizedRoleCode
            )
            .orElseThrow(() ->
                new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "找不到指定的角色"
                )
            );

        boolean superAdminRole =
            RoleCodes.SUPER_ADMIN.equals(
                role.getCode()
            );

        if (
            superAdminRole &&
            !hasActiveRole(
                authenticatedUserId,
                RoleCodes.SUPER_ADMIN
            )
        ) {
            throw new BusinessException(
                HttpStatus.FORBIDDEN,
                "SUPER_ADMIN_REQUIRED",
                "只有最高系統管理員可以修改 SUPER_ADMIN 角色權限"
            );
        }

        String normalizedPermissionCode =
            permissionCode
                .trim()
                .toUpperCase(Locale.ROOT);

        Permission permission =
            permissionRepository
                .findActiveByCodeIgnoreCase(
                    normalizedPermissionCode
                )
                .orElseThrow(() ->
                    new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "找不到指定的權限"
                    )
                );

        /*
        * SUPER_ADMIN 必須永遠保有管理角色權限
        * 的能力，避免系統完全失去授權管理入口。
        */
        if (
            superAdminRole &&
            PermissionCodes
                .ROLE_PERMISSION_MANAGE
                .equals(permission.getCode())
        ) {
            throw new BusinessException(
                HttpStatus.CONFLICT,
                "PROTECTED_PERMISSION_CANNOT_BE_REVOKED",
                "不可撤銷 SUPER_ADMIN 的角色權限管理權限"
            );
        }

        User changedByUser = userRepository
            .findByIdAndDeletedDateIsNull(
                authenticatedUserId
            )
            .orElseThrow(() ->
                new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "目前登入的使用者帳號無法使用"
                )
            );

        RolePermission rolePermission =
            rolePermissionRepository
                .findByRoleIdAndPermissionIdForUpdate(
                    role.getId(),
                    permission.getId()
                )
                .orElseThrow(() ->
                    new BusinessException(
                        HttpStatus.NOT_FOUND,
                        "ROLE_PERMISSION_NOT_FOUND",
                        "角色未擁有此權限"
                    )
                );

        if (!rolePermission.isAvailable()) {
            throw new BusinessException(
                HttpStatus.NOT_FOUND,
                "ROLE_PERMISSION_NOT_FOUND",
                "角色未擁有此權限"
            );
        }

        rolePermission.revoke();

        rolePermissionRepository.save(
            rolePermission
        );

        RolePermissionAssignmentType assignmentType =
            assignmentTypeRepository
                .findByCodeAndActiveTrueAndDeletedDateIsNull(
                    RolePermissionAssignmentTypeCodes.REVOKED
                )
                .orElseThrow(() ->
                    new IllegalStateException(
                        "找不到啟用中的 REVOKED 角色權限異動類型"
                    )
                );

        RolePermissionAssignmentLog assignmentLog =
            new RolePermissionAssignmentLog(
                role,
                permission,
                assignmentType,
                changedByUser,
                note.trim()
            );

        assignmentLogRepository.save(
            assignmentLog
        );
    }

    private boolean hasActiveRole(
        Long userId,
        String roleCode
    ) {
        return userRoleRepository
            .findActiveRoleCodesByUserId(userId)
            .contains(roleCode);
    }

    private void validatePagination(
        int page,
        int size
    ) {
        if (page < 0) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "page 不得小於 0"
            );
        }

        if (size < 1 || size > 100) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "size 必須介於 1 到 100"
            );
        }
}

    private RolePermissionAssignmentLogResponse
        toAssignmentLogResponse(
            RolePermissionAssignmentLog assignmentLog
        ) {
            User changedByUser =
                assignmentLog.getChangedByUser();

            ChangedByUserInfo changedByUserInfo =
                changedByUser == null
                    ? null
                    : new ChangedByUserInfo(
                        changedByUser.getId(),
                        changedByUser.getUserNumber(),
                        changedByUser.getName(),
                        changedByUser.getNickname()
                    );

            RolePermissionAssignmentType assignmentType =
                assignmentLog.getAssignmentType();

            AssignmentTypeInfo assignmentTypeInfo =
                new AssignmentTypeInfo(
                    assignmentType.getId(),
                    assignmentType.getCode(),
                    assignmentType.getName()
                );

            return new RolePermissionAssignmentLogResponse(
                assignmentLog.getId(),
                permissionResponseMapper.toSummary(
                    assignmentLog.getPermission()
                ),
                assignmentTypeInfo,
                changedByUserInfo,
                assignmentLog.getNote(),
                assignmentLog.getChangedDate()
            );
    }
}
