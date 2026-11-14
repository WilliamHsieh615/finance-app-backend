package com.williamhsieh.financeapp.identity.authentication.service;

import com.williamhsieh.financeapp.identity.user.entity.User;

public record RefreshTokenRotationResult(
    User user,
    Long oldRefreshTokenId,
    Long newRefreshTokenId,
    String refreshToken,
    long refreshTokenExpiresIn
) {
}
