package com.williamhsieh.financeapp.identity.authentication.dto;

public record LoginUserResponse(
    Long id,
    String userNumber,
    String name,
    String nickname,
    String email
) {
}
