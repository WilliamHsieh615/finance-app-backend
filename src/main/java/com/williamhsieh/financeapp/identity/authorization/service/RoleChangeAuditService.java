package com.williamhsieh.financeapp.identity.authorization.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.williamhsieh.financeapp.identity.authorization.entity.Role;
import com.williamhsieh.financeapp.identity.authorization.entity.RoleChangeLog;
import com.williamhsieh.financeapp.identity.authorization.entity.RoleChangeType;
import com.williamhsieh.financeapp.identity.authorization.repository.RoleChangeLogRepository;
import com.williamhsieh.financeapp.identity.authorization.repository.RoleChangeTypeRepository;
import com.williamhsieh.financeapp.identity.user.entity.User;
import com.williamhsieh.financeapp.identity.user.repository.UserRepository;

@Service
public class RoleChangeAuditService {

    private final RoleChangeTypeRepository
        changeTypeRepository;

    private final RoleChangeLogRepository
        changeLogRepository;

    private final UserRepository userRepository;

    public RoleChangeAuditService(
        RoleChangeTypeRepository changeTypeRepository,
        RoleChangeLogRepository changeLogRepository,
        UserRepository userRepository
    ) {
        this.changeTypeRepository =
            changeTypeRepository;

        this.changeLogRepository =
            changeLogRepository;

        this.userRepository =
            userRepository;
    }

    public RoleState capture(
        Role role
    ) {
        return new RoleState(
            role.getName(),
            role.getNote(),
            role.isActive(),
            role.getDeletedDate()
        );
    }

    public void recordCreated(
        Role role,
        Long changedByUserId,
        String changeTypeCode
    ) {
        recordChange(
            role,
            changedByUserId,
            changeTypeCode,
            null,
            capture(role)
        );
    }

    public void recordChanged(
        Role role,
        Long changedByUserId,
        String changeTypeCode,
        RoleState previousState
    ) {
        recordChange(
            role,
            changedByUserId,
            changeTypeCode,
            previousState,
            capture(role)
        );
    }

    private void recordChange(
        Role role,
        Long changedByUserId,
        String changeTypeCode,
        RoleState previousState,
        RoleState newState
    ) {
        RoleChangeType changeType =
            changeTypeRepository
                .findByCodeAndActiveTrueAndDeletedDateIsNull(
                    changeTypeCode
                )
                .orElseThrow(() ->
                    new IllegalStateException(
                        "找不到啟用中的角色異動類型：" +
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
                            "找不到執行角色異動的使用者"
                        )
                    );
        }

        RoleChangeLog changeLog =
            new RoleChangeLog(
                role,
                changeType,
                changedByUser,

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

    public record RoleState(
        String name,
        String note,
        Boolean active,
        LocalDateTime deletedDate
    ) {
    }
}
