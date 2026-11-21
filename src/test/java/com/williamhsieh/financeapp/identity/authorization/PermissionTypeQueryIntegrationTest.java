package com.williamhsieh.financeapp.identity.authorization;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import com.jayway.jsonpath.JsonPath;
import com.williamhsieh.financeapp.support.IntegrationTestSupport;

class PermissionTypeQueryIntegrationTest
    extends IntegrationTestSupport {

    @Test
    void permissionReaderCanGetActivePermissionTypes()
        throws Exception {

        /*
         * 停用的權限類型不可出現在結果中。
         */
        jdbcTemplate.update(
            """
            INSERT INTO permission_types (
                id,
                code,
                name,
                note,
                is_active,
                created_date,
                updated_date
            )
            VALUES (
                4,
                'INACTIVE_TYPE',
                '停用類型',
                '這筆資料不應回傳',
                FALSE,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP
            )
            """
        );

        /*
         * 已軟刪除的權限類型也不可出現。
         */
        jdbcTemplate.update(
            """
            INSERT INTO permission_types (
                id,
                code,
                name,
                note,
                is_active,
                created_date,
                updated_date,
                deleted_date
            )
            VALUES (
                5,
                'DELETED_TYPE',
                '已刪除類型',
                '這筆資料不應回傳',
                TRUE,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP
            )
            """
        );

        String accessToken =
            loginAsAdmin();

        mockMvc.perform(
                get(
                    "/api/v1/admin/permission-types"
                )
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
                jsonPath("$.length()")
                    .value(3)
            )
            /*
             * 必須依 code ASC 排序。
             */
            .andExpect(
                jsonPath("$[0].id")
                    .value(3)
            )
            .andExpect(
                jsonPath("$[0].code")
                    .value("AUTHORIZATION")
            )
            .andExpect(
                jsonPath("$[0].name")
                    .value("角色與授權")
            )
            .andExpect(
                jsonPath("$[1].code")
                    .value("USER")
            )
            .andExpect(
                jsonPath("$[2].code")
                    .value("USER_MANAGEMENT")
            )
            /*
             * 無效資料不可出現在結果中。
             */
            .andExpect(
                jsonPath(
                    "$[?(@.code == 'INACTIVE_TYPE')]"
                )
                    .isEmpty()
            )
            .andExpect(
                jsonPath(
                    "$[?(@.code == 'DELETED_TYPE')]"
                )
                    .isEmpty()
            );
    }

    @Test
    void userWithoutPermissionReadCannotGetPermissionTypes()
        throws Exception {

        /*
        * 測試基底中的一般 USER
        * 沒有 PERMISSION_READ。
        */
        String accessToken =
            loginAsDefaultUser();

        String requestPath =
            "/api/v1/admin/permission-types";

        mockMvc.perform(
                get(requestPath)
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
                    )
                    .accept(MediaType.APPLICATION_JSON)
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
                    .value(requestPath)
            )
            .andExpect(
                jsonPath("$.fieldErrors")
                    .isEmpty()
            );
    }

    @Test
    void revokedPermissionReadBlocksExistingAccessToken()
        throws Exception {

        /*
        * 登入當下 ADMIN 還擁有 PERMISSION_READ，
        * 因此 JWT 內會包含這個權限。
        */
        String accessToken =
            loginAsAdmin();

        /*
        * 登入後才從資料庫即時撤銷權限。
        *
        * 測試基底資料：
        * ADMIN role_id = 3
        * PERMISSION_READ permission_id = 9
        */
        jdbcTemplate.update(
            """
            UPDATE role_permissions
            SET
                is_active = FALSE,
                deleted_date = CURRENT_TIMESTAMP
            WHERE role_id = 3
            AND permission_id = 9
            """
        );

        String requestPath =
            "/api/v1/admin/permission-types";

        /*
        * JWT 仍然包含 PERMISSION_READ，
        * 所以 Controller 的 @PreAuthorize 會通過。
        *
        * 但 Service 的 PermissionAccessPolicy
        * 必須根據資料庫即時狀態拒絕請求。
        */
        mockMvc.perform(
                get(requestPath)
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
                    )
                    .accept(MediaType.APPLICATION_JSON)
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
                    .value(requestPath)
            )
            .andExpect(
                jsonPath("$.fieldErrors")
                    .isEmpty()
            );
    }

    @Test
    void permissionTypesWithoutAccessTokenReturnsUnauthorized()
        throws Exception {

        String requestPath =
            "/api/v1/admin/permission-types";

        mockMvc.perform(
                get(requestPath)
                    .accept(MediaType.APPLICATION_JSON)
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
                    .value(requestPath)
            )
            .andExpect(
                jsonPath("$.fieldErrors")
                    .isArray()
            )
            .andExpect(
                jsonPath("$.fieldErrors")
                    .isEmpty()
            );
    }

    @Test
    void permissionTypesWithInvalidAccessTokenReturnsUnauthorized()
        throws Exception {

        String requestPath =
            "/api/v1/admin/permission-types";

        String invalidAccessToken =
            "this-is-not-a-valid-jwt";

        mockMvc.perform(
                get(requestPath)
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
                jsonPath("$.status")
                    .value(401)
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
                    .value(requestPath)
            )
            .andExpect(
                jsonPath("$.fieldErrors")
                    .isArray()
            )
            .andExpect(
                jsonPath("$.fieldErrors")
                    .isEmpty()
            );
    }

    private String loginAsAdmin()
        throws Exception {

        /*
         * 測試資料中的 ADMIN 角色 id 是 3，
         * 而且已經擁有 PERMISSION_READ。
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
                3,
                TRUE,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP
            )
            """
        );

        return loginAsDefaultUser();
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

        return JsonPath.read(
            loginResult
                .getResponse()
                .getContentAsString(),
            "$.accessToken"
        );
    }
}
