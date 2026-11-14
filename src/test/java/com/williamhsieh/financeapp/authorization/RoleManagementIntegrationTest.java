package com.williamhsieh.financeapp.authorization;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import com.jayway.jsonpath.JsonPath;

import com.williamhsieh.financeapp.support.IntegrationTestSupport;

class RoleManagementIntegrationTest
    extends IntegrationTestSupport {

    @Test
    void adminWithRoleReadPermissionCanGetAssignableRoles()
        throws Exception {

        /*
         * 不啟用的角色不可出現在列表。
         */
        jdbcTemplate.update(
            """
            INSERT INTO roles (
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
                'AUDITOR',
                '稽核人員',
                '測試停用角色',
                FALSE,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP
            )
            """
        );

        /*
         * 已軟刪除角色也不可出現在列表。
         */
        jdbcTemplate.update(
            """
            INSERT INTO roles (
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
                'OPERATIONS',
                '營運人員',
                '測試已刪除角色',
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
                get("/api/v1/admin/roles")
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
                jsonPath("$.length()")
                    .value(3)
            )
            .andExpect(
                jsonPath("$[0].id")
                    .value(3)
            )
            .andExpect(
                jsonPath("$[0].code")
                    .value("ADMIN")
            )
            .andExpect(
                jsonPath("$[0].name")
                    .value("系統管理員")
            )
            .andExpect(
                jsonPath("$[1].id")
                    .value(2)
            )
            .andExpect(
                jsonPath("$[1].code")
                    .value("SUPPORT")
            )
            .andExpect(
                jsonPath("$[1].name")
                    .value("客服人員")
            )
            .andExpect(
                jsonPath("$[2].id")
                    .value(1)
            )
            .andExpect(
                jsonPath("$[2].code")
                    .value("USER")
            )
            .andExpect(
                jsonPath("$[2].name")
                    .value("一般使用者")
            );
    }

    @Test
    void userWithoutRoleReadPermissionCannotGetAssignableRoles()
        throws Exception {

        /*
        * 測試基底的 id=100 只有 USER 角色，
        * 沒有 ROLE_READ。
        */
        String accessToken =
            loginAsDefaultUser();

        mockMvc.perform(
                get("/api/v1/admin/roles")
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
                    .value("/api/v1/admin/roles")
            )
            .andExpect(
                jsonPath("$.fieldErrors")
                    .isEmpty()
            );
    }

    @Test
    void revokedRoleReadPermissionBlocksPreviouslyIssuedToken()
        throws Exception {

        /*
        * 登入時 ADMIN 擁有 ROLE_READ，
        * JWT 會包含此權限。
        */
        String accessToken =
            loginAsAdmin();

        /*
        * JWT 發出後，撤銷資料庫中的 ROLE_READ。
        *
        * role_id = 3       -> ADMIN
        * permission_id = 7 -> ROLE_READ
        */
        jdbcTemplate.update(
            """
            UPDATE role_permissions
            SET is_active = FALSE,
                deleted_date = CURRENT_TIMESTAMP
            WHERE role_id = 3
            AND permission_id = 7
            """
        );

        /*
        * @PreAuthorize 仍會依照舊 JWT 判斷通過，
        * PermissionAccessPolicy 必須依 DB 最新狀態
        * 回傳 403。
        */
        mockMvc.perform(
                get("/api/v1/admin/roles")
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
                    .value("/api/v1/admin/roles")
            )
            .andExpect(
                jsonPath("$.fieldErrors")
                    .isEmpty()
            );
    }

    @Test
    void adminWithRoleReadPermissionCanGetUserActiveRoles()
        throws Exception {

        /*
        * 建立一個有效的 AUDITOR 角色，
        * 但稍後將會員與此角色的關聯軟刪除。
        */
        jdbcTemplate.update(
            """
            INSERT INTO roles (
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
                'AUDITOR',
                '稽核人員',
                '唯讀查看稽核資料',
                TRUE,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP
            )
            """
        );

        /*
        * SUPPORT 關聯存在，但 is_active = false。
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
                3,
                100,
                2,
                FALSE,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP
            )
            """
        );

        /*
        * AUDITOR 關聯已軟刪除。
        */
        jdbcTemplate.update(
            """
            INSERT INTO user_roles (
                id,
                user_id,
                role_id,
                is_active,
                created_date,
                updated_date,
                deleted_date
            )
            VALUES (
                4,
                100,
                4,
                TRUE,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP
            )
            """
        );

        /*
        * loginAsAdmin() 會額外把 ADMIN 指派給 id=100。
        * 測試基底原本已有 USER。
        */
        String accessToken =
            loginAsAdmin();

        mockMvc.perform(
                get(
                    "/api/v1/admin/users/100/roles"
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
                jsonPath("$.length()")
                    .value(2)
            )
            .andExpect(
                jsonPath("$[0].id")
                    .value(3)
            )
            .andExpect(
                jsonPath("$[0].code")
                    .value("ADMIN")
            )
            .andExpect(
                jsonPath("$[0].name")
                    .value("系統管理員")
            )
            .andExpect(
                jsonPath("$[1].id")
                    .value(1)
            )
            .andExpect(
                jsonPath("$[1].code")
                    .value("USER")
            )
            .andExpect(
                jsonPath("$[1].name")
                    .value("一般使用者")
            );
    }

    @ParameterizedTest
    @ValueSource(
        longs = {
            101,
            999999
        }
    )
    void gettingRolesForDeletedOrUnknownUserReturnsNotFound(
        long targetUserId
    ) throws Exception {

        /*
        * id=101 是已刪除會員。
        * id=999999 不存在。
        */
        insertDeletedTargetUser();

        String accessToken =
            loginAsAdmin();

        String requestPath =
            "/api/v1/admin/users/" +
                targetUserId +
                "/roles";

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
                        "找不到指定的使用者"
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
    void userWithoutRoleReadPermissionCannotGetUserRoles()
        throws Exception {

        /*
        * 測試基底的 id=100 只有 USER，
        * 沒有 ROLE_READ。
        */
        String accessToken =
            loginAsDefaultUser();

        mockMvc.perform(
                get(
                    "/api/v1/admin/users/100/roles"
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
                        "/api/v1/admin/users/100/roles"
                    )
            )
            .andExpect(
                jsonPath("$.fieldErrors")
                    .isEmpty()
            );
    }

    @Test
    void revokedRoleReadPermissionBlocksUserRolesWithOldToken()
        throws Exception {

        /*
        * 登入時 ADMIN 擁有 ROLE_READ，
        * JWT 會包含此權限。
        */
        String accessToken =
            loginAsAdmin();

        /*
        * JWT 發出後撤銷資料庫中的 ROLE_READ。
        *
        * role_id = 3       -> ADMIN
        * permission_id = 7 -> ROLE_READ
        */
        jdbcTemplate.update(
            """
            UPDATE role_permissions
            SET is_active = FALSE,
                deleted_date = CURRENT_TIMESTAMP
            WHERE role_id = 3
            AND permission_id = 7
            """
        );

        /*
        * @PreAuthorize 會根據舊 JWT 通過，
        * PermissionAccessPolicy 必須根據 DB
        * 最新狀態回傳 403。
        */
        mockMvc.perform(
                get(
                    "/api/v1/admin/users/100/roles"
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
                        "/api/v1/admin/users/100/roles"
                    )
            )
            .andExpect(
                jsonPath("$.fieldErrors")
                    .isEmpty()
            );
    }

    @Test
    void adminWithRoleAssignPermissionCanAssignRole()
        throws Exception {

        String accessToken =
            loginAsAdmin();

        String requestBody = """
            {
            "roleCode": "support",
            "note": "因客服工作需要指派客服角色"
            }
            """;

        mockMvc.perform(
                post(
                    "/api/v1/admin/users/100/roles"
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
                    .value(2)
            )
            .andExpect(
                jsonPath("$.code")
                    .value("SUPPORT")
            )
            .andExpect(
                jsonPath("$.name")
                    .value("客服人員")
            );

        Integer userRoleCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM user_roles
                WHERE user_id = ?
                AND role_id = ?
                AND is_active = TRUE
                AND deleted_date IS NULL
                """,
                Integer.class,
                100L,
                2L
            );

        assertThat(userRoleCount)
            .isEqualTo(1);

        Integer assignmentLogCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM user_role_assignment_logs log
                JOIN user_role_assignment_types type
                ON type.id =
                    log.user_role_assignment_type_id
                WHERE log.user_id = ?
                AND log.role_id = ?
                AND log.changed_by_user_id = ?
                AND type.code = ?
                AND log.note = ?
                """,
                Integer.class,
                100L,
                2L,
                100L,
                "ASSIGNED",
                "因客服工作需要指派客服角色"
            );

        assertThat(assignmentLogCount)
            .isEqualTo(1);
    }

    @Test
    void assigningPreviouslyRevokedRoleReactivatesExistingUserRole()
        throws Exception {

        /*
        * 模擬 SUPPORT 角色以前曾經指派，
        * 後來已被撤銷。
        */
        jdbcTemplate.update(
            """
            INSERT INTO user_roles (
                id,
                user_id,
                role_id,
                is_active,
                created_date,
                updated_date,
                deleted_date
            )
            VALUES (
                3,
                100,
                2,
                FALSE,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP
            )
            """
        );

        String accessToken =
            loginAsAdmin();

        String requestBody = """
            {
            "roleCode": "SUPPORT",
            "note": "重新指派客服角色"
            }
            """;

        mockMvc.perform(
                post(
                    "/api/v1/admin/users/100/roles"
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
                    .value(2)
            )
            .andExpect(
                jsonPath("$.code")
                    .value("SUPPORT")
            )
            .andExpect(
                jsonPath("$.name")
                    .value("客服人員")
            );

        /*
        * UNIQUE(user_id, role_id) 下仍然只能有一筆，
        * 不能新增第二筆關聯。
        */
        Integer userRoleCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM user_roles
                WHERE user_id = ?
                AND role_id = ?
                """,
                Integer.class,
                100L,
                2L
            );

        assertThat(userRoleCount)
            .isEqualTo(1);

        /*
        * 確認被重新啟用的是原本 id=3 的關聯。
        */
        Long userRoleId =
            jdbcTemplate.queryForObject(
                """
                SELECT id
                FROM user_roles
                WHERE user_id = ?
                AND role_id = ?
                AND is_active = TRUE
                AND deleted_date IS NULL
                """,
                Long.class,
                100L,
                2L
            );

        assertThat(userRoleId)
            .isEqualTo(3L);

        Integer assignmentLogCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM user_role_assignment_logs log
                JOIN user_role_assignment_types type
                ON type.id =
                    log.user_role_assignment_type_id
                WHERE log.user_id = ?
                AND log.role_id = ?
                AND log.changed_by_user_id = ?
                AND type.code = ?
                AND log.note = ?
                """,
                Integer.class,
                100L,
                2L,
                100L,
                "ASSIGNED",
                "重新指派客服角色"
            );

        assertThat(assignmentLogCount)
            .isEqualTo(1);
    }

    @Test
    void assigningAlreadyActiveRoleReturnsConflict()
        throws Exception {

        /*
        * loginAsAdmin() 會先將 ADMIN 角色
        * 指派給 id=100 的會員。
        */
        String accessToken =
            loginAsAdmin();

        String requestBody = """
            {
            "roleCode": "ADMIN",
            "note": "重複指派系統管理員角色"
            }
            """;

        mockMvc.perform(
                post(
                    "/api/v1/admin/users/100/roles"
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
                    .value("ROLE_ALREADY_ASSIGNED")
            )
            .andExpect(
                jsonPath("$.message")
                    .value("使用者已擁有此角色")
            )
            .andExpect(
                jsonPath("$.path")
                    .value(
                        "/api/v1/admin/users/100/roles"
                    )
            )
            .andExpect(
                jsonPath("$.fieldErrors")
                    .isEmpty()
            );

        /*
        * ADMIN 關聯仍然只能有一筆。
        */
        Integer userRoleCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM user_roles
                WHERE user_id = ?
                AND role_id = ?
                """,
                Integer.class,
                100L,
                3L
            );

        assertThat(userRoleCount)
            .isEqualTo(1);

        /*
        * 被拒絕的重複指派不屬於成功異動，
        * 不應寫入 ASSIGNED 稽核紀錄。
        */
        Integer assignmentLogCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM user_role_assignment_logs
                WHERE user_id = ?
                AND role_id = ?
                """,
                Integer.class,
                100L,
                3L
            );

        assertThat(assignmentLogCount)
            .isZero();
    }

    @Test
    void userWithoutRoleAssignPermissionCannotAssignRole()
        throws Exception {

        /*
        * 測試基底的 id=100 只有 USER 角色，
        * 沒有 ROLE_ASSIGN。
        */
        String accessToken =
            loginAsDefaultUser();

        String requestBody = """
            {
            "roleCode": "SUPPORT",
            "note": "沒有權限的角色指派"
            }
            """;

        mockMvc.perform(
                post(
                    "/api/v1/admin/users/100/roles"
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
                        "/api/v1/admin/users/100/roles"
                    )
            )
            .andExpect(
                jsonPath("$.fieldErrors")
                    .isEmpty()
            );

        Integer userRoleCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM user_roles
                WHERE user_id = ?
                AND role_id = ?
                """,
                Integer.class,
                100L,
                2L
            );

        assertThat(userRoleCount)
            .isZero();

        Integer assignmentLogCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM user_role_assignment_logs
                WHERE user_id = ?
                AND role_id = ?
                """,
                Integer.class,
                100L,
                2L
            );

        assertThat(assignmentLogCount)
            .isZero();
    }

    @Test
    void revokedRoleAssignPermissionBlocksPreviouslyIssuedToken()
        throws Exception {

        /*
        * 登入當下 ADMIN 擁有 ROLE_ASSIGN，
        * JWT 會包含此權限。
        */
        String accessToken =
            loginAsAdmin();

        /*
        * JWT 發出後，從資料庫撤銷 ROLE_ASSIGN。
        *
        * role_id = 3       -> ADMIN
        * permission_id = 8 -> ROLE_ASSIGN
        */
        jdbcTemplate.update(
            """
            UPDATE role_permissions
            SET is_active = FALSE,
                deleted_date = CURRENT_TIMESTAMP
            WHERE role_id = 3
            AND permission_id = 8
            """
        );

        String requestBody = """
            {
            "roleCode": "SUPPORT",
            "note": "使用舊 Token 嘗試指派角色"
            }
            """;

        mockMvc.perform(
                post(
                    "/api/v1/admin/users/100/roles"
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
                        "/api/v1/admin/users/100/roles"
                    )
            )
            .andExpect(
                jsonPath("$.fieldErrors")
                    .isEmpty()
            );

        /*
        * SUPPORT 不可被指派。
        */
        Integer userRoleCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM user_roles
                WHERE user_id = ?
                AND role_id = ?
                """,
                Integer.class,
                100L,
                2L
            );

        assertThat(userRoleCount)
            .isZero();

        /*
        * 被拒絕的操作不可留下成功異動紀錄。
        */
        Integer assignmentLogCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM user_role_assignment_logs
                WHERE user_id = ?
                AND role_id = ?
                """,
                Integer.class,
                100L,
                2L
            );

        assertThat(assignmentLogCount)
            .isZero();
    }

    @ParameterizedTest
    @ValueSource(
        longs = {
            101,
            999999
        }
    )
    void assigningRoleToDeletedOrUnknownUserReturnsNotFound(
        long targetUserId
    ) throws Exception {

        /*
        * id=101 是已軟刪除會員。
        * id=999999 是不存在的會員。
        */
        insertDeletedTargetUser();

        String accessToken =
            loginAsAdmin();

        String requestPath =
            "/api/v1/admin/users/" +
                targetUserId +
                "/roles";

        String requestBody = """
            {
            "roleCode": "SUPPORT",
            "note": "指派客服角色"
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
                        "找不到指定的使用者"
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

        Integer userRoleCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM user_roles
                WHERE user_id = ?
                AND role_id = ?
                """,
                Integer.class,
                targetUserId,
                2L
            );

        assertThat(userRoleCount)
            .isZero();

        Integer assignmentLogCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM user_role_assignment_logs
                WHERE user_id = ?
                AND role_id = ?
                """,
                Integer.class,
                targetUserId,
                2L
            );

        assertThat(assignmentLogCount)
            .isZero();
    }

    @ParameterizedTest
    @ValueSource(
        strings = {
            "UNKNOWN_ROLE",
            "AUDITOR",
            "OPERATIONS"
        }
    )
    void assigningUnknownInactiveOrDeletedRoleReturnsNotFound(
        String roleCode
    ) throws Exception {

        /*
        * AUDITOR 是停用角色。
        */
        jdbcTemplate.update(
            """
            INSERT INTO roles (
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
                'AUDITOR',
                '稽核人員',
                '測試停用角色',
                FALSE,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP
            )
            """
        );

        /*
        * OPERATIONS 是已軟刪除角色。
        */
        jdbcTemplate.update(
            """
            INSERT INTO roles (
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
                'OPERATIONS',
                '營運人員',
                '測試已刪除角色',
                TRUE,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP
            )
            """
        );

        String accessToken =
            loginAsAdmin();

        String requestBody = """
            {
            "roleCode": "%s",
            "note": "測試無效角色指派"
            }
            """.formatted(roleCode);

        mockMvc.perform(
                post(
                    "/api/v1/admin/users/100/roles"
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
                        "找不到指定的角色"
                    )
            )
            .andExpect(
                jsonPath("$.path")
                    .value(
                        "/api/v1/admin/users/100/roles"
                    )
            )
            .andExpect(
                jsonPath("$.fieldErrors")
                    .isEmpty()
            );

        /*
        * 會員原本只有 USER 與 loginAsAdmin()
        * 加入的 ADMIN，總數仍然必須是兩筆。
        */
        Integer userRoleCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM user_roles
                WHERE user_id = ?
                """,
                Integer.class,
                100L
            );

        assertThat(userRoleCount)
            .isEqualTo(2);

        Integer assignmentLogCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM user_role_assignment_logs
                WHERE user_id = ?
                """,
                Integer.class,
                100L
            );

        assertThat(assignmentLogCount)
            .isZero();
    }

    @Test
    void adminCannotAssignSuperAdminRole()
        throws Exception {

        jdbcTemplate.update(
            """
            INSERT INTO roles (
                id,
                code,
                name,
                note,
                is_active,
                created_date,
                updated_date
            )
            VALUES (
                6,
                'SUPER_ADMIN',
                '最高系統管理員',
                '最高風險系統權限',
                TRUE,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP
            )
            """
        );

        String accessToken =
            loginAsAdmin();

        String requestBody = """
            {
            "roleCode": "SUPER_ADMIN",
            "note": "嘗試指派最高系統管理員"
            }
            """;

        mockMvc.perform(
                post(
                    "/api/v1/admin/users/100/roles"
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
                    .value("SUPER_ADMIN_REQUIRED")
            )
            .andExpect(
                jsonPath("$.message")
                    .value(
                        "只有最高系統管理員可以指派 SUPER_ADMIN 角色"
                    )
            )
            .andExpect(
                jsonPath("$.path")
                    .value(
                        "/api/v1/admin/users/100/roles"
                    )
            )
            .andExpect(
                jsonPath("$.fieldErrors")
                    .isEmpty()
            );

        Integer superAdminRoleCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM user_roles
                WHERE user_id = ?
                AND role_id = ?
                """,
                Integer.class,
                100L,
                6L
            );

        assertThat(superAdminRoleCount)
            .isZero();

        Integer assignmentLogCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM user_role_assignment_logs
                WHERE user_id = ?
                AND role_id = ?
                """,
                Integer.class,
                100L,
                6L
            );

        assertThat(assignmentLogCount)
            .isZero();
    }

    @Test
    void superAdminCanAssignSuperAdminRoleToAnotherUser()
        throws Exception {

        /*
        * 建立 SUPER_ADMIN 角色。
        */
        jdbcTemplate.update(
            """
            INSERT INTO roles (
                id,
                code,
                name,
                note,
                is_active,
                created_date,
                updated_date
            )
            VALUES (
                6,
                'SUPER_ADMIN',
                '最高系統管理員',
                '最高風險系統權限',
                TRUE,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP
            )
            """
        );

        /*
        * 將測試登入會員設為 SUPER_ADMIN。
        *
        * 後面 loginAsAdmin() 還會指派 ADMIN，
        * 讓操作者透過 ADMIN 取得 ROLE_ASSIGN。
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
                3,
                100,
                6,
                TRUE,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP
            )
            """
        );

        /*
        * 建立另一位有效會員作為被指派者。
        */
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
                102,
                NULL,
                NULL,
                1,
                2,
                'TARGET0102',
                'Target User',
                'Target',
                'target-102@example.com',
                'not-used-in-this-test',
                DATE '1992-03-03',
                '0955555102',
                TRUE,
                FALSE,
                TRUE,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP
            )
            """
        );

        String accessToken =
            loginAsAdmin();

        String requestBody = """
            {
            "roleCode": "SUPER_ADMIN",
            "note": "核准指派最高系統管理員"
            }
            """;

        mockMvc.perform(
                post(
                    "/api/v1/admin/users/102/roles"
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
                    .value(6)
            )
            .andExpect(
                jsonPath("$.code")
                    .value("SUPER_ADMIN")
            )
            .andExpect(
                jsonPath("$.name")
                    .value("最高系統管理員")
            );

        Integer superAdminRoleCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM user_roles
                WHERE user_id = ?
                AND role_id = ?
                AND is_active = TRUE
                AND deleted_date IS NULL
                """,
                Integer.class,
                102L,
                6L
            );

        assertThat(superAdminRoleCount)
            .isEqualTo(1);

        Integer assignmentLogCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM user_role_assignment_logs log
                JOIN user_role_assignment_types type
                ON type.id =
                    log.user_role_assignment_type_id
                WHERE log.user_id = ?
                AND log.role_id = ?
                AND log.changed_by_user_id = ?
                AND type.code = ?
                AND log.note = ?
                """,
                Integer.class,
                102L,
                6L,
                100L,
                "ASSIGNED",
                "核准指派最高系統管理員"
            );

        assertThat(assignmentLogCount)
            .isEqualTo(1);
    }

    @ParameterizedTest
    @ValueSource(
        strings = {
            """
            {
            "roleCode": "",
            "note": "角色代碼為空"
            }
            """,
            """
            {
            "roleCode": "SUPPORT-ADMIN",
            "note": "角色代碼格式錯誤"
            }
            """,
            """
            {
            "roleCode": "SUPPORT",
            "note": ""
            }
            """
        }
    )
    void invalidAssignRoleRequestReturnsValidationFailed(
        String requestBody
    ) throws Exception {

        String accessToken =
            loginAsAdmin();

        mockMvc.perform(
                post(
                    "/api/v1/admin/users/100/roles"
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
                    .value("VALIDATION_FAILED")
            )
            .andExpect(
                jsonPath("$.message")
                    .value("請求資料驗證失敗")
            )
            .andExpect(
                jsonPath("$.path")
                    .value(
                        "/api/v1/admin/users/100/roles"
                    )
            )
            .andExpect(
                jsonPath("$.fieldErrors")
                    .isNotEmpty()
            );

        /*
        * 只有測試基底的 USER 與 loginAsAdmin()
        * 加入的 ADMIN，不可建立其他角色關聯。
        */
        Integer userRoleCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM user_roles
                WHERE user_id = ?
                """,
                Integer.class,
                100L
            );

        assertThat(userRoleCount)
            .isEqualTo(2);

        Integer assignmentLogCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM user_role_assignment_logs
                WHERE user_id = ?
                """,
                Integer.class,
                100L
            );

        assertThat(assignmentLogCount)
            .isZero();
    }

    @Test
    void adminWithRoleAssignPermissionCanRevokeRole()
        throws Exception {

        /*
        * 先讓會員擁有有效的 SUPPORT 角色。
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
                3,
                100,
                2,
                TRUE,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP
            )
            """
        );

        String accessToken =
            loginAsAdmin();

        String requestBody = """
            {
            "note": "使用者已不再負責客服工作"
            }
            """;

        mockMvc.perform(
                delete(
                    "/api/v1/admin/users/100/roles/SUPPORT"
                )
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
                    )
                    .contentType(
                        MediaType.APPLICATION_JSON
                    )
                    .content(requestBody)
            )
            .andExpect(status().isNoContent())
            .andExpect(content().string(""));

        /*
        * 原本的關聯仍然存在，但已被撤銷。
        */
        Integer revokedUserRoleCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM user_roles
                WHERE id = ?
                AND user_id = ?
                AND role_id = ?
                AND is_active = FALSE
                AND deleted_date IS NOT NULL
                """,
                Integer.class,
                3L,
                100L,
                2L
            );

        assertThat(revokedUserRoleCount)
            .isEqualTo(1);

        /*
        * 撤銷時必須建立 REVOKED 歷程。
        */
        Integer revocationLogCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM user_role_assignment_logs log
                JOIN user_role_assignment_types type
                ON type.id =
                    log.user_role_assignment_type_id
                WHERE log.user_id = ?
                AND log.role_id = ?
                AND log.changed_by_user_id = ?
                AND type.code = ?
                AND log.note = ?
                """,
                Integer.class,
                100L,
                2L,
                100L,
                "REVOKED",
                "使用者已不再負責客服工作"
            );

        assertThat(revocationLogCount)
            .isEqualTo(1);
    }

    @Test
    void revokedRoleCanBeAssignedAgainUsingSameUserRoleRecord()
        throws Exception {

        /*
        * 一開始會員擁有有效的 SUPPORT 角色。
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
                3,
                100,
                2,
                TRUE,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP
            )
            """
        );

        String accessToken =
            loginAsAdmin();

        String revokeRequestBody = """
            {
            "note": "暫時撤銷客服角色"
            }
            """;

        mockMvc.perform(
                delete(
                    "/api/v1/admin/users/100/roles/SUPPORT"
                )
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
                    )
                    .contentType(
                        MediaType.APPLICATION_JSON
                    )
                    .content(revokeRequestBody)
            )
            .andExpect(status().isNoContent());

        String assignRequestBody = """
            {
            "roleCode": "SUPPORT",
            "note": "重新恢復客服角色"
            }
            """;

        mockMvc.perform(
                post(
                    "/api/v1/admin/users/100/roles"
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
                    .content(assignRequestBody)
            )
            .andExpect(status().isOk())
            .andExpect(
                jsonPath("$.code")
                    .value("SUPPORT")
            );

        /*
        * 同一位會員與同一角色仍然只有一筆關聯。
        */
        Integer userRoleCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM user_roles
                WHERE user_id = ?
                AND role_id = ?
                """,
                Integer.class,
                100L,
                2L
            );

        assertThat(userRoleCount)
            .isEqualTo(1);

        /*
        * 被重新啟用的仍然是原本 id=3 的紀錄。
        */
        Long activeUserRoleId =
            jdbcTemplate.queryForObject(
                """
                SELECT id
                FROM user_roles
                WHERE user_id = ?
                AND role_id = ?
                AND is_active = TRUE
                AND deleted_date IS NULL
                """,
                Long.class,
                100L,
                2L
            );

        assertThat(activeUserRoleId)
            .isEqualTo(3L);

        Integer revokedLogCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM user_role_assignment_logs log
                JOIN user_role_assignment_types type
                ON type.id =
                    log.user_role_assignment_type_id
                WHERE log.user_id = ?
                AND log.role_id = ?
                AND type.code = ?
                AND log.note = ?
                """,
                Integer.class,
                100L,
                2L,
                "REVOKED",
                "暫時撤銷客服角色"
            );

        assertThat(revokedLogCount)
            .isEqualTo(1);

        Integer assignedLogCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM user_role_assignment_logs log
                JOIN user_role_assignment_types type
                ON type.id =
                    log.user_role_assignment_type_id
                WHERE log.user_id = ?
                AND log.role_id = ?
                AND type.code = ?
                AND log.note = ?
                """,
                Integer.class,
                100L,
                2L,
                "ASSIGNED",
                "重新恢復客服角色"
            );

        assertThat(assignedLogCount)
            .isEqualTo(1);
    }

    @ParameterizedTest
    @ValueSource(
        booleans = {
            false,
            true
        }
    )
    void revokingMissingOrAlreadyRevokedRoleReturnsNotFound(
        boolean insertRevokedUserRole
    ) throws Exception {

        /*
        * true：建立一筆已被撤銷的 SUPPORT 關聯。
        * false：完全不建立 SUPPORT 關聯。
        */
        if (insertRevokedUserRole) {
            jdbcTemplate.update(
                """
                INSERT INTO user_roles (
                    id,
                    user_id,
                    role_id,
                    is_active,
                    created_date,
                    updated_date,
                    deleted_date
                )
                VALUES (
                    3,
                    100,
                    2,
                    FALSE,
                    CURRENT_TIMESTAMP,
                    CURRENT_TIMESTAMP,
                    CURRENT_TIMESTAMP
                )
                """
            );
        }

        String accessToken =
            loginAsAdmin();

        String requestBody = """
            {
            "note": "嘗試撤銷不存在的角色關聯"
            }
            """;

        mockMvc.perform(
                delete(
                    "/api/v1/admin/users/100/roles/SUPPORT"
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
                    .value("USER_ROLE_NOT_FOUND")
            )
            .andExpect(
                jsonPath("$.message")
                    .value(
                        "使用者未擁有此角色"
                    )
            )
            .andExpect(
                jsonPath("$.path")
                    .value(
                        "/api/v1/admin/users/100/roles/SUPPORT"
                    )
            )
            .andExpect(
                jsonPath("$.fieldErrors")
                    .isEmpty()
            );

        Integer activeSupportRoleCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM user_roles
                WHERE user_id = ?
                AND role_id = ?
                AND is_active = TRUE
                AND deleted_date IS NULL
                """,
                Integer.class,
                100L,
                2L
            );

        assertThat(activeSupportRoleCount)
            .isZero();

        /*
        * 已撤銷的關聯仍保留原本一筆；
        * 完全不存在時則維持零筆。
        */
        Integer totalSupportRoleCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM user_roles
                WHERE user_id = ?
                AND role_id = ?
                """,
                Integer.class,
                100L,
                2L
            );

        assertThat(totalSupportRoleCount)
            .isEqualTo(
                insertRevokedUserRole
                    ? 1
                    : 0
            );

        /*
        * 失敗的撤銷操作不可被記成成功歷程。
        */
        Integer revocationLogCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM user_role_assignment_logs
                WHERE user_id = ?
                AND role_id = ?
                """,
                Integer.class,
                100L,
                2L
            );

        assertThat(revocationLogCount)
            .isZero();
    }

    @Test
    void adminCannotRevokeSuperAdminRole()
        throws Exception {

        jdbcTemplate.update(
            """
            INSERT INTO roles (
                id,
                code,
                name,
                note,
                is_active,
                created_date,
                updated_date
            )
            VALUES (
                6,
                'SUPER_ADMIN',
                '最高系統管理員',
                '最高風險系統權限',
                TRUE,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP
            )
            """
        );

        /*
        * 建立一位有效的目標會員。
        */
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
                102,
                NULL,
                NULL,
                1,
                2,
                'TARGET0102',
                'Target User',
                'Target',
                'target-102@example.com',
                'not-used-in-this-test',
                DATE '1992-03-03',
                '0955555102',
                TRUE,
                FALSE,
                TRUE,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP
            )
            """
        );

        /*
        * 目標會員目前擁有 SUPER_ADMIN。
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
                3,
                102,
                6,
                TRUE,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP
            )
            """
        );

        /*
        * 操作者只有 ADMIN，沒有 SUPER_ADMIN。
        */
        String accessToken =
            loginAsAdmin();

        String requestBody = """
            {
            "note": "一般管理員嘗試撤銷最高管理員"
            }
            """;

        mockMvc.perform(
                delete(
                    "/api/v1/admin/users/102/roles/SUPER_ADMIN"
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
                    .value("SUPER_ADMIN_REQUIRED")
            )
            .andExpect(
                jsonPath("$.message")
                    .value(
                        "只有最高系統管理員可以撤銷 SUPER_ADMIN 角色"
                    )
            )
            .andExpect(
                jsonPath("$.path")
                    .value(
                        "/api/v1/admin/users/102/roles/SUPER_ADMIN"
                    )
            )
            .andExpect(
                jsonPath("$.fieldErrors")
                    .isEmpty()
            );

        /*
        * 目標會員仍然保有 SUPER_ADMIN。
        */
        Integer activeSuperAdminCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM user_roles
                WHERE user_id = ?
                AND role_id = ?
                AND is_active = TRUE
                AND deleted_date IS NULL
                """,
                Integer.class,
                102L,
                6L
            );

        assertThat(activeSuperAdminCount)
            .isEqualTo(1);

        Integer revocationLogCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM user_role_assignment_logs
                WHERE user_id = ?
                AND role_id = ?
                """,
                Integer.class,
                102L,
                6L
            );

        assertThat(revocationLogCount)
            .isZero();
    }

    @Test
    void lastSuperAdminCannotBeRevoked()
        throws Exception {

        jdbcTemplate.update(
            """
            INSERT INTO roles (
                id,
                code,
                name,
                note,
                is_active,
                created_date,
                updated_date
            )
            VALUES (
                6,
                'SUPER_ADMIN',
                '最高系統管理員',
                '最高風險系統權限',
                TRUE,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP
            )
            """
        );

        /*
        * id=100 是系統目前唯一的 SUPER_ADMIN。
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
                3,
                100,
                6,
                TRUE,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP
            )
            """
        );

        /*
        * loginAsAdmin() 會另外指派 ADMIN，
        * 提供 ROLE_ASSIGN 權限。
        */
        String accessToken =
            loginAsAdmin();

        String requestBody = """
            {
            "note": "嘗試撤銷最後一位最高系統管理員"
            }
            """;

        mockMvc.perform(
                delete(
                    "/api/v1/admin/users/100/roles/SUPER_ADMIN"
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
                    .value(
                        "LAST_SUPER_ADMIN_CANNOT_BE_REVOKED"
                    )
            )
            .andExpect(
                jsonPath("$.message")
                    .value(
                        "系統至少必須保留一位最高系統管理員"
                    )
            )
            .andExpect(
                jsonPath("$.path")
                    .value(
                        "/api/v1/admin/users/100/roles/SUPER_ADMIN"
                    )
            )
            .andExpect(
                jsonPath("$.fieldErrors")
                    .isEmpty()
            );

        /*
        * 唯一的 SUPER_ADMIN 仍然有效。
        */
        Integer activeSuperAdminCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM user_roles
                WHERE role_id = ?
                AND is_active = TRUE
                AND deleted_date IS NULL
                """,
                Integer.class,
                6L
            );

        assertThat(activeSuperAdminCount)
            .isEqualTo(1);

        Integer revocationLogCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM user_role_assignment_logs
                WHERE user_id = ?
                AND role_id = ?
                """,
                Integer.class,
                100L,
                6L
            );

        assertThat(revocationLogCount)
            .isZero();
    }

    @Test
    void superAdminCanRevokeAnotherSuperAdminWhenOneRemains()
        throws Exception {

        jdbcTemplate.update(
            """
            INSERT INTO roles (
                id,
                code,
                name,
                note,
                is_active,
                created_date,
                updated_date
            )
            VALUES (
                6,
                'SUPER_ADMIN',
                '最高系統管理員',
                '最高風險系統權限',
                TRUE,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP
            )
            """
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
                102,
                NULL,
                NULL,
                1,
                2,
                'TARGET0102',
                'Target User',
                'Target',
                'target-102@example.com',
                'not-used-in-this-test',
                DATE '1992-03-03',
                '0955555102',
                TRUE,
                FALSE,
                TRUE,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP
            )
            """
        );

        /*
        * 操作者 id=100 是第一位 SUPER_ADMIN。
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
                3,
                100,
                6,
                TRUE,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP
            )
            """
        );

        /*
        * 目標會員 id=102 是第二位 SUPER_ADMIN。
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
                4,
                102,
                6,
                TRUE,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP
            )
            """
        );

        /*
        * 額外取得 ADMIN 的 ROLE_ASSIGN 權限。
        */
        String accessToken =
            loginAsAdmin();

        String requestBody = """
            {
            "note": "撤銷第二位最高系統管理員"
            }
            """;

        mockMvc.perform(
                delete(
                    "/api/v1/admin/users/102/roles/SUPER_ADMIN"
                )
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
                    )
                    .contentType(
                        MediaType.APPLICATION_JSON
                    )
                    .content(requestBody)
            )
            .andExpect(status().isNoContent())
            .andExpect(content().string(""));

        /*
        * 目標會員的 SUPER_ADMIN 已撤銷。
        */
        Integer revokedTargetRoleCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM user_roles
                WHERE id = ?
                AND user_id = ?
                AND role_id = ?
                AND is_active = FALSE
                AND deleted_date IS NOT NULL
                """,
                Integer.class,
                4L,
                102L,
                6L
            );

        assertThat(revokedTargetRoleCount)
            .isEqualTo(1);

        /*
        * 系統仍然保留操作者這一位 SUPER_ADMIN。
        */
        Integer remainingSuperAdminCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM user_roles
                WHERE role_id = ?
                AND is_active = TRUE
                AND deleted_date IS NULL
                """,
                Integer.class,
                6L
            );

        assertThat(remainingSuperAdminCount)
            .isEqualTo(1);

        Integer revocationLogCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM user_role_assignment_logs log
                JOIN user_role_assignment_types type
                ON type.id =
                    log.user_role_assignment_type_id
                WHERE log.user_id = ?
                AND log.role_id = ?
                AND log.changed_by_user_id = ?
                AND type.code = ?
                AND log.note = ?
                """,
                Integer.class,
                102L,
                6L,
                100L,
                "REVOKED",
                "撤銷第二位最高系統管理員"
            );

        assertThat(revocationLogCount)
            .isEqualTo(1);
    }

    @Test
    void userWithoutRoleAssignPermissionCannotRevokeRole()
        throws Exception {

        /*
        * 先讓會員擁有有效的 SUPPORT。
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
                3,
                100,
                2,
                TRUE,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP
            )
            """
        );

        /*
        * id=100 目前只有 USER 與 SUPPORT，
        * 但這兩個角色都沒有 ROLE_ASSIGN。
        */
        String accessToken =
            loginAsDefaultUser();

        String requestBody = """
            {
            "note": "沒有權限的撤銷操作"
            }
            """;

        mockMvc.perform(
                delete(
                    "/api/v1/admin/users/100/roles/SUPPORT"
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
                        "/api/v1/admin/users/100/roles/SUPPORT"
                    )
            )
            .andExpect(
                jsonPath("$.fieldErrors")
                    .isEmpty()
            );

        /*
        * SUPPORT 仍然必須維持有效。
        */
        Integer activeSupportCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM user_roles
                WHERE user_id = ?
                AND role_id = ?
                AND is_active = TRUE
                AND deleted_date IS NULL
                """,
                Integer.class,
                100L,
                2L
            );

        assertThat(activeSupportCount)
            .isEqualTo(1);

        Integer revocationLogCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM user_role_assignment_logs
                WHERE user_id = ?
                AND role_id = ?
                """,
                Integer.class,
                100L,
                2L
            );

        assertThat(revocationLogCount)
            .isZero();
    }

    @Test
    void revokedRoleAssignPermissionBlocksRevokeWithOldToken()
        throws Exception {

        /*
        * 先讓目標會員擁有有效的 SUPPORT。
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
                3,
                100,
                2,
                TRUE,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP
            )
            """
        );

        /*
        * 登入當下 ADMIN 擁有 ROLE_ASSIGN，
        * JWT 會包含這個權限。
        */
        String accessToken =
            loginAsAdmin();

        /*
        * JWT 發出後，再從資料庫即時撤銷權限。
        *
        * role_id = 3       -> ADMIN
        * permission_id = 8 -> ROLE_ASSIGN
        */
        jdbcTemplate.update(
            """
            UPDATE role_permissions
            SET is_active = FALSE,
                deleted_date = CURRENT_TIMESTAMP
            WHERE role_id = 3
            AND permission_id = 8
            """
        );

        String requestBody = """
            {
            "note": "使用舊 Token 嘗試撤銷角色"
            }
            """;

        mockMvc.perform(
                delete(
                    "/api/v1/admin/users/100/roles/SUPPORT"
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
                        "/api/v1/admin/users/100/roles/SUPPORT"
                    )
            )
            .andExpect(
                jsonPath("$.fieldErrors")
                    .isEmpty()
            );

        /*
        * 舊 JWT 不可成功撤銷 SUPPORT。
        */
        Integer activeSupportCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM user_roles
                WHERE user_id = ?
                AND role_id = ?
                AND is_active = TRUE
                AND deleted_date IS NULL
                """,
                Integer.class,
                100L,
                2L
            );

        assertThat(activeSupportCount)
            .isEqualTo(1);

        Integer revocationLogCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM user_role_assignment_logs
                WHERE user_id = ?
                AND role_id = ?
                """,
                Integer.class,
                100L,
                2L
            );

        assertThat(revocationLogCount)
            .isZero();
    }

    @ParameterizedTest
    @MethodSource("invalidRevokeRoleRequests")
    void invalidRevokeRoleRequestReturnsValidationFailed(
        String requestBody
    ) throws Exception {

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
                3,
                100,
                2,
                TRUE,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP
            )
            """
        );

        String accessToken =
            loginAsAdmin();

        mockMvc.perform(
                delete(
                    "/api/v1/admin/users/100/roles/SUPPORT"
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
                    .value("VALIDATION_FAILED")
            )
            .andExpect(
                jsonPath("$.message")
                    .value("請求資料驗證失敗")
            )
            .andExpect(
                jsonPath("$.path")
                    .value(
                        "/api/v1/admin/users/100/roles/SUPPORT"
                    )
            )
            .andExpect(
                jsonPath("$.fieldErrors")
                    .isNotEmpty()
            );

        Integer activeSupportCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM user_roles
                WHERE user_id = ?
                AND role_id = ?
                AND is_active = TRUE
                AND deleted_date IS NULL
                """,
                Integer.class,
                100L,
                2L
            );

        assertThat(activeSupportCount)
            .isEqualTo(1);

        Integer revocationLogCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM user_role_assignment_logs
                WHERE user_id = ?
                AND role_id = ?
                """,
                Integer.class,
                100L,
                2L
            );

        assertThat(revocationLogCount)
            .isZero();
    }

    @Test
    void revokeRoleWithoutRequestBodyReturnsMalformedRequestBody()
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
                3,
                100,
                2,
                TRUE,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP
            )
            """
        );

        String accessToken =
            loginAsAdmin();

        mockMvc.perform(
                delete(
                    "/api/v1/admin/users/100/roles/SUPPORT"
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
                    .value("MALFORMED_REQUEST_BODY")
            )
            .andExpect(
                jsonPath("$.message")
                    .value(
                        "Request Body 缺少或 JSON 格式不正確"
                    )
            )
            .andExpect(
                jsonPath("$.path")
                    .value(
                        "/api/v1/admin/users/100/roles/SUPPORT"
                    )
            )
            .andExpect(
                jsonPath("$.fieldErrors")
                    .isEmpty()
            );

        /*
        * JSON 解析失敗前不應進入 Service，
        * SUPPORT 必須維持有效。
        */
        Integer activeSupportCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM user_roles
                WHERE user_id = ?
                AND role_id = ?
                AND is_active = TRUE
                AND deleted_date IS NULL
                """,
                Integer.class,
                100L,
                2L
            );

        assertThat(activeSupportCount)
            .isEqualTo(1);

        Integer revocationLogCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM user_role_assignment_logs
                WHERE user_id = ?
                AND role_id = ?
                """,
                Integer.class,
                100L,
                2L
            );

        assertThat(revocationLogCount)
            .isZero();
    }

    @ParameterizedTest
    @ValueSource(
        longs = {
            101,
            999999
        }
    )
    void revokingRoleFromDeletedOrUnknownUserReturnsNotFound(
        long targetUserId
    ) throws Exception {

        /*
        * 建立 id=101 的已刪除會員。
        */
        insertDeletedTargetUser();

        /*
        * 模擬已刪除會員仍殘留一筆有效角色關聯。
        * API 不應因為關聯仍有效而允許修改。
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
                3,
                101,
                2,
                TRUE,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP
            )
            """
        );

        String accessToken =
            loginAsAdmin();

        String requestPath =
            "/api/v1/admin/users/" +
                targetUserId +
                "/roles/SUPPORT";

        String requestBody = """
            {
            "note": "嘗試撤銷無效會員的角色"
            }
            """;

        mockMvc.perform(
                delete(requestPath)
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
                    .value(requestPath)
            )
            .andExpect(
                jsonPath("$.fieldErrors")
                    .isEmpty()
            );

        Integer targetRoleCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM user_roles
                WHERE user_id = ?
                AND role_id = ?
                AND is_active = TRUE
                AND deleted_date IS NULL
                """,
                Integer.class,
                targetUserId,
                2L
            );

        assertThat(targetRoleCount)
            .isEqualTo(
                targetUserId == 101L
                    ? 1
                    : 0
            );

        /*
        * 被拒絕的撤銷不能留下 REVOKED 歷程。
        */
        Integer revocationLogCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM user_role_assignment_logs
                WHERE user_id = ?
                AND role_id = ?
                """,
                Integer.class,
                targetUserId,
                2L
            );

        assertThat(revocationLogCount)
            .isZero();
    }

    @ParameterizedTest
    @ValueSource(
        strings = {
            "UNKNOWN_ROLE",
            "AUDITOR",
            "OPERATIONS"
        }
    )
    void revokingUnknownInactiveOrDeletedRoleReturnsNotFound(
        String roleCode
    ) throws Exception {

        /*
        * 建立停用角色。
        */
        jdbcTemplate.update(
            """
            INSERT INTO roles (
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
                'AUDITOR',
                '稽核人員',
                '測試停用角色',
                FALSE,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP
            )
            """
        );

        /*
        * 建立已軟刪除角色。
        */
        jdbcTemplate.update(
            """
            INSERT INTO roles (
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
                'OPERATIONS',
                '營運人員',
                '測試已刪除角色',
                TRUE,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP
            )
            """
        );

        /*
        * 模擬會員仍殘留停用、已刪除角色的關聯。
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
            VALUES
                (
                    3,
                    100,
                    4,
                    TRUE,
                    CURRENT_TIMESTAMP,
                    CURRENT_TIMESTAMP
                ),
                (
                    4,
                    100,
                    5,
                    TRUE,
                    CURRENT_TIMESTAMP,
                    CURRENT_TIMESTAMP
                )
            """
        );

        String accessToken =
            loginAsAdmin();

        String requestPath =
            "/api/v1/admin/users/100/roles/" +
                roleCode;

        String requestBody = """
            {
            "note": "嘗試撤銷無效角色"
            }
            """;

        mockMvc.perform(
                delete(requestPath)
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
                        "找不到指定的角色"
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

        /*
        * 兩筆無效角色關聯都不可被 API 修改。
        */
        Integer remainingInvalidRoleCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM user_roles
                WHERE user_id = ?
                AND role_id IN (?, ?)
                AND is_active = TRUE
                AND deleted_date IS NULL
                """,
                Integer.class,
                100L,
                4L,
                5L
            );

        assertThat(remainingInvalidRoleCount)
            .isEqualTo(2);

        Integer revocationLogCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM user_role_assignment_logs
                WHERE user_id = ?
                AND role_id IN (?, ?)
                """,
                Integer.class,
                100L,
                4L,
                5L
            );

        assertThat(revocationLogCount)
            .isZero();
    }

    @Test
    void adminWithRoleReadPermissionCanGetRoleAssignmentHistory()
        throws Exception {

        /*
        * 較早的 ASSIGNED 歷程。
        */
        jdbcTemplate.update(
            """
            INSERT INTO user_role_assignment_logs (
                id,
                user_id,
                role_id,
                user_role_assignment_type_id,
                changed_by_user_id,
                note,
                changed_date
            )
            VALUES (
                1,
                100,
                2,
                1,
                100,
                '指派客服角色',
                TIMESTAMP '2026-11-14 10:00:00'
            )
            """
        );

        /*
        * 較新的 REVOKED 歷程。
        */
        jdbcTemplate.update(
            """
            INSERT INTO user_role_assignment_logs (
                id,
                user_id,
                role_id,
                user_role_assignment_type_id,
                changed_by_user_id,
                note,
                changed_date
            )
            VALUES (
                2,
                100,
                2,
                2,
                100,
                '撤銷客服角色',
                TIMESTAMP '2026-11-14 11:00:00'
            )
            """
        );

        String accessToken =
            loginAsAdmin();

        mockMvc.perform(
                get(
                    "/api/v1/admin/users/100/roles/history"
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
                    .value(2)
            )

            /*
            * 最新的 REVOKED 必須排在第一筆。
            */
            .andExpect(
                jsonPath("$.content[0].id")
                    .value(2)
            )
            .andExpect(
                jsonPath(
                    "$.content[0].role.id"
                ).value(2)
            )
            .andExpect(
                jsonPath(
                    "$.content[0].role.code"
                ).value("SUPPORT")
            )
            .andExpect(
                jsonPath(
                    "$.content[0].role.name"
                ).value("客服人員")
            )
            .andExpect(
                jsonPath(
                    "$.content[0].assignmentType.id"
                ).value(2)
            )
            .andExpect(
                jsonPath(
                    "$.content[0].assignmentType.code"
                ).value("REVOKED")
            )
            .andExpect(
                jsonPath(
                    "$.content[0].assignmentType.name"
                ).value("撤銷角色")
            )
            .andExpect(
                jsonPath(
                    "$.content[0].changedByUser.id"
                ).value(100)
            )
            .andExpect(
                jsonPath(
                    "$.content[0].changedByUser.userNumber"
                ).value("TESTUSER01")
            )
            .andExpect(
                jsonPath(
                    "$.content[0].changedByUser.name"
                ).value("Test User")
            )
            .andExpect(
                jsonPath(
                    "$.content[0].changedByUser.nickname"
                ).value("Tester")
            )
            .andExpect(
                jsonPath("$.content[0].note")
                    .value("撤銷客服角色")
            )
            .andExpect(
                jsonPath("$.content[0].changedDate")
                    .value("2026-11-14T11:00:00")
            )

            /*
            * 較早的 ASSIGNED 排在第二筆。
            */
            .andExpect(
                jsonPath("$.content[1].id")
                    .value(1)
            )
            .andExpect(
                jsonPath(
                    "$.content[1].assignmentType.code"
                ).value("ASSIGNED")
            )
            .andExpect(
                jsonPath("$.content[1].note")
                    .value("指派客服角色")
            )
            .andExpect(
                jsonPath("$.content[1].changedDate")
                    .value("2026-11-14T10:00:00")
            )

            /*
            * 分頁資訊。
            */
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
                    .value(2)
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
    void roleAssignmentHistoryUsesIdDescendingWhenDatesAreEqual()
        throws Exception {

        /*
        * 兩筆歷程刻意使用完全相同的 changed_date。
        */
        jdbcTemplate.update(
            """
            INSERT INTO user_role_assignment_logs (
                id,
                user_id,
                role_id,
                user_role_assignment_type_id,
                changed_by_user_id,
                note,
                changed_date
            )
            VALUES
                (
                    1,
                    100,
                    2,
                    1,
                    100,
                    '相同時間的第一筆',
                    TIMESTAMP '2026-11-14 12:00:00'
                ),
                (
                    2,
                    100,
                    2,
                    2,
                    100,
                    '相同時間的第二筆',
                    TIMESTAMP '2026-11-14 12:00:00'
                )
            """
        );

        String accessToken =
            loginAsAdmin();

        /*
        * 第一頁 size=1，應取得 id 較大的第 2 筆。
        */
        mockMvc.perform(
                get(
                    "/api/v1/admin/users/100/roles/history"
                )
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
                    .value(2)
            )
            .andExpect(
                jsonPath("$.content[0].note")
                    .value("相同時間的第二筆")
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
        * 第二頁應取得 id=1，不可重複取得 id=2。
        */
        mockMvc.perform(
                get(
                    "/api/v1/admin/users/100/roles/history"
                )
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
                    .value(1)
            )
            .andExpect(
                jsonPath("$.content[0].note")
                    .value("相同時間的第一筆")
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

    @Test
    void roleAssignmentHistorySupportsNullChangedByUser()
        throws Exception {

        /*
        * 模擬操作者已被實體刪除，
        * changed_by_user_id 因 ON DELETE SET NULL
        * 而變成 NULL。
        */
        jdbcTemplate.update(
            """
            INSERT INTO user_role_assignment_logs (
                id,
                user_id,
                role_id,
                user_role_assignment_type_id,
                changed_by_user_id,
                note,
                changed_date
            )
            VALUES (
                1,
                100,
                2,
                1,
                NULL,
                '操作者資料已不存在',
                TIMESTAMP '2026-11-14 13:00:00'
            )
            """
        );

        String accessToken =
            loginAsAdmin();

        mockMvc.perform(
                get(
                    "/api/v1/admin/users/100/roles/history"
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
                jsonPath("$.content[0].id")
                    .value(1)
            )
            .andExpect(
                jsonPath(
                    "$.content[0].role.code"
                ).value("SUPPORT")
            )
            .andExpect(
                jsonPath(
                    "$.content[0].assignmentType.code"
                ).value("ASSIGNED")
            )
            .andExpect(
                jsonPath("$.content[0].changedByUser")
                    .doesNotExist()
            )
            .andExpect(
                jsonPath("$.content[0].note")
                    .value("操作者資料已不存在")
            )
            .andExpect(
                jsonPath("$.content[0].changedDate")
                    .value("2026-11-14T13:00:00")
            );
    }

    @Test
    void userWithoutRoleReadPermissionCannotGetRoleAssignmentHistory()
        throws Exception {

        /*
        * 測試基底的 id=100 只有 USER，
        * 沒有 ROLE_READ。
        */
        String accessToken =
            loginAsDefaultUser();

        mockMvc.perform(
                get(
                    "/api/v1/admin/users/100/roles/history"
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
                        "/api/v1/admin/users/100/roles/history"
                    )
            )
            .andExpect(
                jsonPath("$.fieldErrors")
                    .isEmpty()
            );
    }

    @Test
    void revokedRoleReadPermissionBlocksRoleHistoryWithOldToken()
        throws Exception {

        /*
        * 登入時 ADMIN 擁有 ROLE_READ，
        * JWT 會包含這個權限。
        */
        String accessToken =
            loginAsAdmin();

        /*
        * JWT 發出後，再從資料庫撤銷 ROLE_READ。
        *
        * role_id = 3       -> ADMIN
        * permission_id = 7 -> ROLE_READ
        */
        jdbcTemplate.update(
            """
            UPDATE role_permissions
            SET is_active = FALSE,
                deleted_date = CURRENT_TIMESTAMP
            WHERE role_id = 3
            AND permission_id = 7
            """
        );

        mockMvc.perform(
                get(
                    "/api/v1/admin/users/100/roles/history"
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
                        "/api/v1/admin/users/100/roles/history"
                    )
            )
            .andExpect(
                jsonPath("$.fieldErrors")
                    .isEmpty()
            );
    }

    @ParameterizedTest
    @ValueSource(
        longs = {
            101,
            999999
        }
    )
    void gettingRoleHistoryForDeletedOrUnknownUserReturnsNotFound(
        long targetUserId
    ) throws Exception {

        /*
        * id=101 是已軟刪除會員。
        * id=999999 是不存在會員。
        */
        insertDeletedTargetUser();

        String accessToken =
            loginAsAdmin();

        String requestPath =
            "/api/v1/admin/users/" +
                targetUserId +
                "/roles/history";

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
                        "找不到指定的使用者"
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

    @ParameterizedTest
    @MethodSource("invalidRoleHistoryPagination")
    void invalidRoleHistoryPaginationReturnsBadRequest(
        int page,
        int size,
        String expectedMessage
    ) throws Exception {

        String accessToken =
            loginAsAdmin();

        mockMvc.perform(
                get(
                    "/api/v1/admin/users/100/roles/history"
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
                        "/api/v1/admin/users/100/roles/history"
                    )
            )
            .andExpect(
                jsonPath("$.fieldErrors")
                    .isEmpty()
            );
    }

    @ParameterizedTest
    @MethodSource("invalidRoleHistoryParameterTypes")
    void invalidRoleHistoryParameterTypeReturnsBadRequest(
        String parameterName,
        String parameterValue,
        String expectedMessage
    ) throws Exception {

        String accessToken =
            loginAsAdmin();

        mockMvc.perform(
                get(
                    "/api/v1/admin/users/100/roles/history"
                )
                    .param(
                        parameterName,
                        parameterValue
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
                        "/api/v1/admin/users/100/roles/history"
                    )
            )
            .andExpect(
                jsonPath("$.fieldErrors")
                    .isEmpty()
            );
    }

    private String loginAsAdmin()
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

    private void insertDeletedTargetUser() {

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
                'DELETED101',
                'Deleted Role Target',
                'DeletedTarget',
                'deleted-role-target@example.com',
                'not-used-in-this-test',
                DATE '1991-02-02',
                '0955555101',
                TRUE,
                FALSE,
                FALSE,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP
            )
            """
        );
    }

    private static Stream<Arguments>
        invalidRevokeRoleRequests() {

        String blankNoteRequest = """
            {
            "note": ""
            }
            """;

        String oversizedNoteRequest = """
            {
            "note": "%s"
            }
            """.formatted(
                "A".repeat(256)
            );

        return Stream.of(
            Arguments.of(blankNoteRequest),
            Arguments.of(oversizedNoteRequest)
        );
    }

    private static Stream<Arguments>
        invalidRoleHistoryPagination() {

        return Stream.of(
            Arguments.of(
                -1,
                20,
                "page 不得小於 0"
            ),
            Arguments.of(
                0,
                0,
                "size 必須介於 1 到 100"
            ),
            Arguments.of(
                0,
                101,
                "size 必須介於 1 到 100"
            )
        );
    }

    private static Stream<Arguments>
        invalidRoleHistoryParameterTypes() {

        return Stream.of(
            Arguments.of(
                "page",
                "abc",
                "請求參數 page 格式不正確"
            ),
            Arguments.of(
                "size",
                "large",
                "請求參數 size 格式不正確"
            )
        );
    }
}
