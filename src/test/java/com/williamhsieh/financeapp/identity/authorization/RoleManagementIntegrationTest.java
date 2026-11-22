package com.williamhsieh.financeapp.identity.authorization;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

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
import org.springframework.beans.factory.annotation.Autowired;

import com.jayway.jsonpath.JsonPath;

import com.williamhsieh.financeapp.support.IntegrationTestSupport;
import com.williamhsieh.financeapp.identity.authorization.constant.RoleChangeTypeCodes;
import com.williamhsieh.financeapp.identity.authorization.entity.Role;
import com.williamhsieh.financeapp.identity.authorization.repository.RoleRepository;
import com.williamhsieh.financeapp.identity.authorization.service.RoleChangeAuditService;

class RoleManagementIntegrationTest
    extends IntegrationTestSupport {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RoleChangeAuditService roleChangeAuditService;

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

    @Test
    void roleCreatorCanCreateRole()
        throws Exception {

        String accessToken =
            loginAsRoleCreator();

        String requestBody = """
            {
            "code": "finance_manager",
            "name": "  財務管理人員  ",
            "note": "   "
            }
            """;

        mockMvc.perform(
                post("/api/v1/admin/roles")
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
            .andExpect(status().isCreated())
            .andExpect(
                content().contentTypeCompatibleWith(
                    MediaType.APPLICATION_JSON
                )
            )
            .andExpect(
                jsonPath("$.id")
                    .isNumber()
            )
            .andExpect(
                jsonPath("$.code")
                    .value("FINANCE_MANAGER")
            )
            .andExpect(
                jsonPath("$.name")
                    .value("財務管理人員")
            )
            .andExpect(
                jsonPath("$.note")
                    .doesNotExist()
            );

        /*
        * 驗證資料庫中的正規化結果。
        */
        Long roleCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM roles
                WHERE code = 'FINANCE_MANAGER'
                AND name = '財務管理人員'
                AND note IS NULL
                AND is_active = TRUE
                AND deleted_date IS NULL
                """,
                Long.class
            );

        assertThat(roleCount)
            .isEqualTo(1L);

        Long createdLogCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM role_change_logs role_log
                JOIN roles role
                ON role.id = role_log.role_id
                JOIN role_change_types change_type
                ON change_type.id =
                    role_log.role_change_type_id
                WHERE role.code = 'FINANCE_MANAGER'
                AND change_type.code = 'CREATED'
                AND role_log.changed_by_user_id = 100

                AND role_log.previous_name IS NULL
                AND role_log.previous_note IS NULL
                AND role_log.previous_is_active IS NULL
                AND role_log.previous_deleted_date IS NULL

                AND role_log.new_name =
                    '財務管理人員'
                AND role_log.new_note IS NULL
                AND role_log.new_is_active = TRUE
                AND role_log.new_deleted_date IS NULL

                AND role_log.changed_date IS NOT NULL
                """,
                Long.class
            );

        assertThat(createdLogCount)
            .isEqualTo(1L);
    }

    @Test
    void duplicateActiveRoleCodeReturnsConflict()
        throws Exception {

        String accessToken =
            loginAsRoleCreator();

        /*
        * USER 已存在。
        * 使用小寫確認重複檢查不區分大小寫。
        */
        String requestBody = """
            {
            "code": "user",
            "name": "另一個一般使用者",
            "note": "不應建立成功"
            }
            """;

        String requestPath =
            "/api/v1/admin/roles";

        mockMvc.perform(
                post(requestPath)
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
                    )
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
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
                        "ROLE_CODE_ALREADY_EXISTS"
                    )
            )
            .andExpect(
                jsonPath("$.message")
                    .value("角色代碼已存在")
            )
            .andExpect(
                jsonPath("$.path")
                    .value(requestPath)
            )
            .andExpect(
                jsonPath("$.fieldErrors")
                    .isEmpty()
            );

        Long roleCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM roles
                WHERE UPPER(code) = 'USER'
                """,
                Long.class
            );

        assertThat(roleCount)
            .isEqualTo(1L);

        assertNoRoleChangeLogs();
    }

    @Test
    void duplicateDeactivatedRoleCodeReturnsConflict()
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
                updated_date,
                deleted_date
            )
            VALUES (
                4,
                'AUDITOR',
                '已停用稽核人員',
                '應使用恢復功能',
                FALSE,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP
            )
            """
        );

        String accessToken =
            loginAsRoleCreator();

        String requestBody = """
            {
            "code": "auditor",
            "name": "新的稽核人員",
            "note": "不應重新建立"
            }
            """;

        String requestPath =
            "/api/v1/admin/roles";

        mockMvc.perform(
                post(requestPath)
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
                    )
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(requestBody)
            )
            .andExpect(status().isConflict())
            .andExpect(
                jsonPath("$.status")
                    .value(409)
            )
            .andExpect(
                jsonPath("$.code")
                    .value(
                        "ROLE_CODE_ALREADY_EXISTS"
                    )
            )
            .andExpect(
                jsonPath("$.message")
                    .value("角色代碼已存在")
            )
            .andExpect(
                jsonPath("$.path")
                    .value(requestPath)
            );

        Long roleCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM roles
                WHERE UPPER(code) = 'AUDITOR'
                """,
                Long.class
            );

        assertThat(roleCount)
            .isEqualTo(1L);

        assertNoRoleChangeLogs();
    }

    @Test
    void userWithoutRoleCreateCannotCreateRole()
        throws Exception {

        String accessToken =
            loginAsDefaultUser();

        String requestBody = """
            {
            "code": "FINANCE_MANAGER",
            "name": "財務管理人員",
            "note": "不應建立成功"
            }
            """;

        String requestPath =
            "/api/v1/admin/roles";

        mockMvc.perform(
                post(requestPath)
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
                    )
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
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
                    .value(requestPath)
            )
            .andExpect(
                jsonPath("$.fieldErrors")
                    .isEmpty()
            );

        Long roleCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM roles
                WHERE code = 'FINANCE_MANAGER'
                """,
                Long.class
            );

        assertThat(roleCount)
            .isZero();

        assertNoRoleChangeLogs();
    }

    @Test
    void revokedRoleCreateBlocksExistingAccessToken()
        throws Exception {

        /*
        * 登入時 JWT 會包含 ROLE_CREATE。
        */
        String accessToken =
            loginAsRoleCreator();

        /*
        * 登入後即時撤銷資料庫權限。
        *
        * ADMIN role_id = 3
        * ROLE_CREATE permission_id = 21
        */
        jdbcTemplate.update(
            """
            UPDATE role_permissions
            SET
                is_active = FALSE,
                deleted_date = CURRENT_TIMESTAMP
            WHERE role_id = 3
            AND permission_id = 21
            """
        );

        String requestBody = """
            {
            "code": "FINANCE_MANAGER",
            "name": "財務管理人員",
            "note": "不應建立成功"
            }
            """;

        String requestPath =
            "/api/v1/admin/roles";

        mockMvc.perform(
                post(requestPath)
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
                    )
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
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
                    .value(requestPath)
            );

        Long roleCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM roles
                WHERE code = 'FINANCE_MANAGER'
                """,
                Long.class
            );

        assertThat(roleCount)
            .isZero();

        assertNoRoleChangeLogs();
    }

    @ParameterizedTest
    @MethodSource("invalidCreateRoleRequests")
    void invalidCreateRoleRequestReturnsBadRequest(
        String requestBody,
        String expectedField,
        String expectedMessage
    ) throws Exception {

        String accessToken =
            loginAsRoleCreator();

        String requestPath =
            "/api/v1/admin/roles";

        mockMvc.perform(
                post(requestPath)
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
                    )
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
                jsonPath("$.path")
                    .value(requestPath)
            )
            .andExpect(
                jsonPath("$.fieldErrors.length()")
                    .value(1)
            )
            .andExpect(
                jsonPath("$.fieldErrors[0].field")
                    .value(expectedField)
            )
            .andExpect(
                jsonPath("$.fieldErrors[0].message")
                    .value(expectedMessage)
            );

        /*
        * 測試基底只有 USER、SUPPORT、ADMIN，
        * 驗證失敗不能新增任何角色。
        */
        Long roleCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM roles
                """,
                Long.class
            );

        assertThat(roleCount)
            .isEqualTo(3L);

        assertNoRoleChangeLogs();
    }

    @Test
    void roleUpdaterCanUpdateRole()
        throws Exception {

        String accessToken =
            loginAsRoleUpdater();

        String requestBody = """
            {
            "name": "  進階客服人員  ",
            "note": "  處理會員帳號與驗證問題  "
            }
            """;

        mockMvc.perform(
                put("/api/v1/admin/roles/support")
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
                    .value("進階客服人員")
            )
            .andExpect(
                jsonPath("$.note")
                    .value(
                        "處理會員帳號與驗證問題"
                    )
            );

        /*
        * code 必須維持 SUPPORT，
        * 只有 name、note 被修改。
        */
        Long updatedRoleCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM roles
                WHERE id = 2
                AND code = 'SUPPORT'
                AND name = '進階客服人員'
                AND note = '處理會員帳號與驗證問題'
                AND is_active = TRUE
                AND deleted_date IS NULL
                """,
                Long.class
            );

        assertThat(updatedRoleCount)
            .isEqualTo(1L);

        Long updatedLogCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM role_change_logs role_log
                JOIN roles role
                ON role.id = role_log.role_id
                JOIN role_change_types change_type
                ON change_type.id =
                    role_log.role_change_type_id
                WHERE role.id = 2
                AND role.code = 'SUPPORT'
                AND change_type.code = 'UPDATED'
                AND role_log.changed_by_user_id = 100

                AND role_log.previous_name =
                    '客服人員'
                AND role_log.previous_note IS NULL
                AND role_log.previous_is_active = TRUE
                AND role_log.previous_deleted_date IS NULL

                AND role_log.new_name =
                    '進階客服人員'
                AND role_log.new_note =
                    '處理會員帳號與驗證問題'
                AND role_log.new_is_active = TRUE
                AND role_log.new_deleted_date IS NULL

                AND role_log.changed_date IS NOT NULL
                """,
                Long.class
            );

        assertThat(updatedLogCount)
            .isEqualTo(1L);
    }

    @Test
    void updateUnknownRoleReturnsNotFound()
        throws Exception {

        String accessToken =
            loginAsRoleUpdater();

        String requestBody = """
            {
            "name": "新的角色名稱",
            "note": "不應寫入"
            }
            """;

        String requestPath =
            "/api/v1/admin/roles/UNKNOWN_ROLE";

        mockMvc.perform(
                put(requestPath)
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
                    )
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
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
                    .value("找不到指定的角色")
            )
            .andExpect(
                jsonPath("$.path")
                    .value(requestPath)
            )
            .andExpect(
                jsonPath("$.fieldErrors")
                    .isEmpty()
            );

        assertNoRoleChangeLogs();
    }

    @Test
    void updateDeactivatedRoleReturnsNotFound()
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
                updated_date,
                deleted_date
            )
            VALUES (
                4,
                'AUDITOR',
                '原本的稽核人員',
                '原本的備註',
                FALSE,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP
            )
            """
        );

        String accessToken =
            loginAsRoleUpdater();

        String requestBody = """
            {
            "name": "不應寫入的新名稱",
            "note": "不應寫入的新備註"
            }
            """;

        String requestPath =
            "/api/v1/admin/roles/AUDITOR";

        mockMvc.perform(
                put(requestPath)
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
                    )
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(requestBody)
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
                    .value("找不到指定的角色")
            )
            .andExpect(
                jsonPath("$.path")
                    .value(requestPath)
            );

        /*
        * 確認停用資料完全沒有被修改。
        */
        Long unchangedRoleCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM roles
                WHERE id = 4
                AND code = 'AUDITOR'
                AND name = '原本的稽核人員'
                AND note = '原本的備註'
                AND is_active = FALSE
                AND deleted_date IS NOT NULL
                """,
                Long.class
            );

        assertThat(unchangedRoleCount)
            .isEqualTo(1L);

        assertNoRoleChangeLogs();
    }

    @ParameterizedTest
    @MethodSource("invalidUpdateRoleRequests")
    void invalidUpdateRoleRequestReturnsBadRequest(
        String requestBody,
        String expectedField,
        String expectedMessage
    ) throws Exception {

        String accessToken =
            loginAsRoleUpdater();

        String requestPath =
            "/api/v1/admin/roles/SUPPORT";

        mockMvc.perform(
                put(requestPath)
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
                    )
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
                jsonPath("$.path")
                    .value(requestPath)
            )
            .andExpect(
                jsonPath("$.fieldErrors.length()")
                    .value(1)
            )
            .andExpect(
                jsonPath("$.fieldErrors[0].field")
                    .value(expectedField)
            )
            .andExpect(
                jsonPath("$.fieldErrors[0].message")
                    .value(expectedMessage)
            );

        /*
        * 驗證失敗不能修改原本角色。
        */
        Long unchangedRoleCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM roles
                WHERE id = 2
                AND code = 'SUPPORT'
                AND name = '客服人員'
                AND is_active = TRUE
                AND deleted_date IS NULL
                """,
                Long.class
            );

        assertThat(unchangedRoleCount)
            .isEqualTo(1L);

        assertNoRoleChangeLogs();
    }

    @Test
    void userWithoutRoleUpdateCannotUpdateRole()
        throws Exception {

        String accessToken =
            loginAsDefaultUser();

        String requestBody = """
            {
            "name": "不應寫入的新名稱",
            "note": "不應寫入"
            }
            """;

        String requestPath =
            "/api/v1/admin/roles/SUPPORT";

        mockMvc.perform(
                put(requestPath)
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
                    )
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
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
                    .value(requestPath)
            )
            .andExpect(
                jsonPath("$.fieldErrors")
                    .isEmpty()
            );

        assertSupportRoleUnchanged();

        assertNoRoleChangeLogs();
    }

    @Test
    void revokedRoleUpdateBlocksExistingAccessToken()
        throws Exception {

        /*
        * 登入時 JWT 包含 ROLE_UPDATE。
        */
        String accessToken =
            loginAsRoleUpdater();

        /*
        * 登入完成後，再從資料庫撤銷。
        *
        * ADMIN role_id = 3
        * ROLE_UPDATE permission_id = 22
        */
        jdbcTemplate.update(
            """
            UPDATE role_permissions
            SET
                is_active = FALSE,
                deleted_date = CURRENT_TIMESTAMP
            WHERE role_id = 3
            AND permission_id = 22
            """
        );

        String requestBody = """
            {
            "name": "不應寫入的新名稱",
            "note": "不應寫入"
            }
            """;

        String requestPath =
            "/api/v1/admin/roles/SUPPORT";

        mockMvc.perform(
                put(requestPath)
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
                    )
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
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
                    .value(requestPath)
            );

        assertSupportRoleUnchanged();

        assertNoRoleChangeLogs();
    }

    @Test
    void adminCannotUpdateSuperAdminRole()
        throws Exception {

        insertSuperAdminRole();

        /*
        * ADMIN 擁有 ROLE_UPDATE，
        * 但本身不是 SUPER_ADMIN。
        */
        String accessToken =
            loginAsRoleUpdater();

        String requestBody = """
            {
            "name": "不應修改的名稱",
            "note": "不應修改"
            }
            """;

        String requestPath =
            "/api/v1/admin/roles/SUPER_ADMIN";

        mockMvc.perform(
                put(requestPath)
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
                    )
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
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
                        "只有最高系統管理員可以修改 SUPER_ADMIN 角色"
                    )
            )
            .andExpect(
                jsonPath("$.path")
                    .value(requestPath)
            );

        Long unchangedRoleCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM roles
                WHERE id = 6
                AND code = 'SUPER_ADMIN'
                AND name = '最高系統管理員'
                AND note = '系統最高權限角色'
                AND is_active = TRUE
                AND deleted_date IS NULL
                """,
                Long.class
            );

        assertThat(unchangedRoleCount)
            .isEqualTo(1L);

        assertNoRoleChangeLogs();
    }

    @Test
    void superAdminCanUpdateSuperAdminRole()
        throws Exception {

        String accessToken =
            loginAsSuperAdminRoleUpdater();

        String requestBody = """
            {
            "name": "  最高權限管理員  ",
            "note": "  管理最高風險系統設定  "
            }
            """;

        mockMvc.perform(
                put(
                    "/api/v1/admin/roles/super_admin"
                )
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
                    )
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(requestBody)
            )
            .andExpect(status().isOk())
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
                    .value("最高權限管理員")
            )
            .andExpect(
                jsonPath("$.note")
                    .value(
                        "管理最高風險系統設定"
                    )
            );

        Long updatedRoleCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM roles
                WHERE id = 6
                AND code = 'SUPER_ADMIN'
                AND name = '最高權限管理員'
                AND note = '管理最高風險系統設定'
                AND is_active = TRUE
                AND deleted_date IS NULL
                """,
                Long.class
            );

        assertThat(updatedRoleCount)
            .isEqualTo(1L);
    }

    @Test
    void roleDeleterCanDeactivateRole()
        throws Exception {

        String accessToken =
            loginAsRoleDeleter();

        Long rolePermissionCountBefore =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM role_permissions
                WHERE role_id = 2
                """,
                Long.class
            );

        mockMvc.perform(
                delete(
                    "/api/v1/admin/roles/support"
                )
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
                    )
            )
            .andExpect(status().isNoContent())
            .andExpect(
                content().string("")
            );

        Long deactivatedRoleCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM roles
                WHERE id = 2
                AND code = 'SUPPORT'
                AND is_active = FALSE
                AND deleted_date IS NOT NULL
                """,
                Long.class
            );

        assertThat(deactivatedRoleCount)
            .isEqualTo(1L);

        /*
        * 軟刪除角色不能清除原本的權限關聯。
        */
        Long rolePermissionCountAfter =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM role_permissions
                WHERE role_id = 2
                """,
                Long.class
            );

        assertThat(rolePermissionCountAfter)
            .isEqualTo(rolePermissionCountBefore);

        Long deactivatedLogCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM role_change_logs role_log
                JOIN roles role
                ON role.id = role_log.role_id
                JOIN role_change_types change_type
                ON change_type.id =
                    role_log.role_change_type_id
                WHERE role.id = 2
                AND role.code = 'SUPPORT'
                AND change_type.code = 'DEACTIVATED'
                AND role_log.changed_by_user_id = 100

                AND role_log.previous_name =
                    '客服人員'
                AND role_log.new_name =
                    '客服人員'

                AND role_log.previous_note IS NULL
                AND role_log.new_note IS NULL

                AND role_log.previous_is_active = TRUE
                AND role_log.new_is_active = FALSE

                AND role_log.previous_deleted_date IS NULL
                AND role_log.new_deleted_date IS NOT NULL

                AND role_log.changed_date IS NOT NULL
                """,
                Long.class
            );

        assertThat(deactivatedLogCount)
            .isEqualTo(1L);
    }

    @Test
    void deactivateUnknownRoleReturnsNotFound()
        throws Exception {

        String accessToken =
            loginAsRoleDeleter();

        String requestPath =
            "/api/v1/admin/roles/UNKNOWN_ROLE";

        mockMvc.perform(
                delete(requestPath)
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
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
                    .value("找不到指定的角色")
            )
            .andExpect(
                jsonPath("$.path")
                    .value(requestPath)
            )
            .andExpect(
                jsonPath("$.fieldErrors")
                    .isEmpty()
            );

        assertNoRoleChangeLogs();
    }

    @Test
    void deactivateAlreadyDeactivatedRoleReturnsNotFound()
        throws Exception {

        jdbcTemplate.update(
            """
            UPDATE roles
            SET
                is_active = FALSE,
                deleted_date = CURRENT_TIMESTAMP
            WHERE code = 'SUPPORT'
            """
        );

        String accessToken =
            loginAsRoleDeleter();

        String requestPath =
            "/api/v1/admin/roles/SUPPORT";

        mockMvc.perform(
                delete(requestPath)
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
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
                    .value("找不到指定的角色")
            )
            .andExpect(
                jsonPath("$.path")
                    .value(requestPath)
            );

        Long roleCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM roles
                WHERE code = 'SUPPORT'
                AND is_active = FALSE
                AND deleted_date IS NOT NULL
                """,
                Long.class
            );

        assertThat(roleCount)
            .isEqualTo(1L);

        assertNoRoleChangeLogs();
    }

    @Test
    void userRoleCannotBeDeactivated()
        throws Exception {

        String accessToken =
            loginAsRoleDeleter();

        String requestPath =
            "/api/v1/admin/roles/USER";

        mockMvc.perform(
                delete(requestPath)
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
                    )
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
                        "SYSTEM_ROLE_CANNOT_BE_DEACTIVATED"
                    )
            )
            .andExpect(
                jsonPath("$.message")
                    .value(
                        "不可停用系統核心角色 USER"
                    )
            )
            .andExpect(
                jsonPath("$.path")
                    .value(requestPath)
            );

        Long activeUserRoleCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM roles
                WHERE code = 'USER'
                AND is_active = TRUE
                AND deleted_date IS NULL
                """,
                Long.class
            );

        assertThat(activeUserRoleCount)
            .isEqualTo(1L);

        assertNoRoleChangeLogs();
    }

    @Test
    void superAdminRoleCannotBeDeactivated()
        throws Exception {

        /*
        * 操作者本身就是 SUPER_ADMIN，
        * 並且擁有 ROLE_DELETE。
        */
        String accessToken =
            loginAsSuperAdminRoleDeleter();

        String requestPath =
            "/api/v1/admin/roles/SUPER_ADMIN";

        mockMvc.perform(
                delete(requestPath)
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
                    )
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
                        "SYSTEM_ROLE_CANNOT_BE_DEACTIVATED"
                    )
            )
            .andExpect(
                jsonPath("$.message")
                    .value(
                        "不可停用系統核心角色 SUPER_ADMIN"
                    )
            )
            .andExpect(
                jsonPath("$.path")
                    .value(requestPath)
            );

        Long activeSuperAdminRoleCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM roles
                WHERE code = 'SUPER_ADMIN'
                AND is_active = TRUE
                AND deleted_date IS NULL
                """,
                Long.class
            );

        assertThat(activeSuperAdminRoleCount)
            .isEqualTo(1L);

        assertNoRoleChangeLogs();
    }

    @Test
    void userWithoutRoleDeleteCannotDeactivateRole()
        throws Exception {

        String accessToken =
            loginAsDefaultUser();

        String requestPath =
            "/api/v1/admin/roles/SUPPORT";

        mockMvc.perform(
                delete(requestPath)
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
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
                    .value(requestPath)
            )
            .andExpect(
                jsonPath("$.fieldErrors")
                    .isEmpty()
            );

        assertSupportRoleUnchanged();

        assertNoRoleChangeLogs();
    }

    @Test
    void revokedRoleDeleteBlocksExistingAccessToken()
        throws Exception {

        /*
        * 登入時 JWT 包含 ROLE_DELETE。
        */
        String accessToken =
            loginAsRoleDeleter();

        /*
        * 登入後才撤銷資料庫中的 ROLE_DELETE。
        *
        * ADMIN role_id = 3
        * ROLE_DELETE permission_id = 23
        */
        jdbcTemplate.update(
            """
            UPDATE role_permissions
            SET
                is_active = FALSE,
                deleted_date = CURRENT_TIMESTAMP
            WHERE role_id = 3
            AND permission_id = 23
            """
        );

        String requestPath =
            "/api/v1/admin/roles/SUPPORT";

        mockMvc.perform(
                delete(requestPath)
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
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
                    .value(requestPath)
            )
            .andExpect(
                jsonPath("$.fieldErrors")
                    .isEmpty()
            );

        assertSupportRoleUnchanged();

        assertNoRoleChangeLogs();
    }

    @Test
    void roleUpdaterCanRestoreDeactivatedRole()
        throws Exception {

        /*
        * 先將 SUPPORT 模擬成已停用狀態。
        */
        jdbcTemplate.update(
            """
            UPDATE roles
            SET
                is_active = FALSE,
                deleted_date =
                    TIMESTAMP '2026-11-21 10:00:00'
            WHERE code = 'SUPPORT'
            """
        );

        Long rolePermissionCountBefore =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM role_permissions
                WHERE role_id = 2
                """,
                Long.class
            );

        String accessToken =
            loginAsRoleUpdater();

        mockMvc.perform(
                post(
                    "/api/v1/admin/roles/support/restore"
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

        Long restoredRoleCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM roles
                WHERE id = 2
                AND code = 'SUPPORT'
                AND is_active = TRUE
                AND deleted_date IS NULL
                """,
                Long.class
            );

        assertThat(restoredRoleCount)
            .isEqualTo(1L);

        /*
        * 恢復角色不應重建或刪除權限關聯。
        */
        Long rolePermissionCountAfter =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM role_permissions
                WHERE role_id = 2
                """,
                Long.class
            );

        assertThat(rolePermissionCountAfter)
            .isEqualTo(rolePermissionCountBefore);

        Long restoredLogCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM role_change_logs role_log
                JOIN roles role
                ON role.id = role_log.role_id
                JOIN role_change_types change_type
                ON change_type.id =
                    role_log.role_change_type_id
                WHERE role.id = 2
                AND role.code = 'SUPPORT'
                AND change_type.code = 'RESTORED'
                AND role_log.changed_by_user_id = 100

                AND role_log.previous_name =
                    '客服人員'
                AND role_log.new_name =
                    '客服人員'

                AND role_log.previous_note IS NULL
                AND role_log.new_note IS NULL

                AND role_log.previous_is_active = FALSE
                AND role_log.new_is_active = TRUE

                AND role_log.previous_deleted_date =
                    TIMESTAMP '2026-11-21 10:00:00'
                AND role_log.new_deleted_date IS NULL

                AND role_log.changed_date IS NOT NULL
                """,
                Long.class
            );

        assertThat(restoredLogCount)
            .isEqualTo(1L);
    }

    @Test
    void restoreUnknownRoleReturnsNotFound()
        throws Exception {

        String accessToken =
            loginAsRoleUpdater();

        String requestPath =
            "/api/v1/admin/roles/UNKNOWN_ROLE/restore";

        mockMvc.perform(
                post(requestPath)
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
                    )
                    .accept(MediaType.APPLICATION_JSON)
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
                    .value("找不到指定的角色")
            )
            .andExpect(
                jsonPath("$.path")
                    .value(requestPath)
            )
            .andExpect(
                jsonPath("$.fieldErrors")
                    .isEmpty()
            );

        assertNoRoleChangeLogs();
    }

    @Test
    void restoreActiveRoleReturnsConflict()
        throws Exception {

        String accessToken =
            loginAsRoleUpdater();

        String requestPath =
            "/api/v1/admin/roles/SUPPORT/restore";

        mockMvc.perform(
                post(requestPath)
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
                    )
                    .accept(MediaType.APPLICATION_JSON)
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
                    .value("ROLE_ALREADY_ACTIVE")
            )
            .andExpect(
                jsonPath("$.message")
                    .value(
                        "角色目前已經是啟用狀態"
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

        assertSupportRoleUnchanged();

        assertNoRoleChangeLogs();
    }

    @Test
    void userWithoutRoleUpdateCannotRestoreRole()
        throws Exception {

        deactivateSupportRoleForTest();

        String accessToken =
            loginAsDefaultUser();

        String requestPath =
            "/api/v1/admin/roles/SUPPORT/restore";

        mockMvc.perform(
                post(requestPath)
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

        assertSupportRoleDeactivated();

        assertNoRoleChangeLogs();
    }

    @Test
    void revokedRoleUpdateBlocksExistingTokenFromRestoringRole()
        throws Exception {

        deactivateSupportRoleForTest();

        /*
        * JWT 發出時仍包含 ROLE_UPDATE。
        */
        String accessToken =
            loginAsRoleUpdater();

        /*
        * ADMIN role_id = 3
        * ROLE_UPDATE permission_id = 22
        */
        jdbcTemplate.update(
            """
            UPDATE role_permissions
            SET
                is_active = FALSE,
                deleted_date = CURRENT_TIMESTAMP
            WHERE role_id = 3
            AND permission_id = 22
            """
        );

        String requestPath =
            "/api/v1/admin/roles/SUPPORT/restore";

        mockMvc.perform(
                post(requestPath)
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
            );

        assertSupportRoleDeactivated();

        assertNoRoleChangeLogs();
    }

    @Test
    void roleUpdaterCanGetDeactivatedRoles()
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
                updated_date,
                deleted_date
            )
            VALUES
                (
                    4,
                    'OPERATIONS',
                    '營運人員',
                    '營運角色暫停使用',
                    FALSE,
                    TIMESTAMP '2026-11-21 09:00:00',
                    TIMESTAMP '2026-11-21 11:00:00',
                    TIMESTAMP '2026-11-21 11:00:00'
                ),
                (
                    5,
                    'AUDITOR',
                    '稽核人員',
                    '稽核角色暫停使用',
                    FALSE,
                    TIMESTAMP '2026-11-21 08:00:00',
                    TIMESTAMP '2026-11-21 10:00:00',
                    TIMESTAMP '2026-11-21 10:00:00'
                )
            """
        );

        String accessToken =
            loginAsRoleUpdater();

        mockMvc.perform(
                get(
                    "/api/v1/admin/roles/deactivated"
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
                    .value(2)
            )
            /*
            * 必須依 code ASC 排序，
            * 不是依資料庫 ID。
            */
            .andExpect(
                jsonPath("$[0].id")
                    .value(5)
            )
            .andExpect(
                jsonPath("$[0].code")
                    .value("AUDITOR")
            )
            .andExpect(
                jsonPath("$[0].name")
                    .value("稽核人員")
            )
            .andExpect(
                jsonPath("$[0].note")
                    .value("稽核角色暫停使用")
            )
            .andExpect(
                jsonPath("$[0].active")
                    .value(false)
            )
            .andExpect(
                jsonPath("$[0].createdDate")
                    .value("2026-11-21T08:00:00")
            )
            .andExpect(
                jsonPath("$[0].updatedDate")
                    .value("2026-11-21T10:00:00")
            )
            .andExpect(
                jsonPath("$[0].deletedDate")
                    .value("2026-11-21T10:00:00")
            )
            .andExpect(
                jsonPath("$[1].id")
                    .value(4)
            )
            .andExpect(
                jsonPath("$[1].code")
                    .value("OPERATIONS")
            )
            .andExpect(
                jsonPath("$[1].active")
                    .value(false)
            )
            /*
            * 有效角色不可出現在停用清單。
            */
            .andExpect(
                jsonPath(
                    "$[?(@.code == 'USER')]"
                )
                    .isEmpty()
            )
            .andExpect(
                jsonPath(
                    "$[?(@.code == 'SUPPORT')]"
                )
                    .isEmpty()
            )
            .andExpect(
                jsonPath(
                    "$[?(@.code == 'ADMIN')]"
                )
                    .isEmpty()
            );
    }

    @Test
    void deactivatedRoleListReturnsEmptyArrayWhenNoneExist()
        throws Exception {

        String accessToken =
            loginAsRoleUpdater();

        mockMvc.perform(
                get(
                    "/api/v1/admin/roles/deactivated"
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
                jsonPath("$")
                    .isArray()
            )
            .andExpect(
                jsonPath("$.length()")
                    .value(0)
            );
    }

    @Test
    void userWithoutRoleUpdateCannotGetDeactivatedRoles()
        throws Exception {

        String accessToken =
            loginAsDefaultUser();

        String requestPath =
            "/api/v1/admin/roles/deactivated";

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
    void revokedRoleUpdateBlocksExistingTokenFromGettingDeactivatedRoles()
        throws Exception {

        /*
        * JWT 發出時包含 ROLE_UPDATE。
        */
        String accessToken =
            loginAsRoleUpdater();

        /*
        * ADMIN role_id = 3
        * ROLE_UPDATE permission_id = 22
        */
        jdbcTemplate.update(
            """
            UPDATE role_permissions
            SET
                is_active = FALSE,
                deleted_date = CURRENT_TIMESTAMP
            WHERE role_id = 3
            AND permission_id = 22
            """
        );

        String requestPath =
            "/api/v1/admin/roles/deactivated";

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
    void updateRoleRollsBackWhenAuditLogCannotBeCreated()
        throws Exception {

        String accessToken =
            loginAsRoleUpdater();

        /*
        * 模擬系統設定錯誤：
        * UPDATED 異動類型不可使用。
        */
        jdbcTemplate.update(
            """
            UPDATE role_change_types
            SET is_active = FALSE
            WHERE code = 'UPDATED'
            """
        );

        String requestBody = """
            {
            "name": "不應儲存的新名稱",
            "note": "這次修改應完整回滾"
            }
            """;

        String requestPath =
            "/api/v1/admin/roles/SUPPORT";

        mockMvc.perform(
                put(requestPath)
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
                    )
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
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
                    .value("Internal Server Error")
            )
            .andExpect(
                jsonPath("$.code")
                    .value("INTERNAL_ERROR")
            )
            .andExpect(
                jsonPath("$.message")
                    .value(
                        "系統發生未預期的錯誤"
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
        * Role 修改必須完整回滾。
        */
        assertSupportRoleUnchanged();

        /*
        * 失敗操作不能留下不完整歷程。
        */
        Long changeLogCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM role_change_logs
                WHERE role_id = 2
                """,
                Long.class
            );

        assertThat(changeLogCount)
            .isZero();
    }

    @Test
    void roleChangeAuditServiceSupportsSystemChangeWithoutUser() {

        Role role =
            roleRepository
                .findByCodeIgnoreCase("SUPPORT")
                .orElseThrow();

        RoleChangeAuditService.RoleState
            previousState =
                roleChangeAuditService.capture(
                    role
                );

        /*
        * 模擬系統自動修改後的新狀態。
        * 這裡不儲存 Role，測試目標只有稽核快照。
        */
        role.updateDetails(
            "系統更新後的客服角色",
            "由系統自動更新"
        );

        roleChangeAuditService.recordChanged(
            role,
            null,
            RoleChangeTypeCodes.UPDATED,
            previousState
        );

        Long logCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM role_change_logs
                WHERE role_id = 2
                AND changed_by_user_id IS NULL
                """,
                Long.class
            );

        assertThat(logCount)
            .isEqualTo(1L);

        String previousName =
            jdbcTemplate.queryForObject(
                """
                SELECT previous_name
                FROM role_change_logs
                WHERE role_id = 2
                """,
                String.class
            );

        assertThat(previousName)
            .isEqualTo("客服人員");

        String newName =
            jdbcTemplate.queryForObject(
                """
                SELECT new_name
                FROM role_change_logs
                WHERE role_id = 2
                """,
                String.class
            );

        assertThat(newName)
            .isEqualTo(
                "系統更新後的客服角色"
            );

        String newNote =
            jdbcTemplate.queryForObject(
                """
                SELECT new_note
                FROM role_change_logs
                WHERE role_id = 2
                """,
                String.class
            );

        assertThat(newNote)
            .isEqualTo("由系統自動更新");

        Boolean previousActive =
            jdbcTemplate.queryForObject(
                """
                SELECT previous_is_active
                FROM role_change_logs
                WHERE role_id = 2
                """,
                Boolean.class
            );

        assertThat(previousActive)
            .isTrue();

        Boolean newActive =
            jdbcTemplate.queryForObject(
                """
                SELECT new_is_active
                FROM role_change_logs
                WHERE role_id = 2
                """,
                Boolean.class
            );

        assertThat(newActive)
            .isTrue();
    }

    @Test
    void roleManagerCanGetCompleteRoleChangeHistory()
        throws Exception {

        String accessToken =
            loginAsRoleDefinitionManager();

        String createRequest = """
            {
            "code": "AUDIT_MANAGER",
            "name": "原本的稽核管理員",
            "note": "原本的備註"
            }
            """;

        mockMvc.perform(
                post("/api/v1/admin/roles")
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
                    )
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(createRequest)
            )
            .andExpect(status().isCreated());

        String updateRequest = """
            {
            "name": "修改後的稽核管理員",
            "note": "修改後的備註"
            }
            """;

        mockMvc.perform(
                put(
                    "/api/v1/admin/roles/AUDIT_MANAGER"
                )
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
                    )
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(updateRequest)
            )
            .andExpect(status().isOk());

        mockMvc.perform(
                delete(
                    "/api/v1/admin/roles/AUDIT_MANAGER"
                )
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
                    )
            )
            .andExpect(status().isNoContent());

        mockMvc.perform(
                post(
                    "/api/v1/admin/roles/AUDIT_MANAGER/restore"
                )
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
                    )
            )
            .andExpect(status().isOk());

        mockMvc.perform(
                get(
                    "/api/v1/admin/roles/AUDIT_MANAGER/history"
                )
                    .param("page", "0")
                    .param("size", "20")
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
                jsonPath("$.content.length()")
                    .value(4)
            )

            /*
            * 最新：RESTORED
            */
            .andExpect(
                jsonPath(
                    "$.content[0].changeType.code"
                )
                    .value("RESTORED")
            )
            .andExpect(
                jsonPath(
                    "$.content[0].previousState.name"
                )
                    .value(
                        "修改後的稽核管理員"
                    )
            )
            .andExpect(
                jsonPath(
                    "$.content[0].previousState.active"
                )
                    .value(false)
            )
            .andExpect(
                jsonPath(
                    "$.content[0].previousState.deletedDate"
                )
                    .exists()
            )
            .andExpect(
                jsonPath(
                    "$.content[0].newState.active"
                )
                    .value(true)
            )
            .andExpect(
                jsonPath(
                    "$.content[0].newState.deletedDate"
                )
                    .doesNotExist()
            )

            /*
            * 第二筆：DEACTIVATED
            */
            .andExpect(
                jsonPath(
                    "$.content[1].changeType.code"
                )
                    .value("DEACTIVATED")
            )
            .andExpect(
                jsonPath(
                    "$.content[1].previousState.active"
                )
                    .value(true)
            )
            .andExpect(
                jsonPath(
                    "$.content[1].newState.active"
                )
                    .value(false)
            )
            .andExpect(
                jsonPath(
                    "$.content[1].newState.deletedDate"
                )
                    .exists()
            )

            /*
            * 第三筆：UPDATED
            */
            .andExpect(
                jsonPath(
                    "$.content[2].changeType.code"
                )
                    .value("UPDATED")
            )
            .andExpect(
                jsonPath(
                    "$.content[2].previousState.name"
                )
                    .value(
                        "原本的稽核管理員"
                    )
            )
            .andExpect(
                jsonPath(
                    "$.content[2].newState.name"
                )
                    .value(
                        "修改後的稽核管理員"
                    )
            )
            .andExpect(
                jsonPath(
                    "$.content[2].previousState.note"
                )
                    .value("原本的備註")
            )
            .andExpect(
                jsonPath(
                    "$.content[2].newState.note"
                )
                    .value("修改後的備註")
            )

            /*
            * 最舊：CREATED
            */
            .andExpect(
                jsonPath(
                    "$.content[3].changeType.code"
                )
                    .value("CREATED")
            )
            .andExpect(
                jsonPath(
                    "$.content[3].previousState"
                )
                    .doesNotExist()
            )
            .andExpect(
                jsonPath(
                    "$.content[3].newState.name"
                )
                    .value(
                        "原本的稽核管理員"
                    )
            )
            .andExpect(
                jsonPath(
                    "$.content[3].newState.active"
                )
                    .value(true)
            )

            /*
            * 操作者資訊。
            */
            .andExpect(
                jsonPath(
                    "$.content[0].changedByUser.id"
                )
                    .value(100)
            )
            .andExpect(
                jsonPath(
                    "$.content[0].changedByUser.userNumber"
                )
                    .value("TESTUSER01")
            )
            .andExpect(
                jsonPath(
                    "$.content[0].changedByUser.name"
                )
                    .value("Test User")
            )
            .andExpect(
                jsonPath(
                    "$.content[0].changedByUser.nickname"
                )
                    .value("Tester")
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
                    .value(4)
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
    void userWithoutRoleReadCannotGetRoleChangeHistory()
        throws Exception {

        String accessToken =
            loginAsDefaultUser();

        String requestPath =
            "/api/v1/admin/roles/SUPPORT/history";

        mockMvc.perform(
                get(requestPath)
                    .param("page", "0")
                    .param("size", "20")
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
    void revokedRoleReadBlocksExistingTokenFromGettingRoleChangeHistory()
        throws Exception {

        /*
        * ADMIN 登入時擁有 ROLE_READ。
        */
        String accessToken =
            loginAsAdmin();

        /*
        * ADMIN role_id = 3
        * ROLE_READ permission_id = 7
        */
        jdbcTemplate.update(
            """
            UPDATE role_permissions
            SET
                is_active = FALSE,
                deleted_date = CURRENT_TIMESTAMP
            WHERE role_id = 3
            AND permission_id = 7
            """
        );

        String requestPath =
            "/api/v1/admin/roles/SUPPORT/history";

        mockMvc.perform(
                get(requestPath)
                    .param("page", "0")
                    .param("size", "20")
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
    void unknownRoleChangeHistoryReturnsNotFound()
        throws Exception {

        String accessToken =
            loginAsAdmin();

        String requestPath =
            "/api/v1/admin/roles/UNKNOWN_ROLE/history";

        mockMvc.perform(
                get(requestPath)
                    .param("page", "0")
                    .param("size", "20")
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
                    )
                    .accept(MediaType.APPLICATION_JSON)
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
                    .value("找不到指定的角色")
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
    void roleWithoutChangeLogsReturnsEmptyPage()
        throws Exception {

        String accessToken =
            loginAsAdmin();

        mockMvc.perform(
                get(
                    "/api/v1/admin/roles/SUPPORT/history"
                )
                    .param("page", "0")
                    .param("size", "20")
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
                jsonPath("$.content")
                    .isArray()
            )
            .andExpect(
                jsonPath("$.content.length()")
                    .value(0)
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
    void deactivatedRoleChangeHistoryCanStillBeRead()
        throws Exception {

        /*
        * 模擬 SUPPORT 已被停用。
        */
        jdbcTemplate.update(
            """
            UPDATE roles
            SET
                is_active = FALSE,
                deleted_date =
                    TIMESTAMP '2026-11-22 10:00:00'
            WHERE id = 2
            """
        );

        /*
        * 建立一筆對應的 DEACTIVATED 歷程。
        */
        jdbcTemplate.update(
            """
            INSERT INTO role_change_logs (
                id,
                role_id,
                role_change_type_id,
                changed_by_user_id,
                previous_name,
                new_name,
                previous_note,
                new_note,
                previous_is_active,
                new_is_active,
                previous_deleted_date,
                new_deleted_date,
                changed_date
            )
            VALUES (
                1,
                2,
                3,
                100,
                '客服人員',
                '客服人員',
                NULL,
                NULL,
                TRUE,
                FALSE,
                NULL,
                TIMESTAMP '2026-11-22 10:00:00',
                TIMESTAMP '2026-11-22 10:00:00'
            )
            """
        );

        String accessToken =
            loginAsAdmin();

        mockMvc.perform(
                get(
                    "/api/v1/admin/roles/support/history"
                )
                    .param("page", "0")
                    .param("size", "20")
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
                jsonPath("$.content.length()")
                    .value(1)
            )
            .andExpect(
                jsonPath("$.content[0].id")
                    .value(1)
            )
            .andExpect(
                jsonPath(
                    "$.content[0].changeType.code"
                )
                    .value("DEACTIVATED")
            )
            .andExpect(
                jsonPath(
                    "$.content[0].changeType.name"
                )
                    .value("停用角色")
            )
            .andExpect(
                jsonPath(
                    "$.content[0].changedByUser.id"
                )
                    .value(100)
            )
            .andExpect(
                jsonPath(
                    "$.content[0].previousState.active"
                )
                    .value(true)
            )
            .andExpect(
                jsonPath(
                    "$.content[0].previousState.deletedDate"
                )
                    .doesNotExist()
            )
            .andExpect(
                jsonPath(
                    "$.content[0].newState.active"
                )
                    .value(false)
            )
            .andExpect(
                jsonPath(
                    "$.content[0].newState.deletedDate"
                )
                    .value(
                        "2026-11-22T10:00:00"
                    )
            )
            .andExpect(
                jsonPath(
                    "$.content[0].changedDate"
                )
                    .value(
                        "2026-11-22T10:00:00"
                    )
            )
            .andExpect(
                jsonPath("$.totalElements")
                    .value(1)
            );
    }

    @ParameterizedTest
    @MethodSource(
        "invalidRoleChangeHistoryPagination"
    )
    void invalidRoleChangeHistoryPaginationReturnsBadRequest(
        int page,
        int size,
        String expectedMessage
    ) throws Exception {

        String accessToken =
            loginAsAdmin();

        String requestPath =
            "/api/v1/admin/roles/SUPPORT/history";

        mockMvc.perform(
                get(requestPath)
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
                    .value(expectedMessage)
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
    @MethodSource(
        "invalidRoleChangeHistoryParameterTypes"
    )
    void invalidRoleChangeHistoryParameterTypeReturnsBadRequest(
        String parameterName,
        String parameterValue,
        String expectedMessage
    ) throws Exception {

        String accessToken =
            loginAsAdmin();

        String requestPath =
            "/api/v1/admin/roles/SUPPORT/history";

        mockMvc.perform(
                get(requestPath)
                    .param(
                        parameterName,
                        parameterValue
                    )
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
                    .value(expectedMessage)
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
    void roleChangeHistorySupportsPagination()
        throws Exception {

        jdbcTemplate.update(
            """
            INSERT INTO role_change_logs (
                id,
                role_id,
                role_change_type_id,
                changed_by_user_id,
                previous_name,
                new_name,
                previous_is_active,
                new_is_active,
                changed_date
            )
            VALUES
                (
                    1,
                    2,
                    2,
                    100,
                    '第一版名稱',
                    '第二版名稱',
                    TRUE,
                    TRUE,
                    TIMESTAMP '2026-11-22 10:00:00'
                ),
                (
                    2,
                    2,
                    2,
                    100,
                    '第二版名稱',
                    '第三版名稱',
                    TRUE,
                    TRUE,
                    TIMESTAMP '2026-11-22 11:00:00'
                ),
                (
                    3,
                    2,
                    2,
                    100,
                    '第三版名稱',
                    '第四版名稱',
                    TRUE,
                    TRUE,
                    TIMESTAMP '2026-11-22 12:00:00'
                )
            """
        );

        String accessToken =
            loginAsAdmin();

        mockMvc.perform(
                get(
                    "/api/v1/admin/roles/SUPPORT/history"
                )
                    .param("page", "1")
                    .param("size", "2")
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
                    )
                    .accept(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            /*
            * 完整排序為 id=3、2、1。
            * page=1、size=2 只會剩下 id=1。
            */
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
                    "$.content[0].changeType.code"
                )
                    .value("UPDATED")
            )
            .andExpect(
                jsonPath(
                    "$.content[0].previousState.name"
                )
                    .value("第一版名稱")
            )
            .andExpect(
                jsonPath(
                    "$.content[0].newState.name"
                )
                    .value("第二版名稱")
            )
            .andExpect(
                jsonPath("$.page")
                    .value(1)
            )
            .andExpect(
                jsonPath("$.size")
                    .value(2)
            )
            .andExpect(
                jsonPath("$.totalElements")
                    .value(3)
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
    void roleChangeHistoryUsesIdAsSecondarySort()
        throws Exception {

        jdbcTemplate.update(
            """
            INSERT INTO role_change_logs (
                id,
                role_id,
                role_change_type_id,
                changed_by_user_id,
                previous_name,
                new_name,
                previous_is_active,
                new_is_active,
                changed_date
            )
            VALUES
                (
                    1,
                    2,
                    2,
                    100,
                    '第一版名稱',
                    '第二版名稱',
                    TRUE,
                    TRUE,
                    TIMESTAMP '2026-11-22 13:00:00'
                ),
                (
                    2,
                    2,
                    2,
                    100,
                    '第二版名稱',
                    '第三版名稱',
                    TRUE,
                    TRUE,
                    TIMESTAMP '2026-11-22 13:00:00'
                )
            """
        );

        String accessToken =
            loginAsAdmin();

        mockMvc.perform(
                get(
                    "/api/v1/admin/roles/SUPPORT/history"
                )
                    .param("page", "0")
                    .param("size", "20")
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
            /*
            * changedDate 相同時，
            * id 較大的新紀錄必須排前面。
            */
            .andExpect(
                jsonPath("$.content[0].id")
                    .value(2)
            )
            .andExpect(
                jsonPath(
                    "$.content[0].previousState.name"
                )
                    .value("第二版名稱")
            )
            .andExpect(
                jsonPath(
                    "$.content[0].newState.name"
                )
                    .value("第三版名稱")
            )
            .andExpect(
                jsonPath("$.content[1].id")
                    .value(1)
            )
            .andExpect(
                jsonPath(
                    "$.content[1].previousState.name"
                )
                    .value("第一版名稱")
            )
            .andExpect(
                jsonPath(
                    "$.content[1].newState.name"
                )
                    .value("第二版名稱")
            );
    }

    @Test
    void roleChangeHistorySupportsNullChangedByUser()
        throws Exception {

        /*
        * 模擬系統自動修改角色，
        * changed_by_user_id 為 NULL。
        */
        jdbcTemplate.update(
            """
            INSERT INTO role_change_logs (
                id,
                role_id,
                role_change_type_id,
                changed_by_user_id,
                previous_name,
                new_name,
                previous_note,
                new_note,
                previous_is_active,
                new_is_active,
                previous_deleted_date,
                new_deleted_date,
                changed_date
            )
            VALUES (
                1,
                2,
                2,
                NULL,
                '客服人員',
                '系統客服人員',
                NULL,
                '由系統自動修改',
                TRUE,
                TRUE,
                NULL,
                NULL,
                TIMESTAMP '2026-11-22 14:00:00'
            )
            """
        );

        String accessToken =
            loginAsAdmin();

        mockMvc.perform(
                get(
                    "/api/v1/admin/roles/SUPPORT/history"
                )
                    .param("page", "0")
                    .param("size", "20")
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
                jsonPath("$.content.length()")
                    .value(1)
            )
            .andExpect(
                jsonPath("$.content[0].id")
                    .value(1)
            )
            .andExpect(
                jsonPath(
                    "$.content[0].changeType.code"
                )
                    .value("UPDATED")
            )
            .andExpect(
                jsonPath(
                    "$.content[0].changedByUser"
                )
                    .doesNotExist()
            )
            .andExpect(
                jsonPath(
                    "$.content[0].previousState.name"
                )
                    .value("客服人員")
            )
            .andExpect(
                jsonPath(
                    "$.content[0].newState.name"
                )
                    .value("系統客服人員")
            )
            .andExpect(
                jsonPath(
                    "$.content[0].newState.note"
                )
                    .value("由系統自動修改")
            )
            .andExpect(
                jsonPath(
                    "$.content[0].changedDate"
                )
                    .value(
                        "2026-11-22T14:00:00"
                    )
            );
    }

    private String loginAsRoleCreator()
        throws Exception {

        /*
        * permission_type_id = 3
        * 代表 AUTHORIZATION。
        */
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
                21,
                3,
                'ROLE_CREATE',
                '建立角色',
                TRUE,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP
            )
            """
        );

        /*
        * 將 ROLE_CREATE 授予 ADMIN。
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
                21,
                3,
                21,
                TRUE,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP
            )
            """
        );

        return loginAsAdmin();
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

    private static Stream<Arguments>
        invalidCreateRoleRequests() {

        String missingCode = """
            {
            "name": "測試角色",
            "note": "測試"
            }
            """;

        String invalidCode = """
            {
            "code": "TEST-ROLE",
            "name": "測試角色",
            "note": "測試"
            }
            """;

        String oversizedCode = """
            {
            "code": "%s",
            "name": "測試角色",
            "note": "測試"
            }
            """.formatted(
                "A".repeat(51)
            );

        String missingName = """
            {
            "code": "TEST_ROLE",
            "note": "測試"
            }
            """;

        String oversizedName = """
            {
            "code": "TEST_ROLE",
            "name": "%s",
            "note": "測試"
            }
            """.formatted(
                "A".repeat(101)
            );

        String oversizedNote = """
            {
            "code": "TEST_ROLE",
            "name": "測試角色",
            "note": "%s"
            }
            """.formatted(
                "A".repeat(256)
            );

        return Stream.of(
            Arguments.of(
                missingCode,
                "code",
                "角色代碼不得為空"
            ),
            Arguments.of(
                invalidCode,
                "code",
                "角色代碼格式不正確"
            ),
            Arguments.of(
                oversizedCode,
                "code",
                "角色代碼長度不得超過 50"
            ),
            Arguments.of(
                missingName,
                "name",
                "角色名稱不得為空"
            ),
            Arguments.of(
                oversizedName,
                "name",
                "角色名稱長度不得超過 100"
            ),
            Arguments.of(
                oversizedNote,
                "note",
                "角色備註長度不得超過 255"
            )
        );
    }

    private String loginAsRoleUpdater()
        throws Exception {

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
                22,
                3,
                'ROLE_UPDATE',
                '修改角色',
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
                22,
                3,
                22,
                TRUE,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP
            )
            """
        );

        return loginAsAdmin();
    }

    private static Stream<Arguments>
        invalidUpdateRoleRequests() {

        String missingName = """
            {
            "note": "測試"
            }
            """;

        String oversizedName = """
            {
            "name": "%s",
            "note": "測試"
            }
            """.formatted(
                "A".repeat(101)
            );

        String oversizedNote = """
            {
            "name": "新的角色名稱",
            "note": "%s"
            }
            """.formatted(
                "A".repeat(256)
            );

        return Stream.of(
            Arguments.of(
                missingName,
                "name",
                "角色名稱不得為空"
            ),
            Arguments.of(
                oversizedName,
                "name",
                "角色名稱長度不得超過 100"
            ),
            Arguments.of(
                oversizedNote,
                "note",
                "角色備註長度不得超過 255"
            )
        );
    }

    private void assertSupportRoleUnchanged() {

        Long unchangedRoleCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM roles
                WHERE id = 2
                AND code = 'SUPPORT'
                AND name = '客服人員'
                AND is_active = TRUE
                AND deleted_date IS NULL
                """,
                Long.class
            );

        assertThat(unchangedRoleCount)
            .isEqualTo(1L);
    }

    private void insertSuperAdminRole() {

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
                '系統最高權限角色',
                TRUE,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP
            )
            """
        );
    }

    private String loginAsSuperAdminRoleUpdater()
        throws Exception {

        insertSuperAdminRole();

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
                22,
                3,
                'ROLE_UPDATE',
                '修改角色',
                TRUE,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP
            )
            """
        );

        /*
        * ROLE_UPDATE 直接授予 SUPER_ADMIN。
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
                22,
                6,
                22,
                TRUE,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP
            )
            """
        );

        /*
        * 將登入會員設為 SUPER_ADMIN。
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
                6,
                TRUE,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP
            )
            """
        );

        return loginAsDefaultUser();
    }

    private String loginAsRoleDeleter()
        throws Exception {

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
                23,
                3,
                'ROLE_DELETE',
                '停用角色',
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
                23,
                3,
                23,
                TRUE,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP
            )
            """
        );

        return loginAsAdmin();
    }

    private String loginAsSuperAdminRoleDeleter()
        throws Exception {

        insertSuperAdminRole();

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
                23,
                3,
                'ROLE_DELETE',
                '停用角色',
                TRUE,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP
            )
            """
        );

        /*
        * ROLE_DELETE 授予 SUPER_ADMIN。
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
                23,
                6,
                23,
                TRUE,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP
            )
            """
        );

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
                6,
                TRUE,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP
            )
            """
        );

        return loginAsDefaultUser();
    }

    private void deactivateSupportRoleForTest() {

        jdbcTemplate.update(
            """
            UPDATE roles
            SET
                is_active = FALSE,
                deleted_date = CURRENT_TIMESTAMP
            WHERE code = 'SUPPORT'
            """
        );
    }

    private void assertSupportRoleDeactivated() {

        Long roleCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM roles
                WHERE id = 2
                AND code = 'SUPPORT'
                AND is_active = FALSE
                AND deleted_date IS NOT NULL
                """,
                Long.class
            );

        assertThat(roleCount)
            .isEqualTo(1L);
    }

    private String loginAsRoleDefinitionManager()
        throws Exception {

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
            VALUES
                (
                    21,
                    3,
                    'ROLE_CREATE',
                    '建立角色',
                    TRUE,
                    CURRENT_TIMESTAMP,
                    CURRENT_TIMESTAMP
                ),
                (
                    22,
                    3,
                    'ROLE_UPDATE',
                    '修改角色',
                    TRUE,
                    CURRENT_TIMESTAMP,
                    CURRENT_TIMESTAMP
                ),
                (
                    23,
                    3,
                    'ROLE_DELETE',
                    '停用角色',
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
            VALUES
                (
                    21,
                    3,
                    21,
                    TRUE,
                    CURRENT_TIMESTAMP,
                    CURRENT_TIMESTAMP
                ),
                (
                    22,
                    3,
                    22,
                    TRUE,
                    CURRENT_TIMESTAMP,
                    CURRENT_TIMESTAMP
                ),
                (
                    23,
                    3,
                    23,
                    TRUE,
                    CURRENT_TIMESTAMP,
                    CURRENT_TIMESTAMP
                )
            """
        );

        /*
        * ADMIN 原本已有 ROLE_READ。
        */
        return loginAsAdmin();
    }

    private static Stream<Arguments>
        invalidRoleChangeHistoryPagination() {

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
        invalidRoleChangeHistoryParameterTypes() {

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

    private void assertNoRoleChangeLogs() {

        Long changeLogCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM role_change_logs
                """,
                Long.class
            );

        assertThat(changeLogCount)
            .isZero();
    }
}
