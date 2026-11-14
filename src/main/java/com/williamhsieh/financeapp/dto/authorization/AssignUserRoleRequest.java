package com.williamhsieh.financeapp.dto.authorization;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AssignUserRoleRequest(

    @NotBlank(
        message = "角色代碼不得為空"
    )
    @Size(
        max = 50,
        message = "角色代碼長度不得超過 50"
    )
    @Pattern(
        regexp = "^[A-Za-z][A-Za-z0-9_]*$",
        message = "角色代碼格式不正確"
    )
    String roleCode,

    @NotBlank(
        message = "指派角色時必須填寫備註"
    )
    @Size(
        max = 255,
        message = "指派備註長度不得超過 255"
    )
    String note

) {
}