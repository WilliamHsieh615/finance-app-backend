package com.williamhsieh.financeapp.identity.registration.dto;

import java.util.List;

public record RegistrationOptionsResponse(
    List<CountryOptionResponse> countries,
    List<LanguageOptionResponse> languages
) {
}
