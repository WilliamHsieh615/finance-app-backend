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
    name = "role_permission_assignment_logs",
    indexes = {
        @Index(
            name = "idx_role_permission_logs_role_date",
            columnList = "role_id, changed_date"
        ),
        @Index(
            name = "idx_role_permission_logs_permission",
            columnList = "permission_id"
        ),
        @Index(
            name = "idx_role_permission_logs_type",
            columnList =
                "role_permission_assignment_type_id"
        ),
        @Index(
            name = "idx_role_permission_logs_changed_by",
            columnList = "changed_by_user_id"
        )
    }
)
public class RolePermissionAssignmentLog {

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
        name = "role_id",
        nullable = false
    )
    private Role role;

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
        name =
            "role_permission_assignment_type_id",
        nullable = false
    )
    private RolePermissionAssignmentType
        assignmentType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "changed_by_user_id")
    private User changedByUser;

    @Column(
        name = "note",
        nullable = false,
        length = 255
    )
    private String note;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(
        name = "changed_date",
        nullable = false,
        insertable = false,
        updatable = false
    )
    private LocalDateTime changedDate;

    protected RolePermissionAssignmentLog() {
    }

    public RolePermissionAssignmentLog(
        Role role,
        Permission permission,
        RolePermissionAssignmentType
            assignmentType,
        User changedByUser,
        String note
    ) {
        this.role = role;
        this.permission = permission;
        this.assignmentType = assignmentType;
        this.changedByUser = changedByUser;
        this.note = note;
    }

    public Long getId() {
        return id;
    }

    public Role getRole() {
        return role;
    }

    public Permission getPermission() {
        return permission;
    }

    public RolePermissionAssignmentType
        getAssignmentType() {

        return assignmentType;
    }

    public User getChangedByUser() {
        return changedByUser;
    }

    public String getNote() {
        return note;
    }

    public LocalDateTime getChangedDate() {
        return changedDate;
    }
}