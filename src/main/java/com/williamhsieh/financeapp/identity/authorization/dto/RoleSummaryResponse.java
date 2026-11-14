package com.williamhsieh.financeapp.identity.authorization.dto;

public record RoleSummaryResponse(
    Long id,
    String code,
    String name,
    String note
) {
}
