package com.williamhsieh.financeapp.service.auth;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.williamhsieh.financeapp.repository.auth.UserLoginLogRepository;

@Service
public class UserSessionAccessPolicy {

    private final UserLoginLogRepository
        userLoginLogRepository;

    public UserSessionAccessPolicy(
        UserLoginLogRepository userLoginLogRepository
    ) {
        this.userLoginLogRepository =
            userLoginLogRepository;
    }

    @Transactional(readOnly = true)
    public boolean isAvailable(
        Long userId,
        String sessionId
    ) {
        if (
            userId == null ||
            sessionId == null ||
            sessionId.isBlank()
        ) {
            return false;
        }

        return userLoginLogRepository
            .existsBySessionIdAndUser_IdAndLogoutTimeIsNullAndRefreshToken_RevokedDateIsNull(
                sessionId,
                userId
            );
    }
}
