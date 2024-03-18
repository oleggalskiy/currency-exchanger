package com.example.currencyexchanger.repository;

import com.example.currencyexchanger.entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface CurrencyRepository extends JpaRepository<Currency, Long> {
    Currency findByCode(String code);
}