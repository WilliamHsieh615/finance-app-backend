package com.williamhsieh.financeapp.identity.user.dto;

import java.time.LocalDateTime;

public record UserAccountStatusLogResponse(
    Long id,
    UserAccountStatusResponse previousStatus,
    UserAccountStatusResponse newStatus,
    ChangedByUserInfo changedByUser,
    String note,
    LocalDateTime changedDate
) {

    public record ChangedByUserInfo(
        Long id,
        String userNumber,
        String name,
        String nickname
    ) {
    }
}
