package com.williamhsieh.financeapp.identity.authorization.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.williamhsieh.financeapp
    .identity.authorization.entity.RoleChangeType;

public interface RoleChangeTypeRepository
    extends JpaRepository<RoleChangeType, Long> {

    Optional<RoleChangeType>
        findByCodeAndActiveTrueAndDeletedDateIsNull(
            String code
        );
}
