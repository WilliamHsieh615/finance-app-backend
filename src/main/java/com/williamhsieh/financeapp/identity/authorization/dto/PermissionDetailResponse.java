package com.williamhsieh.financeapp.identity.authorization.dto;

import java.time.LocalDateTime;

public record PermissionDetailResponse(

    Long id,

    PermissionTypeInfo permissionType,

    String code,

    String name,

    String note,

    boolean active,

    LocalDateTime createdDate,

    LocalDateTime updatedDate,

    LocalDateTime deletedDate

) {

    public record PermissionTypeInfo(
        Long id,
        String code,
        String name
    ) {
    }
}
