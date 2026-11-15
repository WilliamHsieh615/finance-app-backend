package com.williamhsieh.financeapp
    .identity.authorization.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.williamhsieh.financeapp
    .identity.authorization.entity
    .RolePermissionAssignmentLog;

public interface
    RolePermissionAssignmentLogRepository
    extends JpaRepository<
        RolePermissionAssignmentLog,
        Long
    > {

    @EntityGraph(
        attributePaths = {
            "permission",
            "permission.permissionType",
            "assignmentType",
            "changedByUser"
        }
    )
    Page<RolePermissionAssignmentLog>
        findAllByRole_IdOrderByChangedDateDescIdDesc(
            Long roleId,
            Pageable pageable
        );
}
