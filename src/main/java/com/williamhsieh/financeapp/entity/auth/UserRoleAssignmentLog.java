package com.williamhsieh.financeapp.entity.auth;

import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnDefault;

import com.williamhsieh.financeapp.entity.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_role_assignment_logs")
public class UserRoleAssignmentLog {

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
        name = "user_id",
        nullable = false
    )
    private User user;

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
        name = "user_role_assignment_type_id",
        nullable = false
    )
    private UserRoleAssignmentType assignmentType;

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

    protected UserRoleAssignmentLog() {
    }

    public UserRoleAssignmentLog(
        User user,
        Role role,
        UserRoleAssignmentType assignmentType,
        User changedByUser,
        String note
    ) {
        this.user = user;
        this.role = role;
        this.assignmentType = assignmentType;
        this.changedByUser = changedByUser;
        this.note = note;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Role getRole() {
        return role;
    }

    public UserRoleAssignmentType getAssignmentType() {
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
