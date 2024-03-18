package com.example.currencyexchanger.service;

import com.example.currencyexchanger.entity.Currency;
import com.example.currencyexchanger.entity.ExchangeRate;
import com.example.currencyexchanger.repository.CurrencyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class SimpleCurrencyService implements CurrencyService {


    private final CurrencyRepository currencyRepository;


    private final ExchangeRateService exchangeRateService;


    @Override
    @Cacheable("currencies")
    public List<Currency> getCurrencies() {
        return currencyRepository.findAll();
    }

    @Override
    public void addCurrency(String code, String name) {
        Currency currency = Currency.builder()
                .code(code)
                .name(name)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
        currencyRepository.save(currency);
        log.info("Add new currency code: {}, name: {}", code, name);
    }

    @Override
    public Map<String, BigDecimal> getExchangeRates(String code) {
        return exchangeRateService.getLatestRates(code);
    }

    @Override
    public Currency getCurrencyByCode(String code) {
        return currencyRepository.findByCode(code);
    }
}
