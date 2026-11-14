package com.williamhsieh.financeapp.identity.registration.dto;

public record TimezoneOptionResponse(
    Long id,
    String code,
    String ianaName,
    String name,
    Integer utcOffset,
    Boolean hasDst,
    Boolean defaultTimezone
) {
}
