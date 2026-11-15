package com.williamhsieh.financeapp
    .identity.authorization.dto;

import java.time.LocalDateTime;

public record RolePermissionAssignmentLogResponse(

    Long id,

    PermissionSummaryResponse permission,

    AssignmentTypeInfo assignmentType,

    ChangedByUserInfo changedByUser,

    String note,

    LocalDateTime changedDate

) {

    public record AssignmentTypeInfo(
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
}
