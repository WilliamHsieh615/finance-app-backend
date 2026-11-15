package com.williamhsieh.financeapp.identity.authorization.repository;

import java.util.List;
import java.util.Optional;

import jakarta.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Lock;

import com.williamhsieh.financeapp.identity.authorization.entity.RolePermission;
import com.williamhsieh.financeapp.identity.authorization.entity.Permission;

public interface RolePermissionRepository
    extends JpaRepository<RolePermission, Long> {

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

    @Query(
        """
        SELECT DISTINCT permission
        FROM RolePermission rolePermission
        JOIN rolePermission.role role
        JOIN rolePermission.permission permission
        JOIN FETCH permission.permissionType permissionType
        WHERE LOWER(role.code) =
              LOWER(:roleCode)
          AND role.active = true
          AND role.deletedDate IS NULL
          AND rolePermission.active = true
          AND rolePermission.deletedDate IS NULL
          AND permission.active = true
          AND permission.deletedDate IS NULL
          AND permissionType.active = true
          AND permissionType.deletedDate IS NULL
        ORDER BY
            permissionType.code ASC,
            permission.code ASC
        """
    )
    List<Permission>
        findActivePermissionsByRoleCode(
            @Param("roleCode") String roleCode
        );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(
        """
        SELECT rolePermission
        FROM RolePermission rolePermission
        WHERE rolePermission.role.id = :roleId
        AND rolePermission.permission.id =
            :permissionId
        """
    )
    Optional<RolePermission>
        findByRoleIdAndPermissionIdForUpdate(
            @Param("roleId") Long roleId,
            @Param("permissionId") Long permissionId
        );
}
