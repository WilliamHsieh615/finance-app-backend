package com.williamhsieh.financeapp.finance.recurring.entity;

import com.williamhsieh.financeapp.finance.category.entity.Category;
import com.williamhsieh.financeapp.finance.merchant.entity.Merchant;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(
    name = "cashflow_recurring_transaction_details",
    indexes = {
        @Index(
            name = "idx_cashflow_recurring_details_category",
            columnList = "category_id"
        )
    }
)
public class CashflowRecurringTransactionDetail {

    @Id
    private Long recurringTransactionEntryId;

    @OneToOne(
        fetch = FetchType.LAZY,
        optional = false
    )
    @MapsId
    @JoinColumn(
        name = "recurring_transaction_entry_id",
        nullable = false
    )
    private RecurringTransactionEntry recurringTransactionEntry;

    @ManyToOne(
        fetch = FetchType.LAZY,
        optional = false
    )
    @JoinColumn(
        name = "category_id",
        nullable = false
    )
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "merchant_id")
    private Merchant merchant;

    protected CashflowRecurringTransactionDetail() {
    }

    public Long getRecurringTransactionEntryId() {
        return recurringTransactionEntryId;
    }

    public RecurringTransactionEntry
        getRecurringTransactionEntry() {

        return recurringTransactionEntry;
    }

    public void setRecurringTransactionEntry(
        RecurringTransactionEntry recurringTransactionEntry
    ) {
        this.recurringTransactionEntry =
            recurringTransactionEntry;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(
        Category category
    ) {
        this.category = category;
    }

    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(
        Merchant merchant
    ) {
        this.merchant = merchant;
    }
}