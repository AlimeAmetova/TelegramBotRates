package ru.ametova.bot_exchange.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.ametova.bot_exchange.client.CryptoJsoupClient;
import ru.ametova.bot_exchange.service.CryptoRatesService;

import static ru.ametova.bot_exchange.exception.error.botErrorHandler.getErrorCommand;

@Slf4j
@Service
public class CryptoRatesServiceImpl implements CryptoRatesService {
    private final CryptoJsoupClient cryptoJsoupClient;

    public CryptoRatesServiceImpl(CryptoJsoupClient cryptoJsoupClient) {
        this.cryptoJsoupClient = cryptoJsoupClient;
    }


    @Override
    public String getCurrencyCryptoRate() {
        try {
            String cryptoVal = cryptoJsoupClient.getCurrencyCrypto();
            if (cryptoVal == null || cryptoVal.isEmpty()) {
                throw new IllegalStateException("Not found crypto rate");
            }
            return cryptoVal;
        } catch (Exception e){
            log.error("Error create crypto rates {}: ", e.getMessage());
            return getErrorCommand();
        }
    }
}
