package com.williamhsieh.financeapp.repository.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.williamhsieh.financeapp.entity.user.UserAccountStatus;

public interface UserAccountStatusRepository
    extends JpaRepository<UserAccountStatus, Long> {

    Optional<UserAccountStatus>
        findByCodeAndActiveTrueAndDeletedDateIsNull(
            String code
        );
}
