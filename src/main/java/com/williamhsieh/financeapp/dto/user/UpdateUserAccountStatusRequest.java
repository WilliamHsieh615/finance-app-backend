package com.williamhsieh.financeapp.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateUserAccountStatusRequest(

    @NotBlank(message = "會員狀態不得為空")
    @Size(
        max = 50,
        message = "會員狀態長度不得超過 50"
    )
    String status,

    @Size(
        max = 255,
        message = "狀態異動原因長度不得超過 255"
    )
    String note

) {
}