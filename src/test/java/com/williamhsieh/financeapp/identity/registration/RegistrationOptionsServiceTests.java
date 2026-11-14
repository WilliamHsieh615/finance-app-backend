package com.williamhsieh.financeapp.identity.registration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.williamhsieh.financeapp.identity.registration.dto.RegistrationOptionsResponse;
import com.williamhsieh.financeapp.identity.registration.service.RegistrationOptionsService;
import com.williamhsieh.financeapp.reference.geography.repository.CountryLanguageRepository;
import com.williamhsieh.financeapp.reference.geography.repository.CountryRepository;
import com.williamhsieh.financeapp.reference.geography.repository.CountryTimezoneRepository;
import com.williamhsieh.financeapp.reference.geography.repository.LanguageRepository;

@ExtendWith(MockitoExtension.class)
class RegistrationOptionsServiceTests {

    @Mock
    private CountryRepository countryRepository;

    @Mock
    private CountryTimezoneRepository countryTimezoneRepository;

    @Mock
    private CountryLanguageRepository countryLanguageRepository;

    @Mock
    private LanguageRepository languageRepository;

    private RegistrationOptionsService service;

    @BeforeEach
    void setUp() {
        service = new RegistrationOptionsService(
            countryRepository,
            countryTimezoneRepository,
            countryLanguageRepository,
            languageRepository
        );
    }

    @Test
    void returnsEmptyListsWhenReferenceDataIsEmpty() {
        when(countryTimezoneRepository.findAllAvailableWithCountryAndTimezone())
            .thenReturn(List.of());
        when(countryLanguageRepository.findAllAvailableDefaults())
            .thenReturn(List.of());
        when(countryRepository.findAllByDeletedDateIsNullOrderByNameAsc())
            .thenReturn(List.of());
        when(languageRepository.findAllByActiveTrueAndDeletedDateIsNullOrderByNameAsc())
            .thenReturn(List.of());

        RegistrationOptionsResponse response = service.getOptions();

        assertThat(response.countries()).isEmpty();
        assertThat(response.languages()).isEmpty();
    }
}
