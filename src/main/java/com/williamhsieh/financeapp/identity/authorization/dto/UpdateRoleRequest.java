package com.williamhsieh.financeapp.identity.authorization.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateRoleRequest(

    @NotBlank(
        message = "角色名稱不得為空"
    )
    @Size(
        max = 100,
        message = "角色名稱長度不得超過 100"
    )
    String name,

    @Size(
        max = 255,
        message = "角色備註長度不得超過 255"
    )
    String note

) {
}
