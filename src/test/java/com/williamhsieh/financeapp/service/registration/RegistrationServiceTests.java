package com.williamhsieh.financeapp.service.registration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.williamhsieh.financeapp.constant.user.UserAccountStatusCodes;
import com.williamhsieh.financeapp.dto.registration.RegisterUserRequest;
import com.williamhsieh.financeapp.entity.region.Language;
import com.williamhsieh.financeapp.entity.user.User;
import com.williamhsieh.financeapp.entity.user.UserAccountStatus;
import com.williamhsieh.financeapp.entity.user.UserAccountStatusLog;
import com.williamhsieh.financeapp.entity.auth.UserRole;
import com.williamhsieh.financeapp.exception.BusinessException;
import com.williamhsieh.financeapp.repository.auth.RoleRepository;
import com.williamhsieh.financeapp.repository.auth.UserRoleRepository;
import com.williamhsieh.financeapp.repository.region.CountryRepository;
import com.williamhsieh.financeapp.repository.region.CountryTimezoneRepository;
import com.williamhsieh.financeapp.repository.region.LanguageRepository;
import com.williamhsieh.financeapp.repository.region.TimezoneRepository;
import com.williamhsieh.financeapp.repository.user.UserAccountStatusLogRepository;
import com.williamhsieh.financeapp.repository.user.UserAccountStatusRepository;
import com.williamhsieh.financeapp.repository.user.UserRepository;

@ExtendWith(MockitoExtension.class)
class RegistrationServiceTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CountryRepository countryRepository;

    @Mock
    private TimezoneRepository timezoneRepository;

    @Mock
    private LanguageRepository languageRepository;

    @Mock
    private CountryTimezoneRepository
        countryTimezoneRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserRoleRepository userRoleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserAccountStatusRepository
        userAccountStatusRepository;

    @Mock
    private UserAccountStatusLogRepository
        userAccountStatusLogRepository;

    @Mock
    private Language language;

    @Mock
    private UserAccountStatus pendingStatus;

    private RegistrationService registrationService;

    @BeforeEach
    void setUp() {
        registrationService =
            new RegistrationService(
                userRepository,
                countryRepository,
                timezoneRepository,
                languageRepository,
                countryTimezoneRepository,
                roleRepository,
                userRoleRepository,
                passwordEncoder,
                userAccountStatusRepository,
                userAccountStatusLogRepository
            );
    }

    @Test
    void databaseUniqueConstraintViolationReturnsRegistrationConflict() {

        RegisterUserRequest request =
            new RegisterUserRequest(
                null,
                null,
                1L,
                "Concurrent User",
                "Concurrent",
                "race@example.com",
                "Password123!",
                LocalDate.of(1995, 5, 20),
                "+886923456789"
            );

        when(
            userRepository.findByEmailIgnoreCase(
                "race@example.com"
            )
        )
            .thenReturn(Optional.empty());

        when(
            userRepository.existsByPhone(
                "+886923456789"
            )
        )
            .thenReturn(false);

        when(
            languageRepository
                .findByIdAndActiveTrueAndDeletedDateIsNull(
                    1L
                )
        )
            .thenReturn(
                Optional.of(language)
            );

        when(
            userAccountStatusRepository
                .findByCodeAndActiveTrueAndDeletedDateIsNull(
                    UserAccountStatusCodes
                        .PENDING_EMAIL_VERIFICATION
                )
        )
            .thenReturn(
                Optional.of(pendingStatus)
            );

        when(pendingStatus.getCode())
            .thenReturn(
                UserAccountStatusCodes
                    .PENDING_EMAIL_VERIFICATION
            );

        when(
            passwordEncoder.encode(
                "Password123!"
            )
        )
            .thenReturn(
                "encoded-password"
            );

        /*
         * 模擬另一個並行請求已先寫入相同資料，
         * 因此資料庫 UNIQUE 約束拒絕目前 INSERT。
         */
        when(
            userRepository.saveAndFlush(
                any(User.class)
            )
        )
            .thenThrow(
                new DataIntegrityViolationException(
                    "simulated unique constraint violation"
                )
            );

        BusinessException exception =
            assertThrows(
                BusinessException.class,
                () ->
                    registrationService.register(
                        request
                    )
            );

        assertThat(exception.getStatus())
            .isEqualTo(HttpStatus.CONFLICT);

        assertThat(exception.getCode())
            .isEqualTo(
                "REGISTRATION_DATA_CONFLICT"
            );

        assertThat(exception.getMessage())
            .isEqualTo(
                "Email、手機號碼或使用者編號已被使用，請重新確認"
            );

        /*
         * 使用者 INSERT 失敗後，
         * 不得再建立狀態紀錄或預設角色。
         */
        verify(
            userAccountStatusLogRepository,
            never()
        )
            .save(
                any(UserAccountStatusLog.class)
            );

        verify(
            userRoleRepository,
            never()
        )
            .save(
                any(UserRole.class)
            );
    }
}
