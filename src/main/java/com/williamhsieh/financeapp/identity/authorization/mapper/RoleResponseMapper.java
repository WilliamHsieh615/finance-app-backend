package com.williamhsieh.financeapp.identity.authorization.mapper;

import org.springframework.stereotype.Component;

import com.williamhsieh.financeapp.identity.authorization.dto.RoleSummaryResponse;
import com.williamhsieh.financeapp.identity.authorization.entity.Role;
import com.williamhsieh.financeapp.identity.authorization.dto.RoleDetailResponse;

@Component
public class RoleResponseMapper {

    public RoleSummaryResponse toSummary(
        Role role
    ) {
        return new RoleSummaryResponse(
            role.getId(),
            role.getCode(),
            role.getName(),
            role.getNote()
        );
    }

    public RoleDetailResponse toDetail(
        Role role
    ) {
        return new RoleDetailResponse(
            role.getId(),
            role.getCode(),
            role.getName(),
            role.getNote(),
            role.isActive(),
            role.getCreatedDate(),
            role.getUpdatedDate(),
            role.getDeletedDate()
        );
    }
}
