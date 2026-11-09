package com.williamhsieh.financeapp.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RestoreUserRequest(

    @NotBlank(
        message = "恢復會員時必須填寫原因"
    )
    @Size(
        max = 255,
        message = "恢復原因長度不得超過 255"
    )
    String reason

) {
}
