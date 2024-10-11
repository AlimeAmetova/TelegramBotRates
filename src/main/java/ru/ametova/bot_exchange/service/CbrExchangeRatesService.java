package ru.ametova.bot_exchange.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import ru.ametova.bot_exchange.exception.NotBeParsedAndRetrieved;

import java.io.IOException;

public interface CbrExchangeRatesService {

    String getValutesCBR() throws IOException;

    @Cacheable(value = "usd", unless = "#result == null or #result.isEmpty()")
    String getUSDExchangeRateCbrFormatted() throws NotBeParsedAndRetrieved;

    @Cacheable(value = "eur", unless = "#result == null or #result.isEmpty()")
    String getEURExchangeRateCbrFormatted() throws NotBeParsedAndRetrieved;

    String getCbrUSDRateWith5Percent();

    String getCbrEURRateWith3Percent();

    @CacheEvict("usd")
    void clearUSDCache();

    @CacheEvict("eur")
    void clearEURCache();
}
