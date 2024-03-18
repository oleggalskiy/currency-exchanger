package com.example.currencyexchanger.rest;


import com.example.currencyexchanger.dto.CurrencyDto;
import com.example.currencyexchanger.entity.Currency;
import com.example.currencyexchanger.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/currencies")
public class CurrencyController {

    private final CurrencyService currencyService;

    @GetMapping
    public ResponseEntity<List<Currency>> getCurrencies() {
        return ResponseEntity.ok(currencyService.getCurrencies());
    }

    @PostMapping
    public ResponseEntity<Void> addCurrency(@RequestBody CurrencyDto currency) {
        if (currency.code().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        currencyService.addCurrency(currency.code(), currency.name());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{code}")
    public ResponseEntity<Currency> getCurrencyByCode(@PathVariable String code) {
        Currency currency = currencyService.getCurrencyByCode(code);
        if (currency == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(currency);
    }

    @GetMapping("/{code}/rates")
    public ResponseEntity<Map<String, BigDecimal>> getExchangeRates(@PathVariable String code) {
        return ResponseEntity.ok(currencyService.getExchangeRates(code));
    }
}
