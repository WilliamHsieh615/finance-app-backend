package com.williamhsieh.financeapp.identity.authorization.dto;

public record PermissionTypeSummaryResponse(
    Long id,
    String code,
    String name,
    String note
) {
}