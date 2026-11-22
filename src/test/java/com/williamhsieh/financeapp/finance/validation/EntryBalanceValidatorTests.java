package com.williamhsieh.financeapp.finance.validation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.williamhsieh.financeapp.finance.recurring.entity.RecurringTransactionEntry;
import com.williamhsieh.financeapp.finance.transaction.entity.TransactionEntry;
import com.williamhsieh.financeapp.shared.exception.BusinessException;

class EntryBalanceValidatorTests {

    private final EntryBalanceValidator validator =
        new EntryBalanceValidator();

    @Test
    void balancedTransactionEntriesShouldPass() {
        List<TransactionEntry> entries =
            List.of(
                transactionEntry(
                    1,
                    "1000.00000000",
                    "0"
                ),
                transactionEntry(
                    2,
                    "0",
                    "1000.00000000"
                )
            );

        assertThatCode(() ->
            validator.validateTransactionEntries(
                entries
            )
        ).doesNotThrowAnyException();
    }

    @Test
    void multipleBalancedTransactionEntriesShouldPass() {
        List<TransactionEntry> entries =
            List.of(
                transactionEntry(
                    1,
                    "22000",
                    "0"
                ),
                transactionEntry(
                    2,
                    "8000",
                    "0"
                ),
                transactionEntry(
                    3,
                    "0",
                    "30000"
                )
            );

        assertThatCode(() ->
            validator.validateTransactionEntries(
                entries
            )
        ).doesNotThrowAnyException();
    }

    @Test
    void fewerThanTwoEntriesShouldFail() {
        List<TransactionEntry> entries =
            List.of(
                transactionEntry(
                    1,
                    "1000",
                    "0"
                )
            );

        assertThatThrownBy(() ->
            validator.validateTransactionEntries(
                entries
            )
        )
            .isInstanceOfSatisfying(
                BusinessException.class,
                exception ->
                    assertThat(exception.getCode())
                        .isEqualTo(
                            "ENTRY_MINIMUM_LINES_REQUIRED"
                        )
            );
    }

    @Test
    void duplicatedLineNumberShouldFail() {
        List<TransactionEntry> entries =
            List.of(
                transactionEntry(
                    1,
                    "1000",
                    "0"
                ),
                transactionEntry(
                    1,
                    "0",
                    "1000"
                )
            );

        assertThatThrownBy(() ->
            validator.validateTransactionEntries(
                entries
            )
        )
            .isInstanceOfSatisfying(
                BusinessException.class,
                exception ->
                    assertThat(exception.getCode())
                        .isEqualTo(
                            "ENTRY_LINE_NUMBER_DUPLICATED"
                        )
            );
    }

    @Test
    void debitAndCreditOnSameEntryShouldFail() {
        List<TransactionEntry> entries =
            List.of(
                transactionEntry(
                    1,
                    "1000",
                    "1000"
                ),
                transactionEntry(
                    2,
                    "0",
                    "1000"
                )
            );

        assertThatThrownBy(() ->
            validator.validateTransactionEntries(
                entries
            )
        )
            .isInstanceOfSatisfying(
                BusinessException.class,
                exception ->
                    assertThat(exception.getCode())
                        .isEqualTo(
                            "ENTRY_SIDE_INVALID"
                        )
            );
    }

    @Test
    void unbalancedTransactionEntriesShouldFail() {
        List<TransactionEntry> entries =
            List.of(
                transactionEntry(
                    1,
                    "1000",
                    "0"
                ),
                transactionEntry(
                    2,
                    "0",
                    "900"
                )
            );

        assertThatThrownBy(() ->
            validator.validateTransactionEntries(
                entries
            )
        )
            .isInstanceOfSatisfying(
                BusinessException.class,
                exception ->
                    assertThat(exception.getCode())
                        .isEqualTo(
                            "JOURNAL_ENTRY_UNBALANCED"
                        )
            );
    }

    @Test
    void balancedRecurringEntriesShouldPass() {
        List<RecurringTransactionEntry> entries =
            List.of(
                recurringTransactionEntry(
                    1,
                    "20000",
                    "0"
                ),
                recurringTransactionEntry(
                    2,
                    "0",
                    "20000"
                )
            );

        assertThatCode(() ->
            validator
                .validateRecurringTransactionEntries(
                    entries
                )
        ).doesNotThrowAnyException();
    }

    @Test
    void unbalancedRecurringEntriesShouldFail() {
        List<RecurringTransactionEntry> entries =
            List.of(
                recurringTransactionEntry(
                    1,
                    "20000",
                    "0"
                ),
                recurringTransactionEntry(
                    2,
                    "0",
                    "19000"
                )
            );

        assertThatThrownBy(() ->
            validator
                .validateRecurringTransactionEntries(
                    entries
                )
        )
            .isInstanceOfSatisfying(
                BusinessException.class,
                exception ->
                    assertThat(exception.getCode())
                        .isEqualTo(
                            "JOURNAL_ENTRY_UNBALANCED"
                        )
            );
    }

    private TransactionEntry transactionEntry(
        int lineNumber,
        String debitAmount,
        String creditAmount
    ) {
        TransactionEntry entry =
            mock(TransactionEntry.class);

        when(entry.getLineNumber())
            .thenReturn(lineNumber);

        when(entry.getDebitAmount())
            .thenReturn(
                new BigDecimal(debitAmount)
            );

        when(entry.getCreditAmount())
            .thenReturn(
                new BigDecimal(creditAmount)
            );

        return entry;
    }

    private RecurringTransactionEntry
        recurringTransactionEntry(
            int lineNumber,
            String debitAmount,
            String creditAmount
        ) {

        RecurringTransactionEntry entry =
            mock(
                RecurringTransactionEntry.class
            );

        when(entry.getLineNumber())
            .thenReturn(lineNumber);

        when(entry.getDebitAmount())
            .thenReturn(
                new BigDecimal(debitAmount)
            );

        when(entry.getCreditAmount())
            .thenReturn(
                new BigDecimal(creditAmount)
            );

        return entry;
    }
}
