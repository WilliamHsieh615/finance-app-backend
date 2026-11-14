package com.williamhsieh.financeapp.identity.authentication.dto;

public record LoginResponse(
    String accessToken,
    String refreshToken,
    String tokenType,
    long expiresIn,
    long refreshTokenExpiresIn,
    LoginUserResponse user
) {
}
