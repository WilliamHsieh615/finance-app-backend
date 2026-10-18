package com.williamhsieh.financeapp.repository.user;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.williamhsieh.financeapp.entity.user.UserAccountStatusLog;

public interface UserAccountStatusLogRepository
    extends JpaRepository<UserAccountStatusLog, Long> {

    List<UserAccountStatusLog>
        findAllByUser_IdOrderByChangedDateDescIdDesc(
            Long userId
        );
}
