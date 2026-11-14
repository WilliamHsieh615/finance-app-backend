package com.williamhsieh.financeapp.identity.authentication.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.williamhsieh.financeapp.identity.authentication.constant.LoginStatusCodes;
import com.williamhsieh.financeapp.identity.authentication.entity.UserLoginLog;
import com.williamhsieh.financeapp.identity.authentication.entity.UserLoginStatus;
import com.williamhsieh.financeapp.identity.authentication.entity.UserRefreshToken;
import com.williamhsieh.financeapp.identity.authentication.repository.UserLoginLogRepository;
import com.williamhsieh.financeapp.identity.authentication.repository.UserLoginStatusRepository;
import com.williamhsieh.financeapp.identity.authentication.repository.UserRefreshTokenRepository;
import com.williamhsieh.financeapp.identity.user.entity.User;

@Service
public class LoginAuditService {

    private final UserLoginLogRepository loginLogRepository;
    private final UserLoginStatusRepository loginStatusRepository;
    private final UserRefreshTokenRepository refreshTokenRepository;

    public LoginAuditService(
        UserLoginLogRepository loginLogRepository,
        UserLoginStatusRepository loginStatusRepository,
        UserRefreshTokenRepository refreshTokenRepository
    ) {
        this.loginLogRepository = loginLogRepository;
        this.loginStatusRepository = loginStatusRepository;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Transactional
    public UserLoginLog recordSuccess(
        User user,
        String email,
        Long refreshTokenId,
        String sessionId,
        LoginRequestMetadata metadata
    ) {
        UserLoginStatus status = findStatus(
            LoginStatusCodes.SUCCESS
        );

        UserRefreshToken refreshToken =
            refreshTokenRepository.getReferenceById(
                refreshTokenId
            );

        UserLoginLog loginLog = new UserLoginLog();

        loginLog.setUser(user);
        loginLog.setLoginStatus(status);
        loginLog.setRefreshToken(refreshToken);
        loginLog.setEmail(email);
        loginLog.setSessionId(sessionId);

        applyMetadata(loginLog, metadata);

        return loginLogRepository.save(loginLog);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void recordFailure(
        User user,
        String email,
        String statusCode,
        LoginRequestMetadata metadata
    ) {
        UserLoginStatus status = findStatus(statusCode);

        UserLoginLog loginLog = new UserLoginLog();

        loginLog.setUser(user);
        loginLog.setLoginStatus(status);
        loginLog.setRefreshToken(null);
        loginLog.setEmail(email);
        loginLog.setSessionId(null);

        applyMetadata(loginLog, metadata);

        loginLogRepository.save(loginLog);
    }

    @Transactional
    public String updateRefreshToken(
        Long oldRefreshTokenId,
        Long newRefreshTokenId
    ) {
        UserLoginLog loginLog = loginLogRepository
            .findByRefreshToken_IdAndLogoutTimeIsNull(
                oldRefreshTokenId
            )
            .orElseThrow(() -> new IllegalStateException(
                "找不到 Refresh Token 對應的登入紀錄"
            ));

        UserRefreshToken newRefreshToken =
            refreshTokenRepository.getReferenceById(
                newRefreshTokenId
            );

        loginLog.setRefreshToken(newRefreshToken);

        return loginLog.getSessionId();
    }

    @Transactional
    public String recordLogout(Long refreshTokenId) {
        if (refreshTokenId == null) {
            return null;
        }

        var loginLogOptional = loginLogRepository
            .findByRefreshToken_IdAndLogoutTimeIsNull(
                refreshTokenId
            );

        if (loginLogOptional.isEmpty()) {
            return null;
        }

        UserLoginLog loginLog = loginLogOptional.get();

        loginLog.markAsLoggedOut();

        return loginLog.getSessionId();
    }

    private UserLoginStatus findStatus(String statusCode) {
        return loginStatusRepository
            .findByCodeAndActiveTrueAndDeletedDateIsNull(
                statusCode
            )
            .orElseThrow(() -> new IllegalStateException(
                "找不到啟用的登入狀態：" + statusCode
            ));
    }

    private void applyMetadata(
        UserLoginLog loginLog,
        LoginRequestMetadata metadata
    ) {
        if (metadata == null) {
            return;
        }

        loginLog.setIpAddress(metadata.ipAddress());
        loginLog.setUserAgent(metadata.userAgent());
        loginLog.setDeviceName(metadata.deviceName());
        loginLog.setOsName(metadata.osName());
        loginLog.setBrowserName(metadata.browserName());
    }
}
