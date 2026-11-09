package com.williamhsieh.financeapp.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.hamcrest.Matchers.nullValue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import com.jayway.jsonpath.JsonPath;
import com.williamhsieh.financeapp.support.IntegrationTestSupport;

class UserManagementIntegrationTest
    extends IntegrationTestSupport {

    @Test
    void anonymousUserCannotAccessUserManagementApi()
        throws Exception {

        mockMvc.perform(
                get("/api/v1/admin/users/100")
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
                        "/api/v1/admin/users/100"
                    )
            )
            .andExpect(
                jsonPath("$.fieldErrors")
                    .isEmpty()
            );
    }

    @Test
    void userWithoutUserReadPermissionCannotAccessUserManagementApi()
        throws Exception {

        String accessToken = loginAsDefaultUser();

        mockMvc.perform(
                get("/api/v1/admin/users/100")
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
                        "/api/v1/admin/users/100"
                    )
            )
            .andExpect(
                jsonPath("$.fieldErrors")
                    .isEmpty()
            );
    }

    @Test
    void supportWithUserReadPermissionCanGetUserDetail()
        throws Exception {

        String accessToken = loginAsSupport();

        mockMvc.perform(
                get("/api/v1/admin/users/100")
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
                jsonPath("$.id")
                    .value(100)
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
                    .value("Tester")
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
                jsonPath("$.accountStatus.id")
                    .value(2)
            )
            .andExpect(
                jsonPath("$.accountStatus.code")
                    .value("ACTIVE")
            )
            .andExpect(
                jsonPath("$.accountStatus.name")
                    .value("正常使用")
            )
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
                jsonPath("$.country")
                    .doesNotExist()
            )
            .andExpect(
                jsonPath("$.timezone")
                    .doesNotExist()
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
            )
            /*
             * 敏感欄位絕對不能回傳。
             */
            .andExpect(
                jsonPath("$.password")
                    .doesNotExist()
            )
            /*
             * 這是會員資料 API，不需要暴露合併後的權限清單。
             */
            .andExpect(
                jsonPath("$.permissions")
                    .doesNotExist()
            );
    }

    @Test
    void userReadPermissionReturnsNotFoundForUnknownUser()
        throws Exception {

        String accessToken = loginAsSupport();

        /*
         * 使用一個不存在的 userId。
         */
        mockMvc.perform(
                get("/api/v1/admin/users/999999")
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
                    )
                    .accept(
                        MediaType.APPLICATION_JSON
                    )
            )
            .andExpect(status().isNotFound())
            .andExpect(
                content().contentTypeCompatibleWith(
                    MediaType.APPLICATION_JSON
                )
            )
            .andExpect(
                jsonPath("$.status")
                    .value(404)
            )
            .andExpect(
                jsonPath("$.error")
                    .value("Not Found")
            )
            .andExpect(
                jsonPath("$.code")
                    .value("RESOURCE_NOT_FOUND")
            )
            .andExpect(
                jsonPath("$.message")
                    .value(
                        "找不到指定的使用者"
                    )
            )
            .andExpect(
                jsonPath("$.path")
                    .value(
                        "/api/v1/admin/users/999999"
                    )
            )
            .andExpect(
                jsonPath("$.fieldErrors")
                    .isEmpty()
            );
    }

    @Test
    void userReadPermissionReturnsNotFoundForSoftDeletedUser()
        throws Exception {

        /*
        * id=100 是目前登入且保持 ACTIVE 的 SUPPORT。
        */
        String accessToken =
            loginAsSupport();

        /*
        * 建立另一個 id=101 的會員，
        * 再將該會員軟刪除。
        */
        insertSearchTargetUser();

        jdbcTemplate.update(
            """
            UPDATE users
            SET user_account_status_id = 5,
                is_active = FALSE,
                deleted_date = CURRENT_TIMESTAMP
            WHERE id = 101
            """
        );

        mockMvc.perform(
                get("/api/v1/admin/users/101")
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
                    )
                    .accept(
                        MediaType.APPLICATION_JSON
                    )
            )
            .andExpect(status().isNotFound())
            .andExpect(
                content().contentTypeCompatibleWith(
                    MediaType.APPLICATION_JSON
                )
            )
            .andExpect(
                jsonPath("$.status")
                    .value(404)
            )
            .andExpect(
                jsonPath("$.error")
                    .value("Not Found")
            )
            .andExpect(
                jsonPath("$.code")
                    .value("RESOURCE_NOT_FOUND")
            )
            .andExpect(
                jsonPath("$.message")
                    .value(
                        "找不到指定的使用者"
                    )
            )
            .andExpect(
                jsonPath("$.path")
                    .value(
                        "/api/v1/admin/users/101"
                    )
            )
            .andExpect(
                jsonPath("$.fieldErrors")
                    .isEmpty()
            );

        /*
        * 確認資料沒有真的被刪除，
        * 而是保留資料並標記為 DELETED。
        */
        Long softDeletedUserCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM users app_user
                JOIN user_account_statuses account_status
                ON account_status.id =
                    app_user.user_account_status_id
                WHERE app_user.id = 101
                AND app_user.is_active = FALSE
                AND app_user.deleted_date IS NOT NULL
                AND account_status.code = 'DELETED'
                """,
                Long.class
            );

        assertThat(softDeletedUserCount)
            .isEqualTo(1);
    }
    
    @Test
    void supportWithUserReadPermissionCanGetPagedUsers()
        throws Exception {

        String accessToken = loginAsSupport();

        jdbcTemplate.update(
            """
            INSERT INTO users (
                id,
                country_id,
                timezone_id,
                language_id,
                user_account_status_id,
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
                101,
                NULL,
                NULL,
                1,
                2,
                'SECOND0001',
                'Second User',
                'Second',
                'second@example.com',
                'not-used-in-this-test',
                DATE '1991-02-02',
                '0922222222',
                TRUE,
                TRUE,
                TRUE,
                TIMESTAMP '2020-01-01 00:00:00',
                TIMESTAMP '2020-01-01 00:00:00'
            )
            """
        );

        /*
         * 第一頁，每頁一筆。
         * id=100 的 created_date 較新，所以排在第一頁。
         */
        mockMvc.perform(
                get("/api/v1/admin/users")
                    .param("page", "0")
                    .param("size", "1")
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
                jsonPath("$.content.length()")
                    .value(1)
            )
            .andExpect(
                jsonPath("$.content[0].id")
                    .value(100)
            )
            .andExpect(
                jsonPath("$.content[0].userNumber")
                    .value("TESTUSER01")
            )
            .andExpect(
                jsonPath("$.content[0].email")
                    .value("test@example.com")
            )
            .andExpect(
                jsonPath("$.content[0].active")
                    .value(true)
            )
            .andExpect(
                jsonPath("$.content[0].accountStatus.id")
                    .value(2)
            )
            .andExpect(
                jsonPath("$.content[0].accountStatus.code")
                    .value("ACTIVE")
            )
            .andExpect(
                jsonPath("$.content[0].accountStatus.name")
                    .value("正常使用")
            )
            .andExpect(
                jsonPath("$.content[0].password")
                    .doesNotExist()
            )
            .andExpect(
                jsonPath("$.content[0].birthday")
                    .doesNotExist()
            )
            .andExpect(
                jsonPath("$.content[0].phone")
                    .doesNotExist()
            )
            .andExpect(
                jsonPath("$.page")
                    .value(0)
            )
            .andExpect(
                jsonPath("$.size")
                    .value(1)
            )
            .andExpect(
                jsonPath("$.totalElements")
                    .value(2)
            )
            .andExpect(
                jsonPath("$.totalPages")
                    .value(2)
            )
            .andExpect(
                jsonPath("$.first")
                    .value(true)
            )
            .andExpect(
                jsonPath("$.last")
                    .value(false)
            );

        /*
         * 第二頁應該取得較舊的 id=101。
         */
        mockMvc.perform(
                get("/api/v1/admin/users")
                    .param("page", "1")
                    .param("size", "1")
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
                jsonPath("$.content.length()")
                    .value(1)
            )
            .andExpect(
                jsonPath("$.content[0].id")
                    .value(101)
            )
            .andExpect(
                jsonPath("$.content[0].userNumber")
                    .value("SECOND0001")
            )
            .andExpect(
                jsonPath("$.content[0].accountStatus.code")
                    .value("ACTIVE")
            )
            .andExpect(
                jsonPath("$.page")
                    .value(1)
            )
            .andExpect(
                jsonPath("$.first")
                    .value(false)
            )
            .andExpect(
                jsonPath("$.last")
                    .value(true)
            );
    }

    @Test
    void pagedUsersExcludeSoftDeletedUsers()
        throws Exception {

        String accessToken = loginAsSupport();

        jdbcTemplate.update(
            """
            INSERT INTO users (
                id,
                country_id,
                timezone_id,
                language_id,
                user_account_status_id,
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
                updated_date,
                deleted_date
            )
            VALUES (
                101,
                NULL,
                NULL,
                1,
                5,
                'DELETED001',
                'Deleted User',
                'Deleted',
                'deleted@example.com',
                'not-used-in-this-test',
                DATE '1991-02-02',
                '0933333333',
                TRUE,
                TRUE,
                FALSE,
                TIMESTAMP '2020-01-01 00:00:00',
                TIMESTAMP '2020-01-01 00:00:00',
                CURRENT_TIMESTAMP
            )
            """
        );

        mockMvc.perform(
                get("/api/v1/admin/users")
                    .param("page", "0")
                    .param("size", "20")
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
                    )
                    .accept(
                        MediaType.APPLICATION_JSON
                    )
            )
            .andExpect(status().isOk())
            /*
             * 只有未刪除的 id=100。
             */
            .andExpect(
                jsonPath("$.content.length()")
                    .value(1)
            )
            .andExpect(
                jsonPath("$.content[0].id")
                    .value(100)
            )
            .andExpect(
                jsonPath("$.content[0].email")
                    .value("test@example.com")
            )
            /*
             * countQuery 也必須排除軟刪除會員。
             */
            .andExpect(
                jsonPath("$.totalElements")
                    .value(1)
            )
            .andExpect(
                jsonPath("$.totalPages")
                    .value(1)
            )
            .andExpect(
                jsonPath("$.first")
                    .value(true)
            )
            .andExpect(
                jsonPath("$.last")
                    .value(true)
            );

        /*
         * 確認軟刪除資料其實仍存在資料庫。
         */
        Long softDeletedUserCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM users
                WHERE id = 101
                  AND deleted_date IS NOT NULL
                """,
                Long.class
            );

        assertThat(softDeletedUserCount)
            .isEqualTo(1);
    }

    @Test
    void pagedUsersRejectNegativePage()
        throws Exception {

        String accessToken = loginAsSupport();

        mockMvc.perform(
                get("/api/v1/admin/users")
                    .param("page", "-1")
                    .param("size", "20")
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
                    )
                    .accept(
                        MediaType.APPLICATION_JSON
                    )
            )
            .andExpect(status().isBadRequest())
            .andExpect(
                content().contentTypeCompatibleWith(
                    MediaType.APPLICATION_JSON
                )
            )
            .andExpect(
                jsonPath("$.status")
                    .value(400)
            )
            .andExpect(
                jsonPath("$.error")
                    .value("Bad Request")
            )
            .andExpect(
                jsonPath("$.code")
                    .value("BAD_REQUEST")
            )
            .andExpect(
                jsonPath("$.message")
                    .value("page 不得小於 0")
            )
            .andExpect(
                jsonPath("$.path")
                    .value(
                        "/api/v1/admin/users"
                    )
            )
            .andExpect(
                jsonPath("$.fieldErrors")
                    .isEmpty()
            );
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 101})
    void pagedUsersRejectSizeOutsideAllowedRange(
        int invalidSize
    ) throws Exception {

        String accessToken = loginAsSupport();

        mockMvc.perform(
                get("/api/v1/admin/users")
                    .param("page", "0")
                    .param(
                        "size",
                        String.valueOf(invalidSize)
                    )
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
                    )
                    .accept(
                        MediaType.APPLICATION_JSON
                    )
            )
            .andExpect(status().isBadRequest())
            .andExpect(
                content().contentTypeCompatibleWith(
                    MediaType.APPLICATION_JSON
                )
            )
            .andExpect(
                jsonPath("$.status")
                    .value(400)
            )
            .andExpect(
                jsonPath("$.error")
                    .value("Bad Request")
            )
            .andExpect(
                jsonPath("$.code")
                    .value("BAD_REQUEST")
            )
            .andExpect(
                jsonPath("$.message")
                    .value(
                        "size 必須介於 1 到 100"
                    )
            )
            .andExpect(
                jsonPath("$.path")
                    .value(
                        "/api/v1/admin/users"
                    )
            )
            .andExpect(
                jsonPath("$.fieldErrors")
                    .isEmpty()
            );
    }

    @ParameterizedTest
    @CsvSource({
        "page, abc",
        "size, large"
    })
    void pagedUsersRejectNonNumericPaginationParameter(
        String parameterName,
        String invalidValue
    ) throws Exception {

        String accessToken = loginAsSupport();

        mockMvc.perform(
                get("/api/v1/admin/users")
                    .param(
                        parameterName,
                        invalidValue
                    )
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
                    )
                    .accept(
                        MediaType.APPLICATION_JSON
                    )
            )
            .andExpect(status().isBadRequest())
            .andExpect(
                content().contentTypeCompatibleWith(
                    MediaType.APPLICATION_JSON
                )
            )
            .andExpect(
                jsonPath("$.status")
                    .value(400)
            )
            .andExpect(
                jsonPath("$.error")
                    .value("Bad Request")
            )
            .andExpect(
                jsonPath("$.code")
                    .value("BAD_REQUEST")
            )
            .andExpect(
                jsonPath("$.message")
                    .value(
                        "請求參數 " +
                            parameterName +
                            " 格式不正確"
                    )
            )
            .andExpect(
                jsonPath("$.path")
                    .value(
                        "/api/v1/admin/users"
                    )
            )
            .andExpect(
                jsonPath("$.fieldErrors")
                    .isEmpty()
            );
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "search0001",
        "Alice",
        "queen",
        "ALICE.CHEN@EXAMPLE.COM"
    })
    void userReadPermissionCanSearchUsersByKeyword(
        String keyword
    ) throws Exception {

        insertSearchTargetUser();

        String accessToken =
            loginAsSupport();

        mockMvc.perform(
                get("/api/v1/admin/users")
                    .param("page", "0")
                    .param("size", "20")
                    .param("keyword", keyword)
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
                jsonPath("$.content.length()")
                    .value(1)
            )
            .andExpect(
                jsonPath("$.content[0].id")
                    .value(101)
            )
            .andExpect(
                jsonPath("$.content[0].userNumber")
                    .value("SEARCH0001")
            )
            .andExpect(
                jsonPath("$.content[0].name")
                    .value("Alice Chen")
            )
            .andExpect(
                jsonPath("$.content[0].nickname")
                    .value("FinanceQueen")
            )
            .andExpect(
                jsonPath("$.content[0].email")
                    .value(
                        "alice.chen@example.com"
                    )
            )
            .andExpect(
                jsonPath("$.totalElements")
                    .value(1)
            )
            .andExpect(
                jsonPath("$.totalPages")
                    .value(1)
            );
    }

    @Test
    void userSearchReturnsEmptyPageWhenNoUsersMatch()
        throws Exception {

        String accessToken =
            loginAsSupport();

        mockMvc.perform(
                get("/api/v1/admin/users")
                    .param("page", "0")
                    .param("size", "20")
                    .param(
                        "keyword",
                        "no-such-user"
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
                jsonPath("$.content")
                    .isArray()
            )
            .andExpect(
                jsonPath("$.content")
                    .isEmpty()
            )
            .andExpect(
                jsonPath("$.page")
                    .value(0)
            )
            .andExpect(
                jsonPath("$.size")
                    .value(20)
            )
            .andExpect(
                jsonPath("$.totalElements")
                    .value(0)
            )
            .andExpect(
                jsonPath("$.totalPages")
                    .value(0)
            )
            .andExpect(
                jsonPath("$.first")
                    .value(true)
            )
            .andExpect(
                jsonPath("$.last")
                    .value(true)
            );
    }

    @Test
    void blankKeywordReturnsAllActiveUsers()
        throws Exception {

        String accessToken =
            loginAsSupport();

        mockMvc.perform(
                get("/api/v1/admin/users")
                    .param("page", "0")
                    .param("size", "20")
                    .param("keyword", "   ")
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
                jsonPath("$.content.length()")
                    .value(1)
            )
            .andExpect(
                jsonPath("$.content[0].id")
                    .value(100)
            )
            .andExpect(
                jsonPath("$.content[0].userNumber")
                    .value("TESTUSER01")
            )
            .andExpect(
                jsonPath("$.content[0].email")
                    .value("test@example.com")
            )
            .andExpect(
                jsonPath("$.totalElements")
                    .value(1)
            )
            .andExpect(
                jsonPath("$.totalPages")
                    .value(1)
            )
            .andExpect(
                jsonPath("$.first")
                    .value(true)
            )
            .andExpect(
                jsonPath("$.last")
                    .value(true)
            );
    }

    @Test
    void keywordIsTrimmedBeforeSearching()
        throws Exception {

        insertSearchTargetUser();

        String accessToken =
            loginAsSupport();

        mockMvc.perform(
                get("/api/v1/admin/users")
                    .param("page", "0")
                    .param("size", "20")
                    .param(
                        "keyword",
                        "   Alice   "
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
                jsonPath("$.content.length()")
                    .value(1)
            )
            .andExpect(
                jsonPath("$.content[0].id")
                    .value(101)
            )
            .andExpect(
                jsonPath("$.content[0].userNumber")
                    .value("SEARCH0001")
            )
            .andExpect(
                jsonPath("$.content[0].name")
                    .value("Alice Chen")
            )
            .andExpect(
                jsonPath("$.content[0].email")
                    .value(
                        "alice.chen@example.com"
                    )
            )
            .andExpect(
                jsonPath("$.totalElements")
                    .value(1)
            );
    }

    @Test
    void keywordLongerThanMaximumLengthReturnsBadRequest()
        throws Exception {

        String accessToken =
            loginAsSupport();

        String oversizedKeyword =
            "a".repeat(101);

        mockMvc.perform(
                get("/api/v1/admin/users")
                    .param("page", "0")
                    .param("size", "20")
                    .param(
                        "keyword",
                        oversizedKeyword
                    )
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
                    )
                    .accept(
                        MediaType.APPLICATION_JSON
                    )
            )
            .andExpect(status().isBadRequest())
            .andExpect(
                content().contentTypeCompatibleWith(
                    MediaType.APPLICATION_JSON
                )
            )
            .andExpect(
                jsonPath("$.status")
                    .value(400)
            )
            .andExpect(
                jsonPath("$.error")
                    .value("Bad Request")
            )
            .andExpect(
                jsonPath("$.code")
                    .value("BAD_REQUEST")
            )
            .andExpect(
                jsonPath("$.message")
                    .value(
                        "keyword 長度不得超過 100"
                    )
            )
            .andExpect(
                jsonPath("$.path")
                    .value(
                        "/api/v1/admin/users"
                    )
            )
            .andExpect(
                jsonPath("$.fieldErrors")
                    .isEmpty()
            );
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "%",
        "_"
    })
    void likeWildcardCharactersDoNotMatchAllUsers(
        String keyword
    ) throws Exception {

        String accessToken =
            loginAsSupport();

        mockMvc.perform(
                get("/api/v1/admin/users")
                    .param("page", "0")
                    .param("size", "20")
                    .param("keyword", keyword)
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
                jsonPath("$.content")
                    .isArray()
            )
            .andExpect(
                jsonPath("$.content")
                    .isEmpty()
            )
            .andExpect(
                jsonPath("$.totalElements")
                    .value(0)
            )
            .andExpect(
                jsonPath("$.totalPages")
                    .value(0)
            )
            .andExpect(
                jsonPath("$.first")
                    .value(true)
            )
            .andExpect(
                jsonPath("$.last")
                    .value(true)
            );
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "%",
        "_",
        "!"
    })
    void escapedLikeCharactersCanBeSearchedLiterally(
        String keyword
    ) throws Exception {

        insertSpecialCharacterUser();

        String accessToken =
            loginAsSupport();

        mockMvc.perform(
                get("/api/v1/admin/users")
                    .param("page", "0")
                    .param("size", "20")
                    .param("keyword", keyword)
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
                jsonPath("$.content.length()")
                    .value(1)
            )
            .andExpect(
                jsonPath("$.content[0].id")
                    .value(101)
            )
            .andExpect(
                jsonPath("$.content[0].userNumber")
                    .value("SPECIAL001")
            )
            .andExpect(
                jsonPath("$.content[0].name")
                    .value(
                        "Budget_100%! User"
                    )
            )
            .andExpect(
                jsonPath("$.content[0].email")
                    .value(
                        "special@example.com"
                    )
            )
            .andExpect(
                jsonPath("$.totalElements")
                    .value(1)
            );
    }

    @Test
    void supportCanFilterUsersByAccountStatus()
        throws Exception {

        String accessToken = loginAsSupport();

        insertSuspendedUser();

        mockMvc.perform(
                get("/api/v1/admin/users")
                    .param("page", "0")
                    .param("size", "20")
                    /*
                     * 使用小寫，確認 Service 會轉成
                     * SUSPENDED。
                     */
                    .param("status", "suspended")
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
                    )
                    .accept(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(
                jsonPath("$.content.length()")
                    .value(1)
            )
            .andExpect(
                jsonPath("$.content[0].id")
                    .value(101)
            )
            .andExpect(
                jsonPath("$.content[0].userNumber")
                    .value("SUSPTEST01")
            )
            .andExpect(
                jsonPath("$.content[0].active")
                    .value(false)
            )
            .andExpect(
                jsonPath(
                    "$.content[0].accountStatus.id"
                )
                    .value(3)
            )
            .andExpect(
                jsonPath(
                    "$.content[0].accountStatus.code"
                )
                    .value("SUSPENDED")
            )
            .andExpect(
                jsonPath("$.totalElements")
                    .value(1)
            );
    }

    @Test
    void keywordAndStatusFiltersAreCombined()
        throws Exception {

        String accessToken = loginAsSupport();

        insertSuspendedUser();

        mockMvc.perform(
                get("/api/v1/admin/users")
                    .param("page", "0")
                    .param("size", "20")
                    .param("keyword", "test")
                    .param("status", "SUSPENDED")
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
                    )
                    .accept(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(
                jsonPath("$.content.length()")
                    .value(1)
            )
            .andExpect(
                jsonPath("$.content[0].id")
                    .value(101)
            )
            .andExpect(
                jsonPath(
                    "$.content[0].accountStatus.code"
                )
                    .value("SUSPENDED")
            )
            .andExpect(
                jsonPath("$.totalElements")
                    .value(1)
            );
    }

    @Test
    void invalidAccountStatusFilterReturnsBadRequest()
        throws Exception {

        String accessToken = loginAsSupport();

        mockMvc.perform(
                get("/api/v1/admin/users")
                    .param("page", "0")
                    .param("size", "20")
                    .param("status", "UNKNOWN_STATUS")
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
                    )
                    .accept(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isBadRequest())
            .andExpect(
                content().contentTypeCompatibleWith(
                    MediaType.APPLICATION_JSON
                )
            )
            .andExpect(
                jsonPath("$.status")
                    .value(400)
            )
            .andExpect(
                jsonPath("$.error")
                    .value("Bad Request")
            )
            .andExpect(
                jsonPath("$.code")
                    .value("BAD_REQUEST")
            )
            .andExpect(
                jsonPath("$.message")
                    .value("無效的會員狀態")
            )
            .andExpect(
                jsonPath("$.path")
                    .value("/api/v1/admin/users")
            )
            .andExpect(
                jsonPath("$.fieldErrors")
                    .isEmpty()
            );
    }

    @Test
    void accountStatusFilterIsTrimmedAndCaseInsensitive()
        throws Exception {

        String accessToken = loginAsSupport();

        insertSuspendedUser();

        mockMvc.perform(
                get("/api/v1/admin/users")
                    .param("page", "0")
                    .param("size", "20")
                    .param(
                        "status",
                        "  suspended  "
                    )
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
                    )
                    .accept(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(
                jsonPath("$.content.length()")
                    .value(1)
            )
            .andExpect(
                jsonPath("$.content[0].id")
                    .value(101)
            )
            .andExpect(
                jsonPath(
                    "$.content[0].accountStatus.code"
                )
                    .value("SUSPENDED")
            )
            .andExpect(
                jsonPath("$.totalElements")
                    .value(1)
            );
    }

    @Test
    void blankAccountStatusFilterReturnsAllUsers()
        throws Exception {

        String accessToken = loginAsSupport();

        insertSuspendedUser();

        mockMvc.perform(
                get("/api/v1/admin/users")
                    .param("page", "0")
                    .param("size", "20")
                    .param("status", "   ")
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
                    )
                    .accept(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(
                jsonPath("$.content.length()")
                    .value(2)
            )
            .andExpect(
                jsonPath("$.totalElements")
                    .value(2)
            );
    }

    @Test
    void adminCanSuspendUserAndStatusChangeIsLogged()
        throws Exception {

        String accessToken = loginAsAdmin();

        String requestBody = """
            {
            "status": "SUSPENDED",
            "note": "  多次違反使用規範  "
            }
            """;

        mockMvc.perform(
                patch(
                    "/api/v1/admin/users/100/status"
                )
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
                    )
                    .contentType(
                        MediaType.APPLICATION_JSON
                    )
                    .accept(
                        MediaType.APPLICATION_JSON
                    )
                    .content(requestBody)
            )
            .andExpect(status().isOk())
            .andExpect(
                jsonPath("$.id")
                    .value(100)
            )
            .andExpect(
                jsonPath("$.active")
                    .value(false)
            )
            .andExpect(
                jsonPath("$.accountStatus.id")
                    .value(3)
            )
            .andExpect(
                jsonPath("$.accountStatus.code")
                    .value("SUSPENDED")
            )
            .andExpect(
                jsonPath("$.accountStatus.name")
                    .value("已停權")
            );

        Long suspendedUserCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM users app_user
                JOIN user_account_statuses account_status
                ON account_status.id =
                    app_user.user_account_status_id
                WHERE app_user.id = 100
                AND app_user.is_active = FALSE
                AND account_status.code =
                    'SUSPENDED'
                """,
                Long.class
            );

        Long statusLogCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM user_account_status_logs status_log
                JOIN user_account_statuses previous_status
                ON previous_status.id =
                    status_log.previous_status_id
                JOIN user_account_statuses new_status
                ON new_status.id =
                    status_log.new_status_id
                WHERE status_log.user_id = 100
                AND previous_status.code = 'ACTIVE'
                AND new_status.code = 'SUSPENDED'
                AND status_log.changed_by_user_id = 200
                AND status_log.note =
                    '多次違反使用規範'
                AND status_log.changed_date IS NOT NULL
                """,
                Long.class
            );

        assertThat(suspendedUserCount)
            .isEqualTo(1);

        assertThat(statusLogCount)
            .isEqualTo(1);
    }

    @Test
    void supportWithoutStatusUpdatePermissionCannotChangeUserStatus()
        throws Exception {

        String accessToken = loginAsSupport();

        String requestBody = """
            {
            "status": "SUSPENDED",
            "note": "客服嘗試停權會員"
            }
            """;

        mockMvc.perform(
                patch(
                    "/api/v1/admin/users/100/status"
                )
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
                    )
                    .contentType(
                        MediaType.APPLICATION_JSON
                    )
                    .accept(
                        MediaType.APPLICATION_JSON
                    )
                    .content(requestBody)
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
                        "/api/v1/admin/users/100/status"
                    )
            )
            .andExpect(
                jsonPath("$.fieldErrors")
                    .isEmpty()
            );

        Long activeUserCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM users app_user
                JOIN user_account_statuses account_status
                ON account_status.id =
                    app_user.user_account_status_id
                WHERE app_user.id = 100
                AND app_user.is_active = TRUE
                AND account_status.code = 'ACTIVE'
                """,
                Long.class
            );

        Long statusLogCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM user_account_status_logs
                WHERE user_id = 100
                """,
                Long.class
            );

        /*
        * 權限被拒絕後，會員資料不能有任何改變。
        */
        assertThat(activeUserCount)
            .isEqualTo(1);

        assertThat(statusLogCount)
            .isZero();
    }

    @ParameterizedTest
    @ValueSource(
        strings = {
            "SUSPENDED",
            "LOCKED"
        }
    )
    void suspendingOrLockingUserWithoutReasonReturnsBadRequest(
        String targetStatus
    ) throws Exception {

        String accessToken = loginAsAdmin();

        String requestBody = """
            {
            "status": "%s"
            }
            """.formatted(targetStatus);

        mockMvc.perform(
                patch(
                    "/api/v1/admin/users/100/status"
                )
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
                    )
                    .contentType(
                        MediaType.APPLICATION_JSON
                    )
                    .accept(
                        MediaType.APPLICATION_JSON
                    )
                    .content(requestBody)
            )
            .andExpect(status().isBadRequest())
            .andExpect(
                content().contentTypeCompatibleWith(
                    MediaType.APPLICATION_JSON
                )
            )
            .andExpect(
                jsonPath("$.status")
                    .value(400)
            )
            .andExpect(
                jsonPath("$.error")
                    .value("Bad Request")
            )
            .andExpect(
                jsonPath("$.code")
                    .value("BAD_REQUEST")
            )
            .andExpect(
                jsonPath("$.message")
                    .value(
                        "停權或鎖定會員時必須填寫原因"
                    )
            )
            .andExpect(
                jsonPath("$.path")
                    .value(
                        "/api/v1/admin/users/100/status"
                    )
            )
            .andExpect(
                jsonPath("$.fieldErrors")
                    .isEmpty()
            );

        Long unchangedUserCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM users app_user
                JOIN user_account_statuses account_status
                ON account_status.id =
                    app_user.user_account_status_id
                WHERE app_user.id = 100
                AND app_user.is_active = TRUE
                AND account_status.code = 'ACTIVE'
                """,
                Long.class
            );

        Long statusLogCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM user_account_status_logs
                WHERE user_id = 100
                """,
                Long.class
            );

        assertThat(unchangedUserCount)
            .isEqualTo(1);

        assertThat(statusLogCount)
            .isZero();
    }

    @Test
    void changingUserToCurrentStatusReturnsConflict()
        throws Exception {

        String accessToken = loginAsAdmin();

        String requestBody = """
            {
            "status": "ACTIVE",
            "note": "重複設定成啟用狀態"
            }
            """;

        mockMvc.perform(
                patch(
                    "/api/v1/admin/users/100/status"
                )
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
                    )
                    .contentType(
                        MediaType.APPLICATION_JSON
                    )
                    .accept(
                        MediaType.APPLICATION_JSON
                    )
                    .content(requestBody)
            )
            .andExpect(status().isConflict())
            .andExpect(
                content().contentTypeCompatibleWith(
                    MediaType.APPLICATION_JSON
                )
            )
            .andExpect(
                jsonPath("$.status")
                    .value(409)
            )
            .andExpect(
                jsonPath("$.error")
                    .value("Conflict")
            )
            .andExpect(
                jsonPath("$.code")
                    .value("CONFLICT")
            )
            .andExpect(
                jsonPath("$.message")
                    .value(
                        "會員目前已經是指定狀態"
                    )
            )
            .andExpect(
                jsonPath("$.path")
                    .value(
                        "/api/v1/admin/users/100/status"
                    )
            )
            .andExpect(
                jsonPath("$.fieldErrors")
                    .isEmpty()
            );

        Long unchangedUserCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM users app_user
                JOIN user_account_statuses account_status
                ON account_status.id =
                    app_user.user_account_status_id
                WHERE app_user.id = 100
                AND app_user.is_active = TRUE
                AND account_status.code = 'ACTIVE'
                """,
                Long.class
            );

        Long statusLogCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM user_account_status_logs
                WHERE user_id = 100
                """,
                Long.class
            );

        assertThat(unchangedUserCount)
            .isEqualTo(1);

        /*
        * 不應寫入 ACTIVE → ACTIVE 的無效歷程。
        */
        assertThat(statusLogCount)
            .isZero();
    }

    @ParameterizedTest
    @ValueSource(
        strings = {
            "PENDING_EMAIL_VERIFICATION",
            "DELETED"
        }
    )
    void administrativelyForbiddenStatusesReturnBadRequest(
        String targetStatus
    ) throws Exception {

        String accessToken = loginAsAdmin();

        String requestBody = """
            {
            "status": "%s",
            "note": "嘗試設定後台禁止的狀態"
            }
            """.formatted(targetStatus);

        mockMvc.perform(
                patch(
                    "/api/v1/admin/users/100/status"
                )
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
                    )
                    .contentType(
                        MediaType.APPLICATION_JSON
                    )
                    .accept(
                        MediaType.APPLICATION_JSON
                    )
                    .content(requestBody)
            )
            .andExpect(status().isBadRequest())
            .andExpect(
                content().contentTypeCompatibleWith(
                    MediaType.APPLICATION_JSON
                )
            )
            .andExpect(
                jsonPath("$.status")
                    .value(400)
            )
            .andExpect(
                jsonPath("$.error")
                    .value("Bad Request")
            )
            .andExpect(
                jsonPath("$.code")
                    .value("BAD_REQUEST")
            )
            .andExpect(
                jsonPath("$.message")
                    .value(
                        "後台不可設定此會員狀態"
                    )
            )
            .andExpect(
                jsonPath("$.path")
                    .value(
                        "/api/v1/admin/users/100/status"
                    )
            )
            .andExpect(
                jsonPath("$.fieldErrors")
                    .isEmpty()
            );

        Long unchangedUserCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM users app_user
                JOIN user_account_statuses account_status
                ON account_status.id =
                    app_user.user_account_status_id
                WHERE app_user.id = 100
                AND app_user.is_active = TRUE
                AND app_user.deleted_date IS NULL
                AND account_status.code = 'ACTIVE'
                """,
                Long.class
            );

        Long statusLogCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM user_account_status_logs
                WHERE user_id = 100
                """,
                Long.class
            );

        assertThat(unchangedUserCount)
            .isEqualTo(1);

        assertThat(statusLogCount)
            .isZero();
    }

    @Test
    void administratorCannotChangeOwnAccountStatus()
        throws Exception {

        String accessToken = loginAsAdmin();

        String requestBody = """
            {
            "status": "SUSPENDED",
            "note": "嘗試停權自己的帳號"
            }
            """;

        /*
        * 測試管理員的 user id 是 200，
        * 因此這裡刻意操作 /users/200/status。
        */
        mockMvc.perform(
                patch(
                    "/api/v1/admin/users/200/status"
                )
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
                    )
                    .contentType(
                        MediaType.APPLICATION_JSON
                    )
                    .accept(
                        MediaType.APPLICATION_JSON
                    )
                    .content(requestBody)
            )
            .andExpect(status().isConflict())
            .andExpect(
                content().contentTypeCompatibleWith(
                    MediaType.APPLICATION_JSON
                )
            )
            .andExpect(
                jsonPath("$.status")
                    .value(409)
            )
            .andExpect(
                jsonPath("$.error")
                    .value("Conflict")
            )
            .andExpect(
                jsonPath("$.code")
                    .value("CONFLICT")
            )
            .andExpect(
                jsonPath("$.message")
                    .value(
                        "不可變更自己的會員狀態"
                    )
            )
            .andExpect(
                jsonPath("$.path")
                    .value(
                        "/api/v1/admin/users/200/status"
                    )
            )
            .andExpect(
                jsonPath("$.fieldErrors")
                    .isEmpty()
            );

        Long activeAdministratorCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM users administrator
                JOIN user_account_statuses account_status
                ON account_status.id =
                    administrator.user_account_status_id
                WHERE administrator.id = 200
                AND administrator.is_active = TRUE
                AND administrator.deleted_date IS NULL
                AND account_status.code = 'ACTIVE'
                """,
                Long.class
            );

        Long administratorStatusLogCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM user_account_status_logs
                WHERE user_id = 200
                """,
                Long.class
            );

        assertThat(activeAdministratorCount)
            .isEqualTo(1);

        /*
        * 被拒絕的自我狀態修改不能留下歷程。
        */
        assertThat(administratorStatusLogCount)
            .isZero();
    }

    @Test
    void adminCanReactivateSuspendedUserWithoutNote()
        throws Exception {

        /*
        * 建立 id=101、狀態為 SUSPENDED 的會員。
        */
        insertSuspendedUser();

        String accessToken = loginAsAdmin();

        /*
        * ACTIVE 不強制要求 note。
        */
        String requestBody = """
            {
            "status": "ACTIVE"
            }
            """;

        mockMvc.perform(
                patch(
                    "/api/v1/admin/users/101/status"
                )
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
                    )
                    .contentType(
                        MediaType.APPLICATION_JSON
                    )
                    .accept(
                        MediaType.APPLICATION_JSON
                    )
                    .content(requestBody)
            )
            .andExpect(status().isOk())
            .andExpect(
                jsonPath("$.id")
                    .value(101)
            )
            .andExpect(
                jsonPath("$.active")
                    .value(true)
            )
            .andExpect(
                jsonPath("$.accountStatus.id")
                    .value(2)
            )
            .andExpect(
                jsonPath("$.accountStatus.code")
                    .value("ACTIVE")
            )
            .andExpect(
                jsonPath("$.accountStatus.name")
                    .value("正常使用")
            );

        Long activeUserCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM users app_user
                JOIN user_account_statuses account_status
                ON account_status.id =
                    app_user.user_account_status_id
                WHERE app_user.id = 101
                AND app_user.email_verified = TRUE
                AND app_user.is_active = TRUE
                AND app_user.deleted_date IS NULL
                AND account_status.code = 'ACTIVE'
                """,
                Long.class
            );

        Long activationLogCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM user_account_status_logs status_log
                JOIN user_account_statuses previous_status
                ON previous_status.id =
                    status_log.previous_status_id
                JOIN user_account_statuses new_status
                ON new_status.id =
                    status_log.new_status_id
                WHERE status_log.user_id = 101
                AND previous_status.code =
                    'SUSPENDED'
                AND new_status.code = 'ACTIVE'
                AND status_log.changed_by_user_id = 200
                AND status_log.note IS NULL
                AND status_log.changed_date IS NOT NULL
                """,
                Long.class
            );

        assertThat(activeUserCount)
            .isEqualTo(1);

        assertThat(activationLogCount)
            .isEqualTo(1);
    }

    @Test
    void suspendingUserImmediatelyRevokesRefreshTokens()
        throws Exception {

        /*
        * 會員先正常登入，取得 Refresh Token。
        */
        String userLoginRequestBody = """
            {
            "email": "test@example.com",
            "password": "Password123!"
            }
            """;

        MvcResult userLoginResult =
            mockMvc.perform(
                    post("/api/v1/auth/login")
                        .contentType(
                            MediaType.APPLICATION_JSON
                        )
                        .accept(
                            MediaType.APPLICATION_JSON
                        )
                        .content(userLoginRequestBody)
                )
                .andExpect(status().isOk())
                .andReturn();

        String userRefreshToken =
            JsonPath.read(
                userLoginResult
                    .getResponse()
                    .getContentAsString(),
                "$.refreshToken"
            );

        Long userRefreshTokenId =
            jdbcTemplate.queryForObject(
                """
                SELECT id
                FROM user_refresh_tokens
                WHERE user_id = 100
                AND revoked_date IS NULL
                """,
                Long.class
            );

        /*
        * 建立並登入獨立的管理員。
        */
        String adminAccessToken =
            loginAsAdmin();

        String suspendRequestBody = """
            {
            "status": "SUSPENDED",
            "note": "安全性事件，立即停權"
            }
            """;

        mockMvc.perform(
                patch(
                    "/api/v1/admin/users/100/status"
                )
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + adminAccessToken
                    )
                    .contentType(
                        MediaType.APPLICATION_JSON
                    )
                    .accept(
                        MediaType.APPLICATION_JSON
                    )
                    .content(suspendRequestBody)
            )
            .andExpect(status().isOk())
            .andExpect(
                jsonPath("$.active")
                    .value(false)
            )
            .andExpect(
                jsonPath("$.accountStatus.code")
                    .value("SUSPENDED")
            );

        /*
        * 尚未呼叫 /refresh 前，就必須已經撤銷。
        */
        Long immediatelyRevokedTokenCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM user_refresh_tokens
                WHERE id = ?
                AND user_id = 100
                AND revoked_date IS NOT NULL
                AND last_used_date IS NULL
                """,
                Long.class,
                userRefreshTokenId
            );

        assertThat(immediatelyRevokedTokenCount)
            .isEqualTo(1);

        /*
        * 再嘗試使用已被管理員撤銷的 Token。
        */
        String refreshRequestBody = """
            {
            "refreshToken": "%s"
            }
            """.formatted(userRefreshToken);

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
                jsonPath("$.code")
                    .value("UNAUTHORIZED")
            )
            .andExpect(
                jsonPath("$.message")
                    .value(
                        "Refresh Token 無效或已過期"
                    )
            )
            .andExpect(
                jsonPath("$.path")
                    .value("/api/v1/auth/refresh")
            );

        Long activeUserTokenCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM user_refresh_tokens
                WHERE user_id = 100
                AND revoked_date IS NULL
                """,
                Long.class
            );

        assertThat(activeUserTokenCount)
            .isZero();
    }

    @Test
    void suspendedAuthenticatedUserCannotAccessProtectedUserApi()
        throws Exception {

        /*
        * id=100 先以 SUPPORT 身分登入，
        * JWT 內具有 USER_READ。
        */
        String accessToken =
            loginAsSupport();

        /*
        * Access Token 簽發後，
        * 將目前登入者停權。
        */
        jdbcTemplate.update(
            """
            UPDATE users
            SET user_account_status_id = 3,
                is_active = FALSE
            WHERE id = 100
            """
        );

        /*
        * JWT 本身仍有效且仍有 USER_READ，
        * 但必須先被 ActiveAccountFilter 阻擋。
        */
        mockMvc.perform(
                get("/api/v1/admin/users/100")
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
                        "/api/v1/admin/users/100"
                    )
            )
            .andExpect(
                jsonPath("$.fieldErrors")
                    .isEmpty()
            );
    }

    @Test
    void supportCanGetPagedUserAccountStatusHistory()
        throws Exception {

        /*
        * 建立要被管理的 id=101 會員。
        * 目前狀態為 ACTIVE。
        */
        insertSearchTargetUser();

        /*
        * 模擬該會員過去由系統產生的兩筆歷程。
        */
        jdbcTemplate.update(
            """
            INSERT INTO user_account_status_logs (
                user_id,
                previous_status_id,
                new_status_id,
                changed_by_user_id,
                note,
                changed_date
            )
            VALUES (
                101,
                NULL,
                1,
                NULL,
                '使用者完成註冊，等待 Email 驗證',
                TIMESTAMP '2020-01-01 00:00:00'
            )
            """
        );

        jdbcTemplate.update(
            """
            INSERT INTO user_account_status_logs (
                user_id,
                previous_status_id,
                new_status_id,
                changed_by_user_id,
                note,
                changed_date
            )
            VALUES (
                101,
                1,
                2,
                NULL,
                'Email 驗證成功，啟用帳號',
                TIMESTAMP '2020-01-02 00:00:00'
            )
            """
        );

        /*
        * 管理員將 id=101 停權，
        * 產生第三筆、也是最新的歷程。
        */
        String adminAccessToken =
            loginAsAdmin();

        String suspendRequestBody = """
            {
            "status": "SUSPENDED",
            "note": "多次違反使用規範"
            }
            """;

        mockMvc.perform(
                patch(
                    "/api/v1/admin/users/101/status"
                )
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + adminAccessToken
                    )
                    .contentType(
                        MediaType.APPLICATION_JSON
                    )
                    .accept(
                        MediaType.APPLICATION_JSON
                    )
                    .content(suspendRequestBody)
            )
            .andExpect(status().isOk());

        /*
        * id=100 以 SUPPORT 身分登入，
        * 使用 USER_READ 查詢 id=101 的歷程。
        */
        String supportAccessToken =
            loginAsSupport();

        mockMvc.perform(
                get(
                    "/api/v1/admin/users/101/status-history"
                )
                    .param("page", "0")
                    .param("size", "20")
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + supportAccessToken
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
                jsonPath("$.content.length()")
                    .value(3)
            )

            /*
            * 最新：ACTIVE → SUSPENDED，
            * 由管理員 id=200 執行。
            */
            .andExpect(
                jsonPath(
                    "$.content[0].previousStatus.code"
                )
                    .value("ACTIVE")
            )
            .andExpect(
                jsonPath(
                    "$.content[0].newStatus.code"
                )
                    .value("SUSPENDED")
            )
            .andExpect(
                jsonPath(
                    "$.content[0].changedByUser.id"
                )
                    .value(200)
            )
            .andExpect(
                jsonPath(
                    "$.content[0].changedByUser.userNumber"
                )
                    .value("ADMIN00001")
            )
            .andExpect(
                jsonPath(
                    "$.content[0].changedByUser.name"
                )
                    .value("Test Administrator")
            )
            .andExpect(
                jsonPath("$.content[0].note")
                    .value("多次違反使用規範")
            )

            /*
            * 第二筆：PENDING → ACTIVE，
            * 由系統自動執行。
            */
            .andExpect(
                jsonPath(
                    "$.content[1].previousStatus.code"
                )
                    .value(
                        "PENDING_EMAIL_VERIFICATION"
                    )
            )
            .andExpect(
                jsonPath(
                    "$.content[1].newStatus.code"
                )
                    .value("ACTIVE")
            )
            .andExpect(
                jsonPath(
                    "$.content[1].changedByUser"
                )
                    .doesNotExist()
            )
            .andExpect(
                jsonPath("$.content[1].note")
                    .value(
                        "Email 驗證成功，啟用帳號"
                    )
            )

            /*
            * 最早：NULL → PENDING。
            */
            .andExpect(
                jsonPath(
                    "$.content[2].previousStatus"
                )
                    .doesNotExist()
            )
            .andExpect(
                jsonPath(
                    "$.content[2].newStatus.code"
                )
                    .value(
                        "PENDING_EMAIL_VERIFICATION"
                    )
            )
            .andExpect(
                jsonPath(
                    "$.content[2].changedByUser"
                )
                    .doesNotExist()
            )
            .andExpect(
                jsonPath("$.totalElements")
                    .value(3)
            )
            .andExpect(
                jsonPath("$.totalPages")
                    .value(1)
            )
            .andExpect(
                jsonPath("$.page")
                    .value(0)
            )
            .andExpect(
                jsonPath("$.size")
                    .value(20)
            )
            .andExpect(
                jsonPath("$.first")
                    .value(true)
            )
            .andExpect(
                jsonPath("$.last")
                    .value(true)
            );
    }

    @Test
    void userWithoutUserReadPermissionCannotGetStatusHistory()
        throws Exception {

        String accessToken =
            loginAsDefaultUser();

        mockMvc.perform(
                get(
                    "/api/v1/admin/users/100/status-history"
                )
                    .param("page", "0")
                    .param("size", "20")
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
                        "/api/v1/admin/users/100/status-history"
                    )
            )
            .andExpect(
                jsonPath("$.fieldErrors")
                    .isEmpty()
            );
    }

    @Test
    void statusHistoryForUnknownUserReturnsNotFound()
        throws Exception {

        String accessToken =
            loginAsSupport();

        mockMvc.perform(
                get(
                    "/api/v1/admin/users/999999/status-history"
                )
                    .param("page", "0")
                    .param("size", "20")
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
                    )
                    .accept(
                        MediaType.APPLICATION_JSON
                    )
            )
            .andExpect(status().isNotFound())
            .andExpect(
                content().contentTypeCompatibleWith(
                    MediaType.APPLICATION_JSON
                )
            )
            .andExpect(
                jsonPath("$.status")
                    .value(404)
            )
            .andExpect(
                jsonPath("$.error")
                    .value("Not Found")
            )
            .andExpect(
                jsonPath("$.code")
                    .value("RESOURCE_NOT_FOUND")
            )
            .andExpect(
                jsonPath("$.message")
                    .value(
                        "找不到指定的使用者"
                    )
            )
            .andExpect(
                jsonPath("$.path")
                    .value(
                        "/api/v1/admin/users/999999/status-history"
                    )
            )
            .andExpect(
                jsonPath("$.fieldErrors")
                    .isEmpty()
            );
    }

    @ParameterizedTest
    @CsvSource({
        "-1, 20, page 不得小於 0",
        "0, 0, size 必須介於 1 到 100",
        "0, 101, size 必須介於 1 到 100"
    })
    void invalidStatusHistoryPaginationReturnsBadRequest(
        int page,
        int size,
        String expectedMessage
    ) throws Exception {

        String accessToken =
            loginAsSupport();

        mockMvc.perform(
                get(
                    "/api/v1/admin/users/100/status-history"
                )
                    .param(
                        "page",
                        String.valueOf(page)
                    )
                    .param(
                        "size",
                        String.valueOf(size)
                    )
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
                    )
                    .accept(
                        MediaType.APPLICATION_JSON
                    )
            )
            .andExpect(status().isBadRequest())
            .andExpect(
                content().contentTypeCompatibleWith(
                    MediaType.APPLICATION_JSON
                )
            )
            .andExpect(
                jsonPath("$.status")
                    .value(400)
            )
            .andExpect(
                jsonPath("$.error")
                    .value("Bad Request")
            )
            .andExpect(
                jsonPath("$.code")
                    .value("BAD_REQUEST")
            )
            .andExpect(
                jsonPath("$.message")
                    .value(expectedMessage)
            )
            .andExpect(
                jsonPath("$.path")
                    .value(
                        "/api/v1/admin/users/100/status-history"
                    )
            )
            .andExpect(
                jsonPath("$.fieldErrors")
                    .isEmpty()
            );
    }

    @Test
    void revokedStatusUpdatePermissionBlocksPreviouslyIssuedToken()
        throws Exception {

        /*
        * 管理員先登入。
        * 此時 JWT 已包含 USER_STATUS_UPDATE。
        */
        String accessToken =
            loginAsAdmin();

        /*
        * 登入後撤銷 ADMIN role assignment。
        * 舊 JWT 內容不會跟著改變。
        */
        jdbcTemplate.update(
            """
            UPDATE user_roles
            SET is_active = FALSE,
                deleted_date = CURRENT_TIMESTAMP
            WHERE user_id = 200
            AND role_id = 3
            """
        );

        String requestBody = """
            {
            "status": "SUSPENDED",
            "note": "使用已撤銷權限的舊 Token"
            }
            """;

        mockMvc.perform(
                patch(
                    "/api/v1/admin/users/100/status"
                )
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
                    )
                    .contentType(
                        MediaType.APPLICATION_JSON
                    )
                    .accept(
                        MediaType.APPLICATION_JSON
                    )
                    .content(requestBody)
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
                        "/api/v1/admin/users/100/status"
                    )
            )
            .andExpect(
                jsonPath("$.fieldErrors")
                    .isEmpty()
            );

        /*
        * 目標會員仍必須維持 ACTIVE。
        */
        Long unchangedUserCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM users app_user
                JOIN user_account_statuses account_status
                ON account_status.id =
                    app_user.user_account_status_id
                WHERE app_user.id = 100
                AND app_user.is_active = TRUE
                AND account_status.code = 'ACTIVE'
                """,
                Long.class
            );

        Long statusLogCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM user_account_status_logs
                WHERE user_id = 100
                """,
                Long.class
            );

        assertThat(unchangedUserCount)
            .isEqualTo(1);

        assertThat(statusLogCount)
            .isZero();
    }

    @Test
    void changingStatusOfUnknownUserReturnsNotFound()
        throws Exception {

        String accessToken =
            loginAsAdmin();

        String requestBody = """
            {
            "status": "SUSPENDED",
            "note": "測試不存在的會員"
            }
            """;

        mockMvc.perform(
                patch(
                    "/api/v1/admin/users/999999/status"
                )
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
                    )
                    .contentType(
                        MediaType.APPLICATION_JSON
                    )
                    .accept(
                        MediaType.APPLICATION_JSON
                    )
                    .content(requestBody)
            )
            .andExpect(status().isNotFound())
            .andExpect(
                content().contentTypeCompatibleWith(
                    MediaType.APPLICATION_JSON
                )
            )
            .andExpect(
                jsonPath("$.status")
                    .value(404)
            )
            .andExpect(
                jsonPath("$.error")
                    .value("Not Found")
            )
            .andExpect(
                jsonPath("$.code")
                    .value("RESOURCE_NOT_FOUND")
            )
            .andExpect(
                jsonPath("$.message")
                    .value(
                        "找不到指定的使用者"
                    )
            )
            .andExpect(
                jsonPath("$.path")
                    .value(
                        "/api/v1/admin/users/999999/status"
                    )
            )
            .andExpect(
                jsonPath("$.fieldErrors")
                    .isEmpty()
            );

        Long statusLogCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM user_account_status_logs
                """,
                Long.class
            );

        assertThat(statusLogCount)
            .isZero();
    }

    @Test
    void administratorCannotActivatePendingEmailVerificationUser()
        throws Exception {

        /*
        * 將 id=100 模擬為剛註冊、
        * 尚未完成 Email 驗證的會員。
        */
        jdbcTemplate.update(
            """
            UPDATE users
            SET email_verified = FALSE,
                sms_verified = FALSE,
                user_account_status_id = 1,
                is_active = FALSE
            WHERE id = 100
            """
        );

        String accessToken =
            loginAsAdmin();

        String requestBody = """
            {
            "status": "ACTIVE",
            "note": "嘗試略過 Email 驗證"
            }
            """;

        mockMvc.perform(
                patch(
                    "/api/v1/admin/users/100/status"
                )
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
                    )
                    .contentType(
                        MediaType.APPLICATION_JSON
                    )
                    .accept(
                        MediaType.APPLICATION_JSON
                    )
                    .content(requestBody)
            )
            .andExpect(status().isConflict())
            .andExpect(
                content().contentTypeCompatibleWith(
                    MediaType.APPLICATION_JSON
                )
            )
            .andExpect(
                jsonPath("$.status")
                    .value(409)
            )
            .andExpect(
                jsonPath("$.error")
                    .value("Conflict")
            )
            .andExpect(
                jsonPath("$.code")
                    .value("CONFLICT")
            )
            .andExpect(
                jsonPath("$.message")
                    .value(
                        "尚未完成 Email 驗證的會員不可由後台啟用"
                    )
            )
            .andExpect(
                jsonPath("$.path")
                    .value(
                        "/api/v1/admin/users/100/status"
                    )
            )
            .andExpect(
                jsonPath("$.fieldErrors")
                    .isEmpty()
            );

        Long pendingUserCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM users app_user
                JOIN user_account_statuses account_status
                ON account_status.id =
                    app_user.user_account_status_id
                WHERE app_user.id = 100
                AND app_user.email_verified = FALSE
                AND app_user.is_active = FALSE
                AND account_status.code =
                    'PENDING_EMAIL_VERIFICATION'
                """,
                Long.class
            );

        Long statusLogCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM user_account_status_logs
                WHERE user_id = 100
                """,
                Long.class
            );

        assertThat(pendingUserCount)
            .isEqualTo(1);

        assertThat(statusLogCount)
            .isZero();
    }

    @Test
    void adminCanSoftDeleteUserAndRevokeRefreshTokens()
        throws Exception {

        /*
        * 目標會員先登入，產生有效 Refresh Token。
        */
        String userLoginRequestBody = """
            {
            "email": "test@example.com",
            "password": "Password123!"
            }
            """;

        MvcResult userLoginResult =
            mockMvc.perform(
                    post("/api/v1/auth/login")
                        .contentType(
                            MediaType.APPLICATION_JSON
                        )
                        .accept(
                            MediaType.APPLICATION_JSON
                        )
                        .content(userLoginRequestBody)
                )
                .andExpect(status().isOk())
                .andReturn();

        String userRefreshToken =
            JsonPath.read(
                userLoginResult
                    .getResponse()
                    .getContentAsString(),
                "$.refreshToken"
            );

        Long userRefreshTokenId =
            jdbcTemplate.queryForObject(
                """
                SELECT id
                FROM user_refresh_tokens
                WHERE user_id = 100
                AND revoked_date IS NULL
                """,
                Long.class
            );

        String adminAccessToken =
            loginAsAdmin();

        String deleteRequestBody = """
            {
            "reason": "  使用者申請刪除帳號  "
            }
            """;

        mockMvc.perform(
                delete(
                    "/api/v1/admin/users/100"
                )
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + adminAccessToken
                    )
                    .contentType(
                        MediaType.APPLICATION_JSON
                    )
                    .accept(
                        MediaType.APPLICATION_JSON
                    )
                    .content(deleteRequestBody)
            )
            .andExpect(status().isNoContent())
            .andExpect(
                content().string("")
            );

        Long deletedUserCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM users app_user
                JOIN user_account_statuses account_status
                ON account_status.id =
                    app_user.user_account_status_id
                WHERE app_user.id = 100
                AND app_user.is_active = FALSE
                AND app_user.deleted_date IS NOT NULL
                AND account_status.code = 'DELETED'
                """,
                Long.class
            );

        Long deletionLogCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM user_account_status_logs status_log
                JOIN user_account_statuses previous_status
                ON previous_status.id =
                    status_log.previous_status_id
                JOIN user_account_statuses new_status
                ON new_status.id =
                    status_log.new_status_id
                WHERE status_log.user_id = 100
                AND previous_status.code = 'ACTIVE'
                AND new_status.code = 'DELETED'
                AND status_log.changed_by_user_id = 200
                AND status_log.note =
                    '使用者申請刪除帳號'
                AND status_log.changed_date IS NOT NULL
                """,
                Long.class
            );

        Long revokedTokenCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM user_refresh_tokens
                WHERE id = ?
                AND user_id = 100
                AND revoked_date IS NOT NULL
                AND last_used_date IS NULL
                """,
                Long.class,
                userRefreshTokenId
            );

        assertThat(deletedUserCount)
            .isEqualTo(1);

        assertThat(deletionLogCount)
            .isEqualTo(1);

        assertThat(revokedTokenCount)
            .isEqualTo(1);

        /*
        * 已刪除會員的 Refresh Token 不可再使用。
        */
        String refreshRequestBody = """
            {
            "refreshToken": "%s"
            }
            """.formatted(userRefreshToken);

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
            .andExpect(status().isUnauthorized())
            .andExpect(
                jsonPath("$.status")
                    .value(401)
            )
            .andExpect(
                jsonPath("$.code")
                    .value("UNAUTHORIZED")
            )
            .andExpect(
                jsonPath("$.message")
                    .value(
                        "Refresh Token 無效或已過期"
                    )
            );
    }

    @Test
    void supportWithoutUserDeletePermissionCannotDeleteUser()
        throws Exception {

        String accessToken =
            loginAsSupport();

        String requestBody = """
            {
            "reason": "客服嘗試刪除會員"
            }
            """;

        mockMvc.perform(
                delete("/api/v1/admin/users/100")
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
                    )
                    .contentType(
                        MediaType.APPLICATION_JSON
                    )
                    .accept(
                        MediaType.APPLICATION_JSON
                    )
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
                    .value(
                        "你沒有執行此操作的權限"
                    )
            )
            .andExpect(
                jsonPath("$.path")
                    .value(
                        "/api/v1/admin/users/100"
                    )
            );

        Long unchangedUserCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM users app_user
                JOIN user_account_statuses account_status
                ON account_status.id =
                    app_user.user_account_status_id
                WHERE app_user.id = 100
                AND app_user.is_active = TRUE
                AND app_user.deleted_date IS NULL
                AND account_status.code = 'ACTIVE'
                """,
                Long.class
            );

        assertThat(unchangedUserCount)
            .isEqualTo(1);
    }

    @Test
    void deletingUserWithBlankReasonReturnsValidationError()
        throws Exception {

        String accessToken =
            loginAsAdmin();

        String requestBody = """
            {
            "reason": "   "
            }
            """;

        mockMvc.perform(
                delete("/api/v1/admin/users/100")
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
                    )
                    .contentType(
                        MediaType.APPLICATION_JSON
                    )
                    .accept(
                        MediaType.APPLICATION_JSON
                    )
                    .content(requestBody)
            )
            .andExpect(status().isBadRequest())
            .andExpect(
                content().contentTypeCompatibleWith(
                    MediaType.APPLICATION_JSON
                )
            )
            .andExpect(
                jsonPath("$.status")
                    .value(400)
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
                    .value(
                        "/api/v1/admin/users/100"
                    )
            )
            .andExpect(
                jsonPath("$.fieldErrors[0].field")
                    .value("reason")
            )
            .andExpect(
                jsonPath("$.fieldErrors[0].message")
                    .value(
                        "刪除會員時必須填寫原因"
                    )
            );

        Long unchangedUserCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM users
                WHERE id = 100
                AND is_active = TRUE
                AND deleted_date IS NULL
                """,
                Long.class
            );

        Long statusLogCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM user_account_status_logs
                WHERE user_id = 100
                """,
                Long.class
            );

        assertThat(unchangedUserCount)
            .isEqualTo(1);

        assertThat(statusLogCount)
            .isZero();
    }

    @Test
    void administratorCannotDeleteOwnAccount()
        throws Exception {

        String accessToken =
            loginAsAdmin();

        String requestBody = """
            {
            "reason": "嘗試刪除自己的帳號"
            }
            """;

        mockMvc.perform(
                delete("/api/v1/admin/users/200")
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
                    )
                    .contentType(
                        MediaType.APPLICATION_JSON
                    )
                    .accept(
                        MediaType.APPLICATION_JSON
                    )
                    .content(requestBody)
            )
            .andExpect(status().isConflict())
            .andExpect(
                jsonPath("$.status")
                    .value(409)
            )
            .andExpect(
                jsonPath("$.code")
                    .value("CONFLICT")
            )
            .andExpect(
                jsonPath("$.message")
                    .value(
                        "不可刪除自己的會員帳號"
                    )
            )
            .andExpect(
                jsonPath("$.path")
                    .value(
                        "/api/v1/admin/users/200"
                    )
            );

        Long activeAdministratorCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM users app_user
                JOIN user_account_statuses account_status
                ON account_status.id =
                    app_user.user_account_status_id
                WHERE app_user.id = 200
                AND app_user.is_active = TRUE
                AND app_user.deleted_date IS NULL
                AND account_status.code = 'ACTIVE'
                """,
                Long.class
            );

        Long statusLogCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM user_account_status_logs
                WHERE user_id = 200
                """,
                Long.class
            );

        assertThat(activeAdministratorCount)
            .isEqualTo(1);

        assertThat(statusLogCount)
            .isZero();
    }

    @Test
    void deletingUnknownUserReturnsNotFound()
        throws Exception {

        String accessToken =
            loginAsAdmin();

        String requestBody = """
            {
            "reason": "測試刪除不存在的會員"
            }
            """;

        mockMvc.perform(
                delete(
                    "/api/v1/admin/users/999999"
                )
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
                    )
                    .contentType(
                        MediaType.APPLICATION_JSON
                    )
                    .accept(
                        MediaType.APPLICATION_JSON
                    )
                    .content(requestBody)
            )
            .andExpect(status().isNotFound())
            .andExpect(
                content().contentTypeCompatibleWith(
                    MediaType.APPLICATION_JSON
                )
            )
            .andExpect(
                jsonPath("$.status")
                    .value(404)
            )
            .andExpect(
                jsonPath("$.error")
                    .value("Not Found")
            )
            .andExpect(
                jsonPath("$.code")
                    .value("RESOURCE_NOT_FOUND")
            )
            .andExpect(
                jsonPath("$.message")
                    .value(
                        "找不到指定的使用者"
                    )
            )
            .andExpect(
                jsonPath("$.path")
                    .value(
                        "/api/v1/admin/users/999999"
                    )
            )
            .andExpect(
                jsonPath("$.fieldErrors")
                    .isEmpty()
            );

        Long statusLogCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM user_account_status_logs
                """,
                Long.class
            );

        assertThat(statusLogCount)
            .isZero();
    }

    @Test
    void revokedUserDeletePermissionBlocksPreviouslyIssuedToken()
        throws Exception {

        /*
        * 登入時 JWT 已包含 USER_DELETE。
        */
        String accessToken =
            loginAsAdmin();

        /*
        * 登入後只撤銷 ADMIN 的 USER_DELETE，
        * ADMIN role 與 USER_STATUS_UPDATE 仍保留。
        */
        jdbcTemplate.update(
            """
            UPDATE role_permissions
            SET is_active = FALSE,
                deleted_date = CURRENT_TIMESTAMP
            WHERE role_id = 3
            AND permission_id = 4
            """
        );

        String requestBody = """
            {
            "reason": "使用已撤銷權限的舊 Token"
            }
            """;

        mockMvc.perform(
                delete("/api/v1/admin/users/100")
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
                    )
                    .contentType(
                        MediaType.APPLICATION_JSON
                    )
                    .accept(
                        MediaType.APPLICATION_JSON
                    )
                    .content(requestBody)
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
                        "/api/v1/admin/users/100"
                    )
            )
            .andExpect(
                jsonPath("$.fieldErrors")
                    .isEmpty()
            );

        Long unchangedUserCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM users app_user
                JOIN user_account_statuses account_status
                ON account_status.id =
                    app_user.user_account_status_id
                WHERE app_user.id = 100
                AND app_user.is_active = TRUE
                AND app_user.deleted_date IS NULL
                AND account_status.code = 'ACTIVE'
                """,
                Long.class
            );

        Long deletionLogCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM user_account_status_logs
                WHERE user_id = 100
                """,
                Long.class
            );

        assertThat(unchangedUserCount)
            .isEqualTo(1);

        assertThat(deletionLogCount)
            .isZero();
    }

    @Test
    void adminWithUserReadDeletedPermissionCanGetDeletedUsers()
        throws Exception {

        insertDeletedUser();

        String accessToken =
            loginAsAdmin();

        mockMvc.perform(
                get("/api/v1/admin/users/deleted")
                    .param("page", "0")
                    .param("size", "20")
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
                jsonPath("$.content.length()")
                    .value(1)
            )
            .andExpect(
                jsonPath("$.content[0].id")
                    .value(101)
            )
            .andExpect(
                jsonPath("$.content[0].userNumber")
                    .value("DELETED001")
            )
            .andExpect(
                jsonPath("$.content[0].email")
                    .value(
                        "deleted-test@example.com"
                    )
            )
            .andExpect(
                jsonPath("$.content[0].active")
                    .value(false)
            )
            .andExpect(
                jsonPath(
                    "$.content[0].accountStatus.code"
                )
                    .value("DELETED")
            )
            .andExpect(
                jsonPath("$.content[0].deletedDate")
                    .value(
                        "2026-02-01T10:00:00"
                    )
            )
            .andExpect(
                jsonPath("$.page")
                    .value(0)
            )
            .andExpect(
                jsonPath("$.size")
                    .value(20)
            )
            .andExpect(
                jsonPath("$.totalElements")
                    .value(1)
            )
            .andExpect(
                jsonPath("$.totalPages")
                    .value(1)
            )
            .andExpect(
                jsonPath("$.first")
                    .value(true)
            )
            .andExpect(
                jsonPath("$.last")
                    .value(true)
            );
    }

    @Test
    void supportWithoutUserReadDeletedPermissionCannotGetDeletedUsers()
        throws Exception {

        insertDeletedUser();

        String accessToken =
            loginAsSupport();

        mockMvc.perform(
                get("/api/v1/admin/users/deleted")
                    .param("page", "0")
                    .param("size", "20")
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
                        "/api/v1/admin/users/deleted"
                    )
            )
            .andExpect(
                jsonPath("$.fieldErrors")
                    .isEmpty()
            );
    }

    @ParameterizedTest
    @ValueSource(
        strings = {
            "DELETED001",
            "deleted test",
            "deletedtester",
            "deleted-test@example.com"
        }
    )
    void adminCanSearchDeletedUsersByKeyword(
        String keyword
    ) throws Exception {

        insertDeletedUser();
        insertAnotherDeletedUser();

        String accessToken =
            loginAsAdmin();

        mockMvc.perform(
                get("/api/v1/admin/users/deleted")
                    .param("page", "0")
                    .param("size", "20")
                    .param("keyword", keyword)
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
                jsonPath("$.content.length()")
                    .value(1)
            )
            .andExpect(
                jsonPath("$.content[0].id")
                    .value(101)
            )
            .andExpect(
                jsonPath("$.content[0].userNumber")
                    .value("DELETED001")
            )
            .andExpect(
                jsonPath("$.content[0].name")
                    .value("Deleted Test User")
            )
            .andExpect(
                jsonPath("$.content[0].nickname")
                    .value("DeletedTester")
            )
            .andExpect(
                jsonPath("$.content[0].email")
                    .value(
                        "deleted-test@example.com"
                    )
            )
            .andExpect(
                jsonPath("$.totalElements")
                    .value(1)
            )
            .andExpect(
                jsonPath("$.totalPages")
                    .value(1)
            );
    }

    @Test
    void deletedUsersArePagedByDeletedDateDescending()
        throws Exception {

        /*
        * id=101：
        * deleted_date = 2026-02-01 10:00:00
        */
        insertDeletedUser();

        /*
        * id=102：
        * deleted_date = 2026-03-01 10:00:00
        */
        insertAnotherDeletedUser();

        String accessToken =
            loginAsAdmin();

        /*
        * 第一頁應回傳刪除時間較新的 id=102。
        */
        mockMvc.perform(
                get("/api/v1/admin/users/deleted")
                    .param("page", "0")
                    .param("size", "1")
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
                jsonPath("$.content.length()")
                    .value(1)
            )
            .andExpect(
                jsonPath("$.content[0].id")
                    .value(102)
            )
            .andExpect(
                jsonPath("$.content[0].deletedDate")
                    .value(
                        "2026-03-01T10:00:00"
                    )
            )
            .andExpect(
                jsonPath("$.page")
                    .value(0)
            )
            .andExpect(
                jsonPath("$.size")
                    .value(1)
            )
            .andExpect(
                jsonPath("$.totalElements")
                    .value(2)
            )
            .andExpect(
                jsonPath("$.totalPages")
                    .value(2)
            )
            .andExpect(
                jsonPath("$.first")
                    .value(true)
            )
            .andExpect(
                jsonPath("$.last")
                    .value(false)
            );

        /*
        * 第二頁應回傳刪除時間較舊的 id=101。
        */
        mockMvc.perform(
                get("/api/v1/admin/users/deleted")
                    .param("page", "1")
                    .param("size", "1")
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
                jsonPath("$.content.length()")
                    .value(1)
            )
            .andExpect(
                jsonPath("$.content[0].id")
                    .value(101)
            )
            .andExpect(
                jsonPath("$.content[0].deletedDate")
                    .value(
                        "2026-02-01T10:00:00"
                    )
            )
            .andExpect(
                jsonPath("$.page")
                    .value(1)
            )
            .andExpect(
                jsonPath("$.size")
                    .value(1)
            )
            .andExpect(
                jsonPath("$.totalElements")
                    .value(2)
            )
            .andExpect(
                jsonPath("$.totalPages")
                    .value(2)
            )
            .andExpect(
                jsonPath("$.first")
                    .value(false)
            )
            .andExpect(
                jsonPath("$.last")
                    .value(true)
            );
    }

    @ParameterizedTest
    @CsvSource({
        "-1, 20, page 不得小於 0",
        "0, 0, size 必須介於 1 到 100",
        "0, 101, size 必須介於 1 到 100"
    })
    void invalidDeletedUsersPaginationReturnsBadRequest(
        int page,
        int size,
        String expectedMessage
    ) throws Exception {

        String accessToken =
            loginAsAdmin();

        mockMvc.perform(
                get("/api/v1/admin/users/deleted")
                    .param(
                        "page",
                        String.valueOf(page)
                    )
                    .param(
                        "size",
                        String.valueOf(size)
                    )
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
                    )
                    .accept(
                        MediaType.APPLICATION_JSON
                    )
            )
            .andExpect(status().isBadRequest())
            .andExpect(
                content().contentTypeCompatibleWith(
                    MediaType.APPLICATION_JSON
                )
            )
            .andExpect(
                jsonPath("$.status")
                    .value(400)
            )
            .andExpect(
                jsonPath("$.error")
                    .value("Bad Request")
            )
            .andExpect(
                jsonPath("$.code")
                    .value("BAD_REQUEST")
            )
            .andExpect(
                jsonPath("$.message")
                    .value(expectedMessage)
            )
            .andExpect(
                jsonPath("$.path")
                    .value(
                        "/api/v1/admin/users/deleted"
                    )
            )
            .andExpect(
                jsonPath("$.fieldErrors")
                    .isEmpty()
            );
    }

    @ParameterizedTest
    @ValueSource(
        strings = {
            "%",
            "_",
            "!"
        }
    )
    void deletedUserKeywordTreatsLikeCharactersAsLiteralText(
        String keyword
    ) throws Exception {

        /*
        * id=101 不包含 LIKE 特殊字元。
        */
        insertDeletedUser();

        /*
        * id=102 原本是另一筆已刪除會員，
        * 再將姓名修改成包含 _、%、!。
        */
        insertAnotherDeletedUser();

        jdbcTemplate.update(
            """
            UPDATE users
            SET name = 'Budget_100%! User'
            WHERE id = 102
            """
        );

        String accessToken =
            loginAsAdmin();

        mockMvc.perform(
                get("/api/v1/admin/users/deleted")
                    .param("page", "0")
                    .param("size", "20")
                    .param("keyword", keyword)
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
                jsonPath("$.content.length()")
                    .value(1)
            )
            .andExpect(
                jsonPath("$.content[0].id")
                    .value(102)
            )
            .andExpect(
                jsonPath("$.content[0].name")
                    .value("Budget_100%! User")
            )
            .andExpect(
                jsonPath("$.totalElements")
                    .value(1)
            );
    }

    @Test
    void deletedUserKeywordLongerThanOneHundredCharactersReturnsBadRequest()
        throws Exception {

        String accessToken =
            loginAsAdmin();

        String oversizedKeyword =
            "a".repeat(101);

        mockMvc.perform(
                get("/api/v1/admin/users/deleted")
                    .param("page", "0")
                    .param("size", "20")
                    .param(
                        "keyword",
                        oversizedKeyword
                    )
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
                    )
                    .accept(
                        MediaType.APPLICATION_JSON
                    )
            )
            .andExpect(status().isBadRequest())
            .andExpect(
                content().contentTypeCompatibleWith(
                    MediaType.APPLICATION_JSON
                )
            )
            .andExpect(
                jsonPath("$.status")
                    .value(400)
            )
            .andExpect(
                jsonPath("$.error")
                    .value("Bad Request")
            )
            .andExpect(
                jsonPath("$.code")
                    .value("BAD_REQUEST")
            )
            .andExpect(
                jsonPath("$.message")
                    .value(
                        "keyword 長度不得超過 100"
                    )
            )
            .andExpect(
                jsonPath("$.path")
                    .value(
                        "/api/v1/admin/users/deleted"
                    )
            )
            .andExpect(
                jsonPath("$.fieldErrors")
                    .isEmpty()
            );
    }

    @Test
    void revokedUserReadDeletedPermissionBlocksPreviouslyIssuedToken()
        throws Exception {

        insertDeletedUser();

        /*
        * 登入時 ADMIN 仍擁有 USER_READ_DELETED，
        * 因此 JWT 會包含這項權限。
        */
        String accessToken =
            loginAsAdmin();

        /*
        * 登入後撤銷資料庫中的 USER_READ_DELETED。
        *
        * role_id = 3       -> ADMIN
        * permission_id = 5 -> USER_READ_DELETED
        */
        jdbcTemplate.update(
            """
            UPDATE role_permissions
            SET is_active = FALSE,
                deleted_date = CURRENT_TIMESTAMP
            WHERE role_id = 3
            AND permission_id = 5
            """
        );

        /*
        * JWT 仍有舊權限，所以 @PreAuthorize 會通過；
        * Service 的即時資料庫權限檢查必須回傳 403。
        */
        mockMvc.perform(
                get("/api/v1/admin/users/deleted")
                    .param("page", "0")
                    .param("size", "20")
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
                        "/api/v1/admin/users/deleted"
                    )
            )
            .andExpect(
                jsonPath("$.fieldErrors")
                    .isEmpty()
            );
    }

    @Test
    void adminWithUserReadDeletedPermissionCanGetDeletedUserById()
        throws Exception {

        insertDeletedUser();

        String accessToken =
            loginAsAdmin();

        mockMvc.perform(
                get(
                    "/api/v1/admin/users/deleted/101"
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
                jsonPath("$.id")
                    .value(101)
            )
            .andExpect(
                jsonPath("$.userNumber")
                    .value("DELETED001")
            )
            .andExpect(
                jsonPath("$.name")
                    .value("Deleted Test User")
            )
            .andExpect(
                jsonPath("$.nickname")
                    .value("DeletedTester")
            )
            .andExpect(
                jsonPath("$.email")
                    .value(
                        "deleted-test@example.com"
                    )
            )
            .andExpect(
                jsonPath("$.birthday")
                    .value("1991-02-02")
            )
            .andExpect(
                jsonPath("$.phone")
                    .value("0955555555")
            )
            .andExpect(
                jsonPath("$.emailVerified")
                    .value(true)
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
                jsonPath("$.accountStatus.id")
                    .value(5)
            )
            .andExpect(
                jsonPath("$.accountStatus.code")
                    .value("DELETED")
            )
            .andExpect(
                jsonPath("$.roles")
                    .isEmpty()
            )
            .andExpect(
                jsonPath("$.country")
                    .value(nullValue())
            )
            .andExpect(
                jsonPath("$.timezone")
                    .value(nullValue())
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
            )
            .andExpect(
                jsonPath("$.createdDate")
                    .value(
                        "2026-01-01T10:00:00"
                    )
            )
            .andExpect(
                jsonPath("$.updatedDate")
                    .value(
                        "2026-02-01T10:00:00"
                    )
            )
            .andExpect(
                jsonPath("$.deletedDate")
                    .value(
                        "2026-02-01T10:00:00"
                    )
            );
    }

    @ParameterizedTest
    @ValueSource(
        longs = {
            100,
            999999
        }
    )
    void gettingNonDeletedOrUnknownUserFromDeletedEndpointReturnsNotFound(
        long userId
    ) throws Exception {

        String accessToken =
            loginAsAdmin();

        String requestPath =
            "/api/v1/admin/users/deleted/" +
                userId;

        mockMvc.perform(
                get(requestPath)
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
                    )
                    .accept(
                        MediaType.APPLICATION_JSON
                    )
            )
            .andExpect(status().isNotFound())
            .andExpect(
                content().contentTypeCompatibleWith(
                    MediaType.APPLICATION_JSON
                )
            )
            .andExpect(
                jsonPath("$.status")
                    .value(404)
            )
            .andExpect(
                jsonPath("$.error")
                    .value("Not Found")
            )
            .andExpect(
                jsonPath("$.code")
                    .value("RESOURCE_NOT_FOUND")
            )
            .andExpect(
                jsonPath("$.message")
                    .value(
                        "找不到指定的已刪除使用者"
                    )
            )
            .andExpect(
                jsonPath("$.path")
                    .value(requestPath)
            )
            .andExpect(
                jsonPath("$.fieldErrors")
                    .isEmpty()
            );
    }

    @Test
    void supportWithoutUserReadDeletedPermissionCannotGetDeletedUserById()
        throws Exception {

        insertDeletedUser();

        String accessToken =
            loginAsSupport();

        mockMvc.perform(
                get(
                    "/api/v1/admin/users/deleted/101"
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
                        "/api/v1/admin/users/deleted/101"
                    )
            )
            .andExpect(
                jsonPath("$.fieldErrors")
                    .isEmpty()
            );
    }

    @Test
    void revokedUserReadDeletedPermissionBlocksDeletedUserDetailWithOldToken()
        throws Exception {

        insertDeletedUser();

        /*
        * 登入時 ADMIN 擁有 USER_READ_DELETED，
        * JWT 會包含這項權限。
        */
        String accessToken =
            loginAsAdmin();

        /*
        * JWT 發出後，從資料庫撤銷權限。
        *
        * role_id = 3       -> ADMIN
        * permission_id = 5 -> USER_READ_DELETED
        */
        jdbcTemplate.update(
            """
            UPDATE role_permissions
            SET is_active = FALSE,
                deleted_date = CURRENT_TIMESTAMP
            WHERE role_id = 3
            AND permission_id = 5
            """
        );

        /*
        * @PreAuthorize 仍會根據舊 JWT 判斷通過，
        * 但 Service 必須依據資料庫最新權限回傳 403。
        */
        mockMvc.perform(
                get(
                    "/api/v1/admin/users/deleted/101"
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
                        "/api/v1/admin/users/deleted/101"
                    )
            )
            .andExpect(
                jsonPath("$.fieldErrors")
                    .isEmpty()
            );
    }

    @Test
    void adminCanGetDeletedUserAccountStatusHistory()
        throws Exception {

        /*
        * 建立 ACTIVE 狀態的 id=101。
        */
        insertSearchTargetUser();

        String accessToken =
            loginAsAdmin();

        /*
        * 透過真正的 API 軟刪除會員，
        * 由程式產生狀態異動紀錄。
        */
        String deleteRequestBody = """
            {
            "reason": "  會員要求刪除帳號  "
            }
            """;

        mockMvc.perform(
                delete(
                    "/api/v1/admin/users/101"
                )
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
                    )
                    .contentType(
                        MediaType.APPLICATION_JSON
                    )
                    .accept(
                        MediaType.APPLICATION_JSON
                    )
                    .content(deleteRequestBody)
            )
            .andExpect(status().isNoContent())
            .andExpect(
                content().string("")
            );

        /*
        * 查詢已刪除會員的狀態歷程。
        */
        mockMvc.perform(
                get(
                    "/api/v1/admin/users/deleted/101/status-history"
                )
                    .param("page", "0")
                    .param("size", "20")
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
                jsonPath("$.content.length()")
                    .value(1)
            )
            .andExpect(
                jsonPath(
                    "$.content[0].previousStatus.id"
                )
                    .value(2)
            )
            .andExpect(
                jsonPath(
                    "$.content[0].previousStatus.code"
                )
                    .value("ACTIVE")
            )
            .andExpect(
                jsonPath(
                    "$.content[0].previousStatus.name"
                )
                    .value("正常使用")
            )
            .andExpect(
                jsonPath(
                    "$.content[0].newStatus.id"
                )
                    .value(5)
            )
            .andExpect(
                jsonPath(
                    "$.content[0].newStatus.code"
                )
                    .value("DELETED")
            )
            .andExpect(
                jsonPath(
                    "$.content[0].newStatus.name"
                )
                    .value("已刪除")
            )
            .andExpect(
                jsonPath(
                    "$.content[0].changedByUser.id"
                )
                    .value(200)
            )
            .andExpect(
                jsonPath(
                    "$.content[0].changedByUser.userNumber"
                )
                    .value("ADMIN00001")
            )
            .andExpect(
                jsonPath(
                    "$.content[0].changedByUser.name"
                )
                    .value(
                        "Test Administrator"
                    )
            )
            .andExpect(
                jsonPath(
                    "$.content[0].changedByUser.nickname"
                )
                    .value("Administrator")
            )
            .andExpect(
                jsonPath("$.content[0].note")
                    .value(
                        "會員要求刪除帳號"
                    )
            )
            .andExpect(
                jsonPath("$.content[0].changedDate")
                    .isNotEmpty()
            )
            .andExpect(
                jsonPath("$.page")
                    .value(0)
            )
            .andExpect(
                jsonPath("$.size")
                    .value(20)
            )
            .andExpect(
                jsonPath("$.totalElements")
                    .value(1)
            )
            .andExpect(
                jsonPath("$.totalPages")
                    .value(1)
            )
            .andExpect(
                jsonPath("$.first")
                    .value(true)
            )
            .andExpect(
                jsonPath("$.last")
                    .value(true)
            );
    }

    @ParameterizedTest
    @ValueSource(
        longs = {
            100,
            999999
        }
    )
    void gettingStatusHistoryForNonDeletedOrUnknownUserReturnsNotFound(
        long userId
    ) throws Exception {

        String accessToken =
            loginAsAdmin();

        String requestPath =
            "/api/v1/admin/users/deleted/" +
                userId +
                "/status-history";

        mockMvc.perform(
                get(requestPath)
                    .param("page", "0")
                    .param("size", "20")
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
                    )
                    .accept(
                        MediaType.APPLICATION_JSON
                    )
            )
            .andExpect(status().isNotFound())
            .andExpect(
                content().contentTypeCompatibleWith(
                    MediaType.APPLICATION_JSON
                )
            )
            .andExpect(
                jsonPath("$.status")
                    .value(404)
            )
            .andExpect(
                jsonPath("$.error")
                    .value("Not Found")
            )
            .andExpect(
                jsonPath("$.code")
                    .value("RESOURCE_NOT_FOUND")
            )
            .andExpect(
                jsonPath("$.message")
                    .value(
                        "找不到指定的已刪除使用者"
                    )
            )
            .andExpect(
                jsonPath("$.path")
                    .value(requestPath)
            )
            .andExpect(
                jsonPath("$.fieldErrors")
                    .isEmpty()
            );
    }

    @Test
    void supportWithoutUserReadDeletedPermissionCannotGetDeletedUserStatusHistory()
        throws Exception {

        insertDeletedUser();

        String accessToken =
            loginAsSupport();

        mockMvc.perform(
                get(
                    "/api/v1/admin/users/deleted/101/status-history"
                )
                    .param("page", "0")
                    .param("size", "20")
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
                        "/api/v1/admin/users/deleted/101/status-history"
                    )
            )
            .andExpect(
                jsonPath("$.fieldErrors")
                    .isEmpty()
            );
    }

    @Test
    void revokedUserReadDeletedPermissionBlocksDeletedUserStatusHistoryWithOldToken()
        throws Exception {

        insertDeletedUser();

        /*
        * 登入時 ADMIN 擁有 USER_READ_DELETED，
        * JWT 會包含這項權限。
        */
        String accessToken =
            loginAsAdmin();

        /*
        * JWT 發出後，撤銷資料庫中的權限。
        *
        * role_id = 3       -> ADMIN
        * permission_id = 5 -> USER_READ_DELETED
        */
        jdbcTemplate.update(
            """
            UPDATE role_permissions
            SET is_active = FALSE,
                deleted_date = CURRENT_TIMESTAMP
            WHERE role_id = 3
            AND permission_id = 5
            """
        );

        /*
        * @PreAuthorize 仍會因舊 JWT 而通過，
        * 但 Service 的即時權限檢查必須回傳 403。
        */
        mockMvc.perform(
                get(
                    "/api/v1/admin/users/deleted/101/status-history"
                )
                    .param("page", "0")
                    .param("size", "20")
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
                        "/api/v1/admin/users/deleted/101/status-history"
                    )
            )
            .andExpect(
                jsonPath("$.fieldErrors")
                    .isEmpty()
            );
    }

    @ParameterizedTest
    @CsvSource({
        "-1, 20, page 不得小於 0",
        "0, 0, size 必須介於 1 到 100",
        "0, 101, size 必須介於 1 到 100"
    })
    void invalidDeletedUserStatusHistoryPaginationReturnsBadRequest(
        int page,
        int size,
        String expectedMessage
    ) throws Exception {

        insertDeletedUser();

        String accessToken =
            loginAsAdmin();

        mockMvc.perform(
                get(
                    "/api/v1/admin/users/deleted/101/status-history"
                )
                    .param(
                        "page",
                        String.valueOf(page)
                    )
                    .param(
                        "size",
                        String.valueOf(size)
                    )
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
                    )
                    .accept(
                        MediaType.APPLICATION_JSON
                    )
            )
            .andExpect(status().isBadRequest())
            .andExpect(
                content().contentTypeCompatibleWith(
                    MediaType.APPLICATION_JSON
                )
            )
            .andExpect(
                jsonPath("$.status")
                    .value(400)
            )
            .andExpect(
                jsonPath("$.error")
                    .value("Bad Request")
            )
            .andExpect(
                jsonPath("$.code")
                    .value("BAD_REQUEST")
            )
            .andExpect(
                jsonPath("$.message")
                    .value(expectedMessage)
            )
            .andExpect(
                jsonPath("$.path")
                    .value(
                        "/api/v1/admin/users/deleted/101/status-history"
                    )
            )
            .andExpect(
                jsonPath("$.fieldErrors")
                    .isEmpty()
            );
    }

    @Test
    void adminCanRestoreEmailVerifiedDeletedUserToActive()
        throws Exception {

        /*
        * insertDeletedUser() 的 email_verified = TRUE，
        * 帳號狀態為 DELETED，deleted_date 不為 null。
        */
        insertDeletedUser();

        String accessToken =
            loginAsAdmin();

        String requestBody = """
            {
            "reason": "  會員本人申請恢復帳號  "
            }
            """;

        mockMvc.perform(
                post(
                    "/api/v1/admin/users/deleted/101/restore"
                )
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
                    )
                    .contentType(
                        MediaType.APPLICATION_JSON
                    )
                    .accept(
                        MediaType.APPLICATION_JSON
                    )
                    .content(requestBody)
            )
            .andExpect(status().isOk())
            .andExpect(
                content().contentTypeCompatibleWith(
                    MediaType.APPLICATION_JSON
                )
            )
            .andExpect(
                jsonPath("$.id")
                    .value(101)
            )
            .andExpect(
                jsonPath("$.userNumber")
                    .value("DELETED001")
            )
            .andExpect(
                jsonPath("$.email")
                    .value(
                        "deleted-test@example.com"
                    )
            )
            .andExpect(
                jsonPath("$.emailVerified")
                    .value(true)
            )
            .andExpect(
                jsonPath("$.active")
                    .value(true)
            )
            .andExpect(
                jsonPath("$.accountStatus.id")
                    .value(2)
            )
            .andExpect(
                jsonPath("$.accountStatus.code")
                    .value("ACTIVE")
            )
            .andExpect(
                jsonPath("$.accountStatus.name")
                    .value("正常使用")
            )
            .andExpect(
                jsonPath("$.deletedDate")
                    .value(
                        org.hamcrest.Matchers.nullValue()
                    )
            );

        /*
        * 確認 users 資料確實恢復。
        */
        Long restoredUserCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM users app_user
                JOIN user_account_statuses account_status
                ON account_status.id =
                    app_user.user_account_status_id
                WHERE app_user.id = 101
                AND app_user.email_verified = TRUE
                AND app_user.is_active = TRUE
                AND app_user.deleted_date IS NULL
                AND account_status.code = 'ACTIVE'
                """,
                Long.class
            );

        /*
        * 確認寫入 DELETED → ACTIVE 歷程，
        * 且原因已去除前後空白。
        */
        Long restorationLogCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM user_account_status_logs status_log
                JOIN user_account_statuses previous_status
                ON previous_status.id =
                    status_log.previous_status_id
                JOIN user_account_statuses new_status
                ON new_status.id =
                    status_log.new_status_id
                WHERE status_log.user_id = 101
                AND previous_status.code = 'DELETED'
                AND new_status.code = 'ACTIVE'
                AND status_log.changed_by_user_id = 200
                AND status_log.note =
                    '會員本人申請恢復帳號'
                AND status_log.changed_date IS NOT NULL
                """,
                Long.class
            );

        assertThat(restoredUserCount)
            .isEqualTo(1);

        assertThat(restorationLogCount)
            .isEqualTo(1);
    }

    @Test
    void restoringUnverifiedDeletedUserReturnsToPendingAndCannotLogin()
        throws Exception {

        insertDeletedUser();

        /*
        * 設定可供登入測試的 BCrypt 密碼，
        * 並改成 Email 尚未驗證。
        */
        String encodedPassword =
            passwordEncoder.encode(
                "RestoredPassword123!"
            );

        jdbcTemplate.update(
            """
            UPDATE users
            SET email_verified = FALSE,
                sms_verified = FALSE,
                is_active = FALSE,
                password = ?
            WHERE id = 101
            """,
            encodedPassword
        );

        String adminAccessToken =
            loginAsAdmin();

        String restoreRequestBody = """
            {
            "reason": "會員申請恢復並重新驗證 Email"
            }
            """;

        /*
        * 恢復帳號。
        */
        mockMvc.perform(
                post(
                    "/api/v1/admin/users/deleted/101/restore"
                )
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + adminAccessToken
                    )
                    .contentType(
                        MediaType.APPLICATION_JSON
                    )
                    .accept(
                        MediaType.APPLICATION_JSON
                    )
                    .content(restoreRequestBody)
            )
            .andExpect(status().isOk())
            .andExpect(
                jsonPath("$.id")
                    .value(101)
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
                jsonPath("$.accountStatus.id")
                    .value(1)
            )
            .andExpect(
                jsonPath("$.accountStatus.code")
                    .value(
                        "PENDING_EMAIL_VERIFICATION"
                    )
            )
            .andExpect(
                jsonPath("$.accountStatus.name")
                    .value(
                        "等待電子郵件驗證"
                    )
            )
            .andExpect(
                jsonPath("$.deletedDate")
                    .value(
                        org.hamcrest.Matchers.nullValue()
                    )
            );

        /*
        * 確認資料庫狀態。
        */
        Long restoredPendingUserCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM users app_user
                JOIN user_account_statuses account_status
                ON account_status.id =
                    app_user.user_account_status_id
                WHERE app_user.id = 101
                AND app_user.email_verified = FALSE
                AND app_user.is_active = FALSE
                AND app_user.deleted_date IS NULL
                AND account_status.code =
                    'PENDING_EMAIL_VERIFICATION'
                """,
                Long.class
            );

        /*
        * 確認 DELETED → PENDING_EMAIL_VERIFICATION
        * 的狀態歷程。
        */
        Long restorationLogCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM user_account_status_logs status_log
                JOIN user_account_statuses previous_status
                ON previous_status.id =
                    status_log.previous_status_id
                JOIN user_account_statuses new_status
                ON new_status.id =
                    status_log.new_status_id
                WHERE status_log.user_id = 101
                AND previous_status.code = 'DELETED'
                AND new_status.code =
                    'PENDING_EMAIL_VERIFICATION'
                AND status_log.changed_by_user_id = 200
                AND status_log.note =
                    '會員申請恢復並重新驗證 Email'
                """,
                Long.class
            );

        assertThat(restoredPendingUserCount)
            .isEqualTo(1);

        assertThat(restorationLogCount)
            .isEqualTo(1);

        /*
        * 尚未重新驗證 Email，因此不能登入。
        */
        String loginRequestBody = """
            {
            "email": "deleted-test@example.com",
            "password": "RestoredPassword123!"
            }
            """;

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
                        "請先完成 Email 驗證"
                    )
            )
            .andExpect(
                jsonPath("$.path")
                    .value("/api/v1/auth/login")
            );
    }

    @ParameterizedTest
    @ValueSource(
        longs = {
            100,
            999999
        }
    )
    void restoringNonDeletedOrUnknownUserReturnsNotFound(
        long userId
    ) throws Exception {

        String accessToken =
            loginAsAdmin();

        String requestPath =
            "/api/v1/admin/users/deleted/" +
                userId +
                "/restore";

        String requestBody = """
            {
            "reason": "嘗試恢復會員"
            }
            """;

        mockMvc.perform(
                post(requestPath)
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
                    )
                    .contentType(
                        MediaType.APPLICATION_JSON
                    )
                    .accept(
                        MediaType.APPLICATION_JSON
                    )
                    .content(requestBody)
            )
            .andExpect(status().isNotFound())
            .andExpect(
                content().contentTypeCompatibleWith(
                    MediaType.APPLICATION_JSON
                )
            )
            .andExpect(
                jsonPath("$.status")
                    .value(404)
            )
            .andExpect(
                jsonPath("$.error")
                    .value("Not Found")
            )
            .andExpect(
                jsonPath("$.code")
                    .value("RESOURCE_NOT_FOUND")
            )
            .andExpect(
                jsonPath("$.message")
                    .value(
                        "找不到指定的已刪除使用者"
                    )
            )
            .andExpect(
                jsonPath("$.path")
                    .value(requestPath)
            )
            .andExpect(
                jsonPath("$.fieldErrors")
                    .isEmpty()
            );
    }

    @Test
    void restoredUserCannotBeRestoredAgain()
        throws Exception {

        insertDeletedUser();

        String accessToken =
            loginAsAdmin();

        String requestPath =
            "/api/v1/admin/users/deleted/101/restore";

        String firstRequestBody = """
            {
            "reason": "第一次恢復帳號"
            }
            """;

        /*
        * 第一次恢復成功。
        */
        mockMvc.perform(
                post(requestPath)
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
                    )
                    .contentType(
                        MediaType.APPLICATION_JSON
                    )
                    .accept(
                        MediaType.APPLICATION_JSON
                    )
                    .content(firstRequestBody)
            )
            .andExpect(status().isOk())
            .andExpect(
                jsonPath("$.accountStatus.code")
                    .value("ACTIVE")
            )
            .andExpect(
                jsonPath("$.active")
                    .value(true)
            )
            .andExpect(
                jsonPath("$.deletedDate")
                    .value(
                        org.hamcrest.Matchers.nullValue()
                    )
            );

        String secondRequestBody = """
            {
            "reason": "第二次重複恢復帳號"
            }
            """;

        /*
        * 恢復後已不再符合 deleted_date IS NOT NULL，
        * 第二次操作回傳 404。
        */
        mockMvc.perform(
                post(requestPath)
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
                    )
                    .contentType(
                        MediaType.APPLICATION_JSON
                    )
                    .accept(
                        MediaType.APPLICATION_JSON
                    )
                    .content(secondRequestBody)
            )
            .andExpect(status().isNotFound())
            .andExpect(
                jsonPath("$.status")
                    .value(404)
            )
            .andExpect(
                jsonPath("$.code")
                    .value("RESOURCE_NOT_FOUND")
            )
            .andExpect(
                jsonPath("$.message")
                    .value(
                        "找不到指定的已刪除使用者"
                    )
            )
            .andExpect(
                jsonPath("$.path")
                    .value(requestPath)
            );

        /*
        * 確認只產生一筆恢復紀錄。
        */
        Long restorationLogCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM user_account_status_logs status_log
                JOIN user_account_statuses previous_status
                ON previous_status.id =
                    status_log.previous_status_id
                WHERE status_log.user_id = 101
                AND previous_status.code = 'DELETED'
                """,
                Long.class
            );

        assertThat(restorationLogCount)
            .isEqualTo(1);
    }

    @Test
    void restoringUserWithBlankReasonReturnsValidationError()
        throws Exception {

        insertDeletedUser();

        String accessToken =
            loginAsAdmin();

        String requestBody = """
            {
            "reason": "   "
            }
            """;

        mockMvc.perform(
                post(
                    "/api/v1/admin/users/deleted/101/restore"
                )
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
                    )
                    .contentType(
                        MediaType.APPLICATION_JSON
                    )
                    .accept(
                        MediaType.APPLICATION_JSON
                    )
                    .content(requestBody)
            )
            .andExpect(status().isBadRequest())
            .andExpect(
                content().contentTypeCompatibleWith(
                    MediaType.APPLICATION_JSON
                )
            )
            .andExpect(
                jsonPath("$.status")
                    .value(400)
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
                    .value(
                        "/api/v1/admin/users/deleted/101/restore"
                    )
            )
            .andExpect(
                jsonPath("$.fieldErrors[0].field")
                    .value("reason")
            )
            .andExpect(
                jsonPath("$.fieldErrors[0].message")
                    .value(
                        "恢復會員時必須填寫原因"
                    )
            );

        assertUserRemainsDeletedAfterFailedRestore();
    }

    @Test
    void restoringUserWithOversizedReasonReturnsValidationError()
        throws Exception {

        insertDeletedUser();

        String accessToken =
            loginAsAdmin();

        String oversizedReason =
            "a".repeat(256);

        String requestBody = """
            {
            "reason": "%s"
            }
            """.formatted(oversizedReason);

        mockMvc.perform(
                post(
                    "/api/v1/admin/users/deleted/101/restore"
                )
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
                    )
                    .contentType(
                        MediaType.APPLICATION_JSON
                    )
                    .accept(
                        MediaType.APPLICATION_JSON
                    )
                    .content(requestBody)
            )
            .andExpect(status().isBadRequest())
            .andExpect(
                content().contentTypeCompatibleWith(
                    MediaType.APPLICATION_JSON
                )
            )
            .andExpect(
                jsonPath("$.status")
                    .value(400)
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
                    .value(
                        "/api/v1/admin/users/deleted/101/restore"
                    )
            )
            .andExpect(
                jsonPath("$.fieldErrors[0].field")
                    .value("reason")
            )
            .andExpect(
                jsonPath("$.fieldErrors[0].message")
                    .value(
                        "恢復原因長度不得超過 255"
                    )
            );

        assertUserRemainsDeletedAfterFailedRestore();
    }

    @Test
    void supportWithoutUserRestorePermissionCannotRestoreDeletedUser()
        throws Exception {

        insertDeletedUser();

        String accessToken =
            loginAsSupport();

        String requestBody = """
            {
            "reason": "客服嘗試恢復會員"
            }
            """;

        mockMvc.perform(
                post(
                    "/api/v1/admin/users/deleted/101/restore"
                )
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
                    )
                    .contentType(
                        MediaType.APPLICATION_JSON
                    )
                    .accept(
                        MediaType.APPLICATION_JSON
                    )
                    .content(requestBody)
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
                        "/api/v1/admin/users/deleted/101/restore"
                    )
            )
            .andExpect(
                jsonPath("$.fieldErrors")
                    .isEmpty()
            );

        /*
        * 確認權限不足時沒有修改會員資料，
        * 也沒有產生狀態歷程。
        */
        assertUserRemainsDeletedAfterFailedRestore();
    }

    @Test
    void revokedUserRestorePermissionBlocksRestoreWithOldToken()
        throws Exception {

        insertDeletedUser();

        /*
        * 登入時 ADMIN 擁有 USER_RESTORE，
        * 所以 JWT 會包含此權限。
        */
        String accessToken =
            loginAsAdmin();

        /*
        * JWT 發出後，撤銷資料庫中的 USER_RESTORE。
        *
        * role_id = 3       -> ADMIN
        * permission_id = 6 -> USER_RESTORE
        */
        jdbcTemplate.update(
            """
            UPDATE role_permissions
            SET is_active = FALSE,
                deleted_date = CURRENT_TIMESTAMP
            WHERE role_id = 3
            AND permission_id = 6
            """
        );

        String requestBody = """
            {
            "reason": "使用舊 Token 嘗試恢復會員"
            }
            """;

        /*
        * @PreAuthorize 仍會因 JWT 內的舊權限而通過，
        * Service 的即時資料庫權限檢查必須回傳 403。
        */
        mockMvc.perform(
                post(
                    "/api/v1/admin/users/deleted/101/restore"
                )
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
                    )
                    .contentType(
                        MediaType.APPLICATION_JSON
                    )
                    .accept(
                        MediaType.APPLICATION_JSON
                    )
                    .content(requestBody)
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
                        "/api/v1/admin/users/deleted/101/restore"
                    )
            )
            .andExpect(
                jsonPath("$.fieldErrors")
                    .isEmpty()
            );

        /*
        * 舊 JWT 不得繞過資料庫的最新權限狀態。
        */
        assertUserRemainsDeletedAfterFailedRestore();
    }

    @Test
    void restoredActiveUserCanLoginButCannotRegisterDuplicateAccount()
        throws Exception {

        insertDeletedUser();

        /*
        * 設定可供登入測試的 BCrypt 密碼。
        */
        String encodedPassword =
            passwordEncoder.encode(
                "RestoredPassword123!"
            );

        jdbcTemplate.update(
            """
            UPDATE users
            SET password = ?
            WHERE id = 101
            """,
            encodedPassword
        );

        String adminAccessToken =
            loginAsAdmin();

        String restoreRequestBody = """
            {
            "reason": "會員完成身分確認後恢復帳號"
            }
            """;

        /*
        * 恢復已刪除帳號。
        */
        mockMvc.perform(
                post(
                    "/api/v1/admin/users/deleted/101/restore"
                )
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + adminAccessToken
                    )
                    .contentType(
                        MediaType.APPLICATION_JSON
                    )
                    .accept(
                        MediaType.APPLICATION_JSON
                    )
                    .content(restoreRequestBody)
            )
            .andExpect(status().isOk())
            .andExpect(
                jsonPath("$.emailVerified")
                    .value(true)
            )
            .andExpect(
                jsonPath("$.active")
                    .value(true)
            )
            .andExpect(
                jsonPath("$.accountStatus.code")
                    .value("ACTIVE")
            )
            .andExpect(
                jsonPath("$.deletedDate")
                    .value(
                        org.hamcrest.Matchers.nullValue()
                    )
            );

        /*
        * 恢復後可以使用原本 Email 登入。
        */
        String loginRequestBody = """
            {
            "email": "deleted-test@example.com",
            "password": "RestoredPassword123!"
            }
            """;

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
                jsonPath("$.user.id")
                    .value(101)
            )
            .andExpect(
                jsonPath("$.user.email")
                    .value(
                        "deleted-test@example.com"
                    )
            );

        /*
        * 恢復後再次使用相同 Email 註冊，
        * 應視為一般的 Email 重複。
        */
        String registrationRequestBody = """
            {
            "countryId": null,
            "timezoneId": null,
            "languageId": 1,
            "name": "Duplicate Account",
            "nickname": "Duplicate",
            "email": "deleted-test@example.com",
            "password": "AnotherPassword123!",
            "birthday": "1995-05-20",
            "phone": "+886923456789"
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
                    .content(registrationRequestBody)
            )
            .andExpect(status().isConflict())
            .andExpect(
                jsonPath("$.status")
                    .value(409)
            )
            .andExpect(
                jsonPath("$.code")
                    .value("CONFLICT")
            )
            .andExpect(
                jsonPath("$.message")
                    .value("此 Email 已被註冊")
            )
            .andExpect(
                jsonPath("$.path")
                    .value(
                        "/api/v1/registration"
                    )
            );

        /*
        * 相同 Email 始終只有原本的一個帳號。
        */
        Long sameEmailUserCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM users
                WHERE LOWER(email) =
                    LOWER(
                        'deleted-test@example.com'
                    )
                """,
                Long.class
            );

        assertThat(sameEmailUserCount)
            .isEqualTo(1);
    }

    @Test
    void restoringUserDoesNotReactivateOldRefreshToken()
        throws Exception {

        /*
        * id=100 在刪除前先登入，
        * 取得舊 Refresh Token。
        */
        String userLoginRequestBody = """
            {
            "email": "test@example.com",
            "password": "Password123!"
            }
            """;

        MvcResult originalLoginResult =
            mockMvc.perform(
                    post("/api/v1/auth/login")
                        .contentType(
                            MediaType.APPLICATION_JSON
                        )
                        .accept(
                            MediaType.APPLICATION_JSON
                        )
                        .content(userLoginRequestBody)
                )
                .andExpect(status().isOk())
                .andReturn();

        String originalLoginResponseBody =
            originalLoginResult
                .getResponse()
                .getContentAsString();

        String oldRefreshToken =
            JsonPath.read(
                originalLoginResponseBody,
                "$.refreshToken"
            );

        Long oldRefreshTokenId =
            jdbcTemplate.queryForObject(
                """
                SELECT id
                FROM user_refresh_tokens
                WHERE user_id = 100
                AND revoked_date IS NULL
                """,
                Long.class
            );

        String adminAccessToken =
            loginAsAdmin();

        /*
        * 軟刪除會員，這時所有 Refresh Token
        * 都應該被撤銷。
        */
        String deleteRequestBody = """
            {
            "reason": "測試刪除後再恢復帳號"
            }
            """;

        mockMvc.perform(
                delete(
                    "/api/v1/admin/users/100"
                )
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + adminAccessToken
                    )
                    .contentType(
                        MediaType.APPLICATION_JSON
                    )
                    .content(deleteRequestBody)
            )
            .andExpect(status().isNoContent());

        /*
        * 恢復帳號。
        */
        String restoreRequestBody = """
            {
            "reason": "完成身分確認後恢復"
            }
            """;

        mockMvc.perform(
                post(
                    "/api/v1/admin/users/deleted/100/restore"
                )
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + adminAccessToken
                    )
                    .contentType(
                        MediaType.APPLICATION_JSON
                    )
                    .accept(
                        MediaType.APPLICATION_JSON
                    )
                    .content(restoreRequestBody)
            )
            .andExpect(status().isOk())
            .andExpect(
                jsonPath("$.active")
                    .value(true)
            )
            .andExpect(
                jsonPath("$.accountStatus.code")
                    .value("ACTIVE")
            );

        /*
        * 即使帳號已恢復，刪除前取得的 Refresh Token
        * 仍然必須保持失效。
        */
        String oldRefreshRequestBody = """
            {
            "refreshToken": "%s"
            }
            """.formatted(oldRefreshToken);

        mockMvc.perform(
                post("/api/v1/auth/refresh")
                    .contentType(
                        MediaType.APPLICATION_JSON
                    )
                    .accept(
                        MediaType.APPLICATION_JSON
                    )
                    .content(oldRefreshRequestBody)
            )
            .andExpect(status().isUnauthorized())
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
                        "Refresh Token 無效或已過期"
                    )
            )
            .andExpect(
                jsonPath("$.path")
                    .value("/api/v1/auth/refresh")
            );

        /*
        * 確認舊 Token 在資料庫中仍是撤銷狀態。
        */
        Long revokedOldTokenCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM user_refresh_tokens
                WHERE id = ?
                AND user_id = 100
                AND revoked_date IS NOT NULL
                """,
                Long.class,
                oldRefreshTokenId
            );

        assertThat(revokedOldTokenCount)
            .isEqualTo(1);

        /*
        * 帳號恢復後必須重新登入，
        * 才能取得新的 Access Token 與 Refresh Token。
        */
        MvcResult newLoginResult =
            mockMvc.perform(
                    post("/api/v1/auth/login")
                        .contentType(
                            MediaType.APPLICATION_JSON
                        )
                        .accept(
                            MediaType.APPLICATION_JSON
                        )
                        .content(userLoginRequestBody)
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

        String newLoginResponseBody =
            newLoginResult
                .getResponse()
                .getContentAsString();

        String newRefreshToken =
            JsonPath.read(
                newLoginResponseBody,
                "$.refreshToken"
            );

        assertThat(newRefreshToken)
            .isNotEqualTo(oldRefreshToken);
    }

    @Test
    void restoringUserDoesNotReactivateOldAccessToken()
        throws Exception {

        String userLoginRequestBody = """
            {
            "email": "test@example.com",
            "password": "Password123!"
            }
            """;

        /*
        * 使用者在刪除前登入，取得舊 Access Token。
        */
        MvcResult originalLoginResult =
            mockMvc.perform(
                    post("/api/v1/auth/login")
                        .contentType(
                            MediaType.APPLICATION_JSON
                        )
                        .accept(
                            MediaType.APPLICATION_JSON
                        )
                        .content(userLoginRequestBody)
                )
                .andExpect(status().isOk())
                .andReturn();

        String originalLoginResponseBody =
            originalLoginResult
                .getResponse()
                .getContentAsString();

        String oldAccessToken =
            JsonPath.read(
                originalLoginResponseBody,
                "$.accessToken"
            );

        String adminAccessToken =
            loginAsAdmin();

        /*
        * 軟刪除 id=100。
        * 這會撤銷該會員所有 Refresh Token，
        * 使舊 sid 對應的 Session 失效。
        */
        String deleteRequestBody = """
            {
            "reason": "測試舊 Access Token 是否失效"
            }
            """;

        mockMvc.perform(
                delete(
                    "/api/v1/admin/users/100"
                )
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + adminAccessToken
                    )
                    .contentType(
                        MediaType.APPLICATION_JSON
                    )
                    .content(deleteRequestBody)
            )
            .andExpect(status().isNoContent());

        /*
        * 恢復帳號。
        */
        String restoreRequestBody = """
            {
            "reason": "完成身分確認後恢復帳號"
            }
            """;

        mockMvc.perform(
                post(
                    "/api/v1/admin/users/deleted/100/restore"
                )
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + adminAccessToken
                    )
                    .contentType(
                        MediaType.APPLICATION_JSON
                    )
                    .accept(
                        MediaType.APPLICATION_JSON
                    )
                    .content(restoreRequestBody)
            )
            .andExpect(status().isOk())
            .andExpect(
                jsonPath("$.active")
                    .value(true)
            )
            .andExpect(
                jsonPath("$.accountStatus.code")
                    .value("ACTIVE")
            );

        /*
        * 帳號雖然恢復 ACTIVE，
        * 但舊 Access Token 的 Session 已失效。
        */
        mockMvc.perform(
                get("/api/v1/auth/me")
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + oldAccessToken
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
                    .value("/api/v1/auth/me")
            );

        /*
        * 恢復後重新登入，建立全新的 Session。
        */
        MvcResult newLoginResult =
            mockMvc.perform(
                    post("/api/v1/auth/login")
                        .contentType(
                            MediaType.APPLICATION_JSON
                        )
                        .accept(
                            MediaType.APPLICATION_JSON
                        )
                        .content(userLoginRequestBody)
                )
                .andExpect(status().isOk())
                .andReturn();

        String newLoginResponseBody =
            newLoginResult
                .getResponse()
                .getContentAsString();

        String newAccessToken =
            JsonPath.read(
                newLoginResponseBody,
                "$.accessToken"
            );

        assertThat(newAccessToken)
            .isNotEqualTo(oldAccessToken);

        /*
        * 新 Access Token 的 Session 有效，
        * 可以正常取得 /me。
        */
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
                jsonPath("$.id")
                    .value(100)
            )
            .andExpect(
                jsonPath("$.email")
                    .value("test@example.com")
            )
            .andExpect(
                jsonPath("$.active")
                    .value(true)
            )
            .andExpect(
                jsonPath("$.accountStatus.code")
                    .value("ACTIVE")
            );
    }

    @Test
    void revokedUserReadPermissionBlocksUserListWithOldToken()
        throws Exception {

        /*
        * 登入時 SUPPORT 擁有 USER_READ，
        * JWT 會包含這項權限。
        */
        String accessToken =
            loginAsSupport();

        /*
        * JWT 發出後撤銷資料庫中的 USER_READ。
        *
        * role_id = 2       -> SUPPORT
        * permission_id = 2 -> USER_READ
        */
        jdbcTemplate.update(
            """
            UPDATE role_permissions
            SET is_active = FALSE,
                deleted_date = CURRENT_TIMESTAMP
            WHERE role_id = 2
            AND permission_id = 2
            """
        );

        /*
        * 舊 JWT 仍包含 USER_READ，
        * 因此 @PreAuthorize 會通過。
        *
        * Service 必須依資料庫最新權限回傳 403。
        */
        mockMvc.perform(
                get("/api/v1/admin/users")
                    .param("page", "0")
                    .param("size", "20")
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
                        "/api/v1/admin/users"
                    )
            )
            .andExpect(
                jsonPath("$.fieldErrors")
                    .isEmpty()
            );
    }

    private String loginAsDefaultUser()
        throws Exception {

        String requestBody = """
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
                        .content(requestBody)
                )
                .andExpect(status().isOk())
                .andReturn();

        String responseBody =
            loginResult
                .getResponse()
                .getContentAsString();

        return JsonPath.read(
            responseBody,
            "$.accessToken"
        );
    }

    private void assignSupportRole() {
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
    }

    private String loginAsSupport()
        throws Exception {

        assignSupportRole();

        return loginAsDefaultUser();
    }

    private void insertSearchTargetUser() {
        jdbcTemplate.update(
            """
            INSERT INTO users (
                id,
                country_id,
                timezone_id,
                language_id,
                user_account_status_id,
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
                101,
                NULL,
                NULL,
                1,
                2,
                'SEARCH0001',
                'Alice Chen',
                'FinanceQueen',
                'alice.chen@example.com',
                'not-used-in-this-test',
                DATE '1991-02-02',
                '0933333333',
                TRUE,
                TRUE,
                TRUE,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP
            )
            """
        );
    }

    private void insertSuspendedUser() {
        jdbcTemplate.update(
            """
            INSERT INTO users (
                id,
                country_id,
                timezone_id,
                language_id,
                user_account_status_id,
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
                101,
                NULL,
                NULL,
                1,
                3,
                'SUSPTEST01',
                'Suspended Test User',
                'SuspendedTester',
                'suspended-test@example.com',
                'not-used-in-this-test',
                DATE '1991-02-02',
                '0955555555',
                TRUE,
                FALSE,
                FALSE,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP
            )
            """
        );
    }

    private void insertSpecialCharacterUser() {
        jdbcTemplate.update(
            """
            INSERT INTO users (
                id,
                country_id,
                timezone_id,
                language_id,
                user_account_status_id,
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
                101,
                NULL,
                NULL,
                1,
                2,
                'SPECIAL001',
                'Budget_100%! User',
                'Special',
                'special@example.com',
                'not-used-in-this-test',
                DATE '1991-02-02',
                '0944444444',
                TRUE,
                TRUE,
                TRUE,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP
            )
            """
        );
    }

    private void insertAdminUser() {
        String encodedPassword =
            passwordEncoder.encode(
                "AdminPassword123!"
            );

        jdbcTemplate.update(
            """
            INSERT INTO users (
                id,
                country_id,
                timezone_id,
                language_id,
                user_account_status_id,
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
                200,
                NULL,
                NULL,
                1,
                2,
                'ADMIN00001',
                'Test Administrator',
                'Administrator',
                'admin@example.com',
                ?,
                DATE '1990-01-01',
                '0966666666',
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

    private void assignAdminRole() {
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
                10,
                200,
                3,
                TRUE,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP
            )
            """
        );
    }

    private String loginAsAdmin()
        throws Exception {

        insertAdminUser();
        assignAdminRole();

        String requestBody = """
            {
            "email": "admin@example.com",
            "password": "AdminPassword123!"
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
                        .content(requestBody)
                )
                .andExpect(status().isOk())
                .andReturn();

        return JsonPath.read(
            loginResult
                .getResponse()
                .getContentAsString(),
            "$.accessToken"
        );
    }

    private void insertDeletedUser() {
        jdbcTemplate.update(
            """
            INSERT INTO users (
                id,
                country_id,
                timezone_id,
                language_id,
                user_account_status_id,
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
                updated_date,
                deleted_date
            )
            VALUES (
                101,
                NULL,
                NULL,
                1,
                5,
                'DELETED001',
                'Deleted Test User',
                'DeletedTester',
                'deleted-test@example.com',
                'not-used-in-this-test',
                DATE '1991-02-02',
                '0955555555',
                TRUE,
                FALSE,
                FALSE,
                TIMESTAMP '2026-01-01 10:00:00',
                TIMESTAMP '2026-02-01 10:00:00',
                TIMESTAMP '2026-02-01 10:00:00'
            )
            """
        );
    }

    private void insertAnotherDeletedUser() {
        jdbcTemplate.update(
            """
            INSERT INTO users (
                id,
                country_id,
                timezone_id,
                language_id,
                user_account_status_id,
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
                updated_date,
                deleted_date
            )
            VALUES (
                102,
                NULL,
                NULL,
                1,
                5,
                'REMOVED002',
                'Another Removed User',
                'OtherDeleted',
                'another-deleted@example.com',
                'not-used-in-this-test',
                DATE '1992-03-03',
                '0977777777',
                TRUE,
                TRUE,
                FALSE,
                TIMESTAMP '2026-01-02 10:00:00',
                TIMESTAMP '2026-03-01 10:00:00',
                TIMESTAMP '2026-03-01 10:00:00'
            )
            """
        );
    }

    private void assertUserRemainsDeletedAfterFailedRestore() {

        Long deletedUserCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM users app_user
                JOIN user_account_statuses account_status
                ON account_status.id =
                    app_user.user_account_status_id
                WHERE app_user.id = 101
                AND app_user.is_active = FALSE
                AND app_user.deleted_date IS NOT NULL
                AND account_status.code = 'DELETED'
                """,
                Long.class
            );

        Long restorationLogCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM user_account_status_logs
                WHERE user_id = 101
                """,
                Long.class
            );

        assertThat(deletedUserCount)
            .isEqualTo(1);

        assertThat(restorationLogCount)
            .isZero();
    }
}
