package com.williamhsieh.financeapp.repository.auth;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.williamhsieh.financeapp.entity.auth.RolePermission;

public interface RolePermissionRepository
    extends JpaRepository<RolePermission, Long> {

    boolean
        existsByRole_IdAndPermission_IdAndActiveTrueAndDeletedDateIsNull(
            Long roleId,
            Long permissionId
        );

    @Query(
        """
        SELECT DISTINCT permission.code
        FROM RolePermission rolePermission
        JOIN rolePermission.role role
        JOIN rolePermission.permission permission
        JOIN permission.permissionType permissionType
        WHERE role.id IN (
            SELECT userRole.role.id
            FROM UserRole userRole
            WHERE userRole.user.id = :userId
              AND userRole.active = true
              AND userRole.deletedDate IS NULL
              AND userRole.role.active = true
              AND userRole.role.deletedDate IS NULL
        )
          AND role.active = true
          AND role.deletedDate IS NULL
          AND rolePermission.active = true
          AND rolePermission.deletedDate IS NULL
          AND permission.active = true
          AND permission.deletedDate IS NULL
          AND permissionType.active = true
          AND permissionType.deletedDate IS NULL
        ORDER BY permission.code
        """
    )
    List<String> findActivePermissionCodesByUserId(
        @Param("userId") Long userId
    );
}
