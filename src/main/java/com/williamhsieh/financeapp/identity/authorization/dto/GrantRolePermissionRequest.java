package com.williamhsieh.financeapp.identity.authorization.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record GrantRolePermissionRequest(

    @NotBlank(
        message = "權限代碼不得為空"
    )
    @Size(
        max = 100,
        message = "權限代碼長度不得超過 100"
    )
    @Pattern(
        regexp = "^[A-Za-z][A-Za-z0-9_]*$",
        message = "權限代碼格式不正確"
    )
    String permissionCode,

    @NotBlank(
        message = "授予權限時必須填寫備註"
    )
    @Size(
        max = 255,
        message = "授予備註長度不得超過 255"
    )
    String note

) {
}
