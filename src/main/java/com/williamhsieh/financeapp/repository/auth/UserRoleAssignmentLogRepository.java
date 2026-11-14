package com.williamhsieh.financeapp.repository.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;

import com.williamhsieh.financeapp.entity.auth.UserRoleAssignmentLog;

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
