package com.williamhsieh.financeapp.finance.account.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.williamhsieh.financeapp.finance.account.entity.NormalBalanceType;

public interface NormalBalanceTypeRepository
    extends JpaRepository<NormalBalanceType, Long> {

    Optional<NormalBalanceType>
        findByCodeIgnoreCaseAndActiveTrueAndDeletedDateIsNull(
            String code
        );
}
