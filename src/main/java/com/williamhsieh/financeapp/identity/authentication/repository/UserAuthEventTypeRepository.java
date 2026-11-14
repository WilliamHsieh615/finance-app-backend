package com.williamhsieh.financeapp.identity.authentication.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.williamhsieh.financeapp.identity.authentication.entity.UserAuthEventType;

public interface UserAuthEventTypeRepository
    extends JpaRepository<UserAuthEventType, Long> {

    Optional<UserAuthEventType>
        findByCodeAndActiveTrueAndDeletedDateIsNull(String code);
}
