package com.williamhsieh.financeapp.identity.authorization.dto;

import java.time.LocalDateTime;

public record RoleDetailResponse(

    Long id,

    String code,

    String name,

    String note,

    boolean active,

    LocalDateTime createdDate,

    LocalDateTime updatedDate,

    LocalDateTime deletedDate

) {
}
