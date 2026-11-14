package com.williamhsieh.financeapp.repository.auth;

import java.util.Optional;
import java.util.List;

import jakarta.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.williamhsieh.financeapp.entity.auth.Role;

public interface RoleRepository
    extends JpaRepository<Role, Long> {

    Optional<Role>
        findByCodeIgnoreCaseAndActiveTrueAndDeletedDateIsNull(
            String code
        );

    List<Role>
        findAllByActiveTrueAndDeletedDateIsNullOrderByCodeAsc();

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(
        """
        SELECT role
        FROM Role role
        WHERE LOWER(role.code) =
            LOWER(:roleCode)
        AND role.active = true
        AND role.deletedDate IS NULL
        """
    )
    Optional<Role> findActiveRoleByCodeForUpdate(
        @Param("roleCode") String roleCode
    );
}
