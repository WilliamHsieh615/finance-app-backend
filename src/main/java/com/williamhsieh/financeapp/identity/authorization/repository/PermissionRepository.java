package com.williamhsieh.financeapp.identity.authorization.repository;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.williamhsieh.financeapp.identity.authorization.entity.Permission;

public interface PermissionRepository
    extends JpaRepository<Permission, Long> {

    @EntityGraph(
        attributePaths = {
            "permissionType"
        }
    )
    Optional<Permission> findByCodeIgnoreCase(
        String code
    );
    
    @EntityGraph(
        attributePaths = {
            "permissionType"
        }
    )
    @Query(
        """
        SELECT permission
        FROM Permission permission
        JOIN permission.permissionType permissionType
        WHERE LOWER(permission.code) =
            LOWER(:permissionCode)
        AND permission.active = true
        AND permission.deletedDate IS NULL
        AND permissionType.active = true
        AND permissionType.deletedDate IS NULL
        """
    )
    Optional<Permission>
        findActiveByCodeIgnoreCase(
            @Param("permissionCode")
            String permissionCode
        );

    @EntityGraph(
        attributePaths = {
            "permissionType"
        }
    )
    @Query(
        """
        SELECT permission
        FROM Permission permission
        JOIN permission.permissionType permissionType
        WHERE permission.active = true
        AND permission.deletedDate IS NULL
        AND permissionType.active = true
        AND permissionType.deletedDate IS NULL
        ORDER BY
            permissionType.code ASC,
            permission.code ASC
        """
    )
    List<Permission>
        findAllActiveOrderByTypeAndCode();
}
