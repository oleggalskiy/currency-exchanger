package com.example.currencyexchanger.service;

import com.example.currencyexchanger.repository.CurrencyRepository;
import com.example.currencyexchanger.repository.ExchangeRateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class SimpleExchangeRateServiceTest {

    @Mock
    private ExchangeRateRepository exchangeRateRepository;

    @InjectMocks
    private SimpleExchangeRateService exchangeRateService;

    @Test
    public void testGetLatestRates_CashMiss() {
        String code = "USD";
        Map<String, BigDecimal> expectedRates = Collections.emptyMap();
        when(exchangeRateRepository.findByBaseCurrency_Code(code)).thenReturn(Collections.emptyList());

        Map<String, BigDecimal> result = exchangeRateService.getLatestRates(code);

        verify(exchangeRateRepository, times(1)).findByBaseCurrency_Code(code);
        assertThat(result).isEqualTo(expectedRates);
    }

}