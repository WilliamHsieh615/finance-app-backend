package com.williamhsieh.financeapp.identity.authorization.dto;

import java.time.LocalDateTime;

public record RoleChangeLogResponse(

    Long id,

    ChangeTypeInfo changeType,

    ChangedByUserInfo changedByUser,

    RoleStateInfo previousState,

    RoleStateInfo newState,

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

    public record RoleStateInfo(
        String name,
        String note,
        Boolean active,
        LocalDateTime deletedDate
    ) {
    }
}
