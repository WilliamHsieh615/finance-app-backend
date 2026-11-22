package com.williamhsieh.financeapp.finance.transaction.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.williamhsieh.financeapp.finance.transaction.entity.JournalEntry;

public interface JournalEntryRepository
    extends JpaRepository<JournalEntry, Long> {

    Optional<JournalEntry>
        findByIdAndUser_IdAndDeletedDateIsNull(
            Long id,
            Long userId
        );

    boolean existsByUser_IdAndJournalNumber(
        Long userId,
        String journalNumber
    );
}
