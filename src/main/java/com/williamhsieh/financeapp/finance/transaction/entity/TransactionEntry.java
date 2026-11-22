package com.williamhsieh.financeapp.finance.transaction.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.williamhsieh.financeapp.finance.account.entity.Account;
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
    name = "transaction_entries",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_transaction_entries_journal_line",
            columnNames = {
                "journal_entry_id",
                "line_number"
            }
        )
    },
    indexes = {
        @Index(
            name = "idx_transaction_entries_journal",
            columnList = "journal_entry_id"
        ),
        @Index(
            name = "idx_transaction_entries_account",
            columnList = "account_id"
        ),
        @Index(
            name = "idx_transaction_entries_type",
            columnList = "transaction_type_id"
        )
    }
)
public class TransactionEntry {

    private static final BigDecimal ZERO =
        BigDecimal.ZERO;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(
        fetch = FetchType.LAZY,
        optional = false
    )
    @JoinColumn(
        name = "journal_entry_id",
        nullable = false
    )
    private JournalEntry journalEntry;

    @Column(
        name = "line_number",
        nullable = false
    )
    private int lineNumber;

    @ManyToOne(
        fetch = FetchType.LAZY,
        optional = false
    )
    @JoinColumn(
        name = "account_id",
        nullable = false
    )
    private Account account;

    @ManyToOne(
        fetch = FetchType.LAZY,
        optional = false
    )
    @JoinColumn(
        name = "transaction_type_id",
        nullable = false
    )
    private TransactionType transactionType;

    @Column(
        name = "price",
        precision = 18,
        scale = 8
    )
    private BigDecimal price;

    @Column(
        name = "quantity",
        precision = 18,
        scale = 8
    )
    private BigDecimal quantity;

    @Column(
        name = "debit_amount",
        nullable = false,
        precision = 18,
        scale = 8
    )
    private BigDecimal debitAmount = ZERO;

    @Column(
        name = "credit_amount",
        nullable = false,
        precision = 18,
        scale = 8
    )
    private BigDecimal creditAmount = ZERO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "original_currency_id")
    private Currency originalCurrency;

    @Column(
        name = "original_amount",
        precision = 18,
        scale = 8
    )
    private BigDecimal originalAmount;

    @Column(
        name = "exchange_rate_used",
        precision = 18,
        scale = 8
    )
    private BigDecimal exchangeRateUsed;

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

    protected TransactionEntry() {
    }

    public Long getId() {
        return id;
    }

    public JournalEntry getJournalEntry() {
        return journalEntry;
    }

    public void setJournalEntry(
        JournalEntry journalEntry
    ) {
        this.journalEntry = journalEntry;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(
        int lineNumber
    ) {
        if (lineNumber < 1) {
            throw new IllegalArgumentException(
                "分錄行號必須大於 0"
            );
        }

        this.lineNumber = lineNumber;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(
        Account account
    ) {
        this.account = account;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(
        TransactionType transactionType
    ) {
        this.transactionType = transactionType;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(
        BigDecimal price
    ) {
        this.price = price;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(
        BigDecimal quantity
    ) {
        this.quantity = quantity;
    }

    public BigDecimal getDebitAmount() {
        return debitAmount;
    }

    public void setDebitAmount(
        BigDecimal debitAmount
    ) {
        validateNonNegativeAmount(
            debitAmount,
            "借方金額"
        );

        this.debitAmount = debitAmount;
    }

    public BigDecimal getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(
        BigDecimal creditAmount
    ) {
        validateNonNegativeAmount(
            creditAmount,
            "貸方金額"
        );

        this.creditAmount = creditAmount;
    }

    public Currency getOriginalCurrency() {
        return originalCurrency;
    }

    public void setOriginalCurrency(
        Currency originalCurrency
    ) {
        this.originalCurrency = originalCurrency;
    }

    public BigDecimal getOriginalAmount() {
        return originalAmount;
    }

    public void setOriginalAmount(
        BigDecimal originalAmount
    ) {
        this.originalAmount = originalAmount;
    }

    public BigDecimal getExchangeRateUsed() {
        return exchangeRateUsed;
    }

    public void setExchangeRateUsed(
        BigDecimal exchangeRateUsed
    ) {
        this.exchangeRateUsed = exchangeRateUsed;
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

    public BigDecimal getAmount() {
        return debitAmount.add(creditAmount);
    }

    public boolean isDebitEntry() {
        return debitAmount.compareTo(ZERO) > 0
            && creditAmount.compareTo(ZERO) == 0;
    }

    public boolean isCreditEntry() {
        return creditAmount.compareTo(ZERO) > 0
            && debitAmount.compareTo(ZERO) == 0;
    }

    public boolean hasValidEntrySide() {
        return isDebitEntry() || isCreditEntry();
    }

    private void validateNonNegativeAmount(
        BigDecimal amount,
        String fieldName
    ) {
        if (amount == null) {
            throw new IllegalArgumentException(
                fieldName + "不可為 null"
            );
        }

        if (amount.compareTo(ZERO) < 0) {
            throw new IllegalArgumentException(
                fieldName + "不可小於 0"
            );
        }
    }
}