package com.williamhsieh.financeapp.finance.recurring.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.williamhsieh.financeapp.finance.recurring.entity.RecurringTransactionEntry;

public interface RecurringTransactionEntryRepository
    extends JpaRepository<RecurringTransactionEntry, Long> {

    List<RecurringTransactionEntry>
        findAllByRecurringJournalEntry_IdAndDeletedDateIsNullOrderByLineNumberAsc(
            Long recurringJournalEntryId
        );

    boolean existsByRecurringJournalEntry_IdAndLineNumber(
        Long recurringJournalEntryId,
        int lineNumber
    );
}
