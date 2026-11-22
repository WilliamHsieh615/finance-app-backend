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
    name = "role_change_logs",
    indexes = {
        @Index(
            name = "idx_role_changes_role_date",
            columnList =
                "role_id, changed_date"
        ),
        @Index(
            name = "idx_role_changes_type",
            columnList =
                "role_change_type_id"
        ),
        @Index(
            name = "idx_role_changes_changed_by",
            columnList =
                "changed_by_user_id"
        )
    }
)
public class RoleChangeLog {

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
        name = "role_change_type_id",
        nullable = false
    )
    private RoleChangeType changeType;

    /*
     * 系統自動操作時允許為 NULL。
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "changed_by_user_id")
    private User changedByUser;

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

    protected RoleChangeLog() {
    }

    public RoleChangeLog(
        Role role,
        RoleChangeType changeType,
        User changedByUser,
        String previousName,
        String newName,
        String previousNote,
        String newNote,
        Boolean previousActive,
        Boolean newActive,
        LocalDateTime previousDeletedDate,
        LocalDateTime newDeletedDate
    ) {
        this.role = role;
        this.changeType = changeType;
        this.changedByUser = changedByUser;
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

    public Role getRole() {
        return role;
    }

    public RoleChangeType getChangeType() {
        return changeType;
    }

    public User getChangedByUser() {
        return changedByUser;
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
