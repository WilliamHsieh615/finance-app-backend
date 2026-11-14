package com.williamhsieh.financeapp.identity.verification.dto;

public record VerificationResponse(
    boolean verified,
    boolean active,
    String message
) {
}
