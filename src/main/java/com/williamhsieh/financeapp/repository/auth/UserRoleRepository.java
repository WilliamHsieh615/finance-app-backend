package com.williamhsieh.financeapp.repository.auth;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.williamhsieh.financeapp.entity.auth.UserRole;

public interface UserRoleRepository
    extends JpaRepository<UserRole, Long> {

    boolean
        existsByUser_IdAndRole_IdAndActiveTrueAndDeletedDateIsNull(
            Long userId,
            Long roleId
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
}
