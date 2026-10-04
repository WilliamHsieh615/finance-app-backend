package com.williamhsieh.financeapp.auth;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.util.List;

import com.jayway.jsonpath.JsonPath;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import com.williamhsieh.financeapp.service.notification.EmailSender;
import com.williamhsieh.financeapp.service.notification.SmsSender;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtEncoder jwtEncoder;

    @Autowired
    private JwtDecoder jwtDecoder;

    @MockitoBean
    private EmailSender emailSender;

    @MockitoBean
    private SmsSender smsSender;

    @BeforeEach
    void setUpTestData() {
        clearTestData();
        insertLoginStatuses();
        insertAuthEventTypes();
        insertLanguage();
        insertAuthorizationData();
        insertUser();
        insertUserRole();
    }

    private void clearTestData() {
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

    private void insertLoginStatuses() {
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

    private void insertLanguage() {
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

    private void insertUser() {
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

    private void assertFailedLoginRecorded(
            String expectedStatusCode
        ) {
            Long loginLogCount =
                jdbcTemplate.queryForObject(
                    """
                    SELECT COUNT(*)
                    FROM user_login_logs login_log
                    JOIN user_login_statuses login_status
                      ON login_status.id =
                         login_log.user_login_status_id
                    WHERE login_log.user_id = 100
                      AND login_log.email =
                          'test@example.com'
                      AND login_status.code = ?
                      AND login_log.refresh_token_id IS NULL
                      AND login_log.session_id IS NULL
                    """,
                    Long.class,
                    expectedStatusCode
                );

            Long refreshTokenCount =
                jdbcTemplate.queryForObject(
                    """
                    SELECT COUNT(*)
                    FROM user_refresh_tokens
                    WHERE user_id = 100
                    """,
                    Long.class
                );

            assertThat(loginLogCount)
                .isEqualTo(1);

            assertThat(refreshTokenCount)
                .isZero();
        }

    private void insertAuthEventTypes() {
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

    private String createExpiredAccessToken() {
        Instant now = Instant.now();

        Instant issuedAt =
            now.minusSeconds(120);

        Instant expiresAt =
            now.minusSeconds(60);

        JwsHeader header = JwsHeader
            .with(MacAlgorithm.HS256)
            .type("JWT")
            .build();

        JwtClaimsSet claims = JwtClaimsSet
            .builder()
            .issuer("http://localhost:8080")
            .subject("100")
            .issuedAt(issuedAt)
            .expiresAt(expiresAt)
            .claim(
                "userNumber",
                "TESTUSER01"
            )
            .claim(
                "email",
                "test@example.com"
            )
            .claim(
                "sid",
                "00000000-0000-0000-0000-000000000001"
            )
            .claim(
                "tokenType",
                "access"
            )
            .build();

        return jwtEncoder
            .encode(
                JwtEncoderParameters.from(
                    header,
                    claims
                )
            )
            .getTokenValue();
    }

    private void insertAuthorizationData() {
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

    private void insertUserRole() {
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

    @Test
    void meWithoutAccessTokenReturnsUnauthorized()
        throws Exception {

        mockMvc.perform(
            get("/api/v1/auth/me")
                .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isUnauthorized())
        .andExpect(
            content().contentTypeCompatibleWith(
                MediaType.APPLICATION_JSON
            )
        )
        .andExpect(
            jsonPath("$.status").value(401)
        )
        .andExpect(
            jsonPath("$.error")
                .value("Unauthorized")
        )
        .andExpect(
            jsonPath("$.code")
                .value("UNAUTHORIZED")
        )
        .andExpect(
            jsonPath("$.message")
                .value("需要有效的 Access Token")
        )
        .andExpect(
            jsonPath("$.path")
                .value("/api/v1/auth/me")
        )
        .andExpect(
            jsonPath("$.fieldErrors").isArray()
        )
        .andExpect(
            jsonPath("$.fieldErrors").isEmpty()
        );

        Long authEventCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM user_auth_event_logs
                """,
                Long.class
            );

        assertThat(authEventCount)
            .isZero();
    }

    @Test
    void refreshWithBlankTokenReturnsValidationError()
        throws Exception {

        String requestBody = """
            {
                "refreshToken": ""
            }
            """;

        mockMvc.perform(
            post("/api/v1/auth/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(requestBody)
        )
        .andExpect(status().isBadRequest())
        .andExpect(
            content().contentTypeCompatibleWith(
                MediaType.APPLICATION_JSON
            )
        )
        .andExpect(
            jsonPath("$.status").value(400)
        )
        .andExpect(
            jsonPath("$.error")
                .value("Bad Request")
        )
        .andExpect(
            jsonPath("$.code")
                .value("VALIDATION_FAILED")
        )
        .andExpect(
            jsonPath("$.message")
                .value("請求資料驗證失敗")
        )
        .andExpect(
            jsonPath("$.path")
                .value("/api/v1/auth/refresh")
        )
        .andExpect(
            jsonPath("$.fieldErrors[0].field")
                .value("refreshToken")
        )
        .andExpect(
            jsonPath("$.fieldErrors[0].message")
                .value("Refresh Token 不得為空")
        );
    }

    // 登入成功測試
    @Test
    void loginWithValidCredentialsReturnsTokens()
        throws Exception {

        String requestBody = """
            {
                "email": "test@example.com",
                "password": "Password123!"
            }
            """;

        MvcResult result = mockMvc.perform(
            post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(requestBody)
        )
        .andExpect(status().isOk())
        .andExpect(
            content().contentTypeCompatibleWith(
                MediaType.APPLICATION_JSON
            )
        )
        .andExpect(
            jsonPath("$.accessToken").isString()
        )
        .andExpect(
            jsonPath("$.accessToken").isNotEmpty()
        )
        .andExpect(
            jsonPath("$.refreshToken").isString()
        )
        .andExpect(
            jsonPath("$.refreshToken").isNotEmpty()
        )
        .andExpect(
            jsonPath("$.tokenType").value("Bearer")
        )
        .andExpect(
            jsonPath("$.expiresIn").value(900)
        )
        .andExpect(
            jsonPath("$.refreshTokenExpiresIn")
                .value(2592000)
        )
        .andExpect(
            jsonPath("$.user.id").value(100)
        )
        .andExpect(
            jsonPath("$.user.userNumber")
                .value("TESTUSER01")
        )
        .andExpect(
            jsonPath("$.user.email")
                .value("test@example.com")
        )
        .andReturn();

        String responseBody =
            result.getResponse().getContentAsString();
        
        String rawAccessToken =
            JsonPath.read(
                responseBody,
                "$.accessToken"
            );
        
        String rawRefreshToken =
            JsonPath.read(
                responseBody,
                "$.refreshToken"
            );

        Jwt decodedAccessToken =
            jwtDecoder.decode(
                rawAccessToken
            );

        List<String> roles =
            decodedAccessToken.getClaimAsStringList(
                "roles"
            );

        List<String> permissions =
            decodedAccessToken.getClaimAsStringList(
                "permissions"
            );

        Long refreshTokenCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM user_refresh_tokens
                WHERE user_id = 100
                """,
                Long.class
            );

        String storedTokenHash =
            jdbcTemplate.queryForObject(
                """
                SELECT token_hash
                FROM user_refresh_tokens
                WHERE user_id = 100
                """,
                String.class
            );

        Long successfulLoginLogCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM user_login_logs login_log
                JOIN user_login_statuses login_status
                  ON login_status.id = 
                     login_log.user_login_status_id
                WHERE login_log.user_id = 100
                  AND login_status.code = 'SUCCESS'
                  AND login_log.refresh_token_id IS NOT NULL
                  AND login_log.session_id IS NOT NULL
                """,
                Long.class
            );

        String loginSessionId =
            jdbcTemplate.queryForObject(
                """
                SELECT session_id
                FROM user_login_logs
                WHERE user_id = 100
                """,
                String.class
            );

        assertThat(rawRefreshToken)
            .isNotBlank();

        assertThat(refreshTokenCount)
            .isEqualTo(1);

        assertThat(storedTokenHash)
            .isNotBlank()
            .hasSize(64)
            .matches("[0-9a-f]{64}")
            .isNotEqualTo(rawRefreshToken);

        assertThat(successfulLoginLogCount)
            .isEqualTo(1);

        assertThat(loginSessionId)
            .isNotBlank()
            .hasSize(36);

        assertThat(rawAccessToken)
            .isNotBlank();

        assertThat(roles)
            .containsExactly("USER");

        assertThat(permissions)
            .containsExactly(
                "PROFILE_READ_SELF"
            );
        
        assertThat(decodedAccessToken.getSubject())
            .isEqualTo("100");

        assertThat(
            decodedAccessToken.getClaimAsString(
                "userNumber"
            )
        )
            .isEqualTo("TESTUSER01");

        assertThat(
            decodedAccessToken.getClaimAsString(
                "email"
            )
        )
            .isEqualTo("test@example.com");

        assertThat(
            decodedAccessToken.getClaimAsString(
                "tokenType"
            )
        )
            .isEqualTo("access");

        assertThat(
            decodedAccessToken.getClaimAsString(
                "sid"
            )
        )
            .isEqualTo(loginSessionId);
    }

    // 密碼錯誤測試
    @Test
    void loginWithWrongPasswordReturnsUnauthorized()
        throws Exception {

        String requestBody = """
            {
                "email": "test@example.com",
                "password": "WrongPassword!"
            }
            """;

        mockMvc.perform(
            post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(requestBody)
        )
        .andExpect(status().isUnauthorized())
        .andExpect(
            jsonPath("$.status").value(401)
        )
        .andExpect(
            jsonPath("$.code")
                .value("UNAUTHORIZED")
        )
        .andExpect(
            jsonPath("$.message")
                .value("Email 或密碼錯誤")
        )
        .andExpect(
            jsonPath("$.path")
                .value("/api/v1/auth/login")
        );

        Long refreshTokenCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM user_refresh_tokens
                WHERE user_id = 100
                """,
                Long.class
            );

        Long invalidLoginLogCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM user_login_logs login_log
                JOIN user_login_statuses login_status
                  ON login_status.id =
                     login_log.user_login_status_id
                WHERE login_log.user_id = 100
                  AND login_log.email =
                      'test@example.com'
                  AND login_status.code =
                      'INVALID_CREDENTIALS'
                  AND login_log.refresh_token_id IS NULL
                  AND login_log.session_id IS NULL
                """,
                Long.class
            );

        assertThat(refreshTokenCount)
            .isZero();

        assertThat(invalidLoginLogCount)
            .isEqualTo(1);
    }

    // Email未驗證測試
    @Test
    void loginWithUnverifiedEmailReturnsForbidden()
        throws Exception {

        jdbcTemplate.update(
            """
            UPDATE users
            SET email_verified = FALSE
            WHERE id = 100
            """
        );

        String requestBody = """
            {
                "email": "test@example.com",
                "password": "Password123!"
            }
            """;

        mockMvc.perform(
            post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(requestBody)
        )
        .andExpect(status().isForbidden())
        .andExpect(
            jsonPath("$.status").value(403)
        )
        .andExpect(
            jsonPath("$.code")
                .value("ACCESS_DENIED")
        )
        .andExpect(
            jsonPath("$.message")
                .value("請先完成 Email 驗證")
        )
        .andExpect(
            jsonPath("$.path")
                .value("/api/v1/auth/login")
        );

        assertFailedLoginRecorded(
            "EMAIL_NOT_VERIFIED"
        );
    }

    // SMS 未驗證測試
    @Test
    void loginWithUnverifiedSmsReturnsForbidden()
        throws Exception {

        jdbcTemplate.update(
            """
            UPDATE users
            SET sms_verified = FALSE
            WHERE id = 100
            """
        );

        String requestBody = """
            {
                "email": "test@example.com",
                "password": "Password123!"
            }
            """;

        mockMvc.perform(
            post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(requestBody)
        )
        .andExpect(status().isForbidden())
        .andExpect(
            jsonPath("$.status").value(403)
        )
        .andExpect(
            jsonPath("$.code")
                .value("ACCESS_DENIED")
        )
        .andExpect(
            jsonPath("$.message")
                .value("請先完成手機驗證")
        )
        .andExpect(
            jsonPath("$.path")
                .value("/api/v1/auth/login")
        );

        assertFailedLoginRecorded(
            "SMS_NOT_VERIFIED"
        );
    }

    // 帳號停用測試
    @Test
    void loginWithDisabledAccountReturnsForbidden()
        throws Exception {

        jdbcTemplate.update(
            """
            UPDATE users
            SET is_active = FALSE
            WHERE id = 100
            """
        );

        String requestBody = """
            {
                "email": "test@example.com",
                "password": "Password123!"
            }
            """;

        mockMvc.perform(
            post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(requestBody)
        )
        .andExpect(status().isForbidden())
        .andExpect(
            jsonPath("$.status").value(403)
        )
        .andExpect(
            jsonPath("$.code")
                .value("ACCESS_DENIED")
        )
        .andExpect(
            jsonPath("$.message")
                .value("此帳號目前無法使用")
        )
        .andExpect(
            jsonPath("$.path")
                .value("/api/v1/auth/login")
        );

        assertFailedLoginRecorded(
            "ACCOUNT_DISABLED"
        );
    }

    @Test
    void validAccessTokenCanGetCurrentUser()
        throws Exception {

        String loginRequestBody = """
            {
                "email": "test@example.com",
                "password": "Password123!"
            }
            """;

        MvcResult loginResult = mockMvc.perform(
            post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(loginRequestBody)
        )
        .andExpect(status().isOk())
        .andReturn();

        String loginResponseBody =
            loginResult
                .getResponse()
                .getContentAsString();

        String accessToken =
            JsonPath.read(
                loginResponseBody,
                "$.accessToken"
            );

        assertThat(accessToken)
            .isNotBlank();

        /*
         * Token 簽發後修改資料庫 nickname，
         * 用來確認 /me 回傳的是資料庫最新資料，
         * 而不是直接回傳 JWT 內的舊 claims。
         */
        jdbcTemplate.update(
            """
            UPDATE users
            SET nickname = 'Updated Tester',
                updated_date = CURRENT_TIMESTAMP
            WHERE id = 100
            """
        );

        mockMvc.perform(
            get("/api/v1/auth/me")
                .header(
                    HttpHeaders.AUTHORIZATION,
                    "Bearer " + accessToken
                )
                .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andExpect(
            content().contentTypeCompatibleWith(
                MediaType.APPLICATION_JSON
            )
        )
        .andExpect(
            jsonPath("$.id").value(100)
        )
        .andExpect(
            jsonPath("$.userNumber")
                .value("TESTUSER01")
        )
        .andExpect(
            jsonPath("$.name")
                .value("Test User")
        )
        .andExpect(
            jsonPath("$.nickname")
                .value("Updated Tester")
        )
        .andExpect(
            jsonPath("$.email")
                .value("test@example.com")
        )
        .andExpect(
            jsonPath("$.birthday")
                .value("1990-01-01")
        )
        .andExpect(
            jsonPath("$.phone")
                .value("0912345678")
        )
        .andExpect(
            jsonPath("$.emailVerified")
                .value(true)
        )
        .andExpect(
            jsonPath("$.smsVerified")
                .value(true)
        )
        .andExpect(
            jsonPath("$.active")
                .value(true)
        )
        .andExpect(
            jsonPath("$.roles.length()")
                .value(1)
        )
        .andExpect(
            jsonPath("$.roles[0]")
                .value("USER")
        )
        .andExpect(
            jsonPath("$.permissions.length()")
                .value(1)
        )
        .andExpect(
            jsonPath("$.permissions[0]")
                .value("PROFILE_READ_SELF")
        )
        .andExpect(
            jsonPath("$.language.id")
                .value(1)
        )
        .andExpect(
            jsonPath("$.language.code")
                .value("zh-TW")
        )
        .andExpect(
            jsonPath("$.language.name")
                .value("繁體中文")
        );
    }

    @Test
    void invalidAccessTokenReturnsUnauthorizedAndWritesAuditLog()
        throws Exception {

        String invalidAccessToken =
            "this-is-not-a-valid-jwt";

        mockMvc.perform(
            get("/api/v1/auth/me")
                .header(
                    HttpHeaders.AUTHORIZATION,
                    "Bearer " + invalidAccessToken
                )
                .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isUnauthorized())
        .andExpect(
            content().contentTypeCompatibleWith(
                MediaType.APPLICATION_JSON
            )
        )
        .andExpect(
            jsonPath("$.status").value(401)
        )
        .andExpect(
            jsonPath("$.error")
                .value("Unauthorized")
        )
        .andExpect(
            jsonPath("$.code")
                .value("ACCESS_TOKEN_INVALID")
        )
        .andExpect(
            jsonPath("$.message")
                .value("Access Token 無效")
        )
        .andExpect(
            jsonPath("$.path")
                .value("/api/v1/auth/me")
        );

        Long invalidEventCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM user_auth_event_logs event_log
                JOIN user_auth_event_types event_type
                  ON event_type.id =
                     event_log.user_auth_event_type_id
                WHERE event_type.code =
                      'ACCESS_TOKEN_INVALID'
                  AND event_log.is_success = FALSE
                  AND event_log.failure_detail =
                      'Access Token 無效'
                  AND event_log.user_id IS NULL
                  AND event_log.refresh_token_id IS NULL
                  AND event_log.email IS NULL
                  AND event_log.session_id IS NULL
                """,
                Long.class
            );

        String storedFailureDetail =
            jdbcTemplate.queryForObject(
                """
                SELECT failure_detail
                FROM user_auth_event_logs
                """,
                String.class
            );

        assertThat(invalidEventCount)
            .isEqualTo(1);

        assertThat(storedFailureDetail)
            .isEqualTo("Access Token 無效")
            .doesNotContain(invalidAccessToken);
    }

    @Test
    void expiredAccessTokenReturnsUnauthorizedAndWritesAuditLog()
        throws Exception {

        String expiredAccessToken =
            createExpiredAccessToken();

        mockMvc.perform(
            get("/api/v1/auth/me")
                .header(
                    HttpHeaders.AUTHORIZATION,
                    "Bearer " + expiredAccessToken
                )
                .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isUnauthorized())
        .andExpect(
            content().contentTypeCompatibleWith(
                MediaType.APPLICATION_JSON
            )
        )
        .andExpect(
            jsonPath("$.status").value(401)
        )
        .andExpect(
            jsonPath("$.error")
                .value("Unauthorized")
        )
        .andExpect(
            jsonPath("$.code")
                .value("ACCESS_TOKEN_EXPIRED")
        )
        .andExpect(
            jsonPath("$.message")
                .value("Access Token 已過期")
        )
        .andExpect(
            jsonPath("$.path")
                .value("/api/v1/auth/me")
        );

        Long expiredEventCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM user_auth_event_logs event_log
                JOIN user_auth_event_types event_type
                  ON event_type.id =
                     event_log.user_auth_event_type_id
                WHERE event_type.code =
                      'ACCESS_TOKEN_EXPIRED'
                  AND event_log.is_success = FALSE
                  AND event_log.failure_detail =
                      'Access Token 已過期'
                  AND event_log.user_id IS NULL
                  AND event_log.refresh_token_id IS NULL
                  AND event_log.email IS NULL
                  AND event_log.session_id IS NULL
                """,
                Long.class
            );

        String storedFailureDetail =
            jdbcTemplate.queryForObject(
                """
                SELECT failure_detail
                FROM user_auth_event_logs
                """,
                String.class
            );

        assertThat(expiredEventCount)
            .isEqualTo(1);

        assertThat(storedFailureDetail)
            .isEqualTo("Access Token 已過期")
            .doesNotContain(expiredAccessToken);
    }

    @Test
    void refreshTokenRotationRevokesOldTokenAndCreatesNewToken()
        throws Exception {

        String loginRequestBody = """
            {
                "email": "test@example.com",
                "password": "Password123!"
            }
            """;

        MvcResult loginResult = mockMvc.perform(
            post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(loginRequestBody)
        )
        .andExpect(status().isOk())
        .andReturn();

        String loginResponseBody =
            loginResult
                .getResponse()
                .getContentAsString();

        String refreshTokenA =
            JsonPath.read(
                loginResponseBody,
                "$.refreshToken"
            );

        Long refreshTokenAId =
            jdbcTemplate.queryForObject(
                """
                SELECT id
                FROM user_refresh_tokens
                WHERE user_id = 100
                """,
                Long.class
        );

        String originalSessionId =
            jdbcTemplate.queryForObject(
                """
                SELECT session_id
                FROM user_login_logs
                WHERE user_id = 100
                """,
                String.class
            );

        String refreshRequestBody = """
            {
                "refreshToken": "%s"
            }
            """.formatted(refreshTokenA);

        MvcResult refreshResult = mockMvc.perform(
            post("/api/v1/auth/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(refreshRequestBody)
        )
        .andExpect(status().isOk())
        .andExpect(
            jsonPath("$.accessToken")
                .isNotEmpty()
        )
        .andExpect(
            jsonPath("$.refreshToken")
                .isNotEmpty()
        )
        .andExpect(
            jsonPath("$.tokenType")
                .value("Bearer")
        )
        .andExpect(
            jsonPath("$.expiresIn")
                .value(900)
        )
        .andExpect(
            jsonPath("$.refreshTokenExpiresIn")
                .value(2592000)
        )
        .andReturn();

        String refreshResponseBody =
            refreshResult
                .getResponse()
                .getContentAsString();

        String refreshTokenB =
            JsonPath.read(
                refreshResponseBody,
                "$.refreshToken"
            );

        assertThat(refreshTokenB)
            .isNotBlank()
            .isNotEqualTo(refreshTokenA);

        Long refreshTokenBId =
            jdbcTemplate.queryForObject(
                """
                SELECT id
                FROM user_refresh_tokens
                WHERE user_id = 100
                  AND revoked_date IS NULL
                """,
                Long.class
            );

        String refreshTokenBHash =
            jdbcTemplate.queryForObject(
                """
                SELECT token_hash
                FROM user_refresh_tokens
                WHERE id = ?
                """,
                String.class,
                refreshTokenBId
            );

        Long oldTokenRevokedAndUsedCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM user_refresh_tokens
                WHERE id = ?
                  AND revoked_date IS NOT NULL
                  AND last_used_date IS NOT NULL
                """,
                Long.class,
                refreshTokenAId
            );

        Long totalRefreshTokenCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM user_refresh_tokens
                WHERE user_id = 100
                """,
                Long.class
            );

        Long loginLogRefreshTokenId =
            jdbcTemplate.queryForObject(
                """
                SELECT refresh_token_id
                FROM user_login_logs
                WHERE user_id = 100
                """,
                Long.class
            );

        String currentSessionId =
            jdbcTemplate.queryForObject(
                """
                SELECT session_id
                FROM user_login_logs
                WHERE user_id = 100
                """,
                String.class
            );

        Long refreshSuccessEventCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM user_auth_event_logs event_log
                JOIN user_auth_event_types event_type
                  ON event_type.id =
                     event_log.user_auth_event_type_id
                WHERE event_type.code =
                      'TOKEN_REFRESH_SUCCESS'
                  AND event_log.user_id = 100
                  AND event_log.refresh_token_id = ?
                  AND event_log.is_success = TRUE
                  AND event_log.failure_detail IS NULL
                  AND event_log.session_id = ?
                """,
                Long.class,
                refreshTokenAId,
                originalSessionId
            );

        assertThat(refreshTokenAId)
            .isNotEqualTo(refreshTokenBId);

        assertThat(oldTokenRevokedAndUsedCount)
            .isEqualTo(1);

        assertThat(totalRefreshTokenCount)
            .isEqualTo(2);

        assertThat(refreshTokenBHash)
            .isNotBlank()
            .hasSize(64)
            .matches("[0-9a-f]{64}")
            .isNotEqualTo(refreshTokenB);

        assertThat(loginLogRefreshTokenId)
            .isEqualTo(refreshTokenBId);

        assertThat(currentSessionId)
            .isEqualTo(originalSessionId);

        assertThat(refreshSuccessEventCount)
            .isEqualTo(1);
    }

    @Test
    void reusedRefreshTokenRevokesAllActiveUserTokens()
        throws Exception {

        String loginRequestBody = """
            {
                "email": "test@example.com",
                "password": "Password123!"
            }
            """;

        MvcResult loginResult = mockMvc.perform(
            post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(loginRequestBody)
        )
        .andExpect(status().isOk())
        .andReturn();

        String refreshTokenA =
            JsonPath.read(
                loginResult
                    .getResponse()
                    .getContentAsString(),
                "$.refreshToken"
            );

        Long refreshTokenAId =
            jdbcTemplate.queryForObject(
                """
                SELECT id
                FROM user_refresh_tokens
                WHERE user_id = 100
                """,
                Long.class
            );

        String firstRefreshRequestBody = """
            {
                "refreshToken": "%s"
            }
            """.formatted(refreshTokenA);

        MvcResult firstRefreshResult =
            mockMvc.perform(
                post("/api/v1/auth/refresh")
                    .contentType(
                        MediaType.APPLICATION_JSON
                    )
                    .accept(
                        MediaType.APPLICATION_JSON
                    )
                    .content(
                        firstRefreshRequestBody
                    )
            )
            .andExpect(status().isOk())
            .andReturn();

        String refreshTokenB =
            JsonPath.read(
                firstRefreshResult
                    .getResponse()
                    .getContentAsString(),
                "$.refreshToken"
            );

        Long refreshTokenBId =
            jdbcTemplate.queryForObject(
                """
                SELECT id
                FROM user_refresh_tokens
                WHERE user_id = 100
                  AND revoked_date IS NULL
                """,
                Long.class
            );

        assertThat(refreshTokenB)
            .isNotBlank()
            .isNotEqualTo(refreshTokenA);

        /*
         * 再次使用已輪替並撤銷的 Token A。
         */
        mockMvc.perform(
            post("/api/v1/auth/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(firstRefreshRequestBody)
        )
        .andExpect(status().isUnauthorized())
        .andExpect(
            jsonPath("$.status").value(401)
        )
        .andExpect(
            jsonPath("$.code")
                .value("UNAUTHORIZED")
        )
        .andExpect(
            jsonPath("$.message")
                .value("Refresh Token 無效或已過期")
        )
        .andExpect(
            jsonPath("$.path")
                .value("/api/v1/auth/refresh")
        );

        Long tokenAStateCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM user_refresh_tokens
                WHERE id = ?
                  AND revoked_date IS NOT NULL
                  AND last_used_date IS NOT NULL
                """,
                Long.class,
                refreshTokenAId
            );

        Long tokenBRevokedCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM user_refresh_tokens
                WHERE id = ?
                  AND revoked_date IS NOT NULL
                """,
                Long.class,
                refreshTokenBId
            );

        Long activeTokenCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM user_refresh_tokens
                WHERE user_id = 100
                  AND revoked_date IS NULL
                """,
                Long.class
            );

        Long reuseEventCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM user_auth_event_logs event_log
                JOIN user_auth_event_types event_type
                  ON event_type.id =
                     event_log.user_auth_event_type_id
                WHERE event_type.code =
                      'REFRESH_TOKEN_REUSE_DETECTED'
                  AND event_log.user_id = 100
                  AND event_log.refresh_token_id = ?
                  AND event_log.is_success = FALSE
                  AND event_log.failure_detail =
                      '偵測到已使用過的 Refresh Token 再次被使用'
                  AND event_log.email =
                      'test@example.com'
                """,
                Long.class,
                refreshTokenAId
            );

        assertThat(tokenAStateCount)
            .isEqualTo(1);

        assertThat(tokenBRevokedCount)
            .isEqualTo(1);

        assertThat(activeTokenCount)
            .isZero();

        assertThat(reuseEventCount)
            .isEqualTo(1);
    }

    @Test
    void logoutRevokesTokenAndRepeatedLogoutWritesInvalidEvent()
        throws Exception {

        String loginRequestBody = """
            {
                "email": "test@example.com",
                "password": "Password123!"
            }
            """;

        MvcResult loginResult = mockMvc.perform(
            post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(loginRequestBody)
        )
        .andExpect(status().isOk())
        .andReturn();

        String refreshToken =
            JsonPath.read(
                loginResult
                    .getResponse()
                    .getContentAsString(),
                "$.refreshToken"
            );

        Long refreshTokenId =
            jdbcTemplate.queryForObject(
                """
                SELECT id
                FROM user_refresh_tokens
                WHERE user_id = 100
                """,
                Long.class
            );

        String sessionId =
            jdbcTemplate.queryForObject(
                """
                SELECT session_id
                FROM user_login_logs
                WHERE user_id = 100
                """,
                String.class
            );

        String logoutRequestBody = """
            {
                "refreshToken": "%s"
            }
            """.formatted(refreshToken);

        /*
         * 第一次登出。
         */
        mockMvc.perform(
            post("/api/v1/auth/logout")
                .contentType(MediaType.APPLICATION_JSON)
                .content(logoutRequestBody)
        )
        .andExpect(status().isNoContent())
        .andExpect(content().string(""));

        Long revokedTokenCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM user_refresh_tokens
                WHERE id = ?
                  AND revoked_date IS NOT NULL
                  AND last_used_date IS NULL
                """,
                Long.class,
                refreshTokenId
            );

        Long loggedOutLoginLogCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM user_login_logs
                WHERE user_id = 100
                  AND refresh_token_id = ?
                  AND session_id = ?
                  AND logout_time IS NOT NULL
                """,
                Long.class,
                refreshTokenId,
                sessionId
            );

        Long logoutSuccessEventCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM user_auth_event_logs event_log
                JOIN user_auth_event_types event_type
                  ON event_type.id =
                     event_log.user_auth_event_type_id
                WHERE event_type.code =
                      'LOGOUT_SUCCESS'
                  AND event_log.user_id = 100
                  AND event_log.refresh_token_id = ?
                  AND event_log.is_success = TRUE
                  AND event_log.failure_detail IS NULL
                  AND event_log.email =
                      'test@example.com'
                  AND event_log.session_id = ?
                """,
                Long.class,
                refreshTokenId,
                sessionId
            );

        assertThat(revokedTokenCount)
            .isEqualTo(1);

        assertThat(loggedOutLoginLogCount)
            .isEqualTo(1);

        assertThat(logoutSuccessEventCount)
            .isEqualTo(1);

        /*
         * 使用相同 Refresh Token 第二次登出。
         */
        mockMvc.perform(
            post("/api/v1/auth/logout")
                .contentType(MediaType.APPLICATION_JSON)
                .content(logoutRequestBody)
        )
        .andExpect(status().isNoContent())
        .andExpect(content().string(""));

        Long logoutInvalidEventCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM user_auth_event_logs event_log
                JOIN user_auth_event_types event_type
                  ON event_type.id =
                     event_log.user_auth_event_type_id
                WHERE event_type.code =
                      'LOGOUT_INVALID_TOKEN'
                  AND event_log.user_id = 100
                  AND event_log.refresh_token_id = ?
                  AND event_log.is_success = FALSE
                  AND event_log.failure_detail =
                      '登出使用的 Refresh Token 已經被撤銷'
                  AND event_log.email =
                      'test@example.com'
                  AND event_log.session_id IS NULL
                """,
                Long.class,
                refreshTokenId
            );

        Long logoutTimeCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM user_login_logs
                WHERE user_id = 100
                  AND logout_time IS NOT NULL
                """,
                Long.class
            );

        assertThat(logoutInvalidEventCount)
            .isEqualTo(1);

        /*
         * 重複登出不能建立第二筆 Login Log，
         * 也不能清除原本的 logout_time。
         */
        assertThat(logoutTimeCount)
            .isEqualTo(1);
    }

    @Test
    void logoutWithUnknownTokenReturnsNoContentAndWritesInvalidEvent()
        throws Exception {

        String unknownRefreshToken =
            "unknown-refresh-token-that-does-not-exist";

        String logoutRequestBody = """
            {
                "refreshToken": "%s"
            }
            """.formatted(unknownRefreshToken);

        mockMvc.perform(
            post("/api/v1/auth/logout")
                .contentType(MediaType.APPLICATION_JSON)
                .content(logoutRequestBody)
        )
        .andExpect(status().isNoContent())
        .andExpect(content().string(""));

        Long invalidLogoutEventCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM user_auth_event_logs event_log
                JOIN user_auth_event_types event_type
                  ON event_type.id =
                     event_log.user_auth_event_type_id
                WHERE event_type.code =
                      'LOGOUT_INVALID_TOKEN'
                  AND event_log.is_success = FALSE
                  AND event_log.failure_detail =
                      '找不到登出使用的 Refresh Token'
                  AND event_log.user_id IS NULL
                  AND event_log.refresh_token_id IS NULL
                  AND event_log.email IS NULL
                  AND event_log.session_id IS NULL
                """,
                Long.class
            );

        String storedFailureDetail =
            jdbcTemplate.queryForObject(
                """
                SELECT failure_detail
                FROM user_auth_event_logs
                """,
                String.class
            );

        Long refreshTokenCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM user_refresh_tokens
                """,
                Long.class
            );

        Long loginLogCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM user_login_logs
                """,
                Long.class
            );

        assertThat(invalidLogoutEventCount)
            .isEqualTo(1);

        assertThat(storedFailureDetail)
            .isEqualTo(
                "找不到登出使用的 Refresh Token"
            )
            .doesNotContain(unknownRefreshToken);

        assertThat(refreshTokenCount)
            .isZero();

        assertThat(loginLogCount)
            .isZero();
    }

    @Test
    void refreshWithUnknownTokenReturnsUnauthorizedAndWritesInvalidEvent()
        throws Exception {

        String unknownRefreshToken =
            "unknown-refresh-token-that-does-not-exist";

        String requestBody = """
            {
            "refreshToken": "%s"
            }
            """.formatted(unknownRefreshToken);

        mockMvc.perform(
                post("/api/v1/auth/refresh")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody)
            )
            .andExpect(status().isUnauthorized())
            .andExpect(
                content().contentTypeCompatibleWith(
                    MediaType.APPLICATION_JSON
                )
            )
            .andExpect(jsonPath("$.status").value(401))
            .andExpect(
                jsonPath("$.error").value("Unauthorized")
            )
            .andExpect(
                jsonPath("$.code").value("UNAUTHORIZED")
            )
            .andExpect(
                jsonPath("$.message")
                    .value("Refresh Token 無效或已過期")
            )
            .andExpect(
                jsonPath("$.path")
                    .value("/api/v1/auth/refresh")
            )
            .andExpect(jsonPath("$.fieldErrors").isArray())
            .andExpect(jsonPath("$.fieldErrors").isEmpty());

        Long invalidEventCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM user_auth_event_logs event_log
                JOIN user_auth_event_types event_type
                  ON event_type.id =
                     event_log.user_auth_event_type_id
                WHERE event_type.code =
                      'REFRESH_TOKEN_INVALID'
                  AND event_log.is_success = FALSE
                  AND event_log.failure_detail =
                      '找不到對應的 Refresh Token'
                  AND event_log.user_id IS NULL
                  AND event_log.refresh_token_id IS NULL
                  AND event_log.email IS NULL
                  AND event_log.session_id IS NULL
                """,
                Long.class
            );

        String storedFailureDetail =
            jdbcTemplate.queryForObject(
                """
                SELECT failure_detail
                FROM user_auth_event_logs
                """,
                String.class
            );

        Long refreshTokenCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM user_refresh_tokens
                """,
                Long.class
            );

        assertThat(invalidEventCount).isEqualTo(1);

        assertThat(storedFailureDetail)
            .isEqualTo("找不到對應的 Refresh Token")
            .doesNotContain(unknownRefreshToken);

        assertThat(refreshTokenCount).isZero();
    }

    @Test
    void refreshWithExpiredTokenReturnsUnauthorizedAndWritesExpiredEvent()
        throws Exception {

        String loginRequestBody = """
            {
              "email": "test@example.com",
              "password": "Password123!"
            }
            """;

        MvcResult loginResult =
            mockMvc.perform(
                    post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequestBody)
                )
                .andExpect(status().isOk())
                .andReturn();

        String loginResponseBody =
            loginResult
                .getResponse()
                .getContentAsString();

        String refreshToken =
            JsonPath.read(
                loginResponseBody,
                "$.refreshToken"
            );

        Long refreshTokenId =
            jdbcTemplate.queryForObject(
                """
                SELECT id
                FROM user_refresh_tokens
                WHERE user_id = 100
                """,
                Long.class
            );

        /*
         * 將剛建立的 Refresh Token 人工改成過期，
         * 模擬使用者拿過期 Token 呼叫 /refresh。
         */
        jdbcTemplate.update(
            """
            UPDATE user_refresh_tokens
            SET expires_date = DATEADD(
                'SECOND',
                -1,
                CURRENT_TIMESTAMP
            )
            WHERE id = ?
            """,
            refreshTokenId
        );

        String refreshRequestBody = """
            {
              "refreshToken": "%s"
            }
            """.formatted(refreshToken);

        mockMvc.perform(
            post("/api/v1/auth/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .content(refreshRequestBody)
        )
        .andExpect(status().isUnauthorized())
        .andExpect(
            content().contentTypeCompatibleWith(
                MediaType.APPLICATION_JSON
            )
        )
        .andExpect(jsonPath("$.status").value(401))
        .andExpect(
            jsonPath("$.error").value("Unauthorized")
        )
        .andExpect(
            jsonPath("$.code").value("UNAUTHORIZED")
        )
        .andExpect(
            jsonPath("$.message")
                .value("Refresh Token 無效或已過期")
        )
        .andExpect(
            jsonPath("$.path")
                .value("/api/v1/auth/refresh")
        )
        .andExpect(jsonPath("$.fieldErrors").isEmpty());

        Long expiredEventCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM user_auth_event_logs event_log
                JOIN user_auth_event_types event_type
                  ON event_type.id =
                     event_log.user_auth_event_type_id
                WHERE event_type.code =
                      'REFRESH_TOKEN_EXPIRED'
                  AND event_log.user_id = 100
                  AND event_log.refresh_token_id = ?
                  AND event_log.is_success = FALSE
                  AND event_log.failure_detail =
                      'Refresh Token 已過期'
                  AND event_log.email =
                      'test@example.com'
                  AND event_log.session_id IS NULL
                """,
                Long.class,
                refreshTokenId
            );

        Long revokedTokenCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM user_refresh_tokens
                WHERE id = ?
                  AND revoked_date IS NOT NULL
                """,
                Long.class,
                refreshTokenId
            );

        assertThat(expiredEventCount).isEqualTo(1);
        assertThat(revokedTokenCount).isEqualTo(1);
    }

    @Test
    void refreshWithLoggedOutTokenReturnsUnauthorizedAndWritesRevokedEvent()
        throws Exception {

        String loginRequestBody = """
            {
              "email": "test@example.com",
              "password": "Password123!"
            }
            """;

        MvcResult loginResult =
            mockMvc.perform(
                    post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequestBody)
                )
                .andExpect(status().isOk())
                .andReturn();

        String loginResponseBody =
            loginResult
                .getResponse()
                .getContentAsString();

        String refreshToken =
            JsonPath.read(
                loginResponseBody,
                "$.refreshToken"
            );

        Long refreshTokenId =
            jdbcTemplate.queryForObject(
                """
                SELECT id
                FROM user_refresh_tokens
                WHERE user_id = 100
                """,
                Long.class
            );

        String tokenRequestBody = """
            {
              "refreshToken": "%s"
            }
            """.formatted(refreshToken);

        /*
         * 第一次使用 Token 登出，Token 應被撤銷。
         */
        mockMvc.perform(
                post("/api/v1/auth/logout")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(tokenRequestBody)
            )
            .andExpect(status().isNoContent())
            .andExpect(content().string(""));

        Long revokedAfterLogoutCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM user_refresh_tokens
                WHERE id = ?
                  AND revoked_date IS NOT NULL
                  AND last_used_date IS NULL
                """,
                Long.class,
                refreshTokenId
            );

        assertThat(revokedAfterLogoutCount).isEqualTo(1);

        /*
         * 再使用已被 logout 撤銷的 Token 呼叫 /refresh。
         */
        mockMvc.perform(
                post("/api/v1/auth/refresh")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(tokenRequestBody)
            )
            .andExpect(status().isUnauthorized())
            .andExpect(
                content().contentTypeCompatibleWith(
                    MediaType.APPLICATION_JSON
                )
            )
            .andExpect(jsonPath("$.status").value(401))
            .andExpect(
                jsonPath("$.error").value("Unauthorized")
            )
            .andExpect(
                jsonPath("$.code").value("UNAUTHORIZED")
            )
            .andExpect(
                jsonPath("$.message")
                    .value("Refresh Token 無效或已過期")
            )
            .andExpect(
                jsonPath("$.path")
                    .value("/api/v1/auth/refresh")
            )
            .andExpect(jsonPath("$.fieldErrors").isEmpty());

        Long revokedEventCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM user_auth_event_logs event_log
                JOIN user_auth_event_types event_type
                  ON event_type.id =
                     event_log.user_auth_event_type_id
                WHERE event_type.code =
                      'REFRESH_TOKEN_REVOKED'
                  AND event_log.user_id = 100
                  AND event_log.refresh_token_id = ?
                  AND event_log.is_success = FALSE
                  AND event_log.failure_detail =
                      'Refresh Token 已被撤銷'
                  AND event_log.email =
                      'test@example.com'
                  AND event_log.session_id IS NULL
                """,
                Long.class,
                refreshTokenId
            );

        Long refreshTokenCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM user_refresh_tokens
                WHERE user_id = 100
                """,
                Long.class
            );

        assertThat(revokedEventCount).isEqualTo(1);

        /*
         * 使用撤銷的 Token 不可以建立新的 Refresh Token。
         */
        assertThat(refreshTokenCount).isEqualTo(1);
    }

    @Test
    void refreshReloadsLatestRolesAndPermissions()
        throws Exception {

        String loginRequestBody = """
            {
              "email": "test@example.com",
              "password": "Password123!"
            }
            """;

        MvcResult loginResult =
            mockMvc.perform(
                    post("/api/v1/auth/login")
                        .contentType(
                            MediaType.APPLICATION_JSON
                        )
                        .accept(
                            MediaType.APPLICATION_JSON
                        )
                        .content(loginRequestBody)
                )
                .andExpect(status().isOk())
                .andReturn();

        String loginResponseBody =
            loginResult
                .getResponse()
                .getContentAsString();

        String originalAccessToken =
            JsonPath.read(
                loginResponseBody,
                "$.accessToken"
            );

        String refreshToken =
            JsonPath.read(
                loginResponseBody,
                "$.refreshToken"
            );

        Jwt originalJwt =
            jwtDecoder.decode(
                originalAccessToken
            );

        assertThat(
            originalJwt.getClaimAsStringList(
                "roles"
            )
        )
            .containsExactly("USER");

        assertThat(
            originalJwt.getClaimAsStringList(
                "permissions"
            )
        )
            .containsExactly(
                "PROFILE_READ_SELF"
            );

        /*
         * 登入完成後才指派 SUPPORT。
         * 舊 Access Token 不會自動改變。
         */
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
                2,
                100,
                2,
                TRUE,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP
            )
            """
        );

        assertThat(
            originalJwt.getClaimAsStringList(
                "roles"
            )
        )
            .containsExactly("USER");

        String refreshRequestBody = """
            {
              "refreshToken": "%s"
            }
            """.formatted(refreshToken);

        MvcResult refreshResult =
            mockMvc.perform(
                    post("/api/v1/auth/refresh")
                        .contentType(
                            MediaType.APPLICATION_JSON
                        )
                        .accept(
                            MediaType.APPLICATION_JSON
                        )
                        .content(refreshRequestBody)
                )
                .andExpect(status().isOk())
                .andExpect(
                    jsonPath("$.accessToken")
                        .isNotEmpty()
                )
                .andExpect(
                    jsonPath("$.refreshToken")
                        .isNotEmpty()
                )
                .andReturn();

        String refreshResponseBody =
            refreshResult
                .getResponse()
                .getContentAsString();

        String newAccessToken =
            JsonPath.read(
                refreshResponseBody,
                "$.accessToken"
            );

        Jwt newJwt =
            jwtDecoder.decode(
                newAccessToken
            );

        assertThat(
            newJwt.getClaimAsStringList(
                "roles"
            )
        )
            .containsExactly(
                "SUPPORT",
                "USER"
            );

        assertThat(
            newJwt.getClaimAsStringList(
                "permissions"
            )
        )
            .containsExactly(
                "PROFILE_READ_SELF",
                "USER_READ"
            );

        /*
         * 舊 Token 仍然保持簽發當下的 USER。
         */
        Jwt decodedOriginalJwtAgain =
            jwtDecoder.decode(
                originalAccessToken
            );

        assertThat(
            decodedOriginalJwtAgain
                .getClaimAsStringList(
                    "roles"
                )
        )
            .containsExactly("USER");
    }

    @Test
    void userWithoutUserReadPermissionReturnsForbidden()
        throws Exception {

        String loginRequestBody = """
            {
              "email": "test@example.com",
              "password": "Password123!"
            }
            """;

        MvcResult loginResult =
            mockMvc.perform(
                    post("/api/v1/auth/login")
                        .contentType(
                            MediaType.APPLICATION_JSON
                        )
                        .content(loginRequestBody)
                )
                .andExpect(status().isOk())
                .andReturn();

        String responseBody =
            loginResult
                .getResponse()
                .getContentAsString();

        String accessToken =
            JsonPath.read(
                responseBody,
                "$.accessToken"
            );

        mockMvc.perform(
                get(
                    "/api/v1/test/authorization/user-read"
                )
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
                    )
                    .accept(
                        MediaType.APPLICATION_JSON
                    )
            )
            .andExpect(status().isForbidden())
            .andExpect(
                content().contentTypeCompatibleWith(
                    MediaType.APPLICATION_JSON
                )
            )
            .andExpect(
                jsonPath("$.status")
                    .value(403)
            )
            .andExpect(
                jsonPath("$.error")
                    .value("Forbidden")
            )
            .andExpect(
                jsonPath("$.code")
                    .value("ACCESS_DENIED")
            )
            .andExpect(
                jsonPath("$.message")
                    .value(
                        "你沒有執行此操作的權限"
                    )
            )
            .andExpect(
                jsonPath("$.path")
                    .value(
                        "/api/v1/test/authorization/user-read"
                    )
            )
            .andExpect(
                jsonPath("$.fieldErrors")
                    .isEmpty()
            );
    }

    @Test
    void supportWithUserReadPermissionCanAccessProtectedApi()
        throws Exception {

        /*
         * 在登入以前增加 SUPPORT，
         * 讓 Access Token 包含 USER_READ。
         */
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
                2,
                100,
                2,
                TRUE,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP
            )
            """
        );

        String loginRequestBody = """
            {
              "email": "test@example.com",
              "password": "Password123!"
            }
            """;

        MvcResult loginResult =
            mockMvc.perform(
                    post("/api/v1/auth/login")
                        .contentType(
                            MediaType.APPLICATION_JSON
                        )
                        .content(loginRequestBody)
                )
                .andExpect(status().isOk())
                .andReturn();

        String responseBody =
            loginResult
                .getResponse()
                .getContentAsString();

        String accessToken =
            JsonPath.read(
                responseBody,
                "$.accessToken"
            );

        Jwt jwt =
            jwtDecoder.decode(
                accessToken
            );

        assertThat(
            jwt.getClaimAsStringList(
                "roles"
            )
        )
            .containsExactly(
                "SUPPORT",
                "USER"
            );

        assertThat(
            jwt.getClaimAsStringList(
                "permissions"
            )
        )
            .containsExactly(
                "PROFILE_READ_SELF",
                "USER_READ"
            );

        mockMvc.perform(
                get(
                    "/api/v1/test/authorization/user-read"
                )
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
                    )
                    .accept(
                        MediaType.APPLICATION_JSON
                    )
            )
            .andExpect(status().isOk())
            .andExpect(
                content().contentTypeCompatibleWith(
                    MediaType.APPLICATION_JSON
                )
            )
            .andExpect(
                jsonPath("$.result")
                    .value("allowed")
            );
    }

    @Test
    void userWithoutSupportRoleReturnsForbidden()
        throws Exception {

        String loginRequestBody = """
            {
              "email": "test@example.com",
              "password": "Password123!"
            }
            """;

        MvcResult loginResult =
            mockMvc.perform(
                    post("/api/v1/auth/login")
                        .contentType(
                            MediaType.APPLICATION_JSON
                        )
                        .content(loginRequestBody)
                )
                .andExpect(status().isOk())
                .andReturn();

        String responseBody =
            loginResult
                .getResponse()
                .getContentAsString();

        String accessToken =
            JsonPath.read(
                responseBody,
                "$.accessToken"
            );

        mockMvc.perform(
                get(
                    "/api/v1/test/authorization/support"
                )
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
                    )
                    .accept(
                        MediaType.APPLICATION_JSON
                    )
            )
            .andExpect(status().isForbidden())
            .andExpect(
                jsonPath("$.status")
                    .value(403)
            )
            .andExpect(
                jsonPath("$.code")
                    .value("ACCESS_DENIED")
            )
            .andExpect(
                jsonPath("$.message")
                    .value(
                        "你沒有執行此操作的權限"
                    )
            )
            .andExpect(
                jsonPath("$.path")
                    .value(
                        "/api/v1/test/authorization/support"
                    )
            );
    }

    @Test
    void supportRoleCanAccessSupportApi()
        throws Exception {

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
                2,
                100,
                2,
                TRUE,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP
            )
            """
        );

        String loginRequestBody = """
            {
              "email": "test@example.com",
              "password": "Password123!"
            }
            """;

        MvcResult loginResult =
            mockMvc.perform(
                    post("/api/v1/auth/login")
                        .contentType(
                            MediaType.APPLICATION_JSON
                        )
                        .content(loginRequestBody)
                )
                .andExpect(status().isOk())
                .andReturn();

        String responseBody =
            loginResult
                .getResponse()
                .getContentAsString();

        String accessToken =
            JsonPath.read(
                responseBody,
                "$.accessToken"
            );

        Jwt jwt =
            jwtDecoder.decode(
                accessToken
            );

        assertThat(
            jwt.getClaimAsStringList(
                "roles"
            )
        )
            .containsExactly(
                "SUPPORT",
                "USER"
            );

        mockMvc.perform(
                get(
                    "/api/v1/test/authorization/support"
                )
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
                    )
                    .accept(
                        MediaType.APPLICATION_JSON
                    )
            )
            .andExpect(status().isOk())
            .andExpect(
                jsonPath("$.result")
                    .value("support-allowed")
            );
    }

    @Test
    void registrationAssignsOnlyDefaultUserRole()
        throws Exception {

        String requestBody = """
            {
              "countryId": null,
              "timezoneId": null,
              "languageId": 1,
              "name": "New User",
              "nickname": "Newbie",
              "email": "new-user@example.com",
              "password": "Password123!",
              "birthday": "1995-05-20",
              "phone": "+886923456789"
            }
            """;

        MvcResult result =
            mockMvc.perform(
                    post("/api/v1/registration")
                        .contentType(
                            MediaType.APPLICATION_JSON
                        )
                        .accept(
                            MediaType.APPLICATION_JSON
                        )
                        .content(requestBody)
                )
                .andExpect(status().isCreated())
                .andExpect(
                    content().contentTypeCompatibleWith(
                        MediaType.APPLICATION_JSON
                    )
                )
                .andExpect(
                    jsonPath("$.id").isNumber()
                )
                .andExpect(
                    jsonPath("$.userNumber")
                        .isNotEmpty()
                )
                .andExpect(
                    jsonPath("$.email")
                        .value(
                            "new-user@example.com"
                        )
                )
                .andExpect(
                    jsonPath("$.emailVerified")
                        .value(false)
                )
                .andExpect(
                    jsonPath("$.smsVerified")
                        .value(false)
                )
                .andExpect(
                    jsonPath("$.active")
                        .value(false)
                )
                .andExpect(
                    jsonPath("$.message")
                        .value(
                            "註冊資料建立成功，請完成電子郵件與手機驗證"
                        )
                )
                .andReturn();

        String responseBody =
            result
                .getResponse()
                .getContentAsString();

        Number responseUserId =
            JsonPath.read(
                responseBody,
                "$.id"
            );

        String userNumber =
            JsonPath.read(
                responseBody,
                "$.userNumber"
            );

        long registeredUserId =
            responseUserId.longValue();

        assertThat(userNumber)
            .hasSize(10)
            .matches("[A-Z0-9]{10}");

        Long userCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM users
                WHERE id = ?
                  AND email =
                      'new-user@example.com'
                  AND email_verified = FALSE
                  AND sms_verified = FALSE
                  AND is_active = FALSE
                  AND deleted_date IS NULL
                """,
                Long.class,
                registeredUserId
            );

        Long totalRoleCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM user_roles
                WHERE user_id = ?
                  AND is_active = TRUE
                  AND deleted_date IS NULL
                """,
                Long.class,
                registeredUserId
            );

        Long userRoleCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM user_roles user_role
                JOIN roles role
                  ON role.id =
                     user_role.role_id
                WHERE user_role.user_id = ?
                  AND role.code = 'USER'
                  AND user_role.is_active = TRUE
                  AND user_role.deleted_date IS NULL
                  AND role.is_active = TRUE
                  AND role.deleted_date IS NULL
                """,
                Long.class,
                registeredUserId
            );

        Long supportRoleCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM user_roles user_role
                JOIN roles role
                  ON role.id =
                     user_role.role_id
                WHERE user_role.user_id = ?
                  AND role.code = 'SUPPORT'
                """,
                Long.class,
                registeredUserId
            );

        assertThat(userCount)
            .isEqualTo(1);

        /*
         * 新註冊者只應有一個預設角色。
         */
        assertThat(totalRoleCount)
            .isEqualTo(1);

        assertThat(userRoleCount)
            .isEqualTo(1);

        /*
         * 公開註冊流程不能自行取得內部角色。
         */
        assertThat(supportRoleCount)
            .isZero();

        String storedPassword =
            jdbcTemplate.queryForObject(
                """
                SELECT password
                FROM users
                WHERE id = ?
                """,
                String.class,
                registeredUserId
            );

        assertThat(storedPassword)
            .isNotBlank()
            .isNotEqualTo("Password123!");

        assertThat(
            passwordEncoder.matches(
                "Password123!",
                storedPassword
            )
        )
            .isTrue();
    }

    @Test
    void registrationRollsBackWhenDefaultUserRoleIsUnavailable()
        throws Exception {

        /*
         * 模擬系統預設 USER 角色被停用。
         */
        jdbcTemplate.update(
            """
            UPDATE roles
            SET is_active = FALSE
            WHERE code = 'USER'
            """
        );

        String requestBody = """
            {
              "countryId": null,
              "timezoneId": null,
              "languageId": 1,
              "name": "Rollback User",
              "nickname": "Rollback",
              "email": "rollback-user@example.com",
              "password": "Password123!",
              "birthday": "1992-08-10",
              "phone": "+886934567890"
            }
            """;

        mockMvc.perform(
                post("/api/v1/registration")
                    .contentType(
                        MediaType.APPLICATION_JSON
                    )
                    .accept(
                        MediaType.APPLICATION_JSON
                    )
                    .content(requestBody)
            )
            .andExpect(
                status().isInternalServerError()
            )
            .andExpect(
                content().contentTypeCompatibleWith(
                    MediaType.APPLICATION_JSON
                )
            )
            .andExpect(
                jsonPath("$.status")
                    .value(500)
            )
            .andExpect(
                jsonPath("$.error")
                    .value(
                        "Internal Server Error"
                    )
            )
            .andExpect(
                jsonPath("$.code")
                    .value("REQUEST_FAILED")
            )
            .andExpect(
                jsonPath("$.message")
                    .value(
                        "系統預設 USER 角色不存在或已停用"
                    )
            )
            .andExpect(
                jsonPath("$.path")
                    .value(
                        "/api/v1/registration"
                    )
            )
            .andExpect(
                jsonPath("$.fieldErrors")
                    .isEmpty()
            );

        Long createdUserCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM users
                WHERE email =
                      'rollback-user@example.com'
                """,
                Long.class
            );

        Long createdUserRoleCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM user_roles user_role
                JOIN users registered_user
                  ON registered_user.id =
                     user_role.user_id
                WHERE registered_user.email =
                      'rollback-user@example.com'
                """,
                Long.class
            );

        assertThat(createdUserCount)
            .isZero();

        assertThat(createdUserRoleCount)
            .isZero();
    }

    @Test
    void inactiveUserRoleIsExcludedFromAccessToken()
        throws Exception {

        jdbcTemplate.update(
            """
            UPDATE user_roles
            SET is_active = FALSE
            WHERE user_id = 100
              AND role_id = 1
            """
        );

        String loginRequestBody = """
            {
              "email": "test@example.com",
              "password": "Password123!"
            }
            """;

        MvcResult loginResult =
            mockMvc.perform(
                    post("/api/v1/auth/login")
                        .contentType(
                            MediaType.APPLICATION_JSON
                        )
                        .accept(
                            MediaType.APPLICATION_JSON
                        )
                        .content(loginRequestBody)
                )
                .andExpect(status().isOk())
                .andReturn();

        String responseBody =
            loginResult
                .getResponse()
                .getContentAsString();

        String accessToken =
            JsonPath.read(
                responseBody,
                "$.accessToken"
            );

        Jwt jwt =
            jwtDecoder.decode(
                accessToken
            );

        assertThat(
            jwt.getClaimAsStringList(
                "roles"
            )
        )
            .isEmpty();

        assertThat(
            jwt.getClaimAsStringList(
                "permissions"
            )
        )
            .isEmpty();

        /*
         * JWT 本身有效，所以 /me 仍可用於
         * 前端取得目前登入者與授權狀態。
         */
        mockMvc.perform(
                get("/api/v1/auth/me")
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
                    )
                    .accept(
                        MediaType.APPLICATION_JSON
                    )
            )
            .andExpect(status().isOk())
            .andExpect(
                jsonPath("$.roles")
                    .isEmpty()
            )
            .andExpect(
                jsonPath("$.permissions")
                    .isEmpty()
            );

        /*
         * 但需要 Permission 的 API 必須拒絕。
         */
        mockMvc.perform(
                get(
                    "/api/v1/test/authorization/user-read"
                )
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
                    )
                    .accept(
                        MediaType.APPLICATION_JSON
                    )
            )
            .andExpect(status().isForbidden())
            .andExpect(
                jsonPath("$.code")
                    .value("ACCESS_DENIED")
            );
    }

    @Test
    void inactiveRolePermissionIsExcludedFromAccessToken()
        throws Exception {

        /*
         * USER 角色仍有效，但停用它與
         * PROFILE_READ_SELF 的權限關聯。
         */
        jdbcTemplate.update(
            """
            UPDATE role_permissions
            SET is_active = FALSE,
                deleted_date = CURRENT_TIMESTAMP
            WHERE role_id = 1
              AND permission_id = 1
            """
        );

        String loginRequestBody = """
            {
              "email": "test@example.com",
              "password": "Password123!"
            }
            """;

        MvcResult loginResult =
            mockMvc.perform(
                    post("/api/v1/auth/login")
                        .contentType(
                            MediaType.APPLICATION_JSON
                        )
                        .accept(
                            MediaType.APPLICATION_JSON
                        )
                        .content(loginRequestBody)
                )
                .andExpect(status().isOk())
                .andReturn();

        String responseBody =
            loginResult
                .getResponse()
                .getContentAsString();

        String accessToken =
            JsonPath.read(
                responseBody,
                "$.accessToken"
            );

        Jwt jwt =
            jwtDecoder.decode(
                accessToken
            );

        /*
         * UserRole 沒有被停用，所以 USER 仍存在。
         */
        assertThat(
            jwt.getClaimAsStringList(
                "roles"
            )
        )
            .containsExactly("USER");

        /*
         * RolePermission 已停用及軟刪除，
         * 所以 PROFILE_READ_SELF 不得進入 JWT。
         */
        assertThat(
            jwt.getClaimAsStringList(
                "permissions"
            )
        )
            .isEmpty();

        mockMvc.perform(
                get("/api/v1/auth/me")
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
                    )
                    .accept(
                        MediaType.APPLICATION_JSON
                    )
            )
            .andExpect(status().isOk())
            .andExpect(
                jsonPath("$.roles.length()")
                    .value(1)
            )
            .andExpect(
                jsonPath("$.roles[0]")
                    .value("USER")
            )
            .andExpect(
                jsonPath("$.permissions")
                    .isEmpty()
            );
    }

    @Test
    void inactivePermissionIsExcludedFromAccessToken()
        throws Exception {

        /*
         * RolePermission 關聯仍有效，
         * 但 Permission 本身已停用並軟刪除。
         */
        jdbcTemplate.update(
            """
            UPDATE permissions
            SET is_active = FALSE,
                deleted_date = CURRENT_TIMESTAMP
            WHERE id = 1
            """
        );

        String loginRequestBody = """
            {
              "email": "test@example.com",
              "password": "Password123!"
            }
            """;

        MvcResult loginResult =
            mockMvc.perform(
                    post("/api/v1/auth/login")
                        .contentType(
                            MediaType.APPLICATION_JSON
                        )
                        .accept(
                            MediaType.APPLICATION_JSON
                        )
                        .content(loginRequestBody)
                )
                .andExpect(status().isOk())
                .andReturn();

        String responseBody =
            loginResult
                .getResponse()
                .getContentAsString();

        String accessToken =
            JsonPath.read(
                responseBody,
                "$.accessToken"
            );

        Jwt jwt =
            jwtDecoder.decode(
                accessToken
            );

        /*
         * USER 角色仍有效。
         */
        assertThat(
            jwt.getClaimAsStringList("roles")
        )
            .containsExactly("USER");

        /*
         * Permission 本身已停用並軟刪除，
         * 因此不得出現在 JWT。
         */
        assertThat(
            jwt.getClaimAsStringList("permissions")
        )
            .isEmpty();

        /*
         * /me 仍可取得目前使用者，
         * 但不會回傳已停用的權限。
         */
        mockMvc.perform(
                get("/api/v1/auth/me")
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
                    )
                    .accept(
                        MediaType.APPLICATION_JSON
                    )
            )
            .andExpect(status().isOk())
            .andExpect(
                jsonPath("$.roles[0]")
                    .value("USER")
            )
            .andExpect(
                jsonPath("$.permissions")
                    .isEmpty()
            );

        /*
         * 需要該權限的 API 應回傳 403。
         */
        mockMvc.perform(
                get(
                    "/api/v1/test/authorization/user-read"
                )
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
                    )
                    .accept(
                        MediaType.APPLICATION_JSON
                    )
            )
            .andExpect(status().isForbidden())
            .andExpect(
                jsonPath("$.code")
                    .value("ACCESS_DENIED")
            );
    }

    @Test
    void inactivePermissionTypeExcludesItsPermissionsFromAccessToken()
        throws Exception {

        /*
         * Permission 與 RolePermission 都維持有效，
         * 只停用 PermissionType。
         */
        jdbcTemplate.update(
            """
            UPDATE permission_types
            SET is_active = FALSE,
                deleted_date = CURRENT_TIMESTAMP
            WHERE id = 1
            """
        );

        String loginRequestBody = """
            {
              "email": "test@example.com",
              "password": "Password123!"
            }
            """;

        MvcResult loginResult =
            mockMvc.perform(
                    post("/api/v1/auth/login")
                        .contentType(
                            MediaType.APPLICATION_JSON
                        )
                        .accept(
                            MediaType.APPLICATION_JSON
                        )
                        .content(loginRequestBody)
                )
                .andExpect(status().isOk())
                .andReturn();

        String responseBody =
            loginResult
                .getResponse()
                .getContentAsString();

        String accessToken =
            JsonPath.read(
                responseBody,
                "$.accessToken"
            );

        Jwt jwt =
            jwtDecoder.decode(accessToken);

        /*
         * UserRole 與 Role 都有效，因此 USER 角色仍保留。
         */
        assertThat(
            jwt.getClaimAsStringList("roles")
        )
            .containsExactly("USER");

        /*
         * PROFILE_READ_SELF 所屬的 PermissionType
         * 已停用及軟刪除，因此權限不得進入 JWT。
         */
        assertThat(
            jwt.getClaimAsStringList("permissions")
        )
            .isEmpty();

        mockMvc.perform(
                get("/api/v1/auth/me")
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
                    )
                    .accept(
                        MediaType.APPLICATION_JSON
                    )
            )
            .andExpect(status().isOk())
            .andExpect(
                jsonPath("$.roles.length()")
                    .value(1)
            )
            .andExpect(
                jsonPath("$.roles[0]")
                    .value("USER")
            )
            .andExpect(
                jsonPath("$.permissions")
                    .isEmpty()
            );
    }

    @Test
    void inactiveRoleExcludesRoleAndPermissionsFromAccessToken()
        throws Exception {

        /*
         * UserRole、RolePermission、Permission 都維持有效，
         * 只停用 USER Role。
         */
        jdbcTemplate.update(
            """
            UPDATE roles
            SET is_active = FALSE,
                deleted_date = CURRENT_TIMESTAMP
            WHERE id = 1
            """
        );

        String loginRequestBody = """
            {
              "email": "test@example.com",
              "password": "Password123!"
            }
            """;

        MvcResult loginResult =
            mockMvc.perform(
                    post("/api/v1/auth/login")
                        .contentType(
                            MediaType.APPLICATION_JSON
                        )
                        .accept(
                            MediaType.APPLICATION_JSON
                        )
                        .content(loginRequestBody)
                )
                .andExpect(status().isOk())
                .andReturn();

        String responseBody =
            loginResult
                .getResponse()
                .getContentAsString();

        String accessToken =
            JsonPath.read(
                responseBody,
                "$.accessToken"
            );

        Jwt jwt =
            jwtDecoder.decode(accessToken);

        /*
         * Role 本身已失效，因此不能進入 JWT。
         */
        assertThat(
            jwt.getClaimAsStringList("roles")
        )
            .isEmpty();

        /*
         * 失效 Role 底下的權限也不能生效。
         */
        assertThat(
            jwt.getClaimAsStringList("permissions")
        )
            .isEmpty();

        /*
         * JWT 本身仍是有效的登入憑證，
         * 所以 /me 可以呼叫，但沒有任何角色及權限。
         */
        mockMvc.perform(
                get("/api/v1/auth/me")
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
                    )
                    .accept(
                        MediaType.APPLICATION_JSON
                    )
            )
            .andExpect(status().isOk())
            .andExpect(
                jsonPath("$.roles")
                    .isEmpty()
            )
            .andExpect(
                jsonPath("$.permissions")
                    .isEmpty()
            );

        /*
         * 需要 USER_READ 權限的 API 必須拒絕。
         */
        mockMvc.perform(
                get(
                    "/api/v1/test/authorization/user-read"
                )
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
                    )
                    .accept(
                        MediaType.APPLICATION_JSON
                    )
            )
            .andExpect(status().isForbidden())
            .andExpect(
                jsonPath("$.code")
                    .value("ACCESS_DENIED")
            );
    }

    @Test
    void multipleRolesMergePermissionsWithoutDuplicates()
        throws Exception {

        /*
         * 指派 SUPPORT 給測試使用者。
         */
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
                2,
                100,
                2,
                TRUE,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP
            )
            """
        );

        /*
         * SUPPORT 原本已擁有 USER_READ。
         * 再讓 SUPPORT 也擁有 USER 已有的
         * PROFILE_READ_SELF，製造重複權限來源。
         */
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
                3,
                2,
                1,
                TRUE,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP
            )
            """
        );

        String loginRequestBody = """
            {
              "email": "test@example.com",
              "password": "Password123!"
                }
            """;

        MvcResult loginResult =
            mockMvc.perform(
                    post("/api/v1/auth/login")
                        .contentType(
                            MediaType.APPLICATION_JSON
                        )
                        .accept(
                            MediaType.APPLICATION_JSON
                        )
                        .content(loginRequestBody)
                )
                .andExpect(status().isOk())
                .andReturn();

        String responseBody =
            loginResult
                .getResponse()
                .getContentAsString();

        String accessToken =
            JsonPath.read(
                responseBody,
                "$.accessToken"
            );

        Jwt jwt =
            jwtDecoder.decode(accessToken);

        List<String> roles =
            jwt.getClaimAsStringList("roles");

        List<String> permissions =
            jwt.getClaimAsStringList("permissions");

        /*
         * Repository 依照角色代碼排序。
         */
        assertThat(roles)
            .containsExactly(
                "SUPPORT",
                "USER"
            );

        /*
         * PROFILE_READ_SELF 雖然由兩個角色提供，
         * JWT 中仍只能出現一次。
         */
        assertThat(permissions)
            .containsExactly(
                "PROFILE_READ_SELF",
                "USER_READ"
            );

        long profileReadSelfCount =
            permissions.stream()
                .filter(
                    "PROFILE_READ_SELF"::equals
                )
                .count();

        assertThat(profileReadSelfCount)
            .isEqualTo(1);

        /*
         * /me 回傳的授權資料也必須完成合併與去重。
         */
        mockMvc.perform(
                get("/api/v1/auth/me")
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
                    )
                    .accept(
                        MediaType.APPLICATION_JSON
                    )
            )
            .andExpect(status().isOk())
            .andExpect(
                jsonPath("$.roles.length()")
                    .value(2)
            )
            .andExpect(
                jsonPath("$.roles[0]")
                    .value("SUPPORT")
            )
            .andExpect(
                jsonPath("$.roles[1]")
                    .value("USER")
            )
            .andExpect(
                jsonPath("$.permissions.length()")
                    .value(2)
            )
            .andExpect(
                jsonPath("$.permissions[0]")
                    .value("PROFILE_READ_SELF")
            )
            .andExpect(
                jsonPath("$.permissions[1]")
                    .value("USER_READ")
            );
    }

    @Test
    void refreshTokenReloadsAuthoritiesAfterRoleIsRevoked()
        throws Exception {

        String loginRequestBody = """
            {
              "email": "test@example.com",
              "password": "Password123!"
            }
            """;

        /*
         * 先正常登入。
         */
        MvcResult loginResult =
            mockMvc.perform(
                    post("/api/v1/auth/login")
                        .contentType(
                            MediaType.APPLICATION_JSON
                        )
                        .accept(
                            MediaType.APPLICATION_JSON
                        )
                        .content(loginRequestBody)
                )
                .andExpect(status().isOk())
                .andReturn();

        String loginResponseBody =
            loginResult
                .getResponse()
                .getContentAsString();

        String originalAccessToken =
            JsonPath.read(
                loginResponseBody,
                "$.accessToken"
            );

        String refreshToken =
            JsonPath.read(
                loginResponseBody,
                "$.refreshToken"
            );

        Jwt originalJwt =
            jwtDecoder.decode(originalAccessToken);

        assertThat(
            originalJwt.getClaimAsStringList("roles")
        )
            .containsExactly("USER");

        assertThat(
            originalJwt.getClaimAsStringList(
                "permissions"
            )
        )
            .containsExactly(
                "PROFILE_READ_SELF"
            );

        /*
         * 登入後撤銷 USER 角色。
         */
        jdbcTemplate.update(
            """
            UPDATE user_roles
            SET is_active = FALSE,
                deleted_date = CURRENT_TIMESTAMP
            WHERE user_id = 100
              AND role_id = 1
            """
        );

        /*
         * 舊 Access Token 是簽發時的快照，
         * 其中的 USER 與權限仍然存在。
         */
        Jwt unchangedOriginalJwt =
            jwtDecoder.decode(originalAccessToken);

        assertThat(
            unchangedOriginalJwt.getClaimAsStringList(
                "roles"
            )
        )
            .containsExactly("USER");

        /*
         * 使用 Refresh Token 取得新的 Token。
         */
        String refreshRequestBody = """
            {
              "refreshToken": "%s"
            }
            """.formatted(refreshToken);

        MvcResult refreshResult =
            mockMvc.perform(
                    post("/api/v1/auth/refresh")
                        .contentType(
                            MediaType.APPLICATION_JSON
                        )
                        .accept(
                            MediaType.APPLICATION_JSON
                        )
                        .content(refreshRequestBody)
                )
                .andExpect(status().isOk())
                .andReturn();

        String refreshResponseBody =
            refreshResult
                .getResponse()
                .getContentAsString();

        String newAccessToken =
            JsonPath.read(
                refreshResponseBody,
                "$.accessToken"
            );

        Jwt newJwt =
            jwtDecoder.decode(newAccessToken);

        /*
         * Refresh 時會重新查詢資料庫，
         * 新 JWT 不得再包含已撤銷的角色及權限。
         */
        assertThat(
            newJwt.getClaimAsStringList("roles")
        )
            .isEmpty();

        assertThat(
            newJwt.getClaimAsStringList("permissions")
        )
            .isEmpty();

        mockMvc.perform(
                get("/api/v1/auth/me")
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + newAccessToken
                    )
                    .accept(
                        MediaType.APPLICATION_JSON
                    )
            )
            .andExpect(status().isOk())
            .andExpect(
                jsonPath("$.roles")
                    .isEmpty()
            )
            .andExpect(
                jsonPath("$.permissions")
                    .isEmpty()
            );
    }

    @Test
    void anonymousUserAccessingPermissionProtectedApiReturnsUnauthorized()
        throws Exception {

        mockMvc.perform(
                get(
                    "/api/v1/test/authorization/user-read"
                )
                    .accept(
                        MediaType.APPLICATION_JSON
                    )
            )
            .andExpect(status().isUnauthorized())
            .andExpect(
                content().contentTypeCompatibleWith(
                    MediaType.APPLICATION_JSON
                )
            )
            .andExpect(
                jsonPath("$.status")
                    .value(401)
            )
            .andExpect(
                jsonPath("$.error")
                    .value("Unauthorized")
            )
            .andExpect(
                jsonPath("$.code")
                    .value("UNAUTHORIZED")
            )
            .andExpect(
                jsonPath("$.message")
                    .value(
                        "需要有效的 Access Token"
                    )
            )
            .andExpect(
                jsonPath("$.path")
                    .value(
                        "/api/v1/test/authorization/user-read"
                    )
            )
            .andExpect(
                jsonPath("$.fieldErrors")
                    .isEmpty()
            );
    }
}
