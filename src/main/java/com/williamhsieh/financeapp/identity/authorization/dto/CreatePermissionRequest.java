package com.williamhsieh.financeapp
    .identity.authorization.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreatePermissionRequest(

    @NotBlank(
        message = "權限類型代碼不得為空"
    )
    @Size(
        max = 50,
        message = "權限類型代碼長度不得超過 50"
    )
    @Pattern(
        regexp = "^[A-Za-z][A-Za-z0-9_]*$",
        message = "權限類型代碼格式不正確"
    )
    String permissionTypeCode,

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
    String code,

    @NotBlank(
        message = "權限名稱不得為空"
    )
    @Size(
        max = 100,
        message = "權限名稱長度不得超過 100"
    )
    String name,

    @Size(
        max = 255,
        message = "權限備註長度不得超過 255"
    )
    String note

) {
}
