package com.williamhsieh.financeapp.identity.registration.dto;

public record RegisterUserResponse(
    Long id,
    String userNumber,
    String email,
    boolean emailVerified,
    boolean smsVerified,
    boolean active,
    String message
) {
}
