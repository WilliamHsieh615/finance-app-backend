package com.williamhsieh.financeapp.service.user;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.williamhsieh.financeapp.dto.user.UserDetailResponse;
import com.williamhsieh.financeapp.dto.user.UserDetailResponse.CountryInfo;
import com.williamhsieh.financeapp.dto.user.UserDetailResponse.LanguageInfo;
import com.williamhsieh.financeapp.dto.user.UserDetailResponse.TimezoneInfo;
import com.williamhsieh.financeapp.entity.region.Country;
import com.williamhsieh.financeapp.entity.region.Language;
import com.williamhsieh.financeapp.entity.region.Timezone;
import com.williamhsieh.financeapp.entity.user.User;
import com.williamhsieh.financeapp.repository.user.UserRepository;
import com.williamhsieh.financeapp.service.auth.UserAuthorities;
import com.williamhsieh.financeapp.service.auth.UserAuthorityService;
import com.williamhsieh.financeapp.dto.common.PageResponse;
import com.williamhsieh.financeapp.dto.user.UserSummaryResponse;
import com.williamhsieh.financeapp.dto.user.UserAccountStatusResponse;
import com.williamhsieh.financeapp.entity.user.UserAccountStatus;

@Service
public class UserManagementService {

    private final UserRepository userRepository;

    private final UserAuthorityService
        userAuthorityService;

    public UserManagementService(
        UserRepository userRepository,
        UserAuthorityService userAuthorityService
    ) {
        this.userRepository = userRepository;
        this.userAuthorityService =
            userAuthorityService;
    }

    @Transactional(readOnly = true)
    public UserDetailResponse getUserById(
        Long userId
    ) {
        User user = userRepository
            .findByIdAndDeletedDateIsNull(userId)
            .orElseThrow(() ->
                new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "找不到指定的使用者"
                )
            );

        UserAuthorities authorities =
            userAuthorityService
                .getUserAuthorities(userId);

        return toResponse(
            user,
            authorities
        );
    }

    @Transactional(readOnly = true)
    public PageResponse<UserSummaryResponse> getUsers(
        int page,
        int size
    ) {
        return getUsers(
            page,
            size,
            null
        );
    }

    @Transactional(readOnly = true)
    public PageResponse<UserSummaryResponse> getUsers(
        int page,
        int size,
        String keyword
    ) {
        validatePagination(page, size);

        String normalizedKeyword =
            normalizeKeyword(keyword);

        PageRequest pageRequest =
            PageRequest.of(
                page,
                size
            );

        Page<User> users;

        if (normalizedKeyword == null) {
            users = userRepository
                .findAllByDeletedDateIsNull(
                    pageRequest
                );
        } else {
            String escapedKeyword =
                escapeLikePattern(
                    normalizedKeyword
                );
            users = userRepository
                .searchByKeyword(
                    escapedKeyword,
                    pageRequest
                );
        }

        Page<UserSummaryResponse> result =
            users.map(this::toSummaryResponse);

        return PageResponse.from(result);
    }

    private UserDetailResponse toResponse(
        User user,
        UserAuthorities authorities
    ) {
        return new UserDetailResponse(
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
            List.copyOf(authorities.roles()),
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

    private CountryInfo toCountryInfo(
        Country country
    ) {
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

    private void validatePagination(
        int page,
        int size
    ) {
        if (page < 0) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "page 不得小於 0"
            );
        }

        if (size < 1 || size > 100) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "size 必須介於 1 到 100"
            );
        }
    }

    private UserSummaryResponse toSummaryResponse(
        User user
    ) {
        return new UserSummaryResponse(
            user.getId(),
            user.getUserNumber(),
            user.getName(),
            user.getNickname(),
            user.getEmail(),
            user.isEmailVerified(),
            user.isSmsVerified(),
            user.isActive(),
            toAccountStatusResponse(
                user.getAccountStatus()
            ),
            user.getCreatedDate(),
            user.getUpdatedDate()
        );
    }

    private String normalizeKeyword(
        String keyword
    ) {
        if (keyword == null) {
            return null;
        }

        String normalizedKeyword =
            keyword.trim();

        if (normalizedKeyword.isEmpty()) {
            return null;
        }

        if (normalizedKeyword.length() > 100) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "keyword 長度不得超過 100"
            );
        }

        return normalizedKeyword;
    }

    private String escapeLikePattern(
        String keyword
    ) {
        return keyword
            /*
             * 必須先處理跳脫字元本身。
             */
            .replace("!", "!!")
            .replace("%", "!%")
            .replace("_", "!_");
    }
}
