package com.williamhsieh.financeapp.identity.authorization.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.williamhsieh.financeapp.identity.authorization.constant.PermissionCodes;
import com.williamhsieh.financeapp.identity.authorization.dto.UserRoleAssignmentLogResponse;
import com.williamhsieh.financeapp.identity.authorization.dto.UserRoleAssignmentLogResponse.AssignmentTypeInfo;
import com.williamhsieh.financeapp.identity.authorization.dto.UserRoleAssignmentLogResponse.ChangedByUserInfo;
import com.williamhsieh.financeapp.identity.authorization.entity.UserRoleAssignmentLog;
import com.williamhsieh.financeapp.identity.authorization.entity.UserRoleAssignmentType;
import com.williamhsieh.financeapp.identity.authorization.mapper.RoleResponseMapper;
import com.williamhsieh.financeapp.identity.authorization.repository.UserRoleAssignmentLogRepository;
import com.williamhsieh.financeapp.identity.user.entity.User;
import com.williamhsieh.financeapp.identity.user.repository.UserRepository;
import com.williamhsieh.financeapp.shared.dto.PageResponse;

@Service
public class UserRoleAssignmentHistoryService {

    private final UserRepository userRepository;
    private final UserRoleAssignmentLogRepository assignmentLogRepository;
    private final PermissionAccessPolicy permissionAccessPolicy;
    private final RoleResponseMapper roleResponseMapper;

    public UserRoleAssignmentHistoryService(
        UserRepository userRepository,
        UserRoleAssignmentLogRepository assignmentLogRepository,
        PermissionAccessPolicy permissionAccessPolicy,
        RoleResponseMapper roleResponseMapper
    ) {
        this.userRepository = userRepository;
        this.assignmentLogRepository = assignmentLogRepository;
        this.permissionAccessPolicy = permissionAccessPolicy;
        this.roleResponseMapper = roleResponseMapper;
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

        validatePagination(
            page,
            size
        );

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
            assignmentLogRepository
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
            roleResponseMapper.toSummary(
                assignmentLog.getRole()
            ),
            assignmentTypeInfo,
            changedByUserInfo,
            assignmentLog.getNote(),
            assignmentLog.getChangedDate()
        );
    }
}
