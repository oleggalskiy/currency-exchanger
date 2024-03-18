package com.example.currencyexchanger.service;

import java.math.BigDecimal;
import java.util.Map;


public interface ExchangeRateService {

    Map<String, BigDecimal> getLatestRates(String code);

}
