package com.williamhsieh.financeapp.identity.authorization.service;

import java.util.List;
import java.util.Locale;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.williamhsieh.financeapp.shared.exception.BusinessException;
import com.williamhsieh.financeapp.identity.authorization.constant.PermissionCodes;
import com.williamhsieh.financeapp.identity.authorization.constant.RoleCodes;
import com.williamhsieh.financeapp.identity.authorization.constant.UserRoleAssignmentTypeCodes;
import com.williamhsieh.financeapp.identity.authorization.dto.RoleSummaryResponse;
import com.williamhsieh.financeapp.identity.authorization.entity.Role;
import com.williamhsieh.financeapp.identity.authorization.entity.UserRole;
import com.williamhsieh.financeapp.identity.authorization.entity.UserRoleAssignmentLog;
import com.williamhsieh.financeapp.identity.authorization.entity.UserRoleAssignmentType;
import com.williamhsieh.financeapp.identity.authorization.repository.RoleRepository;
import com.williamhsieh.financeapp.identity.authorization.repository.UserRoleAssignmentLogRepository;
import com.williamhsieh.financeapp.identity.authorization.repository.UserRoleAssignmentTypeRepository;
import com.williamhsieh.financeapp.identity.authorization.repository.UserRoleRepository;
import com.williamhsieh.financeapp.identity.user.entity.User;
import com.williamhsieh.financeapp.identity.user.repository.UserRepository;
import com.williamhsieh.financeapp.identity.authorization.mapper.RoleResponseMapper;

@Service
public class UserRoleManagementService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final PermissionAccessPolicy permissionAccessPolicy;
    private final RoleRepository roleRepository;
    private final UserRoleAssignmentTypeRepository userRoleAssignmentTypeRepository;
    private final UserRoleAssignmentLogRepository userRoleAssignmentLogRepository;
    private final RoleResponseMapper roleResponseMapper;

    public UserRoleManagementService(
        UserRepository userRepository,
        UserRoleRepository userRoleRepository,
        RoleRepository roleRepository,
        UserRoleAssignmentTypeRepository userRoleAssignmentTypeRepository,
        UserRoleAssignmentLogRepository userRoleAssignmentLogRepository,
        PermissionAccessPolicy permissionAccessPolicy,
        RoleResponseMapper roleResponseMapper
    ) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.roleRepository = roleRepository;
        this.userRoleAssignmentTypeRepository = userRoleAssignmentTypeRepository;
        this.userRoleAssignmentLogRepository = userRoleAssignmentLogRepository;
        this.permissionAccessPolicy = permissionAccessPolicy;
        this.roleResponseMapper = roleResponseMapper;
    }

    @Transactional(readOnly = true)
    public List<RoleSummaryResponse> getUserRoles(
        Long authenticatedUserId,
        Long targetUserId
    ) {
        permissionAccessPolicy
            .requirePermission(
                authenticatedUserId,
                PermissionCodes.ROLE_READ
            );

        /*
         * 角色管理目前只處理未刪除會員。
         */
        userRepository
            .findByIdAndDeletedDateIsNull(
                targetUserId
            )
            .orElseThrow(() ->
                new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "找不到指定的使用者"
                )
            );

        List<RoleSummaryResponse> roles =
            userRoleRepository
                .findActiveRolesByUserId(
                    targetUserId
                )
                .stream()
                .map(
                    roleResponseMapper::toSummary
                )
                .toList();

        return List.copyOf(roles);
    }

    @Transactional
    public RoleSummaryResponse assignRole(
        Long authenticatedUserId,
        Long targetUserId,
        String roleCode,
        String note
    ) {
        permissionAccessPolicy.requirePermission(
            authenticatedUserId,
            PermissionCodes.ROLE_ASSIGN
        );

        User targetUser = userRepository
            .findByIdAndDeletedDateIsNullForUpdate(
                targetUserId
            )
            .orElseThrow(() ->
                new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "找不到指定的使用者"
                )
            );

        User changedByUser;

        if (authenticatedUserId.equals(targetUserId)) {
            changedByUser = targetUser;
        } else {
            changedByUser = userRepository
                .findByIdAndDeletedDateIsNull(
                    authenticatedUserId
                )
                .orElseThrow(() ->
                    new ResponseStatusException(
                        HttpStatus.FORBIDDEN,
                        "目前登入的使用者帳號無法使用"
                    )
                );
        }

        String normalizedRoleCode = roleCode
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

        /*
        * SUPER_ADMIN 是最高權限角色，
        * 只有 SUPER_ADMIN 可以指派。
        */
        if (
            RoleCodes.SUPER_ADMIN.equals(role.getCode()) &&
            !hasActiveRole(
                authenticatedUserId,
                RoleCodes.SUPER_ADMIN
            )
        ) {
            throw new BusinessException(
                HttpStatus.FORBIDDEN,
                "SUPER_ADMIN_REQUIRED",
                "只有最高系統管理員可以指派 SUPER_ADMIN 角色"
            );
        }

        UserRole userRole = userRoleRepository
            .findByUserIdAndRoleIdForUpdate(
                targetUserId,
                role.getId()
            )
            .map(existingUserRole -> {
                if (existingUserRole.isAvailable()) {
                    throw new BusinessException(
                        HttpStatus.CONFLICT,
                        "ROLE_ALREADY_ASSIGNED",
                        "使用者已擁有此角色"
                    );
                }

                existingUserRole.activate();
                return existingUserRole;
            })
            .orElseGet(() ->
                new UserRole(
                    targetUser,
                    role
                )
            );

        userRoleRepository.save(userRole);

        UserRoleAssignmentType assignmentType =
            userRoleAssignmentTypeRepository
                .findByCodeAndActiveTrueAndDeletedDateIsNull(
                    UserRoleAssignmentTypeCodes.ASSIGNED
                )
                .orElseThrow(() ->
                    new IllegalStateException(
                        "找不到啟用中的 ASSIGNED 角色異動類型"
                    )
                );

        UserRoleAssignmentLog assignmentLog =
            new UserRoleAssignmentLog(
                targetUser,
                role,
                assignmentType,
                changedByUser,
                note.trim()
            );

        userRoleAssignmentLogRepository.save(
            assignmentLog
        );

        return roleResponseMapper
            .toSummary(role);
    }

    @Transactional
    public void revokeRole(
        Long authenticatedUserId,
        Long targetUserId,
        String roleCode,
        String note
    ) {
        permissionAccessPolicy.requirePermission(
            authenticatedUserId,
            PermissionCodes.ROLE_ASSIGN
        );

        User targetUser = userRepository
            .findByIdAndDeletedDateIsNullForUpdate(
                targetUserId
            )
            .orElseThrow(() ->
                new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "找不到指定的使用者"
                )
            );

        User changedByUser;

        if (authenticatedUserId.equals(targetUserId)) {
            changedByUser = targetUser;
        } else {
            changedByUser = userRepository
                .findByIdAndDeletedDateIsNull(
                    authenticatedUserId
                )
                .orElseThrow(() ->
                    new ResponseStatusException(
                        HttpStatus.FORBIDDEN,
                        "目前登入的使用者帳號無法使用"
                    )
                );
        }

        String normalizedRoleCode = roleCode
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

        /*
        * 只有 SUPER_ADMIN 可以撤銷 SUPER_ADMIN。
        */
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
                "只有最高系統管理員可以撤銷 SUPER_ADMIN 角色"
            );
        }

        UserRole userRole = userRoleRepository
            .findByUserIdAndRoleIdForUpdate(
                targetUserId,
                role.getId()
            )
            .orElseThrow(() ->
                new BusinessException(
                    HttpStatus.NOT_FOUND,
                    "USER_ROLE_NOT_FOUND",
                    "使用者未擁有此角色"
                )
            );

        if (!userRole.isAvailable()) {
            throw new BusinessException(
                HttpStatus.NOT_FOUND,
                "USER_ROLE_NOT_FOUND",
                "使用者未擁有此角色"
            );
        }

        /*
        * 系統至少必須保留一位有效的 SUPER_ADMIN。
        */
        if (superAdminRole) {
            long activeSuperAdminCount =
                userRoleRepository
                    .countByRole_IdAndActiveTrueAndDeletedDateIsNull(
                        role.getId()
                    );

            if (activeSuperAdminCount <= 1) {
                throw new BusinessException(
                    HttpStatus.CONFLICT,
                    "LAST_SUPER_ADMIN_CANNOT_BE_REVOKED",
                    "系統至少必須保留一位最高系統管理員"
                );
            }
        }

        userRole.revoke();

        userRoleRepository.save(userRole);

        UserRoleAssignmentType assignmentType =
            userRoleAssignmentTypeRepository
                .findByCodeAndActiveTrueAndDeletedDateIsNull(
                    UserRoleAssignmentTypeCodes.REVOKED
                )
                .orElseThrow(() ->
                    new IllegalStateException(
                        "找不到啟用中的 REVOKED 角色異動類型"
                    )
                );

        UserRoleAssignmentLog assignmentLog =
            new UserRoleAssignmentLog(
                targetUser,
                role,
                assignmentType,
                changedByUser,
                note.trim()
            );

        userRoleAssignmentLogRepository.save(
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
}
