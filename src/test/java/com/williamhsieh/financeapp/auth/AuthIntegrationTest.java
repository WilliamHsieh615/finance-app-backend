package com.williamhsieh.financeapp.auth;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;

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
        insertUser();
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

        String rawRefreshToken =
            JsonPath.read(
                responseBody,
                "$.refreshToken"
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
}
