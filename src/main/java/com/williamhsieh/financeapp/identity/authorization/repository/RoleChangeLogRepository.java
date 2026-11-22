package com.williamhsieh.financeapp.identity.authorization.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.williamhsieh.financeapp
    .identity.authorization.entity.RoleChangeLog;

public interface RoleChangeLogRepository
    extends JpaRepository<RoleChangeLog, Long> {

    @EntityGraph(
        attributePaths = {
            "role",
            "changeType",
            "changedByUser"
        }
    )
    Page<RoleChangeLog>
        findAllByRole_IdOrderByChangedDateDescIdDesc(
            Long roleId,
            Pageable pageable
        );
}
