package com.williamhsieh.financeapp.identity.authorization.service;

import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.williamhsieh.financeapp.identity.authorization.constant.PermissionCodes;
import com.williamhsieh.financeapp.identity.authorization.dto.PermissionSummaryResponse;
import com.williamhsieh.financeapp.identity.authorization.repository.PermissionRepository;
import com.williamhsieh.financeapp.identity.authorization.mapper.PermissionResponseMapper;
import com.williamhsieh.financeapp
    .identity.authorization.dto
    .CreatePermissionRequest;
import com.williamhsieh.financeapp
    .identity.authorization.entity
    .Permission;
import com.williamhsieh.financeapp
    .identity.authorization.entity
    .PermissionType;
import com.williamhsieh.financeapp
    .identity.authorization.repository
    .PermissionTypeRepository;
import com.williamhsieh.financeapp.shared.exception.BusinessException;
import com.williamhsieh.financeapp
    .identity.authorization.dto
    .UpdatePermissionRequest;

@Service
public class PermissionManagementService {

    private final PermissionRepository
        permissionRepository;
    private final PermissionAccessPolicy
        permissionAccessPolicy;
    private final PermissionResponseMapper
        permissionResponseMapper;
    private final PermissionTypeRepository
        permissionTypeRepository;

    public PermissionManagementService(
        PermissionRepository permissionRepository,
        PermissionAccessPolicy permissionAccessPolicy,
        PermissionResponseMapper permissionResponseMapper,
        PermissionTypeRepository permissionTypeRepository
    ) {
        this.permissionRepository =
            permissionRepository;
        this.permissionAccessPolicy =
            permissionAccessPolicy;
        this.permissionResponseMapper =
            permissionResponseMapper;
        this.permissionTypeRepository =
            permissionTypeRepository;
    }

    @Transactional(readOnly = true)
    public List<PermissionSummaryResponse>
        getActivePermissions(
            Long authenticatedUserId
        ) {
            permissionAccessPolicy
                .requirePermission(
                    authenticatedUserId,
                    PermissionCodes.PERMISSION_READ
                );

            List<PermissionSummaryResponse> permissions =
                permissionRepository
                    .findAllActiveOrderByTypeAndCode()
                    .stream()
                    .map(
                        permissionResponseMapper::toSummary
                    )
                    .toList();

            return List.copyOf(permissions);
        }

    @Transactional
    public PermissionSummaryResponse createPermission(
        Long authenticatedUserId,
        CreatePermissionRequest request
    ) {
        permissionAccessPolicy.requirePermission(
            authenticatedUserId,
            PermissionCodes.PERMISSION_MANAGE
        );

        String normalizedPermissionTypeCode =
            request.permissionTypeCode()
                .trim()
                .toUpperCase(Locale.ROOT);

        String normalizedPermissionCode =
            request.code()
                .trim()
                .toUpperCase(Locale.ROOT);

        String normalizedName =
            request.name().trim();

        String normalizedNote =
            normalizeOptionalNote(
                request.note()
            );

        PermissionType permissionType =
            permissionTypeRepository
                .findByCodeIgnoreCaseAndActiveTrueAndDeletedDateIsNull(
                    normalizedPermissionTypeCode
                )
                .orElseThrow(() ->
                    new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "找不到指定的權限類型"
                    )
                );

        permissionRepository
            .findByCodeIgnoreCase(
                normalizedPermissionCode
            )
            .ifPresent(existingPermission -> {
                throw new BusinessException(
                    HttpStatus.CONFLICT,
                    "PERMISSION_CODE_ALREADY_EXISTS",
                    "權限代碼已存在"
                );
            });

        Permission permission =
            new Permission(
                permissionType,
                normalizedPermissionCode,
                normalizedName,
                normalizedNote
            );

        Permission savedPermission =
            permissionRepository.save(
                permission
            );

        return permissionResponseMapper.toSummary(
            savedPermission
        );
    }

    @Transactional
    public PermissionSummaryResponse updatePermission(
        Long authenticatedUserId,
        String permissionCode,
        UpdatePermissionRequest request
    ) {
        permissionAccessPolicy.requirePermission(
            authenticatedUserId,
            PermissionCodes.PERMISSION_MANAGE
        );

        String normalizedPermissionCode =
            permissionCode
                .trim()
                .toUpperCase(Locale.ROOT);

        String normalizedPermissionTypeCode =
            request.permissionTypeCode()
                .trim()
                .toUpperCase(Locale.ROOT);

        String normalizedName =
            request.name().trim();

        String normalizedNote =
            normalizeOptionalNote(
                request.note()
            );

        Permission permission =
            permissionRepository
                .findActiveByCodeIgnoreCase(
                    normalizedPermissionCode
                )
                .orElseThrow(() ->
                    new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "找不到指定的權限"
                    )
                );

        PermissionType permissionType =
            permissionTypeRepository
                .findByCodeIgnoreCaseAndActiveTrueAndDeletedDateIsNull(
                    normalizedPermissionTypeCode
                )
                .orElseThrow(() ->
                    new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "找不到指定的權限類型"
                    )
                );

        permission.updateDetails(
            permissionType,
            normalizedName,
            normalizedNote
        );

        Permission savedPermission =
            permissionRepository.save(
                permission
            );

        return permissionResponseMapper.toSummary(
            savedPermission
        );
    }

    @Transactional
    public void deactivatePermission(
        Long authenticatedUserId,
        String permissionCode
    ) {
        permissionAccessPolicy.requirePermission(
            authenticatedUserId,
            PermissionCodes.PERMISSION_MANAGE
        );

        String normalizedPermissionCode =
            permissionCode
                .trim()
                .toUpperCase(Locale.ROOT);

        Permission permission =
            permissionRepository
                .findActiveByCodeIgnoreCase(
                    normalizedPermissionCode
                )
                .orElseThrow(() ->
                    new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "找不到指定的權限"
                    )
                );

        if (
            PermissionCodes.PERMISSION_MANAGE.equals(
                permission.getCode()
            ) ||
            PermissionCodes.ROLE_PERMISSION_MANAGE.equals(
                permission.getCode()
            )
        ) {
            throw new BusinessException(
                HttpStatus.CONFLICT,
                "PROTECTED_PERMISSION_CANNOT_BE_DEACTIVATED",
                "不可停用系統核心管理權限"
            );
        }

        permission.deactivate();

        permissionRepository.save(
            permission
        );
    }

    @Transactional
    public PermissionSummaryResponse restorePermission(
        Long authenticatedUserId,
        String permissionCode
    ) {
        permissionAccessPolicy.requirePermission(
            authenticatedUserId,
            PermissionCodes.PERMISSION_MANAGE
        );

        String normalizedPermissionCode =
            permissionCode
                .trim()
                .toUpperCase(Locale.ROOT);

        /*
        * 恢復時必須查詢所有狀態，
        * 不能使用只查有效資料的方法。
        */
        Permission permission =
            permissionRepository
                .findByCodeIgnoreCase(
                    normalizedPermissionCode
                )
                .orElseThrow(() ->
                    new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "找不到指定的權限"
                    )
                );

        if (permission.isAvailable()) {
            throw new BusinessException(
                HttpStatus.CONFLICT,
                "PERMISSION_ALREADY_ACTIVE",
                "權限目前已經是啟用狀態"
            );
        }

        PermissionType permissionType =
            permission.getPermissionType();

        if (
            !permissionType.isActive() ||
            permissionType.getDeletedDate() != null
        ) {
            throw new BusinessException(
                HttpStatus.CONFLICT,
                "PERMISSION_TYPE_NOT_AVAILABLE",
                "權限所屬類型目前無法使用"
            );
        }

        permission.activate();

        Permission savedPermission =
            permissionRepository.save(
                permission
            );

        return permissionResponseMapper.toSummary(
            savedPermission
        );
    }

    private String normalizeOptionalNote(
        String note
    ) {
        if (note == null || note.isBlank()) {
            return null;
        }

        return note.trim();
    }
}
