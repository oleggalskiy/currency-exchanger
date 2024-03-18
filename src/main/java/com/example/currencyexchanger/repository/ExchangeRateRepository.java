package com.example.currencyexchanger.repository;

import com.example.currencyexchanger.entity.Currency;
import com.example.currencyexchanger.entity.ExchangeRate;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {
    List<ExchangeRate> findByBaseCurrency(Currency baseCurrency);

    @EntityGraph("ExchangeRate.targetCurrency")
    List<ExchangeRate> findByBaseCurrency_Code(String code);
}