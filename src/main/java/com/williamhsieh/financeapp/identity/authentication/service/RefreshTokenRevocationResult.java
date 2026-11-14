package com.williamhsieh.financeapp.identity.authentication.service;

public record RefreshTokenRevocationResult(
    Long refreshTokenId,
    Long userId,
    String email,
    boolean found,
    boolean newlyRevoked
) {
}
