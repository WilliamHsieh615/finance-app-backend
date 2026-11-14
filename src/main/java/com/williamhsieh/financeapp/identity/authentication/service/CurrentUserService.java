package com.williamhsieh.financeapp.identity.authentication.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.williamhsieh.financeapp.identity.authentication.dto.CurrentUserResponse;
import com.williamhsieh.financeapp.identity.authentication.dto.CurrentUserResponse.CountryInfo;
import com.williamhsieh.financeapp.identity.authentication.dto.CurrentUserResponse.LanguageInfo;
import com.williamhsieh.financeapp.identity.authentication.dto.CurrentUserResponse.TimezoneInfo;
import com.williamhsieh.financeapp.identity.user.constant.UserAccountStatusCodes;
import com.williamhsieh.financeapp.identity.user.dto.UserAccountStatusResponse;
import com.williamhsieh.financeapp.identity.user.entity.User;
import com.williamhsieh.financeapp.identity.user.entity.UserAccountStatus;
import com.williamhsieh.financeapp.identity.user.repository.UserRepository;
import com.williamhsieh.financeapp.identity.user.service.UserAccountAccessPolicy;
import com.williamhsieh.financeapp.reference.geography.entity.Country;
import com.williamhsieh.financeapp.reference.geography.entity.Language;
import com.williamhsieh.financeapp.reference.geography.entity.Timezone;

@Service
public class CurrentUserService {

    private final UserRepository userRepository;
    private final UserAccountAccessPolicy
        userAccountAccessPolicy;

    public CurrentUserService(
        UserRepository userRepository,
        UserAccountAccessPolicy userAccountAccessPolicy
    ) {
        this.userRepository = userRepository;
        this.userAccountAccessPolicy =
            userAccountAccessPolicy;
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
            userAccountAccessPolicy
                .isAvailable(user);

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
