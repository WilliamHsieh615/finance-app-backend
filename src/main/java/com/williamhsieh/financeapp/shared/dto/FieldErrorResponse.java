package com.williamhsieh.financeapp.shared.dto;

public record FieldErrorResponse(
    String field,
    String message
) {
}
