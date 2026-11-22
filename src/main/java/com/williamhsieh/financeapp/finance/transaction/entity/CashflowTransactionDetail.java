package com.williamhsieh.financeapp.finance.transaction.entity;

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
    name = "cashflow_transaction_details",
    indexes = {
        @Index(
            name = "idx_cashflow_transaction_details_category",
            columnList = "category_id"
        )
    }
)
public class CashflowTransactionDetail {

    @Id
    private Long transactionEntryId;

    @OneToOne(
        fetch = FetchType.LAZY,
        optional = false
    )
    @MapsId
    @JoinColumn(
        name = "transaction_entry_id",
        nullable = false
    )
    private TransactionEntry transactionEntry;

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

    protected CashflowTransactionDetail() {
    }

    public Long getTransactionEntryId() {
        return transactionEntryId;
    }

    public TransactionEntry getTransactionEntry() {
        return transactionEntry;
    }

    public void setTransactionEntry(
        TransactionEntry transactionEntry
    ) {
        this.transactionEntry = transactionEntry;
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