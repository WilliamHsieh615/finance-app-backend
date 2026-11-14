package com.williamhsieh.financeapp.repository.auth;

import java.util.List;
import java.util.Optional;

import jakarta.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Lock;

import com.williamhsieh.financeapp.entity.auth.UserRole;
import com.williamhsieh.financeapp.entity.auth.Role;

public interface UserRoleRepository
    extends JpaRepository<UserRole, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(
        """
        SELECT userRole
        FROM UserRole userRole
        WHERE userRole.user.id = :userId
        AND userRole.role.id = :roleId
        """
    )
    Optional<UserRole> findByUserIdAndRoleIdForUpdate(
        @Param("userId") Long userId,
        @Param("roleId") Long roleId
    );

    @Query(
        """
        SELECT DISTINCT role.code
        FROM UserRole userRole
        JOIN userRole.role role
        WHERE userRole.user.id = :userId
          AND userRole.active = true
          AND userRole.deletedDate IS NULL
          AND role.active = true
          AND role.deletedDate IS NULL
        ORDER BY role.code
        """
    )
    List<String> findActiveRoleCodesByUserId(
        @Param("userId") Long userId
    );

    @Query(
        """
        SELECT role
        FROM UserRole userRole
        JOIN userRole.role role
        WHERE userRole.user.id = :userId
        AND userRole.active = true
        AND userRole.deletedDate IS NULL
        AND role.active = true
        AND role.deletedDate IS NULL
        ORDER BY role.code
        """
    )
    List<Role> findActiveRolesByUserId(
        @Param("userId") Long userId
    );

    long countByRole_IdAndActiveTrueAndDeletedDateIsNull(
        Long roleId
    );
}
