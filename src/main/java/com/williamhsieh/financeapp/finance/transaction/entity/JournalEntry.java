package com.williamhsieh.financeapp.finance.transaction.entity;

import java.time.LocalDateTime;

import com.williamhsieh.financeapp.identity.user.entity.User;
import com.williamhsieh.financeapp.reference.currency.entity.Currency;

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
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
    name = "journal_entries",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_journal_entries_user_number",
            columnNames = {
                "user_id",
                "journal_number"
            }
        )
    },
    indexes = {
        @Index(
            name = "idx_journal_entries_user_date",
            columnList = "user_id, transaction_date"
        ),
        @Index(
            name = "idx_journal_entries_source",
            columnList = "transaction_source_id"
        ),
        @Index(
            name = "idx_journal_entries_status",
            columnList = "transaction_status_id"
        ),
        @Index(
            name = "idx_journal_entries_currency",
            columnList = "currency_id"
        ),
        @Index(
            name = "idx_journal_entries_date",
            columnList = "transaction_date"
        )
    }
)
public class JournalEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
        name = "transaction_source_id",
        nullable = false
    )
    private TransactionSource transactionSource;

    @ManyToOne(
        fetch = FetchType.LAZY,
        optional = false
    )
    @JoinColumn(
        name = "transaction_status_id",
        nullable = false
    )
    private TransactionStatus transactionStatus;

    @ManyToOne(
        fetch = FetchType.LAZY,
        optional = false
    )
    @JoinColumn(
        name = "currency_id",
        nullable = false
    )
    private Currency currency;

    @Column(
        name = "journal_number",
        nullable = false,
        length = 30
    )
    private String journalNumber;

    @Column(
        name = "transaction_date",
        nullable = false
    )
    private LocalDateTime transactionDate;

    @Column(name = "posted_date")
    private LocalDateTime postedDate;

    @Column(
        name = "note",
        length = 255
    )
    private String note;

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

    protected JournalEntry() {
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(
        User user
    ) {
        this.user = user;
    }

    public TransactionSource getTransactionSource() {
        return transactionSource;
    }

    public void setTransactionSource(
        TransactionSource transactionSource
    ) {
        this.transactionSource = transactionSource;
    }

    public TransactionStatus getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(
        TransactionStatus transactionStatus
    ) {
        this.transactionStatus = transactionStatus;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(
        Currency currency
    ) {
        this.currency = currency;
    }

    public String getJournalNumber() {
        return journalNumber;
    }

    public void setJournalNumber(
        String journalNumber
    ) {
        this.journalNumber = journalNumber;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(
        LocalDateTime transactionDate
    ) {
        this.transactionDate = transactionDate;
    }

    public LocalDateTime getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(
        LocalDateTime postedDate
    ) {
        this.postedDate = postedDate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(
        String note
    ) {
        this.note = note;
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
