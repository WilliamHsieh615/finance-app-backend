package com.williamhsieh.financeapp.identity.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.williamhsieh.financeapp.identity.user.entity.UserAccountStatus;

public interface UserAccountStatusRepository
    extends JpaRepository<UserAccountStatus, Long> {

    Optional<UserAccountStatus>
        findByCodeAndActiveTrueAndDeletedDateIsNull(
            String code
        );
}
