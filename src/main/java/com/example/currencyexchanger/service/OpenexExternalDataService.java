package com.example.currencyexchanger.service;

import com.example.currencyexchanger.entity.Currency;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpenexExternalDataService implements ExternalDataService {

    private final String RATES_API_URL = "https://openexchangerates.org/api/latest.json";
    private final String CURRENCY_API_URL = "https://openexchangerates.org/api/currencies.json";

    @Value("${openex.api.key:defaultApiKey}")
    private final String apikey = null;
//    private final String apikey = "036ba538768d4da584d94cb80e00f338";

    private final RestTemplate restTemplate;


    @Override
    public Map<String, BigDecimal> getLatestRates(String baseCurrencyCode, List<Currency> currencyList) {
        StringBuilder urlBuilder = new StringBuilder(RATES_API_URL);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>("", headers);

        urlBuilder.append("?app_id=").append(apikey);
        if (!baseCurrencyCode.isEmpty()) {
//            urlBuilder.append("&base=").append(baseCurrencyCode); // Unfortunately, only USD can be used on the test plan
            urlBuilder.append("&base=").append("USD");
        } else {
            urlBuilder.append("&base=").append("USD");
        }

        String symbols = currencyList.stream()
                .map(Currency::getCode)
                .collect(Collectors.joining(","));

        if (!symbols.isEmpty()) {
            urlBuilder.append("&symbols=").append(symbols);
        }
        String url = urlBuilder.toString();

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            Map<String, Object> rates = (Map<String, Object>) response.getBody().get("rates");
            return rates.entrySet().stream()
                    .collect(HashMap::new, (map, entry) -> map.put(entry.getKey(), new BigDecimal(String.valueOf(entry.getValue()))), HashMap::putAll);
        } else {
            throw new RuntimeException("Error fetching exchange rates: " + response.getStatusCode());
        }
    }

    @Override
    public List<Currency> getCurrencies() {
        StringBuilder urlBuilder = new StringBuilder(CURRENCY_API_URL);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>("", headers);

        urlBuilder.append("?prettyprint=").append("false");
        urlBuilder.append("&show_alternative=").append("false");
        urlBuilder.append("&show_inactive=").append("false");
        urlBuilder.append("&app_id=").append(apikey);
        String url = urlBuilder.toString();


        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            Map<String, String> responseBody = response.getBody();
            return responseBody.entrySet().stream()
                    .map(entry -> Currency.builder().code(entry.getKey()).name(entry.getValue()).build()).toList();
        } else {
            throw new RuntimeException("Error getting currencies: " + response.getStatusCode());
        }
    }
}
