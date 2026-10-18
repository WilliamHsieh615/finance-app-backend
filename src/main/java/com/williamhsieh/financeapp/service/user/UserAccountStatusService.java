package com.williamhsieh.financeapp.service.user;

import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.williamhsieh.financeapp.entity.user.User;
import com.williamhsieh.financeapp.entity.user.UserAccountStatus;
import com.williamhsieh.financeapp.entity.user.UserAccountStatusLog;
import com.williamhsieh.financeapp.repository.user.UserAccountStatusLogRepository;
import com.williamhsieh.financeapp.repository.user.UserAccountStatusRepository;

@Service
public class UserAccountStatusService {

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
}
