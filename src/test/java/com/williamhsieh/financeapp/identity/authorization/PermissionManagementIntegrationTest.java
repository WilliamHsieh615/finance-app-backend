package com.williamhsieh.financeapp.identity.authorization;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import com.williamhsieh.financeapp.identity.authorization.constant.PermissionChangeTypeCodes;
import com.williamhsieh.financeapp.identity.authorization.entity.Permission;
import com.williamhsieh.financeapp.identity.authorization.repository.PermissionRepository;
import com.williamhsieh.financeapp.identity.authorization.service.PermissionChangeAuditService;

class PermissionManagementIntegrationTest
    extends IntegrationTestSupport {

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private PermissionChangeAuditService permissionChangeAuditService;

    @Test
    void adminWithPermissionReadCanGetActivePermissions()
        throws Exception {

        /*
         * 停用的 Permission 不可回傳。
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
                10,
                3,
                'ROLE_CREATE',
                '建立角色',
                FALSE,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP
            )
            """
        );

        /*
         * 已軟刪除的 Permission 不可回傳。
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
                updated_date,
                deleted_date
            )
            VALUES (
                11,
                3,
                'ROLE_DELETE',
                '刪除角色',
                TRUE,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP
            )
            """
        );

        /*
         * 建立一個停用的 PermissionType。
         */
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
                4,
                'INACTIVE_TYPE',
                '停用權限類型',
                FALSE,
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
                12,
                4,
                'INACTIVE_TYPE_PERMISSION',
                '停用類型下的權限',
                TRUE,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP
            )
            """
        );

        /*
         * 建立一個已軟刪除的 PermissionType。
         */
        jdbcTemplate.update(
            """
            INSERT INTO permission_types (
                id,
                code,
                name,
                is_active,
                created_date,
                updated_date,
                deleted_date
            )
            VALUES (
                5,
                'DELETED_TYPE',
                '已刪除權限類型',
                TRUE,
                CURRENT_TIMESTAMP,
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
                13,
                5,
                'DELETED_TYPE_PERMISSION',
                '已刪除類型下的權限',
                TRUE,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP
            )
            """
        );

        String accessToken =
            loginAsAdmin();

        mockMvc.perform(
                get("/api/v1/admin/permissions")
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
                    .value(10)
            )

            /*
             * AUTHORIZATION 依類型代碼排在最前，
             * 類型內依 Permission code 排序。
             */
            .andExpect(
                jsonPath("$[0].code")
                    .value("PERMISSION_READ")
            )
            .andExpect(
                jsonPath("$[1].code")
                    .value("ROLE_ASSIGN")
            )
            .andExpect(
                jsonPath("$[2].code")
                    .value("ROLE_PERMISSION_MANAGE")
            )
            .andExpect(
                jsonPath("$[3].code")
                    .value("ROLE_READ")
            )

            /*
             * 接著是 USER 類型。
             */
            .andExpect(
                jsonPath("$[4].code")
                    .value("PROFILE_READ_SELF")
            )
            .andExpect(
                jsonPath(
                    "$[4].permissionType.code"
                ).value("USER")
            )

            /*
             * 無效 Permission 不可出現在結果中。
             */
            .andExpect(
                jsonPath(
                    "$[?(@.code == 'ROLE_CREATE')]"
                ).isEmpty()
            )
            .andExpect(
                jsonPath(
                    "$[?(@.code == 'ROLE_DELETE')]"
                ).isEmpty()
            )
            .andExpect(
                jsonPath(
                    "$[?(@.code == 'INACTIVE_TYPE_PERMISSION')]"
                ).isEmpty()
            )
            .andExpect(
                jsonPath(
                    "$[?(@.code == 'DELETED_TYPE_PERMISSION')]"
                ).isEmpty()
            );
    }

    @Test
    void userWithoutPermissionReadCannotGetPermissions()
        throws Exception {

        /*
        * 測試基底的 id=100 只有 USER，
        * 沒有 PERMISSION_READ。
        */
        String accessToken =
            loginAsDefaultUser();

        mockMvc.perform(
                get("/api/v1/admin/permissions")
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
                        "/api/v1/admin/permissions"
                    )
            )
            .andExpect(
                jsonPath("$.fieldErrors")
                    .isEmpty()
            );
    }

    @Test
    void revokedPermissionReadBlocksPreviouslyIssuedToken()
        throws Exception {

        /*
        * 登入時 ADMIN 擁有 PERMISSION_READ，
        * JWT 會包含這個權限。
        */
        String accessToken =
            loginAsAdmin();

        /*
        * JWT 發出後，再從資料庫撤銷 PERMISSION_READ。
        *
        * role_id = 3       -> ADMIN
        * permission_id = 9 -> PERMISSION_READ
        */
        jdbcTemplate.update(
            """
            UPDATE role_permissions
            SET is_active = FALSE,
                deleted_date = CURRENT_TIMESTAMP
            WHERE role_id = 3
            AND permission_id = 9
            """
        );

        mockMvc.perform(
                get("/api/v1/admin/permissions")
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
                        "/api/v1/admin/permissions"
                    )
            )
            .andExpect(
                jsonPath("$.fieldErrors")
                    .isEmpty()
            );
    }

    @Test
    void adminWithPermissionReadCanGetRoleActivePermissions()
        throws Exception {

        /*
        * 建立兩個有效 Permission，
        * 但稍後讓 RolePermission 關聯失效。
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
            VALUES
                (
                    10,
                    3,
                    'ROLE_CREATE',
                    '建立角色',
                    TRUE,
                    CURRENT_TIMESTAMP,
                    CURRENT_TIMESTAMP
                ),
                (
                    11,
                    3,
                    'ROLE_DELETE',
                    '刪除角色',
                    TRUE,
                    CURRENT_TIMESTAMP,
                    CURRENT_TIMESTAMP
                )
            """
        );

        /*
        * ROLE_CREATE 關聯被停用。
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
                16,
                3,
                10,
                FALSE,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP
            )
            """
        );

        /*
        * ROLE_DELETE 關聯已軟刪除。
        */
        jdbcTemplate.update(
            """
            INSERT INTO role_permissions (
                id,
                role_id,
                permission_id,
                is_active,
                created_date,
                updated_date,
                deleted_date
            )
            VALUES (
                17,
                3,
                11,
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
                    "/api/v1/admin/roles/admin/permissions"
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
                    .value(8)
            )

            /*
            * AUTHORIZATION 類型排在前面，
            * 類型內依 code 排序。
            */
            .andExpect(
                jsonPath("$[0].code")
                    .value("PERMISSION_READ")
            )
            .andExpect(
                jsonPath("$[1].code")
                    .value("ROLE_ASSIGN")
            )
            .andExpect(
                jsonPath("$[2].code")
                    .value("ROLE_PERMISSION_MANAGE")
            )
            .andExpect(
                jsonPath("$[3].code")
                    .value("ROLE_READ")
            )

            /*
            * 接著是 USER_MANAGEMENT。
            */
            .andExpect(
                jsonPath("$[4].code")
                    .value("USER_DELETE")
            )
            .andExpect(
                jsonPath(
                    "$[4].permissionType.code"
                ).value("USER_MANAGEMENT")
            )
            .andExpect(
                jsonPath("$[7].code")
                    .value("USER_STATUS_UPDATE")
            )

            /*
            * 無效 RolePermission 關聯不可回傳。
            */
            .andExpect(
                jsonPath(
                    "$[?(@.code == 'ROLE_CREATE')]"
                ).isEmpty()
            )
            .andExpect(
                jsonPath(
                    "$[?(@.code == 'ROLE_DELETE')]"
                ).isEmpty()
            );
    }

    @ParameterizedTest
    @ValueSource(
        strings = {
            "UNKNOWN_ROLE",
            "AUDITOR",
            "OPERATIONS"
        }
    )
    void gettingPermissionsForUnknownInactiveOrDeletedRoleReturnsNotFound(
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

        String requestPath =
            "/api/v1/admin/roles/" +
                roleCode +
                "/permissions";

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
    }

    @Test
    void userWithoutPermissionReadCannotGetRolePermissions()
        throws Exception {

        /*
        * 測試基底的 id=100 只有 USER，
        * 沒有 PERMISSION_READ。
        */
        String accessToken =
            loginAsDefaultUser();

        mockMvc.perform(
                get(
                    "/api/v1/admin/roles/USER/permissions"
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
                        "/api/v1/admin/roles/USER/permissions"
                    )
            )
            .andExpect(
                jsonPath("$.fieldErrors")
                    .isEmpty()
            );
    }

    @Test
    void revokedPermissionReadBlocksRolePermissionsWithOldToken()
        throws Exception {

        /*
        * 登入時 ADMIN 擁有 PERMISSION_READ，
        * JWT 會包含此權限。
        */
        String accessToken =
            loginAsAdmin();

        /*
        * Token 發出後，從資料庫撤銷權限。
        */
        jdbcTemplate.update(
            """
            UPDATE role_permissions
            SET is_active = FALSE,
                deleted_date = CURRENT_TIMESTAMP
            WHERE role_id = 3
            AND permission_id = 9
            """
        );

        mockMvc.perform(
                get(
                    "/api/v1/admin/roles/ADMIN/permissions"
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
                        "/api/v1/admin/roles/ADMIN/permissions"
                    )
            )
            .andExpect(
                jsonPath("$.fieldErrors")
                    .isEmpty()
            );
    }

    @Test
    void adminWithRolePermissionManageCanGrantPermission()
        throws Exception {

        String accessToken =
            loginAsAdmin();

        String requestBody = """
            {
            "permissionCode": "USER_STATUS_UPDATE",
            "note": "客服需要協助處理會員狀態"
            }
            """;

        mockMvc.perform(
                post(
                    "/api/v1/admin/roles/SUPPORT/permissions"
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
                    .value(3)
            )
            .andExpect(
                jsonPath("$.code")
                    .value("USER_STATUS_UPDATE")
            )
            .andExpect(
                jsonPath("$.name")
                    .value("變更會員帳號狀態")
            )
            .andExpect(
                jsonPath("$.permissionType.id")
                    .value(2)
            )
            .andExpect(
                jsonPath("$.permissionType.code")
                    .value("USER_MANAGEMENT")
            )
            .andExpect(
                jsonPath("$.permissionType.name")
                    .value("會員管理")
            );

        Integer rolePermissionCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM role_permissions
                WHERE role_id = ?
                AND permission_id = ?
                AND is_active = TRUE
                AND deleted_date IS NULL
                """,
                Integer.class,
                2L,
                3L
            );

        assertThat(rolePermissionCount)
            .isEqualTo(1);

        Integer grantedLogCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM role_permission_assignment_logs log
                JOIN role_permission_assignment_types type
                ON type.id =
                    log.role_permission_assignment_type_id
                WHERE log.role_id = ?
                AND log.permission_id = ?
                AND log.changed_by_user_id = ?
                AND type.code = ?
                AND log.note = ?
                """,
                Integer.class,
                2L,
                3L,
                100L,
                "GRANTED",
                "客服需要協助處理會員狀態"
            );

        assertThat(grantedLogCount)
            .isEqualTo(1);
    }

    @Test
    void adminWithRolePermissionManageCanRevokePermission()
        throws Exception {

        String accessToken =
            loginAsAdmin();

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
                101,
                2,
                3,
                TRUE,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP
            )
            """
        );

        String requestBody = """
            {
            "note": "客服不再需要變更會員狀態"
            }
            """;

        mockMvc.perform(
                delete(
                    "/api/v1/admin/roles/SUPPORT/permissions/USER_STATUS_UPDATE"
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

        Integer activeRolePermissionCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM role_permissions
                WHERE role_id = ?
                AND permission_id = ?
                AND is_active = TRUE
                AND deleted_date IS NULL
                """,
                Integer.class,
                2L,
                3L
            );

        assertThat(activeRolePermissionCount)
            .isZero();

        Integer revokedRolePermissionCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM role_permissions
                WHERE role_id = ?
                AND permission_id = ?
                AND is_active = FALSE
                AND deleted_date IS NOT NULL
                """,
                Integer.class,
                2L,
                3L
            );

        assertThat(revokedRolePermissionCount)
            .isEqualTo(1);

        Integer revokedLogCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM role_permission_assignment_logs log
                JOIN role_permission_assignment_types type
                ON type.id =
                    log.role_permission_assignment_type_id
                WHERE log.role_id = ?
                AND log.permission_id = ?
                AND log.changed_by_user_id = ?
                AND type.code = ?
                AND log.note = ?
                """,
                Integer.class,
                2L,
                3L,
                100L,
                "REVOKED",
                "客服不再需要變更會員狀態"
            );

        assertThat(revokedLogCount)
            .isEqualTo(1);
    }

    @Test
    void grantExistingRolePermissionReturnsConflict()
        throws Exception {

        String accessToken =
            loginAsAdmin();

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
                101,
                2,
                3,
                TRUE,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP
            )
            """
        );

        String requestBody = """
            {
            "permissionCode": "USER_STATUS_UPDATE",
            "note": "嘗試重複授予權限"
            }
            """;

        mockMvc.perform(
                post(
                    "/api/v1/admin/roles/SUPPORT/permissions"
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
            .andExpect(status().isConflict())
            .andExpect(
                jsonPath("$.status")
                    .value(409)
            )
            .andExpect(
                jsonPath("$.code")
                    .value("PERMISSION_ALREADY_GRANTED")
            )
            .andExpect(
                jsonPath("$.message")
                    .value("角色已擁有此權限")
            )
            .andExpect(
                jsonPath("$.path")
                    .value(
                        "/api/v1/admin/roles/SUPPORT/permissions"
                    )
            )
            .andExpect(
                jsonPath("$.fieldErrors")
                    .isEmpty()
            );

        Integer rolePermissionCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM role_permissions
                WHERE role_id = ?
                AND permission_id = ?
                """,
                Integer.class,
                2L,
                3L
            );

        assertThat(rolePermissionCount)
            .isEqualTo(1);

        Integer assignmentLogCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM role_permission_assignment_logs
                WHERE role_id = ?
                AND permission_id = ?
                """,
                Integer.class,
                2L,
                3L
            );

        assertThat(assignmentLogCount)
            .isZero();
    }

    @Test
    void grantRevokedRolePermissionReactivatesExistingRelation()
        throws Exception {

        String accessToken =
            loginAsAdmin();

        jdbcTemplate.update(
            """
            INSERT INTO role_permissions (
                id,
                role_id,
                permission_id,
                is_active,
                created_date,
                updated_date,
                deleted_date
            )
            VALUES (
                101,
                2,
                3,
                FALSE,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP
            )
            """
        );

        String requestBody = """
            {
            "permissionCode": "USER_STATUS_UPDATE",
            "note": "重新授予客服會員狀態管理權限"
            }
            """;

        mockMvc.perform(
                post(
                    "/api/v1/admin/roles/SUPPORT/permissions"
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
                    .value(3)
            )
            .andExpect(
                jsonPath("$.code")
                    .value("USER_STATUS_UPDATE")
            );

        Integer relationCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM role_permissions
                WHERE role_id = ?
                AND permission_id = ?
                """,
                Integer.class,
                2L,
                3L
            );

        assertThat(relationCount)
            .isEqualTo(1);

        Integer reactivatedRelationCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM role_permissions
                WHERE id = ?
                AND role_id = ?
                AND permission_id = ?
                AND is_active = TRUE
                AND deleted_date IS NULL
                """,
                Integer.class,
                101L,
                2L,
                3L
            );

        assertThat(reactivatedRelationCount)
            .isEqualTo(1);

        Integer grantedLogCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM role_permission_assignment_logs log
                JOIN role_permission_assignment_types type
                ON type.id =
                    log.role_permission_assignment_type_id
                WHERE log.role_id = ?
                AND log.permission_id = ?
                AND log.changed_by_user_id = ?
                AND type.code = ?
                AND log.note = ?
                """,
                Integer.class,
                2L,
                3L,
                100L,
                "GRANTED",
                "重新授予客服會員狀態管理權限"
            );

        assertThat(grantedLogCount)
            .isEqualTo(1);
    }

    @Test
    void revokeMissingRolePermissionReturnsNotFound()
        throws Exception {

        String accessToken =
            loginAsAdmin();

        String requestBody = """
            {
            "note": "嘗試撤銷角色未擁有的權限"
            }
            """;

        mockMvc.perform(
                delete(
                    "/api/v1/admin/roles/SUPPORT/permissions/USER_STATUS_UPDATE"
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
            .andExpect(status().isNotFound())
            .andExpect(
                jsonPath("$.status")
                    .value(404)
            )
            .andExpect(
                jsonPath("$.code")
                    .value("ROLE_PERMISSION_NOT_FOUND")
            )
            .andExpect(
                jsonPath("$.message")
                    .value("角色未擁有此權限")
            )
            .andExpect(
                jsonPath("$.path")
                    .value(
                        "/api/v1/admin/roles/SUPPORT/permissions/USER_STATUS_UPDATE"
                    )
            )
            .andExpect(
                jsonPath("$.fieldErrors")
                    .isEmpty()
            );

        Integer assignmentLogCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM role_permission_assignment_logs
                WHERE role_id = ?
                AND permission_id = ?
                """,
                Integer.class,
                2L,
                3L
            );

        assertThat(assignmentLogCount)
            .isZero();
    }

    @Test
    void revokeAlreadyRevokedRolePermissionReturnsNotFound()
        throws Exception {

        String accessToken =
            loginAsAdmin();

        jdbcTemplate.update(
            """
            INSERT INTO role_permissions (
                id,
                role_id,
                permission_id,
                is_active,
                created_date,
                updated_date,
                deleted_date
            )
            VALUES (
                101,
                2,
                3,
                FALSE,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP
            )
            """
        );

        String requestBody = """
            {
            "note": "再次撤銷已撤銷的權限"
            }
            """;

        mockMvc.perform(
                delete(
                    "/api/v1/admin/roles/SUPPORT/permissions/USER_STATUS_UPDATE"
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
            .andExpect(status().isNotFound())
            .andExpect(
                jsonPath("$.status")
                    .value(404)
            )
            .andExpect(
                jsonPath("$.code")
                    .value("ROLE_PERMISSION_NOT_FOUND")
            )
            .andExpect(
                jsonPath("$.message")
                    .value("角色未擁有此權限")
            );

        Integer revokedRelationCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM role_permissions
                WHERE id = ?
                AND is_active = FALSE
                AND deleted_date IS NOT NULL
                """,
                Integer.class,
                101L
            );

        assertThat(revokedRelationCount)
            .isEqualTo(1);

        Integer assignmentLogCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM role_permission_assignment_logs
                WHERE role_id = ?
                AND permission_id = ?
                """,
                Integer.class,
                2L,
                3L
            );

        assertThat(assignmentLogCount)
            .isZero();
    }

    @Test
    void grantPermissionWithBlankNoteReturnsBadRequest()
        throws Exception {

        String accessToken =
            loginAsAdmin();

        String requestBody = """
            {
            "permissionCode": "USER_STATUS_UPDATE",
            "note": "   "
            }
            """;

        mockMvc.perform(
                post(
                    "/api/v1/admin/roles/SUPPORT/permissions"
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
            .andExpect(status().isBadRequest())
            .andExpect(
                jsonPath("$.status")
                    .value(400)
            )
            .andExpect(
                jsonPath("$.code")
                    .value("VALIDATION_FAILED")
            )
            .andExpect(
                jsonPath("$.fieldErrors")
                    .isArray()
            )
            .andExpect(
                jsonPath("$.fieldErrors[0].field")
                    .value("note")
            )
            .andExpect(
                jsonPath("$.fieldErrors[0].message")
                    .value(
                        "授予權限時必須填寫備註"
                    )
            );

        Integer rolePermissionCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM role_permissions
                WHERE role_id = ?
                AND permission_id = ?
                """,
                Integer.class,
                2L,
                3L
            );

        assertThat(rolePermissionCount)
            .isZero();

        Integer assignmentLogCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM role_permission_assignment_logs
                WHERE role_id = ?
                AND permission_id = ?
                """,
                Integer.class,
                2L,
                3L
            );

        assertThat(assignmentLogCount)
            .isZero();
    }

    @Test
    void revokePermissionWithBlankNoteReturnsBadRequest()
        throws Exception {

        String accessToken =
            loginAsAdmin();

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
                101,
                2,
                3,
                TRUE,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP
            )
            """
        );

        String requestBody = """
            {
            "note": "   "
            }
            """;

        mockMvc.perform(
                delete(
                    "/api/v1/admin/roles/SUPPORT/permissions/USER_STATUS_UPDATE"
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
            .andExpect(status().isBadRequest())
            .andExpect(
                jsonPath("$.status")
                    .value(400)
            )
            .andExpect(
                jsonPath("$.code")
                    .value("VALIDATION_FAILED")
            )
            .andExpect(
                jsonPath("$.fieldErrors")
                    .isArray()
            )
            .andExpect(
                jsonPath("$.fieldErrors[0].field")
                    .value("note")
            )
            .andExpect(
                jsonPath("$.fieldErrors[0].message")
                    .value(
                        "撤銷權限時必須填寫備註"
                    )
            );

        Integer activeRolePermissionCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM role_permissions
                WHERE id = ?
                AND is_active = TRUE
                AND deleted_date IS NULL
                """,
                Integer.class,
                101L
            );

        assertThat(activeRolePermissionCount)
            .isEqualTo(1);

        Integer assignmentLogCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM role_permission_assignment_logs
                WHERE role_id = ?
                AND permission_id = ?
                """,
                Integer.class,
                2L,
                3L
            );

        assertThat(assignmentLogCount)
            .isZero();
    }

    @Test
    void userWithoutRolePermissionManageCannotGrantPermission()
        throws Exception {

        String accessToken =
            loginAsDefaultUser();

        String requestBody = """
            {
            "permissionCode": "USER_STATUS_UPDATE",
            "note": "一般使用者嘗試授予權限"
            }
            """;

        mockMvc.perform(
                post(
                    "/api/v1/admin/roles/SUPPORT/permissions"
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
                        "/api/v1/admin/roles/SUPPORT/permissions"
                    )
            )
            .andExpect(
                jsonPath("$.fieldErrors")
                    .isEmpty()
            );

        Integer rolePermissionCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM role_permissions
                WHERE role_id = ?
                AND permission_id = ?
                """,
                Integer.class,
                2L,
                3L
            );

        assertThat(rolePermissionCount)
            .isZero();

        Integer assignmentLogCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM role_permission_assignment_logs
                WHERE role_id = ?
                AND permission_id = ?
                """,
                Integer.class,
                2L,
                3L
            );

        assertThat(assignmentLogCount)
            .isZero();
    }

    @Test
    void userWithoutRolePermissionManageCannotRevokePermission()
        throws Exception {

        String accessToken =
            loginAsDefaultUser();

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
                101,
                2,
                3,
                TRUE,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP
            )
            """
        );

        String requestBody = """
            {
            "note": "一般使用者嘗試撤銷權限"
            }
            """;

        mockMvc.perform(
                delete(
                    "/api/v1/admin/roles/SUPPORT/permissions/USER_STATUS_UPDATE"
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
                        "/api/v1/admin/roles/SUPPORT/permissions/USER_STATUS_UPDATE"
                    )
            )
            .andExpect(
                jsonPath("$.fieldErrors")
                    .isEmpty()
            );

        Integer activeRolePermissionCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM role_permissions
                WHERE id = ?
                AND is_active = TRUE
                AND deleted_date IS NULL
                """,
                Integer.class,
                101L
            );

        assertThat(activeRolePermissionCount)
            .isEqualTo(1);

        Integer assignmentLogCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM role_permission_assignment_logs
                WHERE role_id = ?
                AND permission_id = ?
                """,
                Integer.class,
                2L,
                3L
            );

        assertThat(assignmentLogCount)
            .isZero();
    }

    @Test
    void revokedRolePermissionManageBlocksPreviouslyIssuedToken()
        throws Exception {

        /*
        * 登入時 ADMIN 擁有 ROLE_PERMISSION_MANAGE，
        * JWT 會包含這個權限。
        */
        String accessToken =
            loginAsAdmin();

        /*
        * JWT 發出後，再從資料庫撤銷
        * ADMIN 的 ROLE_PERMISSION_MANAGE。
        *
        * role_id = 3        -> ADMIN
        * permission_id = 20 -> ROLE_PERMISSION_MANAGE
        */
        jdbcTemplate.update(
            """
            UPDATE role_permissions
            SET is_active = FALSE,
                deleted_date = CURRENT_TIMESTAMP
            WHERE role_id = 3
            AND permission_id = 20
            """
        );

        String requestBody = """
            {
            "permissionCode": "USER_STATUS_UPDATE",
            "note": "使用已被撤銷權限的舊 Token"
            }
            """;

        mockMvc.perform(
                post(
                    "/api/v1/admin/roles/SUPPORT/permissions"
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
                        "/api/v1/admin/roles/SUPPORT/permissions"
                    )
            )
            .andExpect(
                jsonPath("$.fieldErrors")
                    .isEmpty()
            );

        Integer supportPermissionCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM role_permissions
                WHERE role_id = ?
                AND permission_id = ?
                """,
                Integer.class,
                2L,
                3L
            );

        assertThat(supportPermissionCount)
            .isZero();

        Integer assignmentLogCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM role_permission_assignment_logs
                WHERE role_id = ?
                AND permission_id = ?
                """,
                Integer.class,
                2L,
                3L
            );

        assertThat(assignmentLogCount)
            .isZero();
    }

    @Test
    void adminCannotGrantPermissionToSuperAdminRole()
        throws Exception {

        String accessToken =
            loginAsAdmin();

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
                6,
                'SUPER_ADMIN',
                '最高系統管理員',
                TRUE,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP
            )
            """
        );

        String requestBody = """
            {
            "permissionCode": "USER_STATUS_UPDATE",
            "note": "ADMIN 嘗試修改最高管理員權限"
            }
            """;

        mockMvc.perform(
                post(
                    "/api/v1/admin/roles/SUPER_ADMIN/permissions"
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
                jsonPath("$.code")
                    .value("SUPER_ADMIN_REQUIRED")
            )
            .andExpect(
                jsonPath("$.message")
                    .value(
                        "只有最高系統管理員可以修改 SUPER_ADMIN 角色權限"
                    )
            )
            .andExpect(
                jsonPath("$.path")
                    .value(
                        "/api/v1/admin/roles/SUPER_ADMIN/permissions"
                    )
            )
            .andExpect(
                jsonPath("$.fieldErrors")
                    .isEmpty()
            );

        Integer rolePermissionCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM role_permissions
                WHERE role_id = ?
                AND permission_id = ?
                """,
                Integer.class,
                6L,
                3L
            );

        assertThat(rolePermissionCount)
            .isZero();

        Integer assignmentLogCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM role_permission_assignment_logs
                WHERE role_id = ?
                AND permission_id = ?
                """,
                Integer.class,
                6L,
                3L
            );

        assertThat(assignmentLogCount)
            .isZero();
    }

    @Test
    void adminCannotRevokePermissionFromSuperAdminRole()
        throws Exception {

        String accessToken =
            loginAsAdmin();

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
                6,
                'SUPER_ADMIN',
                '最高系統管理員',
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
                101,
                6,
                3,
                TRUE,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP
            )
            """
        );

        String requestBody = """
            {
            "note": "ADMIN 嘗試撤銷最高管理員權限"
            }
            """;

        mockMvc.perform(
                delete(
                    "/api/v1/admin/roles/SUPER_ADMIN/permissions/USER_STATUS_UPDATE"
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
                jsonPath("$.code")
                    .value("SUPER_ADMIN_REQUIRED")
            )
            .andExpect(
                jsonPath("$.message")
                    .value(
                        "只有最高系統管理員可以修改 SUPER_ADMIN 角色權限"
                    )
            )
            .andExpect(
                jsonPath("$.path")
                    .value(
                        "/api/v1/admin/roles/SUPER_ADMIN/permissions/USER_STATUS_UPDATE"
                    )
            )
            .andExpect(
                jsonPath("$.fieldErrors")
                    .isEmpty()
            );

        Integer activeRolePermissionCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM role_permissions
                WHERE id = ?
                AND role_id = ?
                AND permission_id = ?
                AND is_active = TRUE
                AND deleted_date IS NULL
                """,
                Integer.class,
                101L,
                6L,
                3L
            );

        assertThat(activeRolePermissionCount)
            .isEqualTo(1);

        Integer assignmentLogCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM role_permission_assignment_logs
                WHERE role_id = ?
                AND permission_id = ?
                """,
                Integer.class,
                6L,
                3L
            );

        assertThat(assignmentLogCount)
            .isZero();
    }

    @Test
    void superAdminCanGrantPermissionToSuperAdminRole()
        throws Exception {

        String accessToken =
            loginAsSuperAdmin();

        String requestBody = """
            {
            "permissionCode": "USER_STATUS_UPDATE",
            "note": "最高管理員新增會員狀態管理權限"
            }
            """;

        mockMvc.perform(
                post(
                    "/api/v1/admin/roles/SUPER_ADMIN/permissions"
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
                    .value(3)
            )
            .andExpect(
                jsonPath("$.code")
                    .value("USER_STATUS_UPDATE")
            );

        Integer activeRolePermissionCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM role_permissions
                WHERE role_id = ?
                AND permission_id = ?
                AND is_active = TRUE
                AND deleted_date IS NULL
                """,
                Integer.class,
                6L,
                3L
            );

        assertThat(activeRolePermissionCount)
            .isEqualTo(1);

        Integer grantedLogCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM role_permission_assignment_logs log
                JOIN role_permission_assignment_types type
                ON type.id =
                    log.role_permission_assignment_type_id
                WHERE log.role_id = ?
                AND log.permission_id = ?
                AND log.changed_by_user_id = ?
                AND type.code = ?
                AND log.note = ?
                """,
                Integer.class,
                6L,
                3L,
                100L,
                "GRANTED",
                "最高管理員新增會員狀態管理權限"
            );

        assertThat(grantedLogCount)
            .isEqualTo(1);
    }

    @Test
    void superAdminCanRevokeRegularPermissionFromSuperAdminRole()
        throws Exception {

        String accessToken =
            loginAsSuperAdmin();

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
                102,
                6,
                3,
                TRUE,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP
            )
            """
        );

        String requestBody = """
            {
            "note": "最高管理員撤銷一般會員狀態管理權限"
            }
            """;

        mockMvc.perform(
                delete(
                    "/api/v1/admin/roles/SUPER_ADMIN/permissions/USER_STATUS_UPDATE"
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

        Integer activeRolePermissionCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM role_permissions
                WHERE id = ?
                AND is_active = TRUE
                AND deleted_date IS NULL
                """,
                Integer.class,
                102L
            );

        assertThat(activeRolePermissionCount)
            .isZero();

        Integer revokedRolePermissionCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM role_permissions
                WHERE id = ?
                AND role_id = ?
                AND permission_id = ?
                AND is_active = FALSE
                AND deleted_date IS NOT NULL
                """,
                Integer.class,
                102L,
                6L,
                3L
            );

        assertThat(revokedRolePermissionCount)
            .isEqualTo(1);

        Integer revokedLogCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM role_permission_assignment_logs log
                JOIN role_permission_assignment_types type
                ON type.id =
                    log.role_permission_assignment_type_id
                WHERE log.role_id = ?
                AND log.permission_id = ?
                AND log.changed_by_user_id = ?
                AND type.code = ?
                AND log.note = ?
                """,
                Integer.class,
                6L,
                3L,
                100L,
                "REVOKED",
                "最高管理員撤銷一般會員狀態管理權限"
            );

        assertThat(revokedLogCount)
            .isEqualTo(1);
    }

    @Test
    void superAdminCannotRevokeProtectedRolePermissionManage()
        throws Exception {

        String accessToken =
            loginAsSuperAdmin();

        String requestBody = """
            {
            "note": "嘗試撤銷最高管理員的授權管理權限"
            }
            """;

        mockMvc.perform(
                delete(
                    "/api/v1/admin/roles/SUPER_ADMIN/permissions/ROLE_PERMISSION_MANAGE"
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
                jsonPath("$.code")
                    .value(
                        "PROTECTED_PERMISSION_CANNOT_BE_REVOKED"
                    )
            )
            .andExpect(
                jsonPath("$.message")
                    .value(
                        "不可撤銷 SUPER_ADMIN 的角色權限管理權限"
                    )
            )
            .andExpect(
                jsonPath("$.path")
                    .value(
                        "/api/v1/admin/roles/SUPER_ADMIN/permissions/ROLE_PERMISSION_MANAGE"
                    )
            )
            .andExpect(
                jsonPath("$.fieldErrors")
                    .isEmpty()
            );

        Integer activeRolePermissionCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM role_permissions
                WHERE id = ?
                AND role_id = ?
                AND permission_id = ?
                AND is_active = TRUE
                AND deleted_date IS NULL
                """,
                Integer.class,
                101L,
                6L,
                20L
            );

        assertThat(activeRolePermissionCount)
            .isEqualTo(1);

        Integer assignmentLogCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM role_permission_assignment_logs
                WHERE role_id = ?
                AND permission_id = ?
                """,
                Integer.class,
                6L,
                20L
            );

        assertThat(assignmentLogCount)
            .isZero();
    }

    @Test
    void grantPermissionToMissingRoleReturnsNotFound()
        throws Exception {

        String accessToken =
            loginAsAdmin();

        String requestBody = """
            {
            "permissionCode": "USER_STATUS_UPDATE",
            "note": "嘗試授予不存在的角色"
            }
            """;

        String requestPath =
            "/api/v1/admin/roles/UNKNOWN_ROLE/permissions";

        mockMvc.perform(
                post(requestPath)
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
                    )
                    .contentType(
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

        Integer rolePermissionCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM role_permissions
                WHERE permission_id = ?
                """,
                Integer.class,
                3L
            );

        /*
        * fixture 中 ADMIN 原本就有
        * USER_STATUS_UPDATE，因此仍只有一筆。
        */
        assertThat(rolePermissionCount)
            .isEqualTo(1);

        Integer assignmentLogCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM role_permission_assignment_logs
                WHERE permission_id = ?
                """,
                Integer.class,
                3L
            );

        assertThat(assignmentLogCount)
            .isZero();
    }

    @Test
    void grantMissingPermissionReturnsNotFound()
        throws Exception {

        String accessToken =
            loginAsAdmin();

        String requestBody = """
            {
            "permissionCode": "UNKNOWN_PERMISSION",
            "note": "嘗試授予不存在的權限"
            }
            """;

        String requestPath =
            "/api/v1/admin/roles/SUPPORT/permissions";

        mockMvc.perform(
                post(requestPath)
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
                    )
                    .contentType(
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
                    .value("找不到指定的權限")
            )
            .andExpect(
                jsonPath("$.path")
                    .value(requestPath)
            )
            .andExpect(
                jsonPath("$.fieldErrors")
                    .isEmpty()
            );

        Integer supportPermissionCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM role_permissions
                WHERE role_id = ?
                """,
                Integer.class,
                2L
            );

        /*
        * SUPPORT 在 fixture 中原本只有 USER_READ。
        */
        assertThat(supportPermissionCount)
            .isEqualTo(1);

        Integer assignmentLogCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM role_permission_assignment_logs
                WHERE role_id = ?
                """,
                Integer.class,
                2L
            );

        assertThat(assignmentLogCount)
            .isZero();
    }

    @Test
    void revokePermissionFromMissingRoleReturnsNotFound()
        throws Exception {

        String accessToken =
            loginAsAdmin();

        String requestBody = """
            {
            "note": "嘗試撤銷不存在角色的權限"
            }
            """;

        String requestPath =
            "/api/v1/admin/roles/UNKNOWN_ROLE/permissions/USER_STATUS_UPDATE";

        mockMvc.perform(
                delete(requestPath)
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
                    )
                    .contentType(
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

        Integer existingPermissionCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM role_permissions
                WHERE permission_id = ?
                AND is_active = TRUE
                AND deleted_date IS NULL
                """,
                Integer.class,
                3L
            );

        /*
        * fixture 中 ADMIN 原本擁有
        * USER_STATUS_UPDATE。
        */
        assertThat(existingPermissionCount)
            .isEqualTo(1);

        Integer assignmentLogCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM role_permission_assignment_logs
                WHERE permission_id = ?
                """,
                Integer.class,
                3L
            );

        assertThat(assignmentLogCount)
            .isZero();
    }

    @Test
    void revokeMissingPermissionReturnsNotFound()
        throws Exception {

        String accessToken =
            loginAsAdmin();

        String requestBody = """
            {
            "note": "嘗試撤銷不存在的權限"
            }
            """;

        String requestPath =
            "/api/v1/admin/roles/SUPPORT/permissions/UNKNOWN_PERMISSION";

        mockMvc.perform(
                delete(requestPath)
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
                    )
                    .contentType(
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
                    .value("找不到指定的權限")
            )
            .andExpect(
                jsonPath("$.path")
                    .value(requestPath)
            )
            .andExpect(
                jsonPath("$.fieldErrors")
                    .isEmpty()
            );

        Integer supportPermissionCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM role_permissions
                WHERE role_id = ?
                AND is_active = TRUE
                AND deleted_date IS NULL
                """,
                Integer.class,
                2L
            );

        /*
        * SUPPORT 原本仍保有 USER_READ。
        */
        assertThat(supportPermissionCount)
            .isEqualTo(1);

        Integer assignmentLogCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM role_permission_assignment_logs
                WHERE role_id = ?
                """,
                Integer.class,
                2L
            );

        assertThat(assignmentLogCount)
            .isZero();
    }

    @Test
    void grantPermissionWithoutPermissionCodeReturnsBadRequest()
        throws Exception {

        String accessToken =
            loginAsAdmin();

        String requestBody = """
            {
            "note": "沒有提供權限代碼"
            }
            """;

        mockMvc.perform(
                post(
                    "/api/v1/admin/roles/SUPPORT/permissions"
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
            .andExpect(status().isBadRequest())
            .andExpect(
                jsonPath("$.status")
                    .value(400)
            )
            .andExpect(
                jsonPath("$.code")
                    .value("VALIDATION_FAILED")
            )
            .andExpect(
                jsonPath("$.fieldErrors")
                    .isArray()
            )
            .andExpect(
                jsonPath("$.fieldErrors[0].field")
                    .value("permissionCode")
            )
            .andExpect(
                jsonPath("$.fieldErrors[0].message")
                    .value("權限代碼不得為空")
            );

        Integer supportPermissionCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM role_permissions
                WHERE role_id = ?
                """,
                Integer.class,
                2L
            );

        /*
        * SUPPORT 原本只有 USER_READ。
        */
        assertThat(supportPermissionCount)
            .isEqualTo(1);

        Integer assignmentLogCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM role_permission_assignment_logs
                WHERE role_id = ?
                """,
                Integer.class,
                2L
            );

        assertThat(assignmentLogCount)
            .isZero();
    }

    @Test
    void grantPermissionWithInvalidPermissionCodeReturnsBadRequest()
        throws Exception {

        String accessToken =
            loginAsAdmin();

        String requestBody = """
            {
            "permissionCode": "USER-STATUS-UPDATE",
            "note": "使用格式錯誤的權限代碼"
            }
            """;

        mockMvc.perform(
                post(
                    "/api/v1/admin/roles/SUPPORT/permissions"
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
            .andExpect(status().isBadRequest())
            .andExpect(
                jsonPath("$.status")
                    .value(400)
            )
            .andExpect(
                jsonPath("$.code")
                    .value("VALIDATION_FAILED")
            )
            .andExpect(
                jsonPath("$.fieldErrors")
                    .isArray()
            )
            .andExpect(
                jsonPath("$.fieldErrors[0].field")
                    .value("permissionCode")
            )
            .andExpect(
                jsonPath("$.fieldErrors[0].message")
                    .value("權限代碼格式不正確")
            );

        Integer supportPermissionCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM role_permissions
                WHERE role_id = ?
                """,
                Integer.class,
                2L
            );

        assertThat(supportPermissionCount)
            .isEqualTo(1);

        Integer assignmentLogCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM role_permission_assignment_logs
                WHERE role_id = ?
                """,
                Integer.class,
                2L
            );

        assertThat(assignmentLogCount)
            .isZero();
    }

    @Test
    void grantPermissionNormalizesRoleAndPermissionCodes()
        throws Exception {

        String accessToken =
            loginAsAdmin();

        String requestBody = """
            {
            "permissionCode": "user_status_update",
            "note": "使用小寫代碼授予權限"
            }
            """;

        mockMvc.perform(
                post(
                    "/api/v1/admin/roles/support/permissions"
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
                    .value(3)
            )
            .andExpect(
                jsonPath("$.code")
                    .value("USER_STATUS_UPDATE")
            );

        Integer activeRolePermissionCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM role_permissions
                WHERE role_id = ?
                AND permission_id = ?
                AND is_active = TRUE
                AND deleted_date IS NULL
                """,
                Integer.class,
                2L,
                3L
            );

        assertThat(activeRolePermissionCount)
            .isEqualTo(1);

        Integer grantedLogCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM role_permission_assignment_logs log
                JOIN role_permission_assignment_types type
                ON type.id =
                    log.role_permission_assignment_type_id
                WHERE log.role_id = ?
                AND log.permission_id = ?
                AND type.code = ?
                """,
                Integer.class,
                2L,
                3L,
                "GRANTED"
            );

        assertThat(grantedLogCount)
            .isEqualTo(1);
    }

    @Test
    void adminWithPermissionReadCanGetRolePermissionHistory()
        throws Exception {

        String accessToken =
            loginAsAdmin();

        jdbcTemplate.update(
            """
            INSERT INTO role_permission_assignment_logs (
                id,
                role_id,
                permission_id,
                role_permission_assignment_type_id,
                changed_by_user_id,
                note,
                changed_date
            )
            VALUES
                (
                    1,
                    2,
                    3,
                    1,
                    100,
                    '授予客服會員狀態管理權限',
                    TIMESTAMP '2026-11-15 10:00:00'
                ),
                (
                    2,
                    2,
                    3,
                    2,
                    100,
                    '撤銷客服會員狀態管理權限',
                    TIMESTAMP '2026-11-15 11:00:00'
                )
            """
        );

        mockMvc.perform(
                get(
                    "/api/v1/admin/roles/SUPPORT/permissions/history"
                )
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
                    )
                    .param("page", "0")
                    .param("size", "20")
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
            * changed_date 較新的 REVOKED
            * 必須排在第一筆。
            */
            .andExpect(
                jsonPath("$.content[0].id")
                    .value(2)
            )
            .andExpect(
                jsonPath(
                    "$.content[0].permission.id"
                )
                    .value(3)
            )
            .andExpect(
                jsonPath(
                    "$.content[0].permission.code"
                )
                    .value("USER_STATUS_UPDATE")
            )
            .andExpect(
                jsonPath(
                    "$.content[0].permission.name"
                )
                    .value("變更會員帳號狀態")
            )
            .andExpect(
                jsonPath(
                    "$.content[0].permission.permissionType.id"
                )
                    .value(2)
            )
            .andExpect(
                jsonPath(
                    "$.content[0].permission.permissionType.code"
                )
                    .value("USER_MANAGEMENT")
            )
            .andExpect(
                jsonPath(
                    "$.content[0].assignmentType.id"
                )
                    .value(2)
            )
            .andExpect(
                jsonPath(
                    "$.content[0].assignmentType.code"
                )
                    .value("REVOKED")
            )
            .andExpect(
                jsonPath(
                    "$.content[0].assignmentType.name"
                )
                    .value("撤銷權限")
            )
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
            .andExpect(
                jsonPath("$.content[0].note")
                    .value(
                        "撤銷客服會員狀態管理權限"
                    )
            )
            /*
            * 較舊的 GRANTED 排在第二筆。
            */
            .andExpect(
                jsonPath("$.content[1].id")
                    .value(1)
            )
            .andExpect(
                jsonPath(
                    "$.content[1].assignmentType.code"
                )
                    .value("GRANTED")
            )
            .andExpect(
                jsonPath("$.content[1].note")
                    .value(
                        "授予客服會員狀態管理權限"
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
    void userWithoutPermissionReadCannotGetRolePermissionHistory()
        throws Exception {

        String accessToken =
            loginAsDefaultUser();

        String requestPath =
            "/api/v1/admin/roles/SUPPORT/permissions/history";

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
    void revokedPermissionReadBlocksRolePermissionHistoryWithOldToken()
        throws Exception {

        /*
        * 登入時 ADMIN 擁有 PERMISSION_READ，
        * JWT 會包含此權限。
        */
        String accessToken =
            loginAsAdmin();

        /*
        * JWT 發出後，從資料庫即時撤銷。
        *
        * role_id = 3       -> ADMIN
        * permission_id = 9 -> PERMISSION_READ
        */
        jdbcTemplate.update(
            """
            UPDATE role_permissions
            SET is_active = FALSE,
                deleted_date = CURRENT_TIMESTAMP
            WHERE role_id = 3
            AND permission_id = 9
            """
        );

        String requestPath =
            "/api/v1/admin/roles/SUPPORT/permissions/history";

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
    void getPermissionHistoryForMissingRoleReturnsNotFound()
        throws Exception {

        String accessToken =
            loginAsAdmin();

        String requestPath =
            "/api/v1/admin/roles/UNKNOWN_ROLE/permissions/history";

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
    void existingRoleWithoutPermissionHistoryReturnsEmptyPage()
        throws Exception {

        String accessToken =
            loginAsAdmin();

        mockMvc.perform(
                get(
                    "/api/v1/admin/roles/SUPPORT/permissions/history"
                )
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
                    )
                    .param("page", "0")
                    .param("size", "20")
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

    @ParameterizedTest
    @MethodSource(
        "invalidRolePermissionHistoryPagination"
    )
    void invalidRolePermissionHistoryPaginationReturnsBadRequest(
        int page,
        int size,
        String expectedMessage
    ) throws Exception {

        String accessToken =
            loginAsAdmin();

        String requestPath =
            "/api/v1/admin/roles/SUPPORT/permissions/history";

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
                    .value(requestPath)
            )
            .andExpect(
                jsonPath("$.fieldErrors")
                    .isEmpty()
            );
    }

    @ParameterizedTest
    @MethodSource(
        "invalidRolePermissionHistoryParameterTypes"
    )
    void invalidRolePermissionHistoryParameterTypeReturnsBadRequest(
        String parameterName,
        String parameterValue,
        String expectedMessage
    ) throws Exception {

        String accessToken =
            loginAsAdmin();

        String requestPath =
            "/api/v1/admin/roles/SUPPORT/permissions/history";

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
                    .value(requestPath)
            )
            .andExpect(
                jsonPath("$.fieldErrors")
                    .isEmpty()
            );
    }

    @Test
    void rolePermissionHistorySupportsNullChangedByUser()
        throws Exception {

        /*
        * 模擬系統操作，或原操作者已被刪除，
        * changed_by_user_id 因此為 NULL。
        */
        jdbcTemplate.update(
            """
            INSERT INTO role_permission_assignment_logs (
                id,
                role_id,
                permission_id,
                role_permission_assignment_type_id,
                changed_by_user_id,
                note,
                changed_date
            )
            VALUES (
                1,
                2,
                3,
                1,
                NULL,
                '系統自動授予角色權限',
                TIMESTAMP '2026-11-15 13:00:00'
            )
            """
        );

        String accessToken =
            loginAsAdmin();

        mockMvc.perform(
                get(
                    "/api/v1/admin/roles/SUPPORT/permissions/history"
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
                    "$.content[0].permission.code"
                )
                    .value("USER_STATUS_UPDATE")
            )
            .andExpect(
                jsonPath(
                    "$.content[0].assignmentType.code"
                )
                    .value("GRANTED")
            )
            .andExpect(
                jsonPath(
                    "$.content[0].changedByUser"
                )
                    .doesNotExist()
            )
            .andExpect(
                jsonPath("$.content[0].note")
                    .value(
                        "系統自動授予角色權限"
                    )
            )
            .andExpect(
                jsonPath("$.content[0].changedDate")
                    .value("2026-11-15T13:00:00")
            );
    }

    @Test
    void rolePermissionHistorySupportsPagination()
        throws Exception {

        jdbcTemplate.update(
            """
            INSERT INTO role_permission_assignment_logs (
                id,
                role_id,
                permission_id,
                role_permission_assignment_type_id,
                changed_by_user_id,
                note,
                changed_date
            )
            VALUES
                (
                    1,
                    2,
                    3,
                    1,
                    100,
                    '第一次授予',
                    TIMESTAMP '2026-11-15 10:00:00'
                ),
                (
                    2,
                    2,
                    3,
                    2,
                    100,
                    '第一次撤銷',
                    TIMESTAMP '2026-11-15 11:00:00'
                ),
                (
                    3,
                    2,
                    3,
                    1,
                    100,
                    '第二次授予',
                    TIMESTAMP '2026-11-15 12:00:00'
                )
            """
        );

        String accessToken =
            loginAsAdmin();

        mockMvc.perform(
                get(
                    "/api/v1/admin/roles/SUPPORT/permissions/history"
                )
                    .param("page", "1")
                    .param("size", "2")
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
            /*
            * 排序為 3、2、1。
            * page=1、size=2 只會取得 id=1。
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
                jsonPath("$.content[0].note")
                    .value("第一次授予")
            )
            .andExpect(
                jsonPath(
                    "$.content[0].assignmentType.code"
                )
                    .value("GRANTED")
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
    void rolePermissionHistoryUsesIdAsSecondarySort()
        throws Exception {

        /*
        * 兩筆紀錄刻意使用完全相同的時間，
        * 應由 id DESC 決定順序。
        */
        jdbcTemplate.update(
            """
            INSERT INTO role_permission_assignment_logs (
                id,
                role_id,
                permission_id,
                role_permission_assignment_type_id,
                changed_by_user_id,
                note,
                changed_date
            )
            VALUES
                (
                    1,
                    2,
                    3,
                    1,
                    100,
                    '相同時間的第一筆',
                    TIMESTAMP '2026-11-15 13:00:00'
                ),
                (
                    2,
                    2,
                    3,
                    2,
                    100,
                    '相同時間的第二筆',
                    TIMESTAMP '2026-11-15 13:00:00'
                )
            """
        );

        String accessToken =
            loginAsAdmin();

        mockMvc.perform(
                get(
                    "/api/v1/admin/roles/SUPPORT/permissions/history"
                )
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
                    )
                    .param("page", "0")
                    .param("size", "20")
                    .accept(
                        MediaType.APPLICATION_JSON
                    )
            )
            .andExpect(status().isOk())
            .andExpect(
                jsonPath("$.content.length()")
                    .value(2)
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
                jsonPath("$.content[1].id")
                    .value(1)
            )
            .andExpect(
                jsonPath("$.content[1].note")
                    .value("相同時間的第一筆")
            );
    }

    @Test
    void permissionManagerCanCreatePermission()
        throws Exception {

        String accessToken =
            loginAsPermissionManager();

        String requestBody = """
            {
            "permissionTypeCode": "user_management",
            "code": "user_email_update",
            "name": "  變更會員電子郵件  ",
            "note": "  允許後台變更會員電子郵件  "
            }
            """;

        mockMvc.perform(
                post(
                    "/api/v1/admin/permissions"
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
                    .value("USER_EMAIL_UPDATE")
            )
            .andExpect(
                jsonPath("$.name")
                    .value("變更會員電子郵件")
            )
            .andExpect(
                jsonPath("$.permissionType.id")
                    .value(2)
            )
            .andExpect(
                jsonPath("$.permissionType.code")
                    .value("USER_MANAGEMENT")
            )
            .andExpect(
                jsonPath("$.permissionType.name")
                    .value("會員管理")
            );

        Integer createdPermissionCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM permissions
                WHERE permission_type_id = ?
                AND code = ?
                AND name = ?
                AND note = ?
                AND is_active = TRUE
                AND deleted_date IS NULL
                AND created_date IS NOT NULL
                AND updated_date IS NOT NULL
                """,
                Integer.class,
                2L,
                "USER_EMAIL_UPDATE",
                "變更會員電子郵件",
                "允許後台變更會員電子郵件"
            );

        assertThat(createdPermissionCount)
            .isEqualTo(1);

        Integer createdLogCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM permission_change_logs log
                JOIN permissions permission
                    ON permission.id =
                        log.permission_id
                JOIN permission_change_types type
                    ON type.id =
                        log.permission_change_type_id
                WHERE permission.code = ?
                AND type.code = ?
                AND log.changed_by_user_id = ?
                AND log.previous_permission_type_id IS NULL
                AND log.previous_name IS NULL
                AND log.previous_note IS NULL
                AND log.previous_is_active IS NULL
                AND log.previous_deleted_date IS NULL
                AND log.new_permission_type_id = ?
                AND log.new_name = ?
                AND log.new_note = ?
                AND log.new_is_active = TRUE
                AND log.new_deleted_date IS NULL
                AND log.changed_date IS NOT NULL
                """,
                Integer.class,
                "USER_EMAIL_UPDATE",
                "CREATED",
                100L,
                2L,
                "變更會員電子郵件",
                "允許後台變更會員電子郵件"
            );

        assertThat(createdLogCount)
            .isEqualTo(1);
    }

    @Test
    void adminWithoutPermissionManageCannotCreatePermission()
        throws Exception {

        /*
        * loginAsAdmin() 擁有：
        * ROLE_PERMISSION_MANAGE
        *
        * 但沒有：
        * PERMISSION_MANAGE
        */
        String accessToken =
            loginAsAdmin();

        String requestBody = """
            {
            "permissionTypeCode": "USER_MANAGEMENT",
            "code": "USER_EMAIL_UPDATE",
            "name": "變更會員電子郵件",
            "note": "沒有權限的管理員嘗試建立"
            }
            """;

        mockMvc.perform(
                post(
                    "/api/v1/admin/permissions"
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
                        "/api/v1/admin/permissions"
                    )
            )
            .andExpect(
                jsonPath("$.fieldErrors")
                    .isEmpty()
            );

        Integer createdPermissionCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM permissions
                WHERE code = ?
                """,
                Integer.class,
                "USER_EMAIL_UPDATE"
            );

        assertThat(createdPermissionCount)
            .isZero();
    }

    @Test
    void revokedPermissionManageBlocksPreviouslyIssuedToken()
        throws Exception {

        /*
        * 登入時 JWT 會包含 PERMISSION_MANAGE。
        */
        String accessToken =
            loginAsPermissionManager();

        /*
        * Token 發出後，立即從資料庫撤銷：
        *
        * role_id = 3        -> ADMIN
        * permission_id = 21 -> PERMISSION_MANAGE
        */
        jdbcTemplate.update(
            """
            UPDATE role_permissions
            SET is_active = FALSE,
                deleted_date = CURRENT_TIMESTAMP
            WHERE role_id = 3
            AND permission_id = 21
            """
        );

        String requestBody = """
            {
            "permissionTypeCode": "USER_MANAGEMENT",
            "code": "USER_EMAIL_UPDATE",
            "name": "變更會員電子郵件",
            "note": "使用已撤銷權限的舊 Token"
            }
            """;

        mockMvc.perform(
                post(
                    "/api/v1/admin/permissions"
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
                        "/api/v1/admin/permissions"
                    )
            )
            .andExpect(
                jsonPath("$.fieldErrors")
                    .isEmpty()
            );

        Integer createdPermissionCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM permissions
                WHERE code = ?
                """,
                Integer.class,
                "USER_EMAIL_UPDATE"
            );

        assertThat(createdPermissionCount)
            .isZero();
    }

    @Test
    void createDuplicatePermissionCodeReturnsConflict()
        throws Exception {

        String accessToken =
            loginAsPermissionManager();

        String requestBody = """
            {
            "permissionTypeCode": "USER_MANAGEMENT",
            "code": "user_read",
            "name": "重複的會員查詢權限",
            "note": "測試重複權限代碼"
            }
            """;

        mockMvc.perform(
                post(
                    "/api/v1/admin/permissions"
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
                jsonPath("$.code")
                    .value(
                        "PERMISSION_CODE_ALREADY_EXISTS"
                    )
            )
            .andExpect(
                jsonPath("$.message")
                    .value("權限代碼已存在")
            )
            .andExpect(
                jsonPath("$.path")
                    .value(
                        "/api/v1/admin/permissions"
                    )
            )
            .andExpect(
                jsonPath("$.fieldErrors")
                    .isEmpty()
            );

        Integer permissionCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM permissions
                WHERE UPPER(code) = ?
                """,
                Integer.class,
                "USER_READ"
            );

        assertThat(permissionCount)
            .isEqualTo(1);

        String permissionName =
            jdbcTemplate.queryForObject(
                """
                SELECT name
                FROM permissions
                WHERE UPPER(code) = ?
                """,
                String.class,
                "USER_READ"
            );

        assertThat(permissionName)
            .isEqualTo("查詢會員資料");
    }

    @Test
    void createSoftDeletedPermissionCodeReturnsConflict()
        throws Exception {

        String accessToken =
            loginAsPermissionManager();

        jdbcTemplate.update(
            """
            INSERT INTO permissions (
                id,
                permission_type_id,
                code,
                name,
                note,
                is_active,
                created_date,
                updated_date,
                deleted_date
            )
            VALUES (
                22,
                2,
                'USER_EMAIL_UPDATE',
                '原本的電子郵件更新權限',
                '已經被停用',
                FALSE,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP
            )
            """
        );

        String requestBody = """
            {
            "permissionTypeCode": "USER_MANAGEMENT",
            "code": "user_email_update",
            "name": "新的電子郵件更新權限",
            "note": "嘗試重複使用已停用代碼"
            }
            """;

        mockMvc.perform(
                post(
                    "/api/v1/admin/permissions"
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
            .andExpect(status().isConflict())
            .andExpect(
                jsonPath("$.status")
                    .value(409)
            )
            .andExpect(
                jsonPath("$.code")
                    .value(
                        "PERMISSION_CODE_ALREADY_EXISTS"
                    )
            )
            .andExpect(
                jsonPath("$.message")
                    .value("權限代碼已存在")
            );

        Integer permissionCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM permissions
                WHERE UPPER(code) = ?
                """,
                Integer.class,
                "USER_EMAIL_UPDATE"
            );

        assertThat(permissionCount)
            .isEqualTo(1);

        Integer deletedPermissionCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM permissions
                WHERE id = ?
                AND is_active = FALSE
                AND deleted_date IS NOT NULL
                AND name = ?
                """,
                Integer.class,
                22L,
                "原本的電子郵件更新權限"
            );

        assertThat(deletedPermissionCount)
            .isEqualTo(1);
    }

    @Test
    void createPermissionWithMissingTypeReturnsNotFound()
        throws Exception {

        String accessToken =
            loginAsPermissionManager();

        String requestBody = """
            {
            "permissionTypeCode": "UNKNOWN_TYPE",
            "code": "USER_EMAIL_UPDATE",
            "name": "變更會員電子郵件",
            "note": "使用不存在的權限類型"
            }
            """;

        mockMvc.perform(
                post(
                    "/api/v1/admin/permissions"
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
                        "找不到指定的權限類型"
                    )
            )
            .andExpect(
                jsonPath("$.path")
                    .value(
                        "/api/v1/admin/permissions"
                    )
            )
            .andExpect(
                jsonPath("$.fieldErrors")
                    .isEmpty()
            );

        Integer permissionCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM permissions
                WHERE code = ?
                """,
                Integer.class,
                "USER_EMAIL_UPDATE"
            );

        assertThat(permissionCount)
            .isZero();
    }

    @ParameterizedTest
    @MethodSource(
        "invalidCreatePermissionRequests"
    )
    void invalidCreatePermissionRequestReturnsBadRequest(
        String requestBody,
        String expectedField,
        String expectedMessage
    ) throws Exception {

        String accessToken =
            loginAsPermissionManager();

        mockMvc.perform(
                post(
                    "/api/v1/admin/permissions"
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

        Integer permissionCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM permissions
                WHERE code = ?
                """,
                Integer.class,
                "USER_EMAIL_UPDATE"
            );

        assertThat(permissionCount)
            .isZero();
    }

    @Test
    void createPermissionNormalizesBlankNoteToNull()
        throws Exception {

        String accessToken =
            loginAsPermissionManager();

        String requestBody = """
            {
            "permissionTypeCode": "USER_MANAGEMENT",
            "code": "USER_EMAIL_UPDATE",
            "name": "變更會員電子郵件",
            "note": "   "
            }
            """;

        mockMvc.perform(
                post(
                    "/api/v1/admin/permissions"
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
            .andExpect(status().isCreated())
            .andExpect(
                jsonPath("$.code")
                    .value("USER_EMAIL_UPDATE")
            )
            .andExpect(
                jsonPath("$.name")
                    .value("變更會員電子郵件")
            );

        Integer permissionWithNullNoteCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM permissions
                WHERE code = ?
                AND note IS NULL
                """,
                Integer.class,
                "USER_EMAIL_UPDATE"
            );

        assertThat(permissionWithNullNoteCount)
            .isEqualTo(1);
    }

    @Test
    void permissionManagerCanUpdatePermission()
        throws Exception {

        String accessToken =
            loginAsPermissionManager();

        jdbcTemplate.update(
            """
            INSERT INTO permissions (
                id,
                permission_type_id,
                code,
                name,
                note,
                is_active,
                created_date,
                updated_date
            )
            VALUES (
                22,
                3,
                'USER_EMAIL_UPDATE',
                '原本的權限名稱',
                '原本的備註',
                TRUE,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP
            )
            """
        );

        String requestBody = """
            {
            "permissionTypeCode": "user_management",
            "name": "  管理會員電子郵件  ",
            "note": "  允許後台更新會員電子郵件  "
            }
            """;

        mockMvc.perform(
                put(
                    "/api/v1/admin/permissions/user_email_update"
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
                    .value(22)
            )
            .andExpect(
                jsonPath("$.code")
                    .value("USER_EMAIL_UPDATE")
            )
            .andExpect(
                jsonPath("$.name")
                    .value("管理會員電子郵件")
            )
            .andExpect(
                jsonPath("$.permissionType.id")
                    .value(2)
            )
            .andExpect(
                jsonPath("$.permissionType.code")
                    .value("USER_MANAGEMENT")
            )
            .andExpect(
                jsonPath("$.permissionType.name")
                    .value("會員管理")
            );

        Integer updatedPermissionCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM permissions
                WHERE id = ?
                AND permission_type_id = ?
                AND code = ?
                AND name = ?
                AND note = ?
                AND is_active = TRUE
                AND deleted_date IS NULL
                """,
                Integer.class,
                22L,
                2L,
                "USER_EMAIL_UPDATE",
                "管理會員電子郵件",
                "允許後台更新會員電子郵件"
            );

        assertThat(updatedPermissionCount)
            .isEqualTo(1);

        Integer updatedLogCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM permission_change_logs log
                JOIN permission_change_types type
                    ON type.id =
                        log.permission_change_type_id
                WHERE log.permission_id = ?
                AND type.code = ?
                AND log.changed_by_user_id = ?
                AND log.previous_permission_type_id = ?
                AND log.new_permission_type_id = ?
                AND log.previous_name = ?
                AND log.new_name = ?
                AND log.previous_note = ?
                AND log.new_note = ?
                AND log.previous_is_active = TRUE
                AND log.new_is_active = TRUE
                AND log.previous_deleted_date IS NULL
                AND log.new_deleted_date IS NULL
                AND log.changed_date IS NOT NULL
                """,
                Integer.class,
                22L,
                "UPDATED",
                100L,
                3L,
                2L,
                "原本的權限名稱",
                "管理會員電子郵件",
                "原本的備註",
                "允許後台更新會員電子郵件"
            );

        assertThat(updatedLogCount)
            .isEqualTo(1);
    }

    @Test
    void adminWithoutPermissionManageCannotUpdatePermission()
        throws Exception {

        /*
        * ADMIN 有 ROLE_PERMISSION_MANAGE，
        * 但沒有 PERMISSION_MANAGE。
        */
        String accessToken =
            loginAsAdmin();

        String requestBody = """
            {
            "permissionTypeCode": "USER_MANAGEMENT",
            "name": "被非法修改的名稱",
            "note": "沒有權限的管理員嘗試修改"
            }
            """;

        mockMvc.perform(
                put(
                    "/api/v1/admin/permissions/USER_READ"
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
                        "/api/v1/admin/permissions/USER_READ"
                    )
            )
            .andExpect(
                jsonPath("$.fieldErrors")
                    .isEmpty()
            );

        String permissionName =
            jdbcTemplate.queryForObject(
                """
                SELECT name
                FROM permissions
                WHERE code = ?
                """,
                String.class,
                "USER_READ"
            );

        assertThat(permissionName)
            .isEqualTo("查詢會員資料");
    }

    @Test
    void revokedPermissionManageBlocksUpdateWithOldToken()
        throws Exception {

        String accessToken =
            loginAsPermissionManager();

        /*
        * JWT 發出後撤銷 ADMIN 的
        * PERMISSION_MANAGE。
        */
        jdbcTemplate.update(
            """
            UPDATE role_permissions
            SET is_active = FALSE,
                deleted_date = CURRENT_TIMESTAMP
            WHERE role_id = 3
            AND permission_id = 21
            """
        );

        String requestBody = """
            {
            "permissionTypeCode": "USER_MANAGEMENT",
            "name": "被舊 Token 修改的名稱",
            "note": "權限已撤銷"
            }
            """;

        mockMvc.perform(
                put(
                    "/api/v1/admin/permissions/USER_READ"
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
                        "/api/v1/admin/permissions/USER_READ"
                    )
            );

        String permissionName =
            jdbcTemplate.queryForObject(
                """
                SELECT name
                FROM permissions
                WHERE code = ?
                """,
                String.class,
                "USER_READ"
            );

        assertThat(permissionName)
            .isEqualTo("查詢會員資料");
    }

    @Test
    void updateMissingPermissionReturnsNotFound()
        throws Exception {

        String accessToken =
            loginAsPermissionManager();

        String requestBody = """
            {
            "permissionTypeCode": "USER_MANAGEMENT",
            "name": "不存在的權限",
            "note": "測試修改不存在的 Permission"
            }
            """;

        String requestPath =
            "/api/v1/admin/permissions/UNKNOWN_PERMISSION";

        mockMvc.perform(
                put(requestPath)
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
                    )
                    .contentType(
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
                    .value("找不到指定的權限")
            )
            .andExpect(
                jsonPath("$.path")
                    .value(requestPath)
            )
            .andExpect(
                jsonPath("$.fieldErrors")
                    .isEmpty()
            );

        Integer permissionCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM permissions
                WHERE code = ?
                """,
                Integer.class,
                "UNKNOWN_PERMISSION"
            );

        assertThat(permissionCount)
            .isZero();
    }

    @Test
    void updateSoftDeletedPermissionReturnsNotFound()
        throws Exception {

        String accessToken =
            loginAsPermissionManager();

        jdbcTemplate.update(
            """
            INSERT INTO permissions (
                id,
                permission_type_id,
                code,
                name,
                note,
                is_active,
                created_date,
                updated_date,
                deleted_date
            )
            VALUES (
                22,
                2,
                'USER_EMAIL_UPDATE',
                '已停用的權限',
                '原本的備註',
                FALSE,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP
            )
            """
        );

        String requestBody = """
            {
            "permissionTypeCode": "USER_MANAGEMENT",
            "name": "嘗試修改已停用權限",
            "note": "不應成功"
            }
            """;

        String requestPath =
            "/api/v1/admin/permissions/USER_EMAIL_UPDATE";

        mockMvc.perform(
                put(requestPath)
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
                    )
                    .contentType(
                        MediaType.APPLICATION_JSON
                    )
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
                    .value("找不到指定的權限")
            )
            .andExpect(
                jsonPath("$.path")
                    .value(requestPath)
            );

        Integer unchangedPermissionCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM permissions
                WHERE id = ?
                AND name = ?
                AND note = ?
                AND is_active = FALSE
                AND deleted_date IS NOT NULL
                """,
                Integer.class,
                22L,
                "已停用的權限",
                "原本的備註"
            );

        assertThat(unchangedPermissionCount)
            .isEqualTo(1);
    }

    @Test
    void updatePermissionWithMissingTypeReturnsNotFound()
        throws Exception {

        String accessToken =
            loginAsPermissionManager();

        jdbcTemplate.update(
            """
            INSERT INTO permissions (
                id,
                permission_type_id,
                code,
                name,
                note,
                is_active,
                created_date,
                updated_date
            )
            VALUES (
                22,
                2,
                'USER_EMAIL_UPDATE',
                '原本的權限名稱',
                '原本的備註',
                TRUE,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP
            )
            """
        );

        String requestBody = """
            {
            "permissionTypeCode": "UNKNOWN_TYPE",
            "name": "新的權限名稱",
            "note": "不應寫入"
            }
            """;

        String requestPath =
            "/api/v1/admin/permissions/USER_EMAIL_UPDATE";

        mockMvc.perform(
                put(requestPath)
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
                    )
                    .contentType(
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
                        "找不到指定的權限類型"
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

        Integer unchangedPermissionCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM permissions
                WHERE id = ?
                AND permission_type_id = ?
                AND name = ?
                AND note = ?
                """,
                Integer.class,
                22L,
                2L,
                "原本的權限名稱",
                "原本的備註"
            );

        assertThat(unchangedPermissionCount)
            .isEqualTo(1);
    }

    @ParameterizedTest
    @MethodSource(
        "invalidUpdatePermissionRequests"
    )
    void invalidUpdatePermissionRequestReturnsBadRequest(
        String requestBody,
        String expectedField,
        String expectedMessage
    ) throws Exception {

        String accessToken =
            loginAsPermissionManager();

        jdbcTemplate.update(
            """
            INSERT INTO permissions (
                id,
                permission_type_id,
                code,
                name,
                note,
                is_active,
                created_date,
                updated_date
            )
            VALUES (
                22,
                2,
                'USER_EMAIL_UPDATE',
                '原本的權限名稱',
                '原本的備註',
                TRUE,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP
            )
            """
        );

        mockMvc.perform(
                put(
                    "/api/v1/admin/permissions/USER_EMAIL_UPDATE"
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
            .andExpect(status().isBadRequest())
            .andExpect(
                jsonPath("$.status")
                    .value(400)
            )
            .andExpect(
                jsonPath("$.code")
                    .value("VALIDATION_FAILED")
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

        Integer unchangedPermissionCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM permissions
                WHERE id = ?
                AND permission_type_id = ?
                AND name = ?
                AND note = ?
                """,
                Integer.class,
                22L,
                2L,
                "原本的權限名稱",
                "原本的備註"
            );

        assertThat(unchangedPermissionCount)
            .isEqualTo(1);
    }

    @Test
    void updatePermissionNormalizesBlankNoteToNull()
        throws Exception {

        String accessToken =
            loginAsPermissionManager();

        jdbcTemplate.update(
            """
            INSERT INTO permissions (
                id,
                permission_type_id,
                code,
                name,
                note,
                is_active,
                created_date,
                updated_date
            )
            VALUES (
                22,
                2,
                'USER_EMAIL_UPDATE',
                '原本的權限名稱',
                '原本的備註',
                TRUE,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP
            )
            """
        );

        String requestBody = """
            {
            "permissionTypeCode": "USER_MANAGEMENT",
            "name": "管理會員電子郵件",
            "note": "   "
            }
            """;

        mockMvc.perform(
                put(
                    "/api/v1/admin/permissions/USER_EMAIL_UPDATE"
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
                    .value(22)
            )
            .andExpect(
                jsonPath("$.code")
                    .value("USER_EMAIL_UPDATE")
            )
            .andExpect(
                jsonPath("$.name")
                    .value("管理會員電子郵件")
            );

        Integer permissionWithNullNoteCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM permissions
                WHERE id = ?
                AND name = ?
                AND note IS NULL
                """,
                Integer.class,
                22L,
                "管理會員電子郵件"
            );

        assertThat(permissionWithNullNoteCount)
            .isEqualTo(1);
    }

    @Test
    void permissionManagerCanDeactivatePermission()
        throws Exception {

        String accessToken =
            loginAsPermissionManager();

        jdbcTemplate.update(
            """
            INSERT INTO permissions (
                id,
                permission_type_id,
                code,
                name,
                note,
                is_active,
                created_date,
                updated_date
            )
            VALUES (
                22,
                2,
                'USER_EMAIL_UPDATE',
                '管理會員電子郵件',
                '允許後台更新會員電子郵件',
                TRUE,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP
            )
            """
        );

        /*
        * 模擬 SUPPORT 原本擁有此權限。
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
                2,
                22,
                TRUE,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP
            )
            """
        );

        mockMvc.perform(
                delete(
                    "/api/v1/admin/permissions/user_email_update"
                )
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
                    )
            )
            .andExpect(status().isNoContent())
            .andExpect(content().string(""));

        Integer deactivatedPermissionCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM permissions
                WHERE id = ?
                AND code = ?
                AND is_active = FALSE
                AND deleted_date IS NOT NULL
                """,
                Integer.class,
                22L,
                "USER_EMAIL_UPDATE"
            );

        assertThat(deactivatedPermissionCount)
            .isEqualTo(1);

        /*
        * Permission 停用不會刪除或停用
        * 原本的 RolePermission 關聯。
        */
        Integer retainedRolePermissionCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM role_permissions
                WHERE id = ?
                AND role_id = ?
                AND permission_id = ?
                AND is_active = TRUE
                AND deleted_date IS NULL
                """,
                Integer.class,
                22L,
                2L,
                22L
            );

        assertThat(retainedRolePermissionCount)
            .isEqualTo(1);

        Integer deactivatedLogCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM permission_change_logs log
                JOIN permission_change_types type
                    ON type.id =
                        log.permission_change_type_id
                WHERE log.permission_id = ?
                AND type.code = ?
                AND log.changed_by_user_id = ?
                AND log.previous_permission_type_id = ?
                AND log.new_permission_type_id = ?
                AND log.previous_name = ?
                AND log.new_name = ?
                AND log.previous_note = ?
                AND log.new_note = ?
                AND log.previous_is_active = TRUE
                AND log.new_is_active = FALSE
                AND log.previous_deleted_date IS NULL
                AND log.new_deleted_date IS NOT NULL
                AND log.changed_date IS NOT NULL
                """,
                Integer.class,
                22L,
                "DEACTIVATED",
                100L,
                2L,
                2L,
                "管理會員電子郵件",
                "管理會員電子郵件",
                "允許後台更新會員電子郵件",
                "允許後台更新會員電子郵件"
            );

        assertThat(deactivatedLogCount)
            .isEqualTo(1);
    }

    @Test
    void adminWithoutPermissionManageCannotDeactivatePermission()
        throws Exception {

        String accessToken =
            loginAsAdmin();

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
                2,
                'USER_EMAIL_UPDATE',
                '管理會員電子郵件',
                TRUE,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP
            )
            """
        );

        String requestPath =
            "/api/v1/admin/permissions/USER_EMAIL_UPDATE";

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

        Integer activePermissionCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM permissions
                WHERE id = ?
                AND is_active = TRUE
                AND deleted_date IS NULL
                """,
                Integer.class,
                22L
            );

        assertThat(activePermissionCount)
            .isEqualTo(1);
    }

    @ParameterizedTest
    @ValueSource(
        strings = {
            "PERMISSION_MANAGE",
            "ROLE_PERMISSION_MANAGE"
        }
    )
    void protectedPermissionCannotBeDeactivated(
        String permissionCode
    ) throws Exception {

        String accessToken =
            loginAsPermissionManager();

        String requestPath =
            "/api/v1/admin/permissions/" +
                permissionCode;

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
                jsonPath("$.code")
                    .value(
                        "PROTECTED_PERMISSION_CANNOT_BE_DEACTIVATED"
                    )
            )
            .andExpect(
                jsonPath("$.message")
                    .value(
                        "不可停用系統核心管理權限"
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

        Integer activePermissionCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM permissions
                WHERE code = ?
                AND is_active = TRUE
                AND deleted_date IS NULL
                """,
                Integer.class,
                permissionCode
            );

        assertThat(activePermissionCount)
            .isEqualTo(1);
    }

    @Test
    void deactivateMissingPermissionReturnsNotFound()
        throws Exception {

        String accessToken =
            loginAsPermissionManager();

        String requestPath =
            "/api/v1/admin/permissions/UNKNOWN_PERMISSION";

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
                    .value("找不到指定的權限")
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
    void permissionManagerCanRestorePermission()
        throws Exception {

        String accessToken =
            loginAsPermissionManager();

        jdbcTemplate.update(
            """
            INSERT INTO permissions (
                id,
                permission_type_id,
                code,
                name,
                note,
                is_active,
                created_date,
                updated_date,
                deleted_date
            )
            VALUES (
                22,
                2,
                'USER_EMAIL_UPDATE',
                '管理會員電子郵件',
                '允許後台更新會員電子郵件',
                FALSE,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP
            )
            """
        );

        /*
        * 停用 Permission 時保留下來的角色關聯。
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
                2,
                22,
                TRUE,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP
            )
            """
        );

        mockMvc.perform(
                post(
                    "/api/v1/admin/permissions/user_email_update/restore"
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
                    .value(22)
            )
            .andExpect(
                jsonPath("$.code")
                    .value("USER_EMAIL_UPDATE")
            )
            .andExpect(
                jsonPath("$.name")
                    .value("管理會員電子郵件")
            )
            .andExpect(
                jsonPath("$.permissionType.id")
                    .value(2)
            )
            .andExpect(
                jsonPath("$.permissionType.code")
                    .value("USER_MANAGEMENT")
            );

        Integer restoredPermissionCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM permissions
                WHERE id = ?
                AND is_active = TRUE
                AND deleted_date IS NULL
                """,
                Integer.class,
                22L
            );

        assertThat(restoredPermissionCount)
            .isEqualTo(1);

        /*
        * Permission 與 RolePermission 都有效，
        * 因此 SUPPORT 重新擁有此權限。
        */
        Integer effectiveRolePermissionCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM role_permissions rp
                JOIN permissions p
                    ON p.id = rp.permission_id
                WHERE rp.role_id = ?
                AND p.code = ?
                AND rp.is_active = TRUE
                AND rp.deleted_date IS NULL
                AND p.is_active = TRUE
                AND p.deleted_date IS NULL
                """,
                Integer.class,
                2L,
                "USER_EMAIL_UPDATE"
            );

        assertThat(effectiveRolePermissionCount)
            .isEqualTo(1);

        Integer restoredLogCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM permission_change_logs log
                JOIN permission_change_types type
                    ON type.id =
                        log.permission_change_type_id
                WHERE log.permission_id = ?
                AND type.code = ?
                AND log.changed_by_user_id = ?
                AND log.previous_permission_type_id = ?
                AND log.new_permission_type_id = ?
                AND log.previous_name = ?
                AND log.new_name = ?
                AND log.previous_note = ?
                AND log.new_note = ?
                AND log.previous_is_active = FALSE
                AND log.new_is_active = TRUE
                AND log.previous_deleted_date IS NOT NULL
                AND log.new_deleted_date IS NULL
                AND log.changed_date IS NOT NULL
                """,
                Integer.class,
                22L,
                "RESTORED",
                100L,
                2L,
                2L,
                "管理會員電子郵件",
                "管理會員電子郵件",
                "允許後台更新會員電子郵件",
                "允許後台更新會員電子郵件"
            );

        assertThat(restoredLogCount)
            .isEqualTo(1);
    }

    @Test
    void adminWithoutPermissionManageCannotRestorePermission()
        throws Exception {

        String accessToken =
            loginAsAdmin();

        jdbcTemplate.update(
            """
            INSERT INTO permissions (
                id,
                permission_type_id,
                code,
                name,
                is_active,
                created_date,
                updated_date,
                deleted_date
            )
            VALUES (
                22,
                2,
                'USER_EMAIL_UPDATE',
                '管理會員電子郵件',
                FALSE,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP
            )
            """
        );

        String requestPath =
            "/api/v1/admin/permissions/USER_EMAIL_UPDATE/restore";

        mockMvc.perform(
                post(requestPath)
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

        Integer deletedPermissionCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM permissions
                WHERE id = ?
                AND is_active = FALSE
                AND deleted_date IS NOT NULL
                """,
                Integer.class,
                22L
            );

        assertThat(deletedPermissionCount)
            .isEqualTo(1);
    }

    @Test
    void restoreActivePermissionReturnsConflict()
        throws Exception {

        String accessToken =
            loginAsPermissionManager();

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
                2,
                'USER_EMAIL_UPDATE',
                '管理會員電子郵件',
                TRUE,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP
            )
            """
        );

        String requestPath =
            "/api/v1/admin/permissions/USER_EMAIL_UPDATE/restore";

        mockMvc.perform(
                post(requestPath)
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
                jsonPath("$.code")
                    .value("PERMISSION_ALREADY_ACTIVE")
            )
            .andExpect(
                jsonPath("$.message")
                    .value(
                        "權限目前已經是啟用狀態"
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

        Integer activePermissionCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM permissions
                WHERE id = ?
                AND is_active = TRUE
                AND deleted_date IS NULL
                """,
                Integer.class,
                22L
            );

        assertThat(activePermissionCount)
            .isEqualTo(1);
    }

    @Test
    void restoreMissingPermissionReturnsNotFound()
        throws Exception {

        String accessToken =
            loginAsPermissionManager();

        String requestPath =
            "/api/v1/admin/permissions/UNKNOWN_PERMISSION/restore";

        mockMvc.perform(
                post(requestPath)
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
                    .value("找不到指定的權限")
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
    void restorePermissionWithInactiveTypeReturnsConflict()
        throws Exception {

        String accessToken =
            loginAsPermissionManager();

        jdbcTemplate.update(
            """
            INSERT INTO permissions (
                id,
                permission_type_id,
                code,
                name,
                is_active,
                created_date,
                updated_date,
                deleted_date
            )
            VALUES (
                22,
                2,
                'USER_EMAIL_UPDATE',
                '管理會員電子郵件',
                FALSE,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP
            )
            """
        );

        /*
        * 停用 USER_MANAGEMENT PermissionType。
        */
        jdbcTemplate.update(
            """
            UPDATE permission_types
            SET is_active = FALSE,
                deleted_date = CURRENT_TIMESTAMP
            WHERE id = 2
            """
        );

        String requestPath =
            "/api/v1/admin/permissions/USER_EMAIL_UPDATE/restore";

        mockMvc.perform(
                post(requestPath)
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
                jsonPath("$.code")
                    .value(
                        "PERMISSION_TYPE_NOT_AVAILABLE"
                    )
            )
            .andExpect(
                jsonPath("$.message")
                    .value(
                        "權限所屬類型目前無法使用"
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

        Integer deletedPermissionCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM permissions
                WHERE id = ?
                AND is_active = FALSE
                AND deleted_date IS NOT NULL
                """,
                Integer.class,
                22L
            );

        assertThat(deletedPermissionCount)
            .isEqualTo(1);
    }

    @Test
    void permissionManagerCanGetDeactivatedPermissions()
        throws Exception {

        String accessToken =
            loginAsPermissionManager();

        jdbcTemplate.update(
            """
            INSERT INTO permissions (
                id,
                permission_type_id,
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
                    22,
                    2,
                    'USER_Z_PERMISSION',
                    '會員 Z 權限',
                    '只有停用，沒有刪除時間',
                    FALSE,
                    TIMESTAMP '2026-11-21 09:00:00',
                    TIMESTAMP '2026-11-21 10:00:00',
                    NULL
                ),
                (
                    23,
                    2,
                    'USER_A_PERMISSION',
                    '會員 A 權限',
                    '具有刪除時間',
                    TRUE,
                    TIMESTAMP '2026-11-21 09:00:00',
                    TIMESTAMP '2026-11-21 11:00:00',
                    TIMESTAMP '2026-11-21 11:00:00'
                ),
                (
                    24,
                    3,
                    'ROLE_Z_PERMISSION',
                    '角色 Z 權限',
                    '授權類型的停用權限',
                    FALSE,
                    TIMESTAMP '2026-11-21 09:00:00',
                    TIMESTAMP '2026-11-21 12:00:00',
                    TIMESTAMP '2026-11-21 12:00:00'
                )
            """
        );

        mockMvc.perform(
                get(
                    "/api/v1/admin/permissions/deactivated"
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
                    .value(3)
            )
            /*
            * 先依 PermissionType code 排序：
            * AUTHORIZATION 在 USER_MANAGEMENT 前面。
            */
            .andExpect(
                jsonPath("$[0].id")
                    .value(24)
            )
            .andExpect(
                jsonPath("$[0].code")
                    .value("ROLE_Z_PERMISSION")
            )
            .andExpect(
                jsonPath(
                    "$[0].permissionType.code"
                )
                    .value("AUTHORIZATION")
            )
            .andExpect(
                jsonPath("$[0].active")
                    .value(false)
            )
            /*
            * 同一個 USER_MANAGEMENT 中，
            * 再依 Permission code 排序。
            */
            .andExpect(
                jsonPath("$[1].id")
                    .value(23)
            )
            .andExpect(
                jsonPath("$[1].code")
                    .value("USER_A_PERMISSION")
            )
            .andExpect(
                jsonPath(
                    "$[1].permissionType.code"
                )
                    .value("USER_MANAGEMENT")
            )
            .andExpect(
                jsonPath("$[1].active")
                    .value(true)
            )
            .andExpect(
                jsonPath("$[1].deletedDate")
                    .value("2026-11-21T11:00:00")
            )
            .andExpect(
                jsonPath("$[2].id")
                    .value(22)
            )
            .andExpect(
                jsonPath("$[2].code")
                    .value("USER_Z_PERMISSION")
            )
            .andExpect(
                jsonPath("$[2].name")
                    .value("會員 Z 權限")
            )
            .andExpect(
                jsonPath("$[2].note")
                    .value(
                        "只有停用，沒有刪除時間"
                    )
            )
            .andExpect(
                jsonPath("$[2].active")
                    .value(false)
            )
            .andExpect(
                jsonPath("$[2].deletedDate")
                    .doesNotExist()
            );
    }

    @Test
    void adminWithoutPermissionManageCannotGetDeactivatedPermissions()
        throws Exception {

        String accessToken =
            loginAsAdmin();

        String requestPath =
            "/api/v1/admin/permissions/deactivated";

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
    void revokedPermissionManageBlocksDeactivatedPermissionListWithOldToken()
        throws Exception {

        String accessToken =
            loginAsPermissionManager();

        /*
        * JWT 發出後，即時撤銷資料庫中的
        * PERMISSION_MANAGE。
        */
        jdbcTemplate.update(
            """
            UPDATE role_permissions
            SET is_active = FALSE,
                deleted_date = CURRENT_TIMESTAMP
            WHERE role_id = 3
            AND permission_id = 21
            """
        );

        String requestPath =
            "/api/v1/admin/permissions/deactivated";

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
    void noDeactivatedPermissionsReturnsEmptyList()
        throws Exception {

        String accessToken =
            loginAsPermissionManager();

        mockMvc.perform(
                get(
                    "/api/v1/admin/permissions/deactivated"
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
                jsonPath("$")
                    .isArray()
            )
            .andExpect(
                jsonPath("$")
                    .isEmpty()
            );
    }

    @Test
    void permissionManagerCanGetCompletePermissionChangeHistory()
        throws Exception {

        String accessToken =
            loginAsPermissionManager();

        String createRequest = """
            {
            "permissionTypeCode": "USER_MANAGEMENT",
            "code": "USER_EMAIL_UPDATE",
            "name": "原本的權限名稱",
            "note": "原本的備註"
            }
            """;

        mockMvc.perform(
                post("/api/v1/admin/permissions")
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
                    )
                    .contentType(
                        MediaType.APPLICATION_JSON
                    )
                    .content(createRequest)
            )
            .andExpect(status().isCreated());

        String updateRequest = """
            {
            "permissionTypeCode": "AUTHORIZATION",
            "name": "修改後的權限名稱",
            "note": "修改後的備註"
            }
            """;

        mockMvc.perform(
                put(
                    "/api/v1/admin/permissions/USER_EMAIL_UPDATE"
                )
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
                    )
                    .contentType(
                        MediaType.APPLICATION_JSON
                    )
                    .content(updateRequest)
            )
            .andExpect(status().isOk());

        mockMvc.perform(
                delete(
                    "/api/v1/admin/permissions/USER_EMAIL_UPDATE"
                )
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
                    )
            )
            .andExpect(status().isNoContent());

        mockMvc.perform(
                post(
                    "/api/v1/admin/permissions/USER_EMAIL_UPDATE/restore"
                )
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
                    )
            )
            .andExpect(status().isOk());

        mockMvc.perform(
                get(
                    "/api/v1/admin/permissions/USER_EMAIL_UPDATE/history"
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
                    "$.content[0].previousState.permissionType.code"
                )
                    .value("AUTHORIZATION")
            )
            .andExpect(
                jsonPath(
                    "$.content[0].previousState.name"
                )
                    .value("修改後的權限名稱")
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
                    "$.content[2].previousState.permissionType.code"
                )
                    .value("USER_MANAGEMENT")
            )
            .andExpect(
                jsonPath(
                    "$.content[2].newState.permissionType.code"
                )
                    .value("AUTHORIZATION")
            )
            .andExpect(
                jsonPath(
                    "$.content[2].previousState.name"
                )
                    .value("原本的權限名稱")
            )
            .andExpect(
                jsonPath(
                    "$.content[2].newState.name"
                )
                    .value("修改後的權限名稱")
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
                    "$.content[3].newState.permissionType.code"
                )
                    .value("USER_MANAGEMENT")
            )
            .andExpect(
                jsonPath(
                    "$.content[3].newState.name"
                )
                    .value("原本的權限名稱")
            )
            .andExpect(
                jsonPath(
                    "$.content[3].newState.active"
                )
                    .value(true)
            )
            /*
            * 操作者與分頁資料。
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
    void adminWithoutPermissionManageCannotGetPermissionChangeHistory()
        throws Exception {

        String accessToken =
            loginAsAdmin();

        String requestPath =
            "/api/v1/admin/permissions/USER_READ/history";

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
    void revokedPermissionManageBlocksPermissionHistoryWithOldToken()
        throws Exception {

        String accessToken =
            loginAsPermissionManager();

        /*
        * JWT 發出後，立即撤銷資料庫中的
        * PERMISSION_MANAGE。
        */
        jdbcTemplate.update(
            """
            UPDATE role_permissions
            SET is_active = FALSE,
                deleted_date = CURRENT_TIMESTAMP
            WHERE role_id = 3
            AND permission_id = 21
            """
        );

        String requestPath =
            "/api/v1/admin/permissions/USER_READ/history";

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
    void getHistoryForMissingPermissionReturnsNotFound()
        throws Exception {

        String accessToken =
            loginAsPermissionManager();

        String requestPath =
            "/api/v1/admin/permissions/UNKNOWN_PERMISSION/history";

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
                    .value("找不到指定的權限")
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
    void permissionWithoutChangeHistoryReturnsEmptyPage()
        throws Exception {

        String accessToken =
            loginAsPermissionManager();

        /*
        * USER_READ 存在於 fixture，
        * 但沒有 permission_change_logs。
        */
        mockMvc.perform(
                get(
                    "/api/v1/admin/permissions/USER_READ/history"
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

    @ParameterizedTest
    @MethodSource(
        "invalidPermissionChangeHistoryPagination"
    )
    void invalidPermissionChangeHistoryPaginationReturnsBadRequest(
        int page,
        int size,
        String expectedMessage
    ) throws Exception {

        String accessToken =
            loginAsPermissionManager();

        String requestPath =
            "/api/v1/admin/permissions/USER_READ/history";

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
                    .value(requestPath)
            )
            .andExpect(
                jsonPath("$.fieldErrors")
                    .isEmpty()
            );
    }

    @ParameterizedTest
    @MethodSource(
        "invalidPermissionChangeHistoryParameterTypes"
    )
    void invalidPermissionChangeHistoryParameterTypeReturnsBadRequest(
        String parameterName,
        String parameterValue,
        String expectedMessage
    ) throws Exception {

        String accessToken =
            loginAsPermissionManager();

        String requestPath =
            "/api/v1/admin/permissions/USER_READ/history";

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
                    .value(requestPath)
            )
            .andExpect(
                jsonPath("$.fieldErrors")
                    .isEmpty()
            );
    }

    @Test
    void permissionChangeHistorySupportsPagination()
        throws Exception {

        jdbcTemplate.update(
            """
            INSERT INTO permission_change_logs (
                id,
                permission_id,
                permission_change_type_id,
                changed_by_user_id,
                previous_permission_type_id,
                new_permission_type_id,
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
                    2,
                    2,
                    '第一版名稱',
                    '第二版名稱',
                    TRUE,
                    TRUE,
                    TIMESTAMP '2026-11-21 10:00:00'
                ),
                (
                    2,
                    2,
                    2,
                    100,
                    2,
                    2,
                    '第二版名稱',
                    '第三版名稱',
                    TRUE,
                    TRUE,
                    TIMESTAMP '2026-11-21 11:00:00'
                ),
                (
                    3,
                    2,
                    2,
                    100,
                    2,
                    2,
                    '第三版名稱',
                    '第四版名稱',
                    TRUE,
                    TRUE,
                    TIMESTAMP '2026-11-21 12:00:00'
                )
            """
        );

        String accessToken =
            loginAsPermissionManager();

        mockMvc.perform(
                get(
                    "/api/v1/admin/permissions/USER_READ/history"
                )
                    .param("page", "1")
                    .param("size", "2")
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
            * 排序為 id=3、2、1。
            * 第二頁只剩 id=1。
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
    void permissionChangeHistorySupportsNullChangedByUser()
        throws Exception {

        /*
        * 模擬系統自動異動。
        * 因為不是由會員操作，
        * changed_by_user_id 為 NULL。
        */
        jdbcTemplate.update(
            """
            INSERT INTO permission_change_logs (
                id,
                permission_id,
                permission_change_type_id,
                changed_by_user_id,
                previous_permission_type_id,
                new_permission_type_id,
                previous_name,
                new_name,
                previous_note,
                new_note,
                previous_is_active,
                new_is_active,
                changed_date
            )
            VALUES (
                1,
                2,
                2,
                NULL,
                2,
                2,
                '原本名稱',
                '系統更新後名稱',
                '原本備註',
                '系統更新',
                TRUE,
                TRUE,
                TIMESTAMP '2026-11-21 14:00:00'
            )
            """
        );

        String accessToken =
            loginAsPermissionManager();

        mockMvc.perform(
                get(
                    "/api/v1/admin/permissions/USER_READ/history"
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
                    .value("原本名稱")
            )
            .andExpect(
                jsonPath(
                    "$.content[0].newState.name"
                )
                    .value("系統更新後名稱")
            )
            .andExpect(
                jsonPath(
                    "$.content[0].previousState.note"
                )
                    .value("原本備註")
            )
            .andExpect(
                jsonPath(
                    "$.content[0].newState.note"
                )
                    .value("系統更新")
            )
            .andExpect(
                jsonPath("$.content[0].changedDate")
                    .value("2026-11-21T14:00:00")
            );
    }

    @Test
    void permissionChangeHistoryUsesIdAsSecondarySort()
        throws Exception {

        jdbcTemplate.update(
            """
            INSERT INTO permission_change_logs (
                id,
                permission_id,
                permission_change_type_id,
                changed_by_user_id,
                previous_permission_type_id,
                new_permission_type_id,
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
                    2,
                    2,
                    '第一版',
                    '第二版',
                    TRUE,
                    TRUE,
                    TIMESTAMP '2026-11-21 13:00:00'
                ),
                (
                    2,
                    2,
                    2,
                    100,
                    2,
                    2,
                    '第二版',
                    '第三版',
                    TRUE,
                    TRUE,
                    TIMESTAMP '2026-11-21 13:00:00'
                )
            """
        );

        String accessToken =
            loginAsPermissionManager();

        mockMvc.perform(
                get(
                    "/api/v1/admin/permissions/USER_READ/history"
                )
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
                    )
                    .param("page", "0")
                    .param("size", "20")
                    .accept(
                        MediaType.APPLICATION_JSON
                    )
            )
            .andExpect(status().isOk())
            .andExpect(
                jsonPath("$.content.length()")
                    .value(2)
            )
            .andExpect(
                jsonPath("$.content[0].id")
                    .value(2)
            )
            .andExpect(
                jsonPath(
                    "$.content[0].newState.name"
                )
                    .value("第三版")
            )
            .andExpect(
                jsonPath("$.content[1].id")
                    .value(1)
            )
            .andExpect(
                jsonPath(
                    "$.content[1].newState.name"
                )
                    .value("第二版")
            );
    }

    @Test
    void updatePermissionRollsBackWhenAuditLogCannotBeCreated()
        throws Exception {

        String accessToken =
            loginAsPermissionManager();

        /*
        * 模擬系統設定錯誤：
        * UPDATED 異動類型被停用，
        * 因此稽核紀錄無法建立。
        */
        jdbcTemplate.update(
            """
            UPDATE permission_change_types
            SET is_active = FALSE
            WHERE code = 'UPDATED'
            """
        );

        String requestBody = """
            {
            "permissionTypeCode": "AUTHORIZATION",
            "name": "不應儲存的新名稱",
            "note": "這次修改應該完整回滾"
            }
            """;

        String requestPath =
            "/api/v1/admin/permissions/USER_READ";

        mockMvc.perform(
                put(requestPath)
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + accessToken
                    )
                    .contentType(MediaType.APPLICATION_JSON)
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
                    .value("系統發生未預期的錯誤")
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
        * Permission 的名稱必須維持原值，
        * 證明 update 已經回滾。
        */
        String permissionName =
            jdbcTemplate.queryForObject(
                """
                SELECT name
                FROM permissions
                WHERE code = 'USER_READ'
                """,
                String.class
            );

        assertThat(permissionName)
            .isEqualTo("查詢會員資料");

        /*
        * PermissionType 也不能被修改。
        * USER_MANAGEMENT 的測試資料 ID 是 2。
        */
        Long permissionTypeId =
            jdbcTemplate.queryForObject(
                """
                SELECT permission_type_id
                FROM permissions
                WHERE code = 'USER_READ'
                """,
                Long.class
            );

        assertThat(permissionTypeId)
            .isEqualTo(2L);

        /*
        * 失敗操作不應留下不完整的歷程。
        */
        Long changeLogCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM permission_change_logs
                WHERE permission_id = 2
                """,
                Long.class
            );

        assertThat(changeLogCount)
            .isZero();
    }

    @Test
    void permissionChangeAuditServiceSupportsSystemChangeWithoutUser() {

        Permission permission =
            permissionRepository
                .findByCodeIgnoreCase("USER_READ")
                .orElseThrow();

        var previousState =
            permissionChangeAuditService.capture(
                permission
            );

        /*
        * 模擬系統自動修改後的狀態。
        * 這裡不需要儲存 Permission，
        * 只是在測試稽核 Service 擷取的新快照。
        */
        permission.updateDetails(
            permission.getPermissionType(),
            "系統更新後名稱",
            "由系統自動更新"
        );

        permissionChangeAuditService.recordChanged(
            permission,
            null,
            PermissionChangeTypeCodes.UPDATED,
            previousState
        );

        Long logCount =
            jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM permission_change_logs
                WHERE permission_id = 2
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
                FROM permission_change_logs
                WHERE permission_id = 2
                """,
                String.class
            );

        assertThat(previousName)
            .isEqualTo("查詢會員資料");

        String newName =
            jdbcTemplate.queryForObject(
                """
                SELECT new_name
                FROM permission_change_logs
                WHERE permission_id = 2
                """,
                String.class
            );

        assertThat(newName)
            .isEqualTo("系統更新後名稱");

        String newNote =
            jdbcTemplate.queryForObject(
                """
                SELECT new_note
                FROM permission_change_logs
                WHERE permission_id = 2
                """,
                String.class
            );

        assertThat(newNote)
            .isEqualTo("由系統自動更新");
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

    private String loginAsPermissionManager()
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
                21,
                3,
                'PERMISSION_MANAGE',
                '管理權限資料',
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

    private String loginAsSuperAdmin()
        throws Exception {

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
                6,
                'SUPER_ADMIN',
                '最高系統管理員',
                TRUE,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP
            )
            """
        );

        /*
        * 讓 SUPER_ADMIN 角色本身擁有
        * ROLE_PERMISSION_MANAGE。
        *
        * permission_id = 20
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
                101,
                6,
                20,
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

    private static Stream<Arguments>
        invalidRolePermissionHistoryPagination() {

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
        invalidRolePermissionHistoryParameterTypes() {

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
        invalidCreatePermissionRequests() {

        String missingPermissionTypeCode = """
            {
            "code": "USER_EMAIL_UPDATE",
            "name": "變更會員電子郵件",
            "note": "測試"
            }
            """;

        String invalidPermissionTypeCode = """
            {
            "permissionTypeCode": "USER-MANAGEMENT",
            "code": "USER_EMAIL_UPDATE",
            "name": "變更會員電子郵件",
            "note": "測試"
            }
            """;

        String missingPermissionCode = """
            {
            "permissionTypeCode": "USER_MANAGEMENT",
            "name": "變更會員電子郵件",
            "note": "測試"
            }
            """;

        String invalidPermissionCode = """
            {
            "permissionTypeCode": "USER_MANAGEMENT",
            "code": "USER-EMAIL-UPDATE",
            "name": "變更會員電子郵件",
            "note": "測試"
            }
            """;

        String blankName = """
            {
            "permissionTypeCode": "USER_MANAGEMENT",
            "code": "USER_EMAIL_UPDATE",
            "name": "   ",
            "note": "測試"
            }
            """;

        String oversizedNote = """
            {
            "permissionTypeCode": "USER_MANAGEMENT",
            "code": "USER_EMAIL_UPDATE",
            "name": "變更會員電子郵件",
            "note": "%s"
            }
            """.formatted(
                "A".repeat(256)
            );

        return Stream.of(
            Arguments.of(
                missingPermissionTypeCode,
                "permissionTypeCode",
                "權限類型代碼不得為空"
            ),
            Arguments.of(
                invalidPermissionTypeCode,
                "permissionTypeCode",
                "權限類型代碼格式不正確"
            ),
            Arguments.of(
                missingPermissionCode,
                "code",
                "權限代碼不得為空"
            ),
            Arguments.of(
                invalidPermissionCode,
                "code",
                "權限代碼格式不正確"
            ),
            Arguments.of(
                blankName,
                "name",
                "權限名稱不得為空"
            ),
            Arguments.of(
                oversizedNote,
                "note",
                "權限備註長度不得超過 255"
            )
        );
    }

    private static Stream<Arguments>
        invalidUpdatePermissionRequests() {

        String missingPermissionTypeCode = """
            {
            "name": "新的權限名稱",
            "note": "測試"
            }
            """;

        String invalidPermissionTypeCode = """
            {
            "permissionTypeCode": "USER-MANAGEMENT",
            "name": "新的權限名稱",
            "note": "測試"
            }
            """;

        String blankName = """
            {
            "permissionTypeCode": "USER_MANAGEMENT",
            "name": "   ",
            "note": "測試"
            }
            """;

        String oversizedNote = """
            {
            "permissionTypeCode": "USER_MANAGEMENT",
            "name": "新的權限名稱",
            "note": "%s"
            }
            """.formatted(
                "A".repeat(256)
            );

        return Stream.of(
            Arguments.of(
                missingPermissionTypeCode,
                "permissionTypeCode",
                "權限類型代碼不得為空"
            ),
            Arguments.of(
                invalidPermissionTypeCode,
                "permissionTypeCode",
                "權限類型代碼格式不正確"
            ),
            Arguments.of(
                blankName,
                "name",
                "權限名稱不得為空"
            ),
            Arguments.of(
                oversizedNote,
                "note",
                "權限備註長度不得超過 255"
            )
        );
    }

    private static Stream<Arguments>
        invalidPermissionChangeHistoryPagination() {

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
        invalidPermissionChangeHistoryParameterTypes() {

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
