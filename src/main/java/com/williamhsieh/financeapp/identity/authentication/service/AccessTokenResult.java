package com.williamhsieh.financeapp.identity.authentication.service;

public record AccessTokenResult(
    String token,
    long expiresIn
) {
}
