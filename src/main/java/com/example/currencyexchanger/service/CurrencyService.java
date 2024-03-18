package com.example.currencyexchanger.service;

import com.example.currencyexchanger.entity.Currency;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


public interface CurrencyService {

    List<Currency> getCurrencies();

    void addCurrency(String code, String name);

    Map<String, BigDecimal> getExchangeRates(String code);

    Currency getCurrencyByCode(String code);
}

