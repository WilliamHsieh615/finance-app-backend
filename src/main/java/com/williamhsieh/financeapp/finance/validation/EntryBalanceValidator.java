package com.williamhsieh.financeapp.finance.validation;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.williamhsieh.financeapp.finance.recurring.entity.RecurringTransactionEntry;
import com.williamhsieh.financeapp.finance.transaction.entity.TransactionEntry;
import com.williamhsieh.financeapp.shared.exception.BusinessException;

@Component
public class EntryBalanceValidator {

    private static final BigDecimal ZERO =
        BigDecimal.ZERO;

    public void validateTransactionEntries(
        List<TransactionEntry> entries
    ) {
        validateEntryCount(entries);

        Set<Integer> lineNumbers =
            new HashSet<>();

        BigDecimal totalDebit = ZERO;
        BigDecimal totalCredit = ZERO;

        for (TransactionEntry entry : entries) {
            if (entry == null) {
                throw invalidEntryException();
            }

            validateLineNumber(
                entry.getLineNumber(),
                lineNumbers
            );

            validateEntryAmounts(
                entry.getDebitAmount(),
                entry.getCreditAmount(),
                entry.getLineNumber()
            );

            totalDebit = totalDebit.add(
                entry.getDebitAmount()
            );

            totalCredit = totalCredit.add(
                entry.getCreditAmount()
            );
        }

        validateBalanced(
            totalDebit,
            totalCredit
        );
    }

    public void validateRecurringTransactionEntries(
        List<RecurringTransactionEntry> entries
    ) {
        validateEntryCount(entries);

        Set<Integer> lineNumbers =
            new HashSet<>();

        BigDecimal totalDebit = ZERO;
        BigDecimal totalCredit = ZERO;

        for (
            RecurringTransactionEntry entry
                : entries
        ) {
            if (entry == null) {
                throw invalidEntryException();
            }

            validateLineNumber(
                entry.getLineNumber(),
                lineNumbers
            );

            validateEntryAmounts(
                entry.getDebitAmount(),
                entry.getCreditAmount(),
                entry.getLineNumber()
            );

            totalDebit = totalDebit.add(
                entry.getDebitAmount()
            );

            totalCredit = totalCredit.add(
                entry.getCreditAmount()
            );
        }

        validateBalanced(
            totalDebit,
            totalCredit
        );
    }

    private void validateEntryCount(
        List<?> entries
    ) {
        if (entries == null) {
            throw new BusinessException(
                HttpStatus.BAD_REQUEST,
                "ENTRY_LIST_REQUIRED",
                "分錄明細不可為 null"
            );
        }

        if (entries.size() < 2) {
            throw new BusinessException(
                HttpStatus.BAD_REQUEST,
                "ENTRY_MINIMUM_LINES_REQUIRED",
                "一張傳票至少需要兩條分錄"
            );
        }
    }

    private void validateLineNumber(
        int lineNumber,
        Set<Integer> lineNumbers
    ) {
        if (lineNumber < 1) {
            throw new BusinessException(
                HttpStatus.BAD_REQUEST,
                "ENTRY_LINE_NUMBER_INVALID",
                "分錄行號必須大於 0"
            );
        }

        if (!lineNumbers.add(lineNumber)) {
            throw new BusinessException(
                HttpStatus.BAD_REQUEST,
                "ENTRY_LINE_NUMBER_DUPLICATED",
                "分錄行號不可重複"
            );
        }
    }

    private void validateEntryAmounts(
        BigDecimal debitAmount,
        BigDecimal creditAmount,
        int lineNumber
    ) {
        if (
            debitAmount == null
                || creditAmount == null
        ) {
            throw new BusinessException(
                HttpStatus.BAD_REQUEST,
                "ENTRY_AMOUNT_REQUIRED",
                "第 " + lineNumber
                    + " 行的借貸金額不可為 null"
            );
        }

        if (
            debitAmount.compareTo(ZERO) < 0
                || creditAmount.compareTo(ZERO) < 0
        ) {
            throw new BusinessException(
                HttpStatus.BAD_REQUEST,
                "ENTRY_AMOUNT_NEGATIVE",
                "第 " + lineNumber
                    + " 行的借貸金額不可小於 0"
            );
        }

        boolean debitEntry =
            debitAmount.compareTo(ZERO) > 0
                && creditAmount.compareTo(ZERO) == 0;

        boolean creditEntry =
            creditAmount.compareTo(ZERO) > 0
                && debitAmount.compareTo(ZERO) == 0;

        if (!debitEntry && !creditEntry) {
            throw new BusinessException(
                HttpStatus.BAD_REQUEST,
                "ENTRY_SIDE_INVALID",
                "第 " + lineNumber
                    + " 行只能有借方或貸方其中一邊的金額"
            );
        }
    }

    private void validateBalanced(
        BigDecimal totalDebit,
        BigDecimal totalCredit
    ) {
        if (
            totalDebit.compareTo(totalCredit) != 0
        ) {
            throw new BusinessException(
                HttpStatus.BAD_REQUEST,
                "JOURNAL_ENTRY_UNBALANCED",
                "借方總額與貸方總額不相等"
            );
        }
    }

    private BusinessException
        invalidEntryException() {

        return new BusinessException(
            HttpStatus.BAD_REQUEST,
            "ENTRY_INVALID",
            "分錄明細不可包含 null"
        );
    }
}
