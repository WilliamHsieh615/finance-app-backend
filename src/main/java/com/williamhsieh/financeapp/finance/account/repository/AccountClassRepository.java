package com.williamhsieh.financeapp.finance.account.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.williamhsieh.financeapp.finance.account.entity.AccountClass;

public interface AccountClassRepository
    extends JpaRepository<AccountClass, Long> {

    Optional<AccountClass>
        findByCodeIgnoreCaseAndActiveTrueAndDeletedDateIsNull(
            String code
        );
}
