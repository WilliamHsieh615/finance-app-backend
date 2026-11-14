package com.williamhsieh.financeapp.reference.geography.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.williamhsieh.financeapp.reference.geography.entity.CountryLanguage;

public interface CountryLanguageRepository extends JpaRepository<CountryLanguage, Long> {

    @Query("""
        select cl
        from CountryLanguage cl
        join fetch cl.country c
        join fetch cl.language l
        where cl.deletedDate is null
          and cl.defaultLanguage = true
          and c.deletedDate is null
          and l.deletedDate is null
          and l.active = true
        order by c.name asc, l.name asc
        """)
    List<CountryLanguage> findAllAvailableDefaults();
}
