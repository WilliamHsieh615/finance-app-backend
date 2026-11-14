package com.williamhsieh.financeapp.identity.authentication.service;

public record RefreshTokenResult(
    Long id,
    String token,
    long expiresIn
) {
}
