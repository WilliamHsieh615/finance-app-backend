package com.williamhsieh.financeapp.repository.auth;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.williamhsieh.financeapp.entity.auth.PermissionType;

public interface PermissionTypeRepository
    extends JpaRepository<PermissionType, Long> {

    Optional<PermissionType>
        findByCodeIgnoreCaseAndActiveTrueAndDeletedDateIsNull(
            String code
        );
}
