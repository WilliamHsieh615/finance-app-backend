package com.williamhsieh.financeapp.identity.authorization.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.williamhsieh.financeapp.identity.authorization.entity.PermissionType;

public interface PermissionTypeRepository
    extends JpaRepository<PermissionType, Long> {

    Optional<PermissionType>
        findByCodeIgnoreCaseAndActiveTrueAndDeletedDateIsNull(
            String code
        );

    List<PermissionType>
        findAllByActiveTrueAndDeletedDateIsNullOrderByCodeAsc();
}
