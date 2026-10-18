package com.williamhsieh.financeapp.service.auth;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.williamhsieh.financeapp.dto.auth.CurrentUserResponse;
import com.williamhsieh.financeapp.dto.auth.CurrentUserResponse.CountryInfo;
import com.williamhsieh.financeapp.dto.auth.CurrentUserResponse.LanguageInfo;
import com.williamhsieh.financeapp.dto.auth.CurrentUserResponse.TimezoneInfo;
import com.williamhsieh.financeapp.entity.region.Country;
import com.williamhsieh.financeapp.entity.region.Language;
import com.williamhsieh.financeapp.entity.region.Timezone;
import com.williamhsieh.financeapp.entity.user.User;
import com.williamhsieh.financeapp.repository.user.UserRepository;
import com.williamhsieh.financeapp.constant.user.UserAccountStatusCodes;
import com.williamhsieh.financeapp.dto.user.UserAccountStatusResponse;
import com.williamhsieh.financeapp.entity.user.UserAccountStatus;

@Service
public class CurrentUserService {

    private final UserRepository userRepository;

    public CurrentUserService(
        UserRepository userRepository
    ) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public CurrentUserResponse getCurrentUser(
        String subject,
        List<String> roles,
        List<String> permissions
    ) {
        Long userId = parseUserId(subject);

        User user = userRepository
            .findByIdAndDeletedDateIsNull(userId)
            .orElseThrow(() ->
                new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Access Token 對應的使用者不存在"
                )
            );

        String accountStatusCode =
            user.getAccountStatus().getCode();

        boolean accountAvailable =
            user.isActive() &&
            user.getDeletedDate() == null &&
            UserAccountStatusCodes.ACTIVE.equals(
                accountStatusCode
            );

        if (!accountAvailable) {
            throw new ResponseStatusException(
                HttpStatus.FORBIDDEN,
                accountUnavailableMessage(
                    accountStatusCode
                )
            );
        }

        return toResponse(
            user,
            roles,
            permissions
        );
    }

    private String accountUnavailableMessage(
        String accountStatusCode
    ) {
        if (
            UserAccountStatusCodes
                .PENDING_EMAIL_VERIFICATION
                .equals(accountStatusCode)
        ) {
            return "請先完成 Email 驗證";
        }

        if (
            UserAccountStatusCodes.SUSPENDED.equals(
                accountStatusCode
            )
        ) {
            return "此帳號已被停權";
        }

        if (
            UserAccountStatusCodes.LOCKED.equals(
                accountStatusCode
            )
        ) {
            return "此帳號已被鎖定";
        }

        if (
            UserAccountStatusCodes.DELETED.equals(
                accountStatusCode
            )
        ) {
            return "此帳號已被刪除";
        }

        return "此帳號目前無法使用";
    }

    private Long parseUserId(String subject) {
        if (subject == null || subject.isBlank()) {
            throw invalidSubject();
        }

        try {
            return Long.valueOf(subject);
        } catch (NumberFormatException exception) {
            throw invalidSubject();
        }
    }

    private ResponseStatusException invalidSubject() {
        return new ResponseStatusException(
            HttpStatus.UNAUTHORIZED,
            "Access Token 的使用者資訊無效"
        );
    }

    private CurrentUserResponse toResponse(
        User user,
        List<String> roles,
        List<String> permissions
    ) {
        return new CurrentUserResponse(
            user.getId(),
            user.getUserNumber(),
            user.getName(),
            user.getNickname(),
            user.getEmail(),
            user.getBirthday(),
            user.getPhone(),
            user.isEmailVerified(),
            user.isSmsVerified(),
            user.isActive(),
            toAccountStatusResponse(
                user.getAccountStatus()
            ),
            List.copyOf(roles),
            List.copyOf(permissions),
            toCountryInfo(user.getCountry()),
            toTimezoneInfo(user.getTimezone()),
            toLanguageInfo(user.getLanguage()),
            user.getCreatedDate(),
            user.getUpdatedDate()
        );
    }

    private UserAccountStatusResponse
        toAccountStatusResponse(
            UserAccountStatus accountStatus
        ) {
            return new UserAccountStatusResponse(
                accountStatus.getId(),
                accountStatus.getCode(),
                accountStatus.getName()
            );
        }

    private CountryInfo toCountryInfo(Country country) {
        if (country == null) {
            return null;
        }

        return new CountryInfo(
            country.getId(),
            country.getIso2(),
            country.getIso3(),
            country.getName(),
            country.getNativeName(),
            country.getPhoneCode()
        );
    }

    private TimezoneInfo toTimezoneInfo(
        Timezone timezone
    ) {
        if (timezone == null) {
            return null;
        }

        return new TimezoneInfo(
            timezone.getId(),
            timezone.getCode(),
            timezone.getIanaName(),
            timezone.getName(),
            timezone.getUtcOffset(),
            timezone.getHasDst()
        );
    }

    private LanguageInfo toLanguageInfo(
        Language language
    ) {
        if (language == null) {
            return null;
        }

        return new LanguageInfo(
            language.getId(),
            language.getCode(),
            language.getName()
        );
    }
}
