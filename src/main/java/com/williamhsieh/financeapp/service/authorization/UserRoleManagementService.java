package com.williamhsieh.financeapp.service.authorization;

import java.util.List;
import java.util.Locale;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.williamhsieh.financeapp.constant.auth.PermissionCodes;
import com.williamhsieh.financeapp.dto.authorization.RoleSummaryResponse;
import com.williamhsieh.financeapp.entity.auth.Role;
import com.williamhsieh.financeapp.repository.auth.UserRoleRepository;
import com.williamhsieh.financeapp.repository.user.UserRepository;
import com.williamhsieh.financeapp.constant.auth.RoleCodes;
import com.williamhsieh.financeapp.constant.auth.UserRoleAssignmentTypeCodes;
import com.williamhsieh.financeapp.entity.auth.UserRole;
import com.williamhsieh.financeapp.entity.auth.UserRoleAssignmentLog;
import com.williamhsieh.financeapp.entity.auth.UserRoleAssignmentType;
import com.williamhsieh.financeapp.entity.user.User;
import com.williamhsieh.financeapp.exception.BusinessException;
import com.williamhsieh.financeapp.repository.auth.RoleRepository;
import com.williamhsieh.financeapp.repository.auth.UserRoleAssignmentLogRepository;
import com.williamhsieh.financeapp.repository.auth.UserRoleAssignmentTypeRepository;
import com.williamhsieh.financeapp.dto.authorization.UserRoleAssignmentLogResponse;
import com.williamhsieh.financeapp.dto.authorization.UserRoleAssignmentLogResponse.AssignmentTypeInfo;
import com.williamhsieh.financeapp.dto.authorization.UserRoleAssignmentLogResponse.ChangedByUserInfo;
import com.williamhsieh.financeapp.dto.common.PageResponse;

@Service
public class UserRoleManagementService {

    private final UserRepository userRepository;

    private final UserRoleRepository
        userRoleRepository;

    private final PermissionAccessPolicy
        permissionAccessPolicy;

    private final RoleRepository roleRepository;

    private final UserRoleAssignmentTypeRepository
        userRoleAssignmentTypeRepository;

    private final UserRoleAssignmentLogRepository
        userRoleAssignmentLogRepository;

    public UserRoleManagementService(
        UserRepository userRepository,
        UserRoleRepository userRoleRepository,
        RoleRepository roleRepository,
        UserRoleAssignmentTypeRepository
            userRoleAssignmentTypeRepository,
        UserRoleAssignmentLogRepository
            userRoleAssignmentLogRepository,
        PermissionAccessPolicy permissionAccessPolicy
    ) {
        this.userRepository =
            userRepository;

        this.userRoleRepository =
            userRoleRepository;

        this.roleRepository =
            roleRepository;

        this.userRoleAssignmentTypeRepository =
            userRoleAssignmentTypeRepository;

        this.userRoleAssignmentLogRepository =
            userRoleAssignmentLogRepository;

        this.permissionAccessPolicy =
            permissionAccessPolicy;
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
                .map(this::toResponse)
                .toList();

        return List.copyOf(roles);
    }

    @Transactional(readOnly = true)
    public PageResponse<UserRoleAssignmentLogResponse>
        getUserRoleAssignmentHistory(
            Long authenticatedUserId,
            Long targetUserId,
            int page,
            int size
        ) {
            permissionAccessPolicy.requirePermission(
                authenticatedUserId,
                PermissionCodes.ROLE_READ
            );

            validatePagination(page, size);

            /*
            * 目前角色管理只查詢未刪除會員。
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

            PageRequest pageRequest =
                PageRequest.of(
                    page,
                    size
                );

            Page<UserRoleAssignmentLog> logs =
                userRoleAssignmentLogRepository
                    .findAllByUser_IdOrderByChangedDateDescIdDesc(
                        targetUserId,
                        pageRequest
                    );

            Page<UserRoleAssignmentLogResponse> result =
                logs.map(
                    this::toAssignmentLogResponse
                );

            return PageResponse.from(result);
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

        return toResponse(role);
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

    private UserRoleAssignmentLogResponse
        toAssignmentLogResponse(
            UserRoleAssignmentLog assignmentLog
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

            UserRoleAssignmentType assignmentType =
                assignmentLog.getAssignmentType();

            AssignmentTypeInfo assignmentTypeInfo =
                new AssignmentTypeInfo(
                    assignmentType.getId(),
                    assignmentType.getCode(),
                    assignmentType.getName()
                );

            return new UserRoleAssignmentLogResponse(
                assignmentLog.getId(),
                toResponse(
                    assignmentLog.getRole()
                ),
                assignmentTypeInfo,
                changedByUserInfo,
                assignmentLog.getNote(),
                assignmentLog.getChangedDate()
            );
    }
}
