package com.williamhsieh.financeapp.identity.authorization.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.williamhsieh.financeapp.identity.authorization.constant.PermissionCodes;
import com.williamhsieh.financeapp.identity.authorization.dto.PermissionTypeSummaryResponse;
import com.williamhsieh.financeapp.identity.authorization.repository.PermissionTypeRepository;

@Service
public class PermissionTypeQueryService {

    private final PermissionTypeRepository
        permissionTypeRepository;

    private final PermissionAccessPolicy
        permissionAccessPolicy;

    public PermissionTypeQueryService(
        PermissionTypeRepository permissionTypeRepository,
        PermissionAccessPolicy permissionAccessPolicy
    ) {
        this.permissionTypeRepository =
            permissionTypeRepository;

        this.permissionAccessPolicy =
            permissionAccessPolicy;
    }

    @Transactional(readOnly = true)
    public List<PermissionTypeSummaryResponse>
        getActivePermissionTypes(
            Long authenticatedUserId
        ) {

        permissionAccessPolicy.requirePermission(
            authenticatedUserId,
            PermissionCodes.PERMISSION_READ
        );

        List<PermissionTypeSummaryResponse>
            permissionTypes =
                permissionTypeRepository
                    .findAllByActiveTrueAndDeletedDateIsNullOrderByCodeAsc()
                    .stream()
                    .map(permissionType ->
                        new PermissionTypeSummaryResponse(
                            permissionType.getId(),
                            permissionType.getCode(),
                            permissionType.getName(),
                            permissionType.getNote()
                        )
                    )
                    .toList();

        return List.copyOf(permissionTypes);
    }
}
