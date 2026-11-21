package com.williamhsieh.financeapp.identity.authorization.dto;

import java.time.LocalDateTime;

public record PermissionChangeLogResponse(

    Long id,

    ChangeTypeInfo changeType,

    ChangedByUserInfo changedByUser,

    PermissionStateInfo previousState,

    PermissionStateInfo newState,

    LocalDateTime changedDate

) {

    public record ChangeTypeInfo(
        Long id,
        String code,
        String name
    ) {
    }

    public record ChangedByUserInfo(
        Long id,
        String userNumber,
        String name,
        String nickname
    ) {
    }

    public record PermissionStateInfo(
        PermissionTypeInfo permissionType,
        String name,
        String note,
        Boolean active,
        LocalDateTime deletedDate
    ) {
    }

    public record PermissionTypeInfo(
        Long id,
        String code,
        String name
    ) {
    }
}