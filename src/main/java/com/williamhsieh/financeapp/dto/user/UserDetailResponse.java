package com.williamhsieh.financeapp.dto.user;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record UserDetailResponse(
    Long id,
    String userNumber,
    String name,
    String nickname,
    String email,
    LocalDate birthday,
    String phone,
    boolean emailVerified,
    boolean smsVerified,
    boolean active,
    UserAccountStatusResponse accountStatus,
    List<String> roles,
    CountryInfo country,
    TimezoneInfo timezone,
    LanguageInfo language,
    LocalDateTime createdDate,
    LocalDateTime updatedDate
) {

    public UserDetailResponse {
        roles = List.copyOf(roles);
    }

    public record CountryInfo(
        Long id,
        String iso2,
        String iso3,
        String name,
        String nativeName,
        String phoneCode
    ) {
    }

    public record TimezoneInfo(
        Long id,
        String code,
        String ianaName,
        String name,
        Integer utcOffset,
        Boolean hasDst
    ) {
    }

    public record LanguageInfo(
        Long id,
        String code,
        String name
    ) {
    }
}
