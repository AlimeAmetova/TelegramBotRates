package ru.ametova.bot_exchange.service.impl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.ametova.bot_exchange.client.BankJsoupClient;
import ru.ametova.bot_exchange.service.BankExchangeRatesService;

import static ru.ametova.bot_exchange.exception.error.botErrorHandler.getErrorCommand;

@Service
@Slf4j
public class BankExchangeRatesServiceImpl implements BankExchangeRatesService {

    private final BankJsoupClient bankJsoupClient;

    public BankExchangeRatesServiceImpl(BankJsoupClient bankJsoupClient) {
        this.bankJsoupClient = bankJsoupClient;
    }

    @Override
    public String getCurrencyExchangeRateBank() {
        try {
            var bankVal = bankJsoupClient.getCurrencyBankRatesJsoup();
            if (bankVal == null || bankVal.isEmpty()) {
                throw new IllegalStateException("Not found banks rate");
            }
            return bankVal;
        } catch (Exception e){
            log.error("Error create bank rates {}: ", e.getMessage());
            return getErrorCommand();
        }

    }
}








