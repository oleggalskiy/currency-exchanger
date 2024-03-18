package com.example.currencyexchanger.service;

import com.example.currencyexchanger.entity.Currency;
import com.example.currencyexchanger.entity.ExchangeRate;
import com.example.currencyexchanger.repository.CurrencyRepository;
import com.example.currencyexchanger.repository.ExchangeRateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class SimpleExchangeRateService implements ExchangeRateService {


    private final CurrencyRepository currencyRepository;


    private final ExchangeRateRepository exchangeRateRepository;

    private final ExternalDataService externalDataService;

    private final Map<String,Map<String, BigDecimal>> currencyRatesCash = new ConcurrentHashMap<>();



    @Override
    public Map<String, BigDecimal> getLatestRates(String code) {
        return Optional.ofNullable(currencyRatesCash.get(code))
                .orElseGet(() -> {
                    List<ExchangeRate> rateList = exchangeRateRepository.findByBaseCurrency_Code(code);
                    return rateList.stream()
                                    .collect(Collectors.toMap(rate -> rate.getTargetCurrency().getCode(), ExchangeRate::getRate));
                        }
                );
    }


    @Scheduled(fixedRate = 3600000)
    private void updateExchangeRates() {
        List<Currency> currencies = currencyRepository.findAll();
        Map<String, Currency> currencyMap = currencies.stream().collect(Collectors.toMap(Currency::getCode, Function.identity()));

        for (Currency currency : currencies) {
            Map<String, BigDecimal> extRates = fetchRatesFromExternalSource(currency.getCode());
            currencyRatesCash.remove(currency.getCode());
            currencyRatesCash.put(currency.getCode(), extRates);
            Map<String, ExchangeRate> exchangeRateFromDb = exchangeRateRepository.findByBaseCurrency_Code(currency.getCode()).stream()
                    .collect(Collectors.toMap(rate -> rate.getTargetCurrency().getCode(), Function.identity()));

            List<ExchangeRate> ratesToSave = extRates.entrySet().stream()
                    .map(entry -> {
                        Currency targetCurrency = currencyMap.get(entry.getKey());
                        ExchangeRate existingRate = exchangeRateFromDb.get(currency.getCode());
                        ExchangeRate rateToSave = Optional.ofNullable(existingRate).orElseGet(() -> ExchangeRate.builder()
                                .baseCurrency(currency)
                                .targetCurrency(targetCurrency)
                                .createdAt(Instant.now())
                                .build());
                        rateToSave.setRate(entry.getValue());
                        rateToSave.setUpdatedAt(Instant.now());
                        return rateToSave;
                    })
                    .collect(Collectors.toList());

            exchangeRateRepository.saveAll(ratesToSave);
        }

        log.info("Exchange rates updated successfully");
    }

    private Map<String, BigDecimal> fetchRatesFromExternalSource(String code) {
        List<Currency> currencyList = currencyRepository.findAll();
        return externalDataService.getLatestRates(code, currencyList);
    }
}
