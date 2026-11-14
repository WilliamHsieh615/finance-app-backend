package com.williamhsieh.financeapp.dto.authorization;

public record RoleSummaryResponse(
    Long id,
    String code,
    String name,
    String note
) {
}
