package com.williamhsieh.financeapp.identity.user.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.williamhsieh.financeapp.identity.user.entity.UserAccountStatusLog;

import org.springframework.data.jpa.repository.EntityGraph;

public interface UserAccountStatusLogRepository
    extends JpaRepository<UserAccountStatusLog, Long> {
    
    @EntityGraph(
        attributePaths = {
            "previousStatus",
            "newStatus",
            "changedByUser"
        }
    )    
    Page<UserAccountStatusLog>
        findAllByUser_IdOrderByChangedDateDescIdDesc(
            Long userId,
            Pageable pageable
        );
}
