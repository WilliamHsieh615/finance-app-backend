package com.williamhsieh.financeapp.identity.authorization.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.williamhsieh.financeapp.identity.authorization.entity.PermissionChangeType;

public interface PermissionChangeTypeRepository
    extends JpaRepository<
        PermissionChangeType,
        Long
    > {

    Optional<PermissionChangeType>
        findByCodeAndActiveTrueAndDeletedDateIsNull(
            String code
        );
}
