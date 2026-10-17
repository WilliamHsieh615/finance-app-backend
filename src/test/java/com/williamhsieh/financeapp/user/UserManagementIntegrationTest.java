package com.williamhsieh.financeapp.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

        String accessToken = 
            loginAsSupport();

        jdbcTemplate.update(
            """
            UPDATE users
            SET deleted_date = CURRENT_TIMESTAMP
            WHERE id = 100
            """
        );

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
                        "/api/v1/admin/users/100"
                    )
            )
            .andExpect(
                jsonPath("$.fieldErrors")
                    .isEmpty()
            );

        /*
         * 確認資料沒有真的被刪除，
         * 只是 deleted_date 已經有值。
         */
        Long softDeletedUserCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM users
                WHERE id = 100
                  AND deleted_date IS NOT NULL
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
}
