package com.williamhsieh.financeapp.identity.authorization.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.williamhsieh.financeapp.identity.authorization.entity.UserRoleAssignmentLog;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;

public interface UserRoleAssignmentLogRepository
    extends JpaRepository<
        UserRoleAssignmentLog,
        Long
    > {

    @EntityGraph(
        attributePaths = {
            "role",
            "assignmentType",
            "changedByUser"
        }
    )
    Page<UserRoleAssignmentLog>
        findAllByUser_IdOrderByChangedDateDescIdDesc(
            Long userId,
            Pageable pageable
        );
}
