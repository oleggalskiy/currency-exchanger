package com.example.currencyexchanger.service;

import com.example.currencyexchanger.entity.Currency;
import com.example.currencyexchanger.repository.CurrencyRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class SimpleCurrencyServiceTest {

    @Mock
    private CurrencyRepository currencyRepository;

    @Mock
    private ExchangeRateService exchangeRateService;

    @InjectMocks
    private SimpleCurrencyService simpleCurrencyService;

    @Test
    public void testGetCurrencies() {
        Currency currency1 = Currency.builder().code("USD").name("US Dollar").createdAt(Instant.now()).updatedAt(Instant.now()).build();
        Currency currency2 = Currency.builder().code("EUR").name("Euro").createdAt(Instant.now()).updatedAt(Instant.now()).build();
        List<Currency> currencies = Arrays.asList(currency1, currency2);
        given(currencyRepository.findAll()).willReturn(currencies);

        List<Currency> result = simpleCurrencyService.getCurrencies();

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result).containsExactlyInAnyOrder(currency1, currency2);
    }

    @Test
    public void testAddCurrency() {
        String code = "GBP";
        String name = "British Pound";
        Currency currency = Currency.builder().code(code).name(name).createdAt(Instant.now()).updatedAt(Instant.now()).build();

        simpleCurrencyService.addCurrency(code, name);

        verify(currencyRepository, times(1)).save(currency);
    }

    @Test
    public void testGetExchangeRates() {
        String code = "USD";
        Map<String, BigDecimal> exchangeRates = Map.of("EUR", BigDecimal.valueOf(0.85), "GBP", BigDecimal.valueOf(0.72));
        given(exchangeRateService.getLatestRates(code)).willReturn(exchangeRates);

        Map<String, BigDecimal> result = simpleCurrencyService.getExchangeRates(code);

        assertThat(result).isNotNull();
        assertThat(result).containsKeys("EUR", "GBP");
        assertThat(result.get("EUR")).isEqualByComparingTo(BigDecimal.valueOf(0.85));
        assertThat(result.get("GBP")).isEqualByComparingTo(BigDecimal.valueOf(0.72));
    }

    @Test
    public void testGetCurrencyByCode() {
        String code = "USD";
        Currency currency = Currency.builder().code(code).name("US Dollar").createdAt(Instant.now()).updatedAt(Instant.now()).build();
        given(currencyRepository.findByCode(code)).willReturn(currency);


        Currency result = simpleCurrencyService.getCurrencyByCode(code);

        assertThat(result).isNotNull();
        assertThat(result.getCode()).isEqualTo(code);
        assertThat(result.getName()).isEqualTo("US Dollar");
    }
}