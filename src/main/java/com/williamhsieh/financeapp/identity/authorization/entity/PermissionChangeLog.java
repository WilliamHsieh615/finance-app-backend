package com.williamhsieh.financeapp.identity.authorization.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnDefault;

import com.williamhsieh.financeapp.identity.user.entity.User;

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

@Entity
@Table(
    name = "permission_change_logs",
    indexes = {
        @Index(
            name = "idx_permission_changes_permission_date",
            columnList =
                "permission_id, changed_date"
        ),
        @Index(
            name = "idx_permission_changes_type",
            columnList =
                "permission_change_type_id"
        ),
        @Index(
            name = "idx_permission_changes_changed_by",
            columnList =
                "changed_by_user_id"
        )
    }
)
public class PermissionChangeLog {

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
        name = "permission_id",
        nullable = false
    )
    private Permission permission;

    @ManyToOne(
        fetch = FetchType.LAZY,
        optional = false
    )
    @JoinColumn(
        name = "permission_change_type_id",
        nullable = false
    )
    private PermissionChangeType changeType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "changed_by_user_id")
    private User changedByUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "previous_permission_type_id"
    )
    private PermissionType previousPermissionType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "new_permission_type_id"
    )
    private PermissionType newPermissionType;

    @Column(
        name = "previous_name",
        length = 100
    )
    private String previousName;

    @Column(
        name = "new_name",
        length = 100
    )
    private String newName;

    @Column(
        name = "previous_note",
        length = 255
    )
    private String previousNote;

    @Column(
        name = "new_note",
        length = 255
    )
    private String newNote;

    @Column(name = "previous_is_active")
    private Boolean previousActive;

    @Column(name = "new_is_active")
    private Boolean newActive;

    @Column(name = "previous_deleted_date")
    private LocalDateTime previousDeletedDate;

    @Column(name = "new_deleted_date")
    private LocalDateTime newDeletedDate;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(
        name = "changed_date",
        nullable = false,
        insertable = false,
        updatable = false
    )
    private LocalDateTime changedDate;

    protected PermissionChangeLog() {
    }

    public PermissionChangeLog(
        Permission permission,
        PermissionChangeType changeType,
        User changedByUser,
        PermissionType previousPermissionType,
        PermissionType newPermissionType,
        String previousName,
        String newName,
        String previousNote,
        String newNote,
        Boolean previousActive,
        Boolean newActive,
        LocalDateTime previousDeletedDate,
        LocalDateTime newDeletedDate
    ) {
        this.permission = permission;
        this.changeType = changeType;
        this.changedByUser = changedByUser;
        this.previousPermissionType =
            previousPermissionType;
        this.newPermissionType =
            newPermissionType;
        this.previousName = previousName;
        this.newName = newName;
        this.previousNote = previousNote;
        this.newNote = newNote;
        this.previousActive = previousActive;
        this.newActive = newActive;
        this.previousDeletedDate =
            previousDeletedDate;
        this.newDeletedDate =
            newDeletedDate;
    }

    public Long getId() {
        return id;
    }

    public Permission getPermission() {
        return permission;
    }

    public PermissionChangeType getChangeType() {
        return changeType;
    }

    public User getChangedByUser() {
        return changedByUser;
    }

    public PermissionType
        getPreviousPermissionType() {

        return previousPermissionType;
    }

    public PermissionType getNewPermissionType() {
        return newPermissionType;
    }

    public String getPreviousName() {
        return previousName;
    }

    public String getNewName() {
        return newName;
    }

    public String getPreviousNote() {
        return previousNote;
    }

    public String getNewNote() {
        return newNote;
    }

    public Boolean getPreviousActive() {
        return previousActive;
    }

    public Boolean getNewActive() {
        return newActive;
    }

    public LocalDateTime getPreviousDeletedDate() {
        return previousDeletedDate;
    }

    public LocalDateTime getNewDeletedDate() {
        return newDeletedDate;
    }

    public LocalDateTime getChangedDate() {
        return changedDate;
    }
}
