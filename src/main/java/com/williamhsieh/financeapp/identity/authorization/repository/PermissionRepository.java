package com.williamhsieh.financeapp.identity.authorization.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.williamhsieh.financeapp.identity.authorization.entity.Permission;

public interface PermissionRepository
    extends JpaRepository<Permission, Long> {

    Optional<Permission>
        findByCodeIgnoreCaseAndActiveTrueAndDeletedDateIsNull(
            String code
        );
}
