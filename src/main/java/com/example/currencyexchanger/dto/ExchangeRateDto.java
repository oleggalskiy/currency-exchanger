package com.example.currencyexchanger.dto;

import java.math.BigDecimal;

public record ExchangeRateDto(CurrencyDto baseCurrencyCode, CurrencyDto targetCurrency, BigDecimal rate) {
}
