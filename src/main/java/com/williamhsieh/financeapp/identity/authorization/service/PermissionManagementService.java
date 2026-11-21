package com.williamhsieh.financeapp.identity.authorization.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.williamhsieh.financeapp.identity.authorization.constant.PermissionCodes;
import com.williamhsieh.financeapp.identity.authorization.dto.PermissionSummaryResponse;
import com.williamhsieh.financeapp.identity.authorization.repository.PermissionRepository;
import com.williamhsieh.financeapp.identity.authorization.mapper.PermissionResponseMapper;
import com.williamhsieh.financeapp.identity.authorization.dto.CreatePermissionRequest;
import com.williamhsieh.financeapp.identity.authorization.entity.Permission;
import com.williamhsieh.financeapp.identity.authorization.entity.PermissionType;
import com.williamhsieh.financeapp.identity.authorization.repository.PermissionTypeRepository;
import com.williamhsieh.financeapp.shared.exception.BusinessException;
import com.williamhsieh.financeapp.identity.authorization.dto.UpdatePermissionRequest;
import com.williamhsieh.financeapp.identity.authorization.dto.PermissionDetailResponse;
import com.williamhsieh.financeapp.identity.authorization.constant.PermissionChangeTypeCodes;
import com.williamhsieh.financeapp.identity.authorization.service.PermissionChangeAuditService.PermissionState;
import com.williamhsieh.financeapp.identity.authorization.dto.PermissionChangeLogResponse;
import com.williamhsieh.financeapp.identity.authorization.dto.PermissionChangeLogResponse.ChangeTypeInfo;
import com.williamhsieh.financeapp.identity.authorization.dto.PermissionChangeLogResponse.ChangedByUserInfo;
import com.williamhsieh.financeapp.identity.authorization.dto.PermissionChangeLogResponse.PermissionStateInfo;
import com.williamhsieh.financeapp.identity.authorization.dto.PermissionChangeLogResponse.PermissionTypeInfo;
import com.williamhsieh.financeapp.identity.authorization.entity.PermissionChangeLog;
import com.williamhsieh.financeapp.identity.authorization.entity.PermissionChangeType;
import com.williamhsieh.financeapp.identity.authorization.repository.PermissionChangeLogRepository;
import com.williamhsieh.financeapp.identity.user.entity.User;
import com.williamhsieh.financeapp.shared.dto.PageResponse;

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
    private final PermissionChangeAuditService
        permissionChangeAuditService;
    private final PermissionChangeLogRepository
        permissionChangeLogRepository;

    public PermissionManagementService(
        PermissionRepository permissionRepository,
        PermissionAccessPolicy permissionAccessPolicy,
        PermissionResponseMapper permissionResponseMapper,
        PermissionTypeRepository permissionTypeRepository,
        PermissionChangeAuditService permissionChangeAuditService,
        PermissionChangeLogRepository permissionChangeLogRepository
    ) {
        this.permissionRepository =
            permissionRepository;
        this.permissionAccessPolicy =
            permissionAccessPolicy;
        this.permissionResponseMapper =
            permissionResponseMapper;
        this.permissionTypeRepository =
            permissionTypeRepository;
        this.permissionChangeAuditService =
            permissionChangeAuditService;
        this.permissionChangeLogRepository =
            permissionChangeLogRepository;
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

        permissionChangeAuditService.recordCreated(
            savedPermission,
            authenticatedUserId,
            PermissionChangeTypeCodes.CREATED
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

        PermissionState previousState =
            permissionChangeAuditService.capture(
                permission
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

        permissionChangeAuditService.recordChanged(
            savedPermission,
            authenticatedUserId,
            PermissionChangeTypeCodes.UPDATED,
            previousState
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

        PermissionState previousState =
            permissionChangeAuditService.capture(
                permission
            );

        permission.deactivate();

        Permission savedPermission =
            permissionRepository.save(
                permission
            );

        permissionChangeAuditService.recordChanged(
            savedPermission,
            authenticatedUserId,
            PermissionChangeTypeCodes.DEACTIVATED,
            previousState
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

        PermissionState previousState =
            permissionChangeAuditService.capture(
                permission
            );

        permission.activate();

        Permission savedPermission =
            permissionRepository.save(
                permission
            );

        permissionChangeAuditService.recordChanged(
            savedPermission,
            authenticatedUserId,
            PermissionChangeTypeCodes.RESTORED,
            previousState
        );

        return permissionResponseMapper.toSummary(
            savedPermission
        );
    }

    @Transactional(readOnly = true)
    public List<PermissionDetailResponse>
        getDeactivatedPermissions(
            Long authenticatedUserId
        ) {
            permissionAccessPolicy.requirePermission(
                authenticatedUserId,
                PermissionCodes.PERMISSION_MANAGE
            );

            List<PermissionDetailResponse> permissions =
                permissionRepository
                    .findAllDeactivatedOrderByTypeAndCode()
                    .stream()
                    .map(
                        permissionResponseMapper::toDetail
                    )
                    .toList();

            return List.copyOf(permissions);
        }

    @Transactional(readOnly = true)
    public PageResponse<PermissionChangeLogResponse>
        getPermissionChangeHistory(
            Long authenticatedUserId,
            String permissionCode,
            int page,
            int size
        ) {
            permissionAccessPolicy.requirePermission(
                authenticatedUserId,
                PermissionCodes.PERMISSION_MANAGE
            );

            validatePagination(page, size);

            String normalizedPermissionCode =
                permissionCode
                    .trim()
                    .toUpperCase(Locale.ROOT);

            /*
            * 必須能查詢已停用 Permission 的歷程，
            * 所以不能使用 findActiveByCodeIgnoreCase。
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

            PageRequest pageRequest =
                PageRequest.of(
                    page,
                    size
                );

            Page<PermissionChangeLog> logs =
                permissionChangeLogRepository
                    .findAllByPermission_IdOrderByChangedDateDescIdDesc(
                        permission.getId(),
                        pageRequest
                    );

            Page<PermissionChangeLogResponse> result =
                logs.map(
                    this::toPermissionChangeLogResponse
                );

            return PageResponse.from(result);
        }
    
    private String normalizeOptionalNote(
        String note
    ) {
        if (note == null || note.isBlank()) {
            return null;
        }

        return note.trim();
    }

    private void validatePagination(
        int page,
        int size
    ) {
        if (page < 0) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "page 不得小於 0"
            );
        }

        if (size < 1 || size > 100) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "size 必須介於 1 到 100"
            );
        }
    }

    private PermissionChangeLogResponse
        toPermissionChangeLogResponse(
            PermissionChangeLog changeLog
        ) {
            PermissionChangeType changeType =
                changeLog.getChangeType();

            ChangeTypeInfo changeTypeInfo =
                new ChangeTypeInfo(
                    changeType.getId(),
                    changeType.getCode(),
                    changeType.getName()
                );

            User changedByUser =
                changeLog.getChangedByUser();

            ChangedByUserInfo changedByUserInfo =
                changedByUser == null
                    ? null
                    : new ChangedByUserInfo(
                        changedByUser.getId(),
                        changedByUser.getUserNumber(),
                        changedByUser.getName(),
                        changedByUser.getNickname()
                    );

            PermissionStateInfo previousState =
                toPermissionStateInfo(
                    changeLog.getPreviousPermissionType(),
                    changeLog.getPreviousName(),
                    changeLog.getPreviousNote(),
                    changeLog.getPreviousActive(),
                    changeLog.getPreviousDeletedDate()
                );

            PermissionStateInfo newState =
                toPermissionStateInfo(
                    changeLog.getNewPermissionType(),
                    changeLog.getNewName(),
                    changeLog.getNewNote(),
                    changeLog.getNewActive(),
                    changeLog.getNewDeletedDate()
                );

            return new PermissionChangeLogResponse(
                changeLog.getId(),
                changeTypeInfo,
                changedByUserInfo,
                previousState,
                newState,
                changeLog.getChangedDate()
            );
    }

    private PermissionStateInfo toPermissionStateInfo(
        PermissionType permissionType,
        String name,
        String note,
        Boolean active,
        LocalDateTime deletedDate
    ) {
        if (
            permissionType == null &&
            name == null &&
            note == null &&
            active == null &&
            deletedDate == null
        ) {
            return null;
        }

        PermissionTypeInfo permissionTypeInfo =
            permissionType == null
                ? null
                : new PermissionTypeInfo(
                    permissionType.getId(),
                    permissionType.getCode(),
                    permissionType.getName()
                );

        return new PermissionStateInfo(
            permissionTypeInfo,
            name,
            note,
            active,
            deletedDate
        );
    }
}
