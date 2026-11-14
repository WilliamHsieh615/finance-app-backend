package com.williamhsieh.financeapp.identity.authentication.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.williamhsieh.financeapp.identity.authentication.entity.UserLoginStatus;

public interface UserLoginStatusRepository
    extends JpaRepository<UserLoginStatus, Long> {

    Optional<UserLoginStatus>
        findByCodeAndActiveTrueAndDeletedDateIsNull(
            String code
        );
}