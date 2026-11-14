package com.williamhsieh.financeapp.identity.authentication.dto;

public record TokenRefreshResponse(
    String accessToken,
    String refreshToken,
    String tokenType,
    long expiresIn,
    long refreshTokenExpiresIn
) {
}
