package com.williamhsieh.financeapp.identity.authorization.service;

import java.util.Locale;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.williamhsieh.financeapp.identity.authorization.constant.PermissionCodes;
import com.williamhsieh.financeapp.identity.authorization.dto.RolePermissionAssignmentLogResponse;
import com.williamhsieh.financeapp.identity.authorization.dto.RolePermissionAssignmentLogResponse.AssignmentTypeInfo;
import com.williamhsieh.financeapp.identity.authorization.dto.RolePermissionAssignmentLogResponse.ChangedByUserInfo;
import com.williamhsieh.financeapp.identity.authorization.entity.Role;
import com.williamhsieh.financeapp.identity.authorization.entity.RolePermissionAssignmentLog;
import com.williamhsieh.financeapp.identity.authorization.entity.RolePermissionAssignmentType;
import com.williamhsieh.financeapp.identity.authorization.mapper.PermissionResponseMapper;
import com.williamhsieh.financeapp.identity.authorization.repository.RolePermissionAssignmentLogRepository;
import com.williamhsieh.financeapp.identity.authorization.repository.RoleRepository;
import com.williamhsieh.financeapp.identity.user.entity.User;
import com.williamhsieh.financeapp.shared.dto.PageResponse;

@Service
public class RolePermissionAssignmentHistoryService {

    private final RoleRepository roleRepository;
    private final RolePermissionAssignmentLogRepository assignmentLogRepository;
    private final PermissionAccessPolicy permissionAccessPolicy;
    private final PermissionResponseMapper permissionResponseMapper;

    public RolePermissionAssignmentHistoryService(
        RoleRepository roleRepository,
        RolePermissionAssignmentLogRepository assignmentLogRepository,
        PermissionAccessPolicy permissionAccessPolicy,
        PermissionResponseMapper permissionResponseMapper
    ) {
        this.roleRepository = roleRepository;
        this.assignmentLogRepository = assignmentLogRepository;
        this.permissionAccessPolicy = permissionAccessPolicy;
        this.permissionResponseMapper = permissionResponseMapper;
    }

    @Transactional(readOnly = true)
    public PageResponse<
        RolePermissionAssignmentLogResponse
    > getRolePermissionAssignmentHistory(
        Long authenticatedUserId,
        String roleCode,
        int page,
        int size
    ) {
        permissionAccessPolicy.requirePermission(
            authenticatedUserId,
            PermissionCodes.PERMISSION_READ
        );

        validatePagination(
            page,
            size
        );

        String normalizedRoleCode =
            roleCode
                .trim()
                .toUpperCase(Locale.ROOT);

        Role role =
            roleRepository
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
            result =
                logs.map(
                    this::toAssignmentLogResponse
                );

        return PageResponse.from(result);
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
