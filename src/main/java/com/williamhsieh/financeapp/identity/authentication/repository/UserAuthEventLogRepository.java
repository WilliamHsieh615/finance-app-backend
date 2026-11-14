package com.williamhsieh.financeapp.identity.authentication.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.williamhsieh.financeapp.identity.authentication.entity.UserAuthEventLog;

public interface UserAuthEventLogRepository
    extends JpaRepository<UserAuthEventLog, Long> {

    List<UserAuthEventLog>
        findAllByUser_IdOrderByEventTimeDesc(Long userId);

    List<UserAuthEventLog>
        findAllBySessionIdOrderByEventTimeDesc(String sessionId);
}
