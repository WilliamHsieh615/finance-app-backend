package com.williamhsieh.financeapp.reference.geography.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.williamhsieh.financeapp.reference.geography.entity.Timezone;

public interface TimezoneRepository extends JpaRepository<Timezone, Long> {

    Optional<Timezone> findByIdAndDeletedDateIsNull(Long id);
}
