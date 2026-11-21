package com.williamhsieh.financeapp.identity.authorization.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.williamhsieh.financeapp.identity.authorization.entity.PermissionChangeLog;

public interface PermissionChangeLogRepository
    extends JpaRepository<
        PermissionChangeLog,
        Long
    > {

    @EntityGraph(
        attributePaths = {
            "permission",
            "changeType",
            "changedByUser",
            "previousPermissionType",
            "newPermissionType"
        }
    )
    Page<PermissionChangeLog>
        findAllByPermission_IdOrderByChangedDateDescIdDesc(
            Long permissionId,
            Pageable pageable
        );
}
