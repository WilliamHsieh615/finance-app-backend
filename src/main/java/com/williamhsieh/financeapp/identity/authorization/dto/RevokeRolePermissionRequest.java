package com.williamhsieh.financeapp.identity.authorization.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RevokeRolePermissionRequest(

    @NotBlank(
        message = "撤銷權限時必須填寫備註"
    )
    @Size(
        max = 255,
        message = "撤銷備註長度不得超過 255"
    )
    String note

) {
}
