package com.williamhsieh.financeapp
    .identity.authorization.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.williamhsieh.financeapp.identity.authorization.entity.Permission;
import com.williamhsieh.financeapp.identity.authorization.entity.PermissionChangeLog;
import com.williamhsieh.financeapp.identity.authorization.entity.PermissionChangeType;
import com.williamhsieh.financeapp.identity.authorization.entity.PermissionType;
import com.williamhsieh.financeapp.identity.authorization.repository.PermissionChangeLogRepository;
import com.williamhsieh.financeapp.identity.authorization.repository.PermissionChangeTypeRepository;
import com.williamhsieh.financeapp.identity.user.entity.User;
import com.williamhsieh.financeapp.identity.user.repository.UserRepository;

@Service
public class PermissionChangeAuditService {

    private final PermissionChangeTypeRepository
        changeTypeRepository;

    private final PermissionChangeLogRepository
        changeLogRepository;

    private final UserRepository userRepository;

    public PermissionChangeAuditService(
        PermissionChangeTypeRepository
            changeTypeRepository,
        PermissionChangeLogRepository
            changeLogRepository,
        UserRepository userRepository
    ) {
        this.changeTypeRepository =
            changeTypeRepository;

        this.changeLogRepository =
            changeLogRepository;

        this.userRepository =
            userRepository;
    }

    public PermissionState capture(
        Permission permission
    ) {
        return new PermissionState(
            permission.getPermissionType(),
            permission.getName(),
            permission.getNote(),
            permission.isActive(),
            permission.getDeletedDate()
        );
    }

    public void recordCreated(
        Permission permission,
        Long changedByUserId,
        String changeTypeCode
    ) {
        recordChange(
            permission,
            changedByUserId,
            changeTypeCode,
            null,
            capture(permission)
        );
    }

    public void recordChanged(
        Permission permission,
        Long changedByUserId,
        String changeTypeCode,
        PermissionState previousState
    ) {
        recordChange(
            permission,
            changedByUserId,
            changeTypeCode,
            previousState,
            capture(permission)
        );
    }

    private void recordChange(
        Permission permission,
        Long changedByUserId,
        String changeTypeCode,
        PermissionState previousState,
        PermissionState newState
    ) {
        PermissionChangeType changeType =
            changeTypeRepository
                .findByCodeAndActiveTrueAndDeletedDateIsNull(
                    changeTypeCode
                )
                .orElseThrow(() ->
                    new IllegalStateException(
                        "找不到啟用中的權限異動類型：" +
                            changeTypeCode
                    )
                );

        User changedByUser = null;

        if (changedByUserId != null) {
            changedByUser =
                userRepository
                    .findByIdAndDeletedDateIsNull(
                        changedByUserId
                    )
                    .orElseThrow(() ->
                        new IllegalStateException(
                            "找不到執行權限異動的使用者"
                        )
                    );
        }

        PermissionChangeLog changeLog =
            new PermissionChangeLog(
                permission,
                changeType,
                changedByUser,

                previousState == null
                    ? null
                    : previousState.permissionType(),

                newState.permissionType(),

                previousState == null
                    ? null
                    : previousState.name(),

                newState.name(),

                previousState == null
                    ? null
                    : previousState.note(),

                newState.note(),

                previousState == null
                    ? null
                    : previousState.active(),

                newState.active(),

                previousState == null
                    ? null
                    : previousState.deletedDate(),

                newState.deletedDate()
            );

        changeLogRepository.save(changeLog);
    }

    public record PermissionState(
        PermissionType permissionType,
        String name,
        String note,
        Boolean active,
        LocalDateTime deletedDate
    ) {
    }
}