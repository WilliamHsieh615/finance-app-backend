package com.williamhsieh.financeapp.identity.authentication.dto;

import jakarta.validation.constraints.NotBlank;

public record LogoutRequest(

    @NotBlank(message = "Refresh Token 不得為空")
    String refreshToken

) {
}
