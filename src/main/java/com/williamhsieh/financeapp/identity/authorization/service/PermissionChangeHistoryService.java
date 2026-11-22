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
import com.williamhsieh.financeapp.identity.authorization.dto.PermissionChangeLogResponse;
import com.williamhsieh.financeapp.identity.authorization.dto.PermissionChangeLogResponse.ChangeTypeInfo;
import com.williamhsieh.financeapp.identity.authorization.dto.PermissionChangeLogResponse.ChangedByUserInfo;
import com.williamhsieh.financeapp.identity.authorization.dto.PermissionChangeLogResponse.PermissionStateInfo;
import com.williamhsieh.financeapp.identity.authorization.dto.PermissionChangeLogResponse.PermissionTypeInfo;
import com.williamhsieh.financeapp.identity.authorization.entity.Permission;
import com.williamhsieh.financeapp.identity.authorization.entity.PermissionChangeLog;
import com.williamhsieh.financeapp.identity.authorization.entity.PermissionChangeType;
import com.williamhsieh.financeapp.identity.authorization.entity.PermissionType;
import com.williamhsieh.financeapp.identity.authorization.repository.PermissionChangeLogRepository;
import com.williamhsieh.financeapp.identity.authorization.repository.PermissionRepository;
import com.williamhsieh.financeapp.identity.user.entity.User;
import com.williamhsieh.financeapp.shared.dto.PageResponse;

@Service
public class PermissionChangeHistoryService {

    private final PermissionRepository permissionRepository;
    private final PermissionChangeLogRepository permissionChangeLogRepository;
    private final PermissionAccessPolicy permissionAccessPolicy;

    public PermissionChangeHistoryService(
        PermissionRepository permissionRepository,
        PermissionChangeLogRepository permissionChangeLogRepository,
        PermissionAccessPolicy permissionAccessPolicy
    ) {
        this.permissionRepository = permissionRepository;
        this.permissionChangeLogRepository = permissionChangeLogRepository;
        this.permissionAccessPolicy = permissionAccessPolicy;
    }

    @Transactional(readOnly = true)
    public PageResponse<PermissionChangeLogResponse>
        getPermissionChangeHistory(
            Long authenticatedUserId,
            String permissionCode,
            int page,
            int size
        ) {

        permissionAccessPolicy.requirePermission(
            authenticatedUserId,
            PermissionCodes.PERMISSION_MANAGE
        );

        validatePagination(
            page,
            size
        );

        String normalizedPermissionCode =
            permissionCode
                .trim()
                .toUpperCase(Locale.ROOT);

        /*
         * 已停用 Permission 仍須查詢歷程。
         */
        Permission permission =
            permissionRepository
                .findByCodeIgnoreCase(
                    normalizedPermissionCode
                )
                .orElseThrow(() ->
                    new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "找不到指定的權限"
                    )
                );

        PageRequest pageRequest =
            PageRequest.of(
                page,
                size
            );

        Page<PermissionChangeLog> logs =
            permissionChangeLogRepository
                .findAllByPermission_IdOrderByChangedDateDescIdDesc(
                    permission.getId(),
                    pageRequest
                );

        Page<PermissionChangeLogResponse> result =
            logs.map(
                this::toPermissionChangeLogResponse
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

    private PermissionChangeLogResponse
        toPermissionChangeLogResponse(
            PermissionChangeLog changeLog
        ) {

        PermissionChangeType changeType =
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

        PermissionStateInfo previousState =
            toPermissionStateInfo(
                changeLog.getPreviousPermissionType(),
                changeLog.getPreviousName(),
                changeLog.getPreviousNote(),
                changeLog.getPreviousActive(),
                changeLog.getPreviousDeletedDate()
            );

        PermissionStateInfo newState =
            toPermissionStateInfo(
                changeLog.getNewPermissionType(),
                changeLog.getNewName(),
                changeLog.getNewNote(),
                changeLog.getNewActive(),
                changeLog.getNewDeletedDate()
            );

        return new PermissionChangeLogResponse(
            changeLog.getId(),
            changeTypeInfo,
            changedByUserInfo,
            previousState,
            newState,
            changeLog.getChangedDate()
        );
    }

    private PermissionStateInfo
        toPermissionStateInfo(
            PermissionType permissionType,
            String name,
            String note,
            Boolean active,
            LocalDateTime deletedDate
        ) {

        if (
            permissionType == null &&
            name == null &&
            note == null &&
            active == null &&
            deletedDate == null
        ) {
            return null;
        }

        PermissionTypeInfo permissionTypeInfo =
            permissionType == null
                ? null
                : new PermissionTypeInfo(
                    permissionType.getId(),
                    permissionType.getCode(),
                    permissionType.getName()
                );

        return new PermissionStateInfo(
            permissionTypeInfo,
            name,
            note,
            active,
            deletedDate
        );
    }
}
