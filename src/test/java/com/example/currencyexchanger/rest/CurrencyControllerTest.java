package com.example.currencyexchanger.rest;

import com.example.currencyexchanger.dto.CurrencyDto;
import com.example.currencyexchanger.entity.Currency;
import com.example.currencyexchanger.service.CurrencyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CurrencyControllerTest {

    @Mock
    private CurrencyService currencyService;

    CurrencyController controller;


    @BeforeEach
    void prepare() {
        controller = new CurrencyController(currencyService);
    }

    @Test
    void getCurrencies() {
        // Arrange
        List<Currency> currencies = Arrays.asList(
                Currency.builder().code("USD").name("United States Dollar").build(),
                Currency.builder().code("EUR").name("Euro").build(),
                Currency.builder().code("GBP").name("British Pound").build()
        );
        when(currencyService.getCurrencies()).thenReturn(currencies);


        // Act
        ResponseEntity<List<Currency>> response = controller.getCurrencies();

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(currencies);

    }

    @Test
    void addCurrency() {
        // Arrange
        CurrencyDto currencyDto = new CurrencyDto("USD", "United States Dollar");

        // Act
        ResponseEntity<Void> response = controller.addCurrency(currencyDto);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(currencyService, times(1)).addCurrency(currencyDto.code(), currencyDto.name());
    }

    @Test
    void getCurrencyByCode() {
        String code = "USD";
        Currency currency = Currency.builder().code("USD").name("United States Dollar").build();
        when(currencyService.getCurrencyByCode(code)).thenReturn(currency);

        ResponseEntity<Currency> response = controller.getCurrencyByCode(code);


        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(currency);
    }

}