package com.williamhsieh.financeapp.repository.auth;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.williamhsieh.financeapp.entity.auth.Role;

public interface RoleRepository
    extends JpaRepository<Role, Long> {

    Optional<Role>
        findByCodeIgnoreCaseAndActiveTrueAndDeletedDateIsNull(
            String code
        );
}
