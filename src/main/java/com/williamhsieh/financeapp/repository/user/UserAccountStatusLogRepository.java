package com.williamhsieh.financeapp.repository.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;

import com.williamhsieh.financeapp.entity.user.UserAccountStatusLog;

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
