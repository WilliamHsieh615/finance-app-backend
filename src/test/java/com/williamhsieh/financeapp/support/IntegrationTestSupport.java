package com.williamhsieh.financeapp.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import org.junit.jupiter.api.BeforeEach;

import com.williamhsieh.financeapp.service.notification.EmailSender;
import com.williamhsieh.financeapp.service.notification.SmsSender;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public abstract class IntegrationTestSupport {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    @Autowired
    protected PasswordEncoder passwordEncoder;

    @Autowired
    protected JwtEncoder jwtEncoder;

    @Autowired
    protected JwtDecoder jwtDecoder;

    @MockitoBean
    protected EmailSender emailSender;

    @MockitoBean
    protected SmsSender smsSender;

    @BeforeEach
    protected void setUpBaseTestData() {
        clearTestData();
        insertLoginStatuses();
        insertAuthEventTypes();
        insertLanguage();
        insertAuthorizationData();
        insertUser();
        insertUserRole();
    }

    protected void clearTestData() {
        jdbcTemplate.update(
            "DELETE FROM user_auth_event_logs"
        );

        jdbcTemplate.update(
            "DELETE FROM user_login_logs"
        );

        jdbcTemplate.update(
            "DELETE FROM user_refresh_tokens"
        );

        jdbcTemplate.update(
            "DELETE FROM user_email_verifications"
        );

        jdbcTemplate.update(
            "DELETE FROM user_sms_verifications"
        );

        jdbcTemplate.update(
            "DELETE FROM user_roles"
        );

        jdbcTemplate.update(
            "DELETE FROM role_permissions"
        );

        jdbcTemplate.update(
            "DELETE FROM permissions"
        );

        jdbcTemplate.update(
            "DELETE FROM permission_types"
        );

        jdbcTemplate.update(
            "DELETE FROM roles"
        );

        jdbcTemplate.update(
            "DELETE FROM users"
        );

        jdbcTemplate.update(
            "DELETE FROM user_login_statuses"
        );

        jdbcTemplate.update(
            "DELETE FROM languages"
        );

        jdbcTemplate.update(
            "DELETE FROM user_auth_event_types"
        );
    }

    protected void insertLoginStatuses() {
        jdbcTemplate.update(
            """
            INSERT INTO user_login_statuses (
                id,
                code,
                name,
                is_active,
                created_date,
                updated_date
            )
            VALUES (
                1,
                'SUCCESS',
                '登入成功',
                TRUE,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP
            )
            """
        );

        jdbcTemplate.update(
            """
            INSERT INTO user_login_statuses (
                id,
                code,
                name,
                is_active,
                created_date,
                updated_date
            )
            VALUES (
                2,
                'INVALID_CREDENTIALS',
                '登入資料錯誤',
                TRUE,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP
            )
            """
        );

        jdbcTemplate.update(
            """
            INSERT INTO user_login_statuses (
                id,
                code,
                name,
                is_active,
                created_date,
                updated_date
            )
            VALUES (
                3,
                'EMAIL_NOT_VERIFIED',
                'Email 尚未驗證',
                TRUE,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP
            )
            """
        );

        jdbcTemplate.update(
            """
            INSERT INTO user_login_statuses (
                id,
                code,
                name,
                is_active,
                created_date,
                updated_date
            )
            VALUES (
                4,
                'SMS_NOT_VERIFIED',
                '手機尚未驗證',
                TRUE,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP
            )
            """
        );

        jdbcTemplate.update(
            """
            INSERT INTO user_login_statuses (
                id,
                code,
                name,
                is_active,
                created_date,
                updated_date
            )
            VALUES (
                5,
                'ACCOUNT_DISABLED',
                '帳號已停用',
                TRUE,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP
            )
            """
        );
    }

    protected void insertAuthEventTypes() {
        jdbcTemplate.update(
            """
            INSERT INTO user_auth_event_types (
                id,
                code,
                name,
                is_active,
                created_date,
                updated_date
            )
            VALUES (
                1,
                'ACCESS_TOKEN_INVALID',
                'Access Token 無效',
                TRUE,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP
            )
            """
        );

        jdbcTemplate.update(
            """
            INSERT INTO user_auth_event_types (
                id,
                code,
                name,
                is_active,
                created_date,
                updated_date
            )
            VALUES (
                2,
                'ACCESS_TOKEN_EXPIRED',
                'Access Token 已過期',
                TRUE,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP
            )
            """
        );

        jdbcTemplate.update(
            """
            INSERT INTO user_auth_event_types (
                id,
                code,
                name,
                is_active,
                created_date,
                updated_date
            )
            VALUES (
                3,
                'TOKEN_REFRESH_SUCCESS',
                'Refresh Token 輪替成功',
                TRUE,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP
            )
            """
        );

        jdbcTemplate.update(
            """
            INSERT INTO user_auth_event_types (
                id,
                code,
                name,
                is_active,
                created_date,
                updated_date
            )
            VALUES (
                4,
                'REFRESH_TOKEN_REUSE_DETECTED',
                '偵測到 Refresh Token 重複使用',
                TRUE,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP
            )
            """
        );

        jdbcTemplate.update(
            """
            INSERT INTO user_auth_event_types (
                id,
                code,
                name,
                is_active,
                created_date,
                updated_date
            )
            VALUES (
                5,
                'LOGOUT_SUCCESS',
                '登出成功',
                TRUE,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP
            )
            """
        );

        jdbcTemplate.update(
            """
            INSERT INTO user_auth_event_types (
                id,
                code,
                name,
                is_active,
                created_date,
                updated_date
            )
            VALUES (
                6,
                'LOGOUT_INVALID_TOKEN',
                '登出 Token 無效',
                TRUE,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP
            )
            """
        );

        jdbcTemplate.update(
            """
            INSERT INTO user_auth_event_types (
                id,
                code,
                name,
                is_active,
                created_date,
                updated_date
            )
            VALUES (
                7,
                'REFRESH_TOKEN_INVALID',
                'Refresh Token 無效',
                TRUE,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP
            )   
            """
        );

        jdbcTemplate.update(
            """
            INSERT INTO user_auth_event_types (
                id,
                code,
                name,
                is_active,
                created_date,
                updated_date
            )
            VALUES (
                8,
                'REFRESH_TOKEN_EXPIRED',
                'Refresh Token 已過期',
                TRUE,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP
            )
            """
        );

        jdbcTemplate.update(
            """
            INSERT INTO user_auth_event_types (
                id,
                code,
                name,
                is_active,
                created_date,
                updated_date
            )
            VALUES (
                9,
                'REFRESH_TOKEN_REVOKED',
                'Refresh Token 已撤銷',
                TRUE,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP
            )
            """
        );
    }

    protected void insertLanguage() {
        jdbcTemplate.update(
            """
            INSERT INTO languages (
                id,
                code,
                name,
                is_active,
                created_date,
                updated_date
            )
            VALUES (
                1,
                'zh-TW',
                '繁體中文',
                TRUE,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP
            )
            """
        );
    }

    protected void insertAuthorizationData() {
        jdbcTemplate.update(
            """
            INSERT INTO permission_types (
                id,
                code,
                name,
                is_active,
                created_date,
                updated_date
            )
            VALUES (
                1,
                'USER',
                '個人資料',
                TRUE,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP
            )
            """
        );

        jdbcTemplate.update(
            """
            INSERT INTO permissions (
                id,
                permission_type_id,
                code,
                name,
                is_active,
                created_date,
                updated_date
            )
            VALUES (
                1,
                1,
                'PROFILE_READ_SELF',
                '查看自己的會員資料',
                TRUE,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP
            )
            """
        );

        jdbcTemplate.update(
            """
            INSERT INTO roles (
                id,
                code,
                name,
                is_active,
                created_date,
                updated_date
            )
            VALUES (
                1,
                'USER',
                '一般使用者',
                TRUE,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP
            )
            """
        );

        jdbcTemplate.update(
            """
            INSERT INTO role_permissions (
                id,
                role_id,
                permission_id,
                is_active,
                created_date,
                updated_date
            )
            VALUES (
                1,
                1,
                1,
                TRUE,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP
            )
            """
        );

        jdbcTemplate.update(
            """
            INSERT INTO permission_types (
                id,
                code,
                name,
                is_active,
                created_date,
                updated_date
            )
            VALUES (
                2,
                'USER_MANAGEMENT',
                '會員管理',
                TRUE,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP
            )
            """
        );
        
        jdbcTemplate.update(
            """
            INSERT INTO permissions (
                id,
                permission_type_id,
                code,
                name,
                is_active,
                created_date,
                updated_date
            )
            VALUES (
                2,
                2,
                'USER_READ',
                '查詢會員資料',
                TRUE,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP
            )
            """
        );

        jdbcTemplate.update(
            """
            INSERT INTO roles (
                id,
                code,
                name,
                is_active,
                created_date,
                updated_date
            )
            VALUES (
                2,
                'SUPPORT',
                '客服人員',
                TRUE,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP
            )
            """
        );

        jdbcTemplate.update(
            """
            INSERT INTO role_permissions (
                id,
                role_id,
                permission_id,
                is_active,
                created_date,
                updated_date
                )
            VALUES (
                2,
                2,
                2,
                TRUE,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP
            )
            """
        );
    }

    protected void insertUser() {
        String encodedPassword =
            passwordEncoder.encode("Password123!");

        jdbcTemplate.update(
            """
            INSERT INTO users (
                id,
                country_id,
                timezone_id,
                language_id,
                user_number,
                name,
                nickname,
                email,
                password,
                birthday,
                phone,
                email_verified,
                sms_verified,
                is_active,
                created_date,
                updated_date
            )
            VALUES (
                100,
                NULL,
                NULL,
                1,
                'TESTUSER01',
                'Test User',
                'Tester',
                'test@example.com',
                ?,
                DATE '1990-01-01',
                '0912345678',
                TRUE,
                TRUE,
                TRUE,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP
            )
            """,
            encodedPassword
        );
    }

    protected void insertUserRole() {
        jdbcTemplate.update(
            """
            INSERT INTO user_roles (
                id,
                user_id,
                role_id,
                is_active,
                created_date,
                updated_date
            )
            VALUES (
                1,
                100,
                1,
                TRUE,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP
            )
            """
        );
    }
}