package com.williamhsieh.financeapp.entity.user;

import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnDefault;

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
@Table(name = "user_account_status_logs")
public class UserAccountStatusLog {

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "previous_status_id")
    private UserAccountStatus previousStatus;

    @ManyToOne(
        fetch = FetchType.LAZY,
        optional = false
    )
    @JoinColumn(
        name = "new_status_id",
        nullable = false
    )
    private UserAccountStatus newStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "changed_by_user_id")
    private User changedByUser;

    @Column(name = "note", length = 255)
    private String note;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(
        name = "changed_date",
        nullable = false,
        insertable = false,
        updatable = false
    )
    private LocalDateTime changedDate;

    protected UserAccountStatusLog() {
    }

    public UserAccountStatusLog(
        User user,
        UserAccountStatus previousStatus,
        UserAccountStatus newStatus,
        User changedByUser,
        String note
    ) {
        this.user = user;
        this.previousStatus = previousStatus;
        this.newStatus = newStatus;
        this.changedByUser = changedByUser;
        this.note = note;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public UserAccountStatus getPreviousStatus() {
        return previousStatus;
    }

    public UserAccountStatus getNewStatus() {
        return newStatus;
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