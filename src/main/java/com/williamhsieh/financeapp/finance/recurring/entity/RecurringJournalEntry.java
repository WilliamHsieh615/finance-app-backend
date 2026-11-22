package com.williamhsieh.financeapp.finance.recurring.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.williamhsieh.financeapp.finance.schedule.entity.FlowFrequency;
import com.williamhsieh.financeapp.finance.transaction.entity.TransactionSource;
import com.williamhsieh.financeapp.finance.transaction.entity.TransactionStatus;
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

@Entity
@Table(
    name = "recurring_journal_entries",
    indexes = {
        @Index(
            name = "idx_recurring_journal_entries_user",
            columnList = "user_id"
        ),
        @Index(
            name = "idx_recurring_journal_entries_source",
            columnList = "transaction_source_id"
        ),
        @Index(
            name = "idx_recurring_journal_entries_status",
            columnList = "generated_transaction_status_id"
        ),
        @Index(
            name = "idx_recurring_journal_entries_currency",
            columnList = "currency_id"
        ),
        @Index(
            name = "idx_recurring_journal_entries_frequency",
            columnList = "recurrence_frequency_id"
        ),
        @Index(
            name = "idx_recurring_journal_entries_start_date",
            columnList = "start_date"
        ),
        @Index(
            name = "idx_recurring_journal_entries_next_run",
            columnList = "next_run_date, is_active"
        )
    }
)
public class RecurringJournalEntry {

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
        name = "generated_transaction_status_id",
        nullable = false
    )
    private TransactionStatus generatedTransactionStatus;

    @ManyToOne(
        fetch = FetchType.LAZY,
        optional = false
    )
    @JoinColumn(
        name = "currency_id",
        nullable = false
    )
    private Currency currency;

    @ManyToOne(
        fetch = FetchType.LAZY,
        optional = false
    )
    @JoinColumn(
        name = "recurrence_frequency_id",
        nullable = false
    )
    private FlowFrequency recurrenceFrequency;

    @Column(
        name = "name",
        nullable = false,
        length = 100
    )
    private String name;

    @Column(name = "next_run_date")
    private LocalDate nextRunDate;

    @Column(name = "last_run_date")
    private LocalDate lastRunDate;

    @Column(
        name = "start_date",
        nullable = false
    )
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(
        name = "is_active",
        nullable = false
    )
    private boolean active = true;

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

    protected RecurringJournalEntry() {
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

    public TransactionStatus getGeneratedTransactionStatus() {
        return generatedTransactionStatus;
    }

    public void setGeneratedTransactionStatus(
        TransactionStatus generatedTransactionStatus
    ) {
        this.generatedTransactionStatus =
            generatedTransactionStatus;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(
        Currency currency
    ) {
        this.currency = currency;
    }

    public FlowFrequency getRecurrenceFrequency() {
        return recurrenceFrequency;
    }

    public void setRecurrenceFrequency(
        FlowFrequency recurrenceFrequency
    ) {
        this.recurrenceFrequency = recurrenceFrequency;
    }

    public String getName() {
        return name;
    }

    public void setName(
        String name
    ) {
        this.name = name;
    }

    public LocalDate getNextRunDate() {
        return nextRunDate;
    }

    public void setNextRunDate(
        LocalDate nextRunDate
    ) {
        this.nextRunDate = nextRunDate;
    }

    public LocalDate getLastRunDate() {
        return lastRunDate;
    }

    public void setLastRunDate(
        LocalDate lastRunDate
    ) {
        this.lastRunDate = lastRunDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(
        LocalDate startDate
    ) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(
        LocalDate endDate
    ) {
        this.endDate = endDate;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(
        boolean active
    ) {
        this.active = active;
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
