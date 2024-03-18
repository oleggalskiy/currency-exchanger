package com.example.currencyexchanger.service;

import com.example.currencyexchanger.entity.Currency;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface ExternalDataService {

    Map<String, BigDecimal> getLatestRates(String baseCurrencyCode, List<Currency> currencyList);
    List<Currency> getCurrencies();

}
