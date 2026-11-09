package com.williamhsieh.financeapp.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DeleteUserRequest(

    @NotBlank(message = "刪除會員時必須填寫原因")
    @Size(
        max = 255,
        message = "刪除原因長度不得超過 255"
    )
    String reason

) {
}
