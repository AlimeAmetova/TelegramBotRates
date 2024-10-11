package ru.ametova.bot_exchange.client;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.ametova.bot_exchange.model.Crypto;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
public class CryptoJsoupClient {

    private final List<String> trSelectors = Arrays.asList("#tr_1", "#tr_919", "#tr_4319", "#tr_210");

    @Value("${crypto.currency.rates.jsoup.url}")
    private String cryptoCurrencyJsoupUrl;


    public String getCurrencyCrypto() {
        StringBuilder builder = new StringBuilder();
        for (Crypto crypto : parseCurrencyCryptoJsoup()) {
            builder.append(String.format("%s $ %.2f \nИзменения за последние 12ч / 7дн:\n%s \n%s", crypto.getNameCrypto(), crypto.getAmountCrypto(),
                    crypto.getChangeCryptoAbout12h(), crypto.getChangeCryptoAbout7Days())).append("\n \n");
        }
        return builder.toString();
    }


    private List<Crypto> parseCurrencyCryptoJsoup() {
        List<Crypto> cryptoList = new ArrayList<>();
        try {
            Document document = Jsoup.connect(cryptoCurrencyJsoupUrl).get();

            for (String trSelector : trSelectors) {
                Element element = document.select(trSelector).get(0);
                //название крипты
                String nameCrypto = getCurrencyName(element, trSelector);
                //сумма
                Double amountCrypto = getCurrencyAmount(element, trSelector);
                //за 12 часов
                String changeCryptoAbout12h = getChange12h(element, trSelector);
                // за 7 дней
                String changeCryptoAbout7Days = getChange7d(element, trSelector);
                Crypto crypto = Crypto.builder()
                        .nameCrypto(nameCrypto)
                        .amountCrypto(amountCrypto)
                        .changeCryptoAbout12h(changeCryptoAbout12h)
                        .changeCryptoAbout7Days(changeCryptoAbout7Days).build();
                cryptoList.add(crypto);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error parsing crypto:", e);
        }
        return cryptoList;
    }

    private String getCurrencyName(Element element, String trSelector) {
        return element.select(trSelector + " > td:nth-child(1)").text();
    }


    private Double getCurrencyAmount(Element element, String trSelector) {
        return Double.valueOf(element.select(trSelector + " > td:nth-child(2) > a").text().replaceAll("[$,]", ""));
    }


    private String getChange7d(Element element, String trSelector) {
        return element.select(trSelector + " > td:nth-child(2) > b > span:nth-child(3)").text();
    }

    private String getChange12h(Element element, String trSelector) {
        return element.select(trSelector + " > td:nth-child(2) > b > span:nth-child(1)").text().trim();

    }

}
