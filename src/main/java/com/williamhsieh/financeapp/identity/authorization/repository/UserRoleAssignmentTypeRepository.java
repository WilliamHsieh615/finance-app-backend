package com.williamhsieh.financeapp.identity.authorization.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.williamhsieh.financeapp.identity.authorization.entity.UserRoleAssignmentType;

public interface UserRoleAssignmentTypeRepository
    extends JpaRepository<
        UserRoleAssignmentType,
        Long
    > {

    Optional<UserRoleAssignmentType>
        findByCodeAndActiveTrueAndDeletedDateIsNull(
            String code
        );
}