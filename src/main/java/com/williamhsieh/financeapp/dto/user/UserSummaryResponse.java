package com.williamhsieh.financeapp.dto.user;

import java.time.LocalDateTime;

public record UserSummaryResponse(
    Long id,
    String userNumber,
    String name,
    String nickname,
    String email,
    boolean emailVerified,
    boolean smsVerified,
    boolean active,
    LocalDateTime createdDate,
    LocalDateTime updatedDate
) {
}
