package com.williamhsieh.financeapp.dto.authorization;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RevokeUserRoleRequest(

    @NotBlank(
        message = "撤銷角色時必須填寫備註"
    )
    @Size(
        max = 255,
        message = "撤銷備註長度不得超過 255"
    )
    String note

) {
}
