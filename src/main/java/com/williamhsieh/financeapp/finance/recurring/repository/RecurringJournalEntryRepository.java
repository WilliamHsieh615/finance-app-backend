package com.williamhsieh.financeapp.finance.recurring.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.williamhsieh.financeapp.finance.recurring.entity.RecurringJournalEntry;

public interface RecurringJournalEntryRepository
    extends JpaRepository<RecurringJournalEntry, Long> {

    Optional<RecurringJournalEntry>
        findByIdAndUser_IdAndDeletedDateIsNull(
            Long id,
            Long userId
        );

    List<RecurringJournalEntry>
        findAllByActiveTrueAndDeletedDateIsNullAndNextRunDateLessThanEqualOrderByNextRunDateAsc(
            LocalDate runDate
        );
}
