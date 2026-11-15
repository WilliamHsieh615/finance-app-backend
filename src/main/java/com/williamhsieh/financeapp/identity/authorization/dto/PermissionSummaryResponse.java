package com.williamhsieh.financeapp.identity.authorization.dto;

public record PermissionSummaryResponse(

    Long id,

    PermissionTypeInfo permissionType,

    String code,

    String name,

    String note

) {

    public record PermissionTypeInfo(
        Long id,
        String code,
        String name
    ) {
    }
}
