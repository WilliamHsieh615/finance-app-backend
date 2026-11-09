package com.williamhsieh.financeapp.service.user;

import java.time.LocalDateTime;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

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
import com.williamhsieh.financeapp.repository.user.UserAccountStatusRepository;
import com.williamhsieh.financeapp.dto.user.UpdateUserAccountStatusRequest;
import com.williamhsieh.financeapp.service.auth.RefreshTokenService;
import com.williamhsieh.financeapp.dto.user.UserAccountStatusLogResponse;
import com.williamhsieh.financeapp.dto.user.UserAccountStatusLogResponse.ChangedByUserInfo;
import com.williamhsieh.financeapp.entity.user.UserAccountStatusLog;
import com.williamhsieh.financeapp.repository.user.UserAccountStatusLogRepository;
import com.williamhsieh.financeapp.constant.auth.PermissionCodes;
import com.williamhsieh.financeapp.dto.user.DeleteUserRequest;
import com.williamhsieh.financeapp.constant.user.UserAccountStatusCodes;
import com.williamhsieh.financeapp.dto.user.RestoreUserRequest;

@Service
public class UserManagementService {

    private final UserRepository userRepository;

    private final UserAuthorityService
        userAuthorityService;

    private final UserAccountStatusRepository
        userAccountStatusRepository;

    private final UserAccountStatusService
        userAccountStatusService;

    private final RefreshTokenService
        refreshTokenService;

    private final UserAccountStatusLogRepository
        userAccountStatusLogRepository;

    private final UserAccountAccessPolicy
        userAccountAccessPolicy;

    public UserManagementService(
        UserRepository userRepository,
        UserAuthorityService userAuthorityService,
        UserAccountStatusRepository userAccountStatusRepository,
        UserAccountStatusService userAccountStatusService,
        RefreshTokenService refreshTokenService,
        UserAccountStatusLogRepository userAccountStatusLogRepository,
        UserAccountAccessPolicy userAccountAccessPolicy
    ) {
        this.userRepository = userRepository;
        this.userAuthorityService =
            userAuthorityService;
        this.userAccountStatusRepository =
            userAccountStatusRepository;
        this.userAccountStatusService =
            userAccountStatusService;
        this.refreshTokenService =
            refreshTokenService;
        this.userAccountStatusLogRepository =
            userAccountStatusLogRepository;
        this.userAccountAccessPolicy =
            userAccountAccessPolicy;
    }

    @Transactional(readOnly = true)
    public UserDetailResponse getUserById(
        Long authenticatedUserId,
        Long targetUserId
    ) {
        findActiveAuthorizedUser(
            authenticatedUserId,
            PermissionCodes.USER_READ
        );

        User user = userRepository
            .findByIdAndDeletedDateIsNull(
                targetUserId
            )
            .orElseThrow(() ->
                new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "找不到指定的使用者"
                )
            );

        UserAuthorities authorities =
            userAuthorityService
                .getUserAuthorities(
                    targetUserId
                );

        return toResponse(
            user,
            authorities
        );
    }

    @Transactional(readOnly = true)
    public PageResponse<UserAccountStatusLogResponse>
        getUserAccountStatusHistory(
            Long authenticatedUserId,
            Long targetUserId,
            int page,
            int size
        ) {
            findActiveAuthorizedUser(
                authenticatedUserId,
                PermissionCodes.USER_READ
            );

            validatePagination(page, size);

            userRepository
                .findByIdAndDeletedDateIsNull(
                    targetUserId
                )
                .orElseThrow(() ->
                    new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "找不到指定的使用者"
                    )
                );

            PageRequest pageRequest =
                PageRequest.of(
                    page,
                    size
                );

            Page<UserAccountStatusLog> statusLogs =
                userAccountStatusLogRepository
                    .findAllByUser_IdOrderByChangedDateDescIdDesc(
                        targetUserId,
                        pageRequest
                    );

            Page<UserAccountStatusLogResponse> result =
                statusLogs.map(
                    this::toStatusLogResponse
                );

            return PageResponse.from(result);
        }

    @Transactional(readOnly = true)
    public PageResponse<UserAccountStatusLogResponse>
        getDeletedUserAccountStatusHistory(
            Long authenticatedUserId,
            Long targetUserId,
            int page,
            int size
        ) {
            /*
            * 第一層由 Controller 的 @PreAuthorize 檢查 JWT，
            * 第二層在這裡檢查資料庫目前的有效權限。
            */
            findActiveAuthorizedUser(
                authenticatedUserId,
                PermissionCodes.USER_READ_DELETED
            );

            validatePagination(page, size);

            /*
            * 只允許查詢真正已軟刪除，
            * 且帳號狀態為 DELETED 的會員。
            */
            userRepository
                .findByIdAndDeletedDateIsNotNullAndAccountStatus_Code(
                    targetUserId,
                    UserAccountStatusCodes.DELETED
                )
                .orElseThrow(() ->
                    new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "找不到指定的已刪除使用者"
                    )
                );

            PageRequest pageRequest =
                PageRequest.of(
                    page,
                    size
                );

            Page<UserAccountStatusLog> statusLogs =
                userAccountStatusLogRepository
                    .findAllByUser_IdOrderByChangedDateDescIdDesc(
                        targetUserId,
                        pageRequest
                    );

            Page<UserAccountStatusLogResponse> result =
                statusLogs.map(
                    this::toStatusLogResponse
                );

            return PageResponse.from(result);
        }

    @Transactional
    public UserDetailResponse updateUserAccountStatus(
        Long targetUserId,
        Long changedByUserId,
        UpdateUserAccountStatusRequest request
    ) {
        User changedByUser =
            findActiveAuthorizedUser(
                changedByUserId,
                PermissionCodes.USER_STATUS_UPDATE
            );

        if (
            Objects.equals(
                targetUserId,
                changedByUserId
            )
        ) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "不可變更自己的會員狀態"
            );
        }

        User targetUser =
            userRepository
                .findByIdAndDeletedDateIsNullForUpdate(
                    targetUserId
                )
                .orElseThrow(() ->
                    new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "找不到指定的使用者"
                    )
                );

        userAccountStatusService
            .changeStatusByAuthorizedUser(
                targetUser,
                request.status(),
                changedByUser,
                request.note()
            );

        if (!targetUser.isActive()) {
            refreshTokenService
                .revokeAllActiveTokensForUser(
                    targetUser.getId()
                );
        }

        UserAuthorities authorities =
            userAuthorityService
                .getUserAuthorities(
                    targetUser.getId()
                );

        return toResponse(
            targetUser,
            authorities
        );
    }

    @Transactional
    public void softDeleteUser(
        Long targetUserId,
        Long changedByUserId,
        DeleteUserRequest request
    ) {
        User changedByUser =
            findActiveAuthorizedUser(
                changedByUserId,
                PermissionCodes.USER_DELETE
            );

        if (
            Objects.equals(
                targetUserId,
                changedByUserId
            )
        ) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "不可刪除自己的會員帳號"
            );
        }

        User targetUser =
            userRepository
                .findByIdAndDeletedDateIsNullForUpdate(
                    targetUserId
                )
                .orElseThrow(() ->
                    new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "找不到指定的使用者"
                    )
                );

        userAccountStatusService
            .deleteByAuthorizedUser(
                targetUser,
                changedByUser,
                request.reason()
            );

        targetUser.setDeletedDate(
            LocalDateTime.now()
        );

        refreshTokenService
            .revokeAllActiveTokensForUser(
                targetUser.getId()
            );
    }

    @Transactional
    public UserDetailResponse restoreUser(
        Long targetUserId,
        Long changedByUserId,
        RestoreUserRequest request
    ) {
        User changedByUser =
            findActiveAuthorizedUser(
                changedByUserId,
                PermissionCodes.USER_RESTORE
            );

        /*
        * 正常情況下，登入中的使用者不可能同時是
        * deleted_date 不為 null 的會員。
        * 仍保留這個防禦性檢查。
        */
        if (
            Objects.equals(
                targetUserId,
                changedByUserId
            )
        ) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "不可恢復自己的會員帳號"
            );
        }

        /*
        * 使用悲觀鎖避免兩個管理員同時恢復同一會員。
        */
        User targetUser =
            userRepository
                .findDeletedUserByIdForUpdate(
                    targetUserId,
                    UserAccountStatusCodes.DELETED
                )
                .orElseThrow(() ->
                    new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "找不到指定的已刪除使用者"
                    )
                );

        userAccountStatusService
            .restoreByAuthorizedUser(
                targetUser,
                changedByUser,
                request.reason()
            );

        /*
        * 狀態恢復成功後才清除軟刪除時間。
        * 整個方法在同一個 Transaction 中執行。
        */
        targetUser.setDeletedDate(null);

        /*
        * 不修改任何舊 Refresh Token。
        * 已撤銷 Token 永遠保持撤銷。
        */

        UserAuthorities authorities =
            userAuthorityService
                .getUserAuthorities(
                    targetUser.getId()
                );

        return toResponse(
            targetUser,
            authorities
        );
    }

    @Transactional(readOnly = true)
    public PageResponse<UserSummaryResponse> getUsers(
        Long authenticatedUserId,
        int page,
        int size,
        String keyword,
        String status
    ) {
        findActiveAuthorizedUser(
            authenticatedUserId,
            PermissionCodes.USER_READ
        );

        validatePagination(page, size);

        String normalizedKeyword =
            normalizeKeyword(keyword);

        String normalizedStatus =
            normalizeStatus(status);

        String escapedKeyword =
            normalizedKeyword == null
                ? null
                : escapeLikePattern(
                    normalizedKeyword
                );

        PageRequest pageRequest =
            PageRequest.of(
                page,
                size
            );

        Page<User> users =
            userRepository.searchUsers(
                escapedKeyword,
                normalizedStatus,
                pageRequest
            );

        Page<UserSummaryResponse> result =
            users.map(this::toSummaryResponse);

        return PageResponse.from(result);
    }

    @Transactional(readOnly = true)
    public PageResponse<UserSummaryResponse>
        getDeletedUsers(
            Long authenticatedUserId,
            int page,
            int size,
            String keyword
        ) {
            /*
             * 第二層授權：
             * 不只相信 JWT，還要確認資料庫中的權限仍有效。
             */
            findActiveAuthorizedUser(
                authenticatedUserId,
                PermissionCodes.USER_READ_DELETED
            );

            validatePagination(page, size);

            String normalizedKeyword =
                normalizeKeyword(keyword);

            String escapedKeyword =
                normalizedKeyword == null
                    ? null
                    : escapeLikePattern(
                        normalizedKeyword
                    );

            PageRequest pageRequest =
                PageRequest.of(
                    page,
                    size
                );

            Page<User> users =
                userRepository.searchDeletedUsers(
                    escapedKeyword,
                    pageRequest
                );

            Page<UserSummaryResponse> result =
                users.map(this::toSummaryResponse);

            return PageResponse.from(result);
        }

    @Transactional(readOnly = true)
    public UserDetailResponse getDeletedUserById(
        Long authenticatedUserId,
        Long targetUserId
    ) {
        /*
        * 即時檢查目前登入者是否仍然有效，
        * 且資料庫中仍具有 USER_READ_DELETED。
        */
        findActiveAuthorizedUser(
            authenticatedUserId,
            PermissionCodes.USER_READ_DELETED
        );

        User deletedUser =
            userRepository
                .findByIdAndDeletedDateIsNotNullAndAccountStatus_Code(
                    targetUserId,
                    UserAccountStatusCodes.DELETED
                )
                .orElseThrow(() ->
                    new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "找不到指定的已刪除使用者"
                    )
                );

        UserAuthorities authorities =
            userAuthorityService
                .getUserAuthorities(
                    deletedUser.getId()
                );

        return toResponse(
            deletedUser,
            authorities
        );
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
            user.getUpdatedDate(),
            user.getDeletedDate()
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
            user.getUpdatedDate(),
            user.getDeletedDate()
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

    private String normalizeStatus(
        String status
    ) {
        if (status == null || status.isBlank()) {
            return null;
        }

        String normalizedStatus =
            status.trim().toUpperCase(Locale.ROOT);

        if (normalizedStatus.length() > 50) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "status 長度不得超過 50"
            );
        }

        boolean statusExists =
            userAccountStatusRepository
                .findByCodeAndActiveTrueAndDeletedDateIsNull(
                    normalizedStatus
                )
                .isPresent();

        if (!statusExists) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "無效的會員狀態"
            );
        }

        return normalizedStatus;
    }

    private User findActiveAuthorizedUser(
        Long userId,
        String requiredPermission
    ) {
        User authorizedUser =
            userRepository
                .findByIdAndDeletedDateIsNull(userId)
                .orElseThrow(() ->
                    new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "目前登入的使用者不存在"
                    )
                );

        boolean accountAvailable =
            userAccountAccessPolicy
                .isAvailable(authorizedUser);

        if (!accountAvailable) {
            throw new ResponseStatusException(
                HttpStatus.FORBIDDEN,
                "目前登入的使用者帳號無法使用"
            );
        }

        UserAuthorities currentAuthorities =
            userAuthorityService
                .getUserAuthorities(userId);

        boolean permissionAvailable =
            currentAuthorities
                .permissions()
                .contains(requiredPermission);

        if (!permissionAvailable) {
            throw new ResponseStatusException(
                HttpStatus.FORBIDDEN,
                "你沒有執行此操作的權限"
            );
        }

        return authorizedUser;
    }

    private UserAccountStatusLogResponse
        toStatusLogResponse(
            UserAccountStatusLog statusLog
        ) {
            UserAccountStatusResponse previousStatus =
                statusLog.getPreviousStatus() == null
                    ? null
                    : toAccountStatusResponse(
                        statusLog.getPreviousStatus()
                    );

            ChangedByUserInfo changedByUser =
                toChangedByUserInfo(
                    statusLog.getChangedByUser()
                );

            return new UserAccountStatusLogResponse(
                statusLog.getId(),
                previousStatus,
                toAccountStatusResponse(
                    statusLog.getNewStatus()
                ),
                changedByUser,
                statusLog.getNote(),
                statusLog.getChangedDate()
            );
    }

    private ChangedByUserInfo toChangedByUserInfo(
        User changedByUser
    ) {
        if (changedByUser == null) {
            return null;
        }

        return new ChangedByUserInfo(
            changedByUser.getId(),
            changedByUser.getUserNumber(),
            changedByUser.getName(),
            changedByUser.getNickname()
        );
    }
}
