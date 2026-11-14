package com.williamhsieh.financeapp.repository.auth;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.williamhsieh.financeapp.entity.auth.UserRoleAssignmentType;

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