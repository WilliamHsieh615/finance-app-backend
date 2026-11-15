package com.williamhsieh.financeapp.identity.authorization.mapper;

import org.springframework.stereotype.Component;

import com.williamhsieh.financeapp.identity.authorization.dto.PermissionSummaryResponse;
import com.williamhsieh.financeapp.identity.authorization.dto.PermissionSummaryResponse.PermissionTypeInfo;
import com.williamhsieh.financeapp.identity.authorization.entity.Permission;
import com.williamhsieh.financeapp.identity.authorization.entity.PermissionType;

@Component
public class PermissionResponseMapper {

    public PermissionSummaryResponse toSummary(
        Permission permission
    ) {
        PermissionType permissionType =
            permission.getPermissionType();

        PermissionTypeInfo permissionTypeInfo =
            new PermissionTypeInfo(
                permissionType.getId(),
                permissionType.getCode(),
                permissionType.getName()
            );

        return new PermissionSummaryResponse(
            permission.getId(),
            permissionTypeInfo,
            permission.getCode(),
            permission.getName(),
            permission.getNote()
        );
    }
}
