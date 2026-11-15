package com.williamhsieh.financeapp.identity.authorization.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.williamhsieh.financeapp.identity.authorization.entity.RolePermissionAssignmentType;

public interface
    RolePermissionAssignmentTypeRepository
    extends JpaRepository<
        RolePermissionAssignmentType,
        Long
    > {

    Optional<RolePermissionAssignmentType>
        findByCodeAndActiveTrueAndDeletedDateIsNull(
            String code
        );
}
