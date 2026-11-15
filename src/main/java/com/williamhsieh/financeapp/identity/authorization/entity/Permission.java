package com.williamhsieh.financeapp.identity.authorization.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(
    name = "permissions",
    indexes = {
        @Index(
            name = "idx_permissions_permission_type",
            columnList = "permission_type_id"
        )
    }
)
public class Permission {

    @Id
    @GeneratedValue(
        strategy = GenerationType.IDENTITY
    )
    private Long id;

    @ManyToOne(
        fetch = FetchType.LAZY,
        optional = false
    )
    @JoinColumn(
        name = "permission_type_id",
        nullable = false
    )
    private PermissionType permissionType;

    @Column(
        name = "code",
        nullable = false,
        unique = true,
        length = 100
    )
    private String code;

    @Column(
        name = "name",
        nullable = false,
        length = 100
    )
    private String name;

    @Column(
        name = "note",
        length = 255
    )
    private String note;

    @Column(
        name = "is_active",
        nullable = false
    )
    private boolean active = true;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(
        name = "created_date",
        nullable = false,
        insertable = false,
        updatable = false
    )
    private LocalDateTime createdDate;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(
        name = "updated_date",
        nullable = false,
        insertable = false,
        updatable = false
    )
    private LocalDateTime updatedDate;

    @Column(name = "deleted_date")
    private LocalDateTime deletedDate;

    protected Permission() {
    }

    public Long getId() {
        return id;
    }

    public PermissionType getPermissionType() {
        return permissionType;
    }

    public void setPermissionType(
        PermissionType permissionType
    ) {
        this.permissionType = permissionType;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public LocalDateTime getUpdatedDate() {
        return updatedDate;
    }

    public LocalDateTime getDeletedDate() {
        return deletedDate;
    }

    public void setDeletedDate(
        LocalDateTime deletedDate
    ) {
        this.deletedDate = deletedDate;
    }

    public Permission(
        PermissionType permissionType,
        String code,
        String name,
        String note
    ) {
        this.permissionType = permissionType;
        this.code = code;
        this.name = name;
        this.note = note;
        this.active = true;
    }

    public boolean isAvailable() {
        return active && deletedDate == null;
    }

    public void activate() {
        this.active = true;
        this.deletedDate = null;
    }

    public void deactivate() {
        this.active = false;
        this.deletedDate = LocalDateTime.now();
    }

    public void updateDetails(
        PermissionType permissionType,
        String name,
        String note
    ) {
        this.permissionType = permissionType;
        this.name = name;
        this.note = note;
    }
}
