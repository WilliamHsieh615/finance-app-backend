package com.williamhsieh.financeapp.dto.authorization;

import java.time.LocalDateTime;

public record UserRoleAssignmentLogResponse(

    Long id,

    RoleSummaryResponse role,

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
