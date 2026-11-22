package com.williamhsieh.financeapp.finance.transaction.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.williamhsieh.financeapp.finance.transaction.entity.TransactionEntry;

public interface TransactionEntryRepository
    extends JpaRepository<TransactionEntry, Long> {

    List<TransactionEntry>
        findAllByJournalEntry_IdAndDeletedDateIsNullOrderByLineNumberAsc(
            Long journalEntryId
        );

    boolean existsByJournalEntry_IdAndLineNumber(
        Long journalEntryId,
        int lineNumber
    );
}
