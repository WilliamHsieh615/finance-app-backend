package com.williamhsieh.financeapp.identity.authorization.service;

import java.time.LocalDateTime;
import java.util.Locale;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.williamhsieh.financeapp.identity.authorization.constant.PermissionCodes;
import com.williamhsieh.financeapp.identity.authorization.dto.RoleChangeLogResponse;
import com.williamhsieh.financeapp.identity.authorization.dto.RoleChangeLogResponse.ChangeTypeInfo;
import com.williamhsieh.financeapp.identity.authorization.dto.RoleChangeLogResponse.ChangedByUserInfo;
import com.williamhsieh.financeapp.identity.authorization.dto.RoleChangeLogResponse.RoleStateInfo;
import com.williamhsieh.financeapp.identity.authorization.entity.Role;
import com.williamhsieh.financeapp.identity.authorization.entity.RoleChangeLog;
import com.williamhsieh.financeapp.identity.authorization.entity.RoleChangeType;
import com.williamhsieh.financeapp.identity.authorization.repository.RoleChangeLogRepository;
import com.williamhsieh.financeapp.identity.authorization.repository.RoleRepository;
import com.williamhsieh.financeapp.identity.user.entity.User;
import com.williamhsieh.financeapp.shared.dto.PageResponse;

@Service
public class RoleChangeHistoryService {

    private final RoleRepository roleRepository;
    private final RoleChangeLogRepository roleChangeLogRepository;
    private final PermissionAccessPolicy permissionAccessPolicy;

    public RoleChangeHistoryService(
        RoleRepository roleRepository,
        RoleChangeLogRepository roleChangeLogRepository,
        PermissionAccessPolicy permissionAccessPolicy
    ) {
        this.roleRepository = roleRepository;
        this.roleChangeLogRepository = roleChangeLogRepository;
        this.permissionAccessPolicy = permissionAccessPolicy;
    }

    @Transactional(readOnly = true)
    public PageResponse<RoleChangeLogResponse>
        getRoleChangeHistory(
            Long authenticatedUserId,
            String roleCode,
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

        String normalizedRoleCode =
            roleCode
                .trim()
                .toUpperCase(Locale.ROOT);

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

        PageRequest pageRequest =
            PageRequest.of(
                page,
                size
            );

        Page<RoleChangeLog> logs =
            roleChangeLogRepository
                .findAllByRole_IdOrderByChangedDateDescIdDesc(
                    role.getId(),
                    pageRequest
                );

        Page<RoleChangeLogResponse> result =
            logs.map(
                this::toRoleChangeLogResponse
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

    private RoleChangeLogResponse
        toRoleChangeLogResponse(
            RoleChangeLog changeLog
        ) {

        RoleChangeType changeType =
            changeLog.getChangeType();

        ChangeTypeInfo changeTypeInfo =
            new ChangeTypeInfo(
                changeType.getId(),
                changeType.getCode(),
                changeType.getName()
            );

        User changedByUser =
            changeLog.getChangedByUser();

        ChangedByUserInfo changedByUserInfo =
            changedByUser == null
                ? null
                : new ChangedByUserInfo(
                    changedByUser.getId(),
                    changedByUser.getUserNumber(),
                    changedByUser.getName(),
                    changedByUser.getNickname()
                );

        RoleStateInfo previousState =
            toRoleStateInfo(
                changeLog.getPreviousName(),
                changeLog.getPreviousNote(),
                changeLog.getPreviousActive(),
                changeLog.getPreviousDeletedDate()
            );

        RoleStateInfo newState =
            toRoleStateInfo(
                changeLog.getNewName(),
                changeLog.getNewNote(),
                changeLog.getNewActive(),
                changeLog.getNewDeletedDate()
            );

        return new RoleChangeLogResponse(
            changeLog.getId(),
            changeTypeInfo,
            changedByUserInfo,
            previousState,
            newState,
            changeLog.getChangedDate()
        );
    }

    private RoleStateInfo toRoleStateInfo(
        String name,
        String note,
        Boolean active,
        LocalDateTime deletedDate
    ) {
        if (
            name == null &&
            note == null &&
            active == null &&
            deletedDate == null
        ) {
            return null;
        }

        return new RoleStateInfo(
            name,
            note,
            active,
            deletedDate
        );
    }
}
