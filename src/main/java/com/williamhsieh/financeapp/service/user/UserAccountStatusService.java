package com.williamhsieh.financeapp.service.user;

import java.util.Objects;
import java.util.Locale;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.williamhsieh.financeapp.entity.user.User;
import com.williamhsieh.financeapp.entity.user.UserAccountStatus;
import com.williamhsieh.financeapp.entity.user.UserAccountStatusLog;
import com.williamhsieh.financeapp.repository.user.UserAccountStatusLogRepository;
import com.williamhsieh.financeapp.repository.user.UserAccountStatusRepository;
import com.williamhsieh.financeapp.constant.user.UserAccountStatusCodes;

@Service
public class UserAccountStatusService {

    private static final Set<String>
        ADMINISTRATIVELY_MANAGED_STATUSES =
            Set.of(
                UserAccountStatusCodes.ACTIVE,
                UserAccountStatusCodes.SUSPENDED,
                UserAccountStatusCodes.LOCKED
            );

    private final UserAccountStatusRepository
        accountStatusRepository;

    private final UserAccountStatusLogRepository
        accountStatusLogRepository;

    public UserAccountStatusService(
        UserAccountStatusRepository accountStatusRepository,
        UserAccountStatusLogRepository accountStatusLogRepository
    ) {
        this.accountStatusRepository =
            accountStatusRepository;

        this.accountStatusLogRepository =
            accountStatusLogRepository;
    }

    @Transactional
    public void changeStatusByAuthorizedUser(
        User user,
        String newStatusCode,
        User changedByUser,
        String note
    ) {
        if (changedByUser == null) {
            throw new IllegalArgumentException(
                "狀態異動者不可為 null"
            );
        }

        String normalizedStatusCode =
            normalizeStatusCode(newStatusCode);

        String normalizedNote =
            normalizeNote(note);

        validateAdministrativelyManagedStatus(
            normalizedStatusCode
        );

        validateAdministrativeTransition(
            user,
            normalizedStatusCode,
            normalizedNote
        );

        changeStatus(
            user,
            normalizedStatusCode,
            changedByUser,
            normalizedNote
        );
    }

    @Transactional
    public void deleteByAuthorizedUser(
        User user,
        User changedByUser,
        String reason
    ) {
        if (user == null) {
            throw new IllegalArgumentException(
                "被刪除的會員不可為 null"
            );
        }

        if (changedByUser == null) {
            throw new IllegalArgumentException(
                "刪除會員的操作者不可為 null"
            );
        }

        String normalizedReason =
            normalizeNote(reason);

        if (normalizedReason == null) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "刪除會員時必須填寫原因"
            );
        }

        String currentStatusCode =
            user.getAccountStatus().getCode();

        if (
            UserAccountStatusCodes.DELETED.equals(
                currentStatusCode
            ) ||
            user.getDeletedDate() != null
        ) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "會員帳號已經被刪除"
            );
        }

        changeStatus(
            user,
            UserAccountStatusCodes.DELETED,
            changedByUser,
            normalizedReason
        );
    }

    @Transactional
    public void restoreByAuthorizedUser(
        User user,
        User changedByUser,
        String reason
    ) {
        if (user == null) {
            throw new IllegalArgumentException(
                "被恢復的會員不可為 null"
            );
        }

        if (changedByUser == null) {
            throw new IllegalArgumentException(
                "恢復會員的操作者不可為 null"
            );
        }

        String normalizedReason =
            normalizeNote(reason);

        if (normalizedReason == null) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "恢復會員時必須填寫原因"
            );
        }

        String currentStatusCode =
            user.getAccountStatus().getCode();

        /*
        * 必須同時符合：
        * 1. 帳號狀態為 DELETED
        * 2. deleted_date 不為 null
        */
        if (
            !UserAccountStatusCodes.DELETED.equals(
                currentStatusCode
            ) ||
            user.getDeletedDate() == null
        ) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "會員帳號不是已刪除狀態"
            );
        }

        /*
        * Email 已驗證，可以直接恢復為 ACTIVE。
        * Email 尚未驗證，只能回到等待驗證狀態。
        */
        String restoredStatusCode =
            user.isEmailVerified()
                ? UserAccountStatusCodes.ACTIVE
                : UserAccountStatusCodes
                    .PENDING_EMAIL_VERIFICATION;

        changeStatus(
            user,
            restoredStatusCode,
            changedByUser,
            normalizedReason
        );
    }

    @Transactional
    public void changeStatus(
        User user,
        String newStatusCode,
        User changedByUser,
        String note
    ) {
        UserAccountStatus previousStatus =
            user.getAccountStatus();

        UserAccountStatus newStatus =
            accountStatusRepository
                .findByCodeAndActiveTrueAndDeletedDateIsNull(
                    newStatusCode
                )
                .orElseThrow(() ->
                    new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "找不到帳號狀態設定：" +
                            newStatusCode
                    )
                );

        if (
            previousStatus != null &&
            Objects.equals(
                previousStatus.getId(),
                newStatus.getId()
            )
        ) {
            return;
        }

        user.changeAccountStatus(newStatus);

        UserAccountStatusLog statusLog =
            new UserAccountStatusLog(
                user,
                previousStatus,
                newStatus,
                changedByUser,
                note
            );

        accountStatusLogRepository.save(statusLog);
    }

    private String normalizeStatusCode(
        String statusCode
    ) {
        if (statusCode == null || statusCode.isBlank()) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "會員狀態不得為空"
            );
        }

        return statusCode
            .trim()
            .toUpperCase(Locale.ROOT);
    }

    private String normalizeNote(
        String note
    ) {
        if (note == null || note.isBlank()) {
            return null;
        }

        return note.trim();
    }

    private void validateAdministrativelyManagedStatus(
        String statusCode
    ) {
        if (
            !ADMINISTRATIVELY_MANAGED_STATUSES
                .contains(statusCode)
        ) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "後台不可設定此會員狀態"
            );
        }
    }

    private void validateAdministrativeTransition(
        User user,
        String newStatusCode,
        String note
    ) {
        String currentStatusCode =
            user.getAccountStatus().getCode();

        if (
            UserAccountStatusCodes.DELETED.equals(
                currentStatusCode
            ) ||
            user.getDeletedDate() != null
        ) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "已刪除的會員不可變更帳號狀態"
            );
        }

        if (
            UserAccountStatusCodes
                .PENDING_EMAIL_VERIFICATION
                .equals(currentStatusCode)
        ) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "尚未完成 Email 驗證的會員不可由後台啟用"
            );
        }

        if (currentStatusCode.equals(newStatusCode)) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "會員目前已經是指定狀態"
            );
        }

        if (
            UserAccountStatusCodes.ACTIVE.equals(
                newStatusCode
            ) &&
            !user.isEmailVerified()
        ) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "Email 尚未驗證，無法啟用會員"
            );
        }

        boolean reasonRequired =
            UserAccountStatusCodes.SUSPENDED.equals(
                newStatusCode
            ) ||
            UserAccountStatusCodes.LOCKED.equals(
                newStatusCode
            );

        if (reasonRequired && note == null) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "停權或鎖定會員時必須填寫原因"
            );
        }
    }
}
