package com.williamhsieh.financeapp.finance.account.entity;

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

@Entity
@Table(
    name = "account_classes",
    indexes = {
        @Index(
            name = "idx_account_classes_normal_balance_type",
            columnList = "normal_balance_type_id"
        )
    }
)
public class AccountClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(
        fetch = FetchType.LAZY,
        optional = false
    )
    @JoinColumn(
        name = "normal_balance_type_id",
        nullable = false
    )
    private NormalBalanceType normalBalanceType;

    @Column(
        name = "code",
        nullable = false,
        unique = true,
        length = 50
    )
    private String code;

    @Column(
        name = "name",
        nullable = false,
        length = 100
    )
    private String name;

    @Column(
        name = "net_worth_direction",
        nullable = false
    )
    private byte netWorthDirection;

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

    @Column(
        name = "created_date",
        nullable = false,
        insertable = false,
        updatable = false
    )
    private LocalDateTime createdDate;

    @Column(
        name = "updated_date",
        nullable = false,
        insertable = false,
        updatable = false
    )
    private LocalDateTime updatedDate;

    @Column(name = "deleted_date")
    private LocalDateTime deletedDate;

    protected AccountClass() {
    }

    public Long getId() {
        return id;
    }

    public NormalBalanceType getNormalBalanceType() {
        return normalBalanceType;
    }

    public void setNormalBalanceType(
        NormalBalanceType normalBalanceType
    ) {
        this.normalBalanceType = normalBalanceType;
    }

    public String getCode() {
        return code;
    }

    public void setCode(
        String code
    ) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(
        String name
    ) {
        this.name = name;
    }

    public byte getNetWorthDirection() {
        return netWorthDirection;
    }

    public void setNetWorthDirection(
        byte netWorthDirection
    ) {
        if (
            netWorthDirection != -1
                && netWorthDirection != 0
                && netWorthDirection != 1
        ) {
            throw new IllegalArgumentException(
                "淨資產方向只能是 -1、0 或 1"
            );
        }

        this.netWorthDirection = netWorthDirection;
    }

    public String getNote() {
        return note;
    }

    public void setNote(
        String note
    ) {
        this.note = note;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(
        boolean active
    ) {
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
}
