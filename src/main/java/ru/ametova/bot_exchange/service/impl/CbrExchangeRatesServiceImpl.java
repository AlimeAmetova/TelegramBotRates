package ru.ametova.bot_exchange.service.impl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import ru.ametova.bot_exchange.client.CbrClient;
import ru.ametova.bot_exchange.client.json.CbrJsonRateClient;
import ru.ametova.bot_exchange.component.enums.CurrencyType;
import ru.ametova.bot_exchange.exception.NotBeParsedAndRetrieved;
import ru.ametova.bot_exchange.model.CBRExchangeRates;
import ru.ametova.bot_exchange.service.CbrExchangeRatesService;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.StringReader;

import static ru.ametova.bot_exchange.exception.error.botErrorHandler.getErrorCommand;

@Slf4j
@Service
public class CbrExchangeRatesServiceImpl implements CbrExchangeRatesService {
    private static final String USD_XPATH = "/ValCurs//Valute[@ID='R01235']/Value";
    private static final String EUR_XPATH = "/ValCurs//Valute[@ID='R01239']/Value";
    private final CbrClient cbrClient;
    private final CBRExchangeRates cbrExchangeRates;
    private final CbrJsonRateClient jsonRateClient;

    public CbrExchangeRatesServiceImpl(CbrClient cbrClient, CBRExchangeRates cbrExchangeRates, CbrJsonRateClient jsonRateClient) {
        this.cbrClient = cbrClient;
        this.cbrExchangeRates = cbrExchangeRates;
        this.jsonRateClient = jsonRateClient;
    }

    @Override
    public String getValutesCBR() {
        try {
            return jsonRateClient.getCurrencyBankRatesJson();
        } catch (Exception e) {
            log.error("Error json client CBR rates {}: ", e.getMessage());
            return getErrorCommand();
        }
        //todo дописать логику (этот метод для теста парсинга с помощью json)
        //todo не кэшируется
    }

    @Override
    @Cacheable(value = "usd", unless = "#result == null or #result.isEmpty()")
    public String getUSDExchangeRateCbrFormatted() {
        try {
            var client = cbrClient.getCurrencyRatesXML();
            String xmlClient = client.orElseThrow(() -> new NotBeParsedAndRetrieved("Не удалось получить XML"));
            var usd = extractCurrencyValXML(xmlClient, USD_XPATH).replace(",", ".");
            cbrExchangeRates.setUsd(Double.parseDouble(usd));
            return cbrExchangeRates.getFormattedUsdRate();
        } catch (Exception e) {
            log.error("Error USD CBR rates {}: ", e.getMessage());
            //todo в кэш забивается ошибка и передается вместо валюты (валюта передается текстом)
            return null;
        }
    }


    @Override
    @Cacheable(value = "eur", unless = "#result == null or #result.isEmpty()")
    public String getEURExchangeRateCbrFormatted() {
        try {
            var client = cbrClient.getCurrencyRatesXML();
            String xmlClient = client.orElseThrow(() -> new NotBeParsedAndRetrieved("Не удалось получить XML"));
            var eur = extractCurrencyValXML(xmlClient, EUR_XPATH).replace(",", ".");
            cbrExchangeRates.setEur(Double.parseDouble(eur));
            return cbrExchangeRates.getFormattedEurRate();
        } catch (Exception e) {
            log.error("Error EUR CBR rates {}: ", e.getMessage());
            return null;
        }
    }


    @Override
    public String getCbrUSDRateWith5Percent() {
        try {
            return cbrExchangeRates.formatCurrencyCbrRate(getUSDRateWith5Percent(), CurrencyType.USD);
        } catch (Exception e) {
            log.error("Error usd rates 5% {}: ", e.getMessage());
            return getErrorCommand();
        }
    }

    @Override
    public String getCbrEURRateWith3Percent() {
        try {
            return cbrExchangeRates.formatCurrencyCbrRate(getUSDRateWith3Percent(), CurrencyType.USD);
        } catch (Exception e) {
            log.error("Error usd rates 3% {}: ", e.getMessage());
            return getErrorCommand();
        }
    }


    private static String extractCurrencyValXML(String client, String xpathEuroOrUsd)
            throws NotBeParsedAndRetrieved {
        InputSource source = new InputSource(new StringReader(client));
        try {
            XPath xpath = XPathFactory.newInstance().newXPath();
            Document document = (Document) xpath.evaluate("/", source, XPathConstants.NODE);
            return xpath.evaluate(xpathEuroOrUsd, document);
        } catch (Exception e) {
            throw new NotBeParsedAndRetrieved("Error parsing XML", e);
        }
    }

    @CacheEvict("usd")
    @Override
    public void clearUSDCache() {
        log.info("Cache \"usd\" cleared!");
    }

    @CacheEvict("eur")
    @Override
    public void clearEURCache() {
        log.info("Cache \"eur\" cleared!");
    }


    public Double getUSDRateWith5Percent() {
        // Получаем исходный курс + 5%
        var exchangeRate = cbrExchangeRates.getUsd();
        var result = exchangeRate * 1.05;
        if (!Double.isNaN(result) && result != 0.0) {
            return result;
        } else {
            throw new ArithmeticException("Exchange rate not valid.");
        }
    }

    public Double getUSDRateWith3Percent() {
        // Получаем исходный курс + 3%
        var exchangeRate = cbrExchangeRates.getUsd();
        var result = exchangeRate * 1.03;
        if (!Double.isNaN(result) && result != 0.0) {
            return result;
        } else {
            throw new ArithmeticException("Exchange rate not a valid.");
        }
    }
}
