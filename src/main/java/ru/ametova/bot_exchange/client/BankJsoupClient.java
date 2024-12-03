package ru.ametova.bot_exchange.client;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.ametova.bot_exchange.model.BankExchangeRates;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class BankJsoupClient {


    @Value("${bank.currency.rates.jsoup.url}")
    private String bankCurrencyRatesJsoupUrl;
    private static final String BANK_ROW_SELECTOR = "#app";

    // DecimalFormat df = new DecimalFormat("#00.00");

    public String getCurrencyBankRatesJsoup (){
        StringBuilder builder = new StringBuilder();
        builder.append("Курсы в Банках Москвы на сегодня ").append(LocalDate.now()).append("\n");
        // Перебираем 5 банков и добавляем его в строку
        for (BankExchangeRates rates : parseCurrencyBankRatesJsoup()) {
            builder.append("\n").append(rates.getBankName())
                    .append(String.format("\nКурс доллара:  %.2f руб. |  %.2f руб.",  rates.getBuyUSD(), rates.getSellUSD()))
                    .append(String.format("\nКурс евро:  %.2f руб. |  %.2f руб.",  rates.getBuyEUR(), rates.getSellEUR()))
                    .append("\n");
        }
        return builder.toString();
    }


    private List<BankExchangeRates> parseCurrencyBankRatesJsoup() {
        BankExchangeRates bankExchangeRates;
        List<BankExchangeRates> ratesList = new ArrayList<>();
        try {
            org.jsoup.nodes.Document document = Jsoup.connect(bankCurrencyRatesJsoupUrl).get();
            Element element = document.select(BANK_ROW_SELECTOR).first();
            for (int i =1; i <= 5; i++){
                String bank = element.select("td").select("a").get(i).text();
                String[] usd = element.select("td > div:nth-child(2) > div:nth-child(1)").get(i).text().split(" ");
                String[] eur = element.select("td > div:nth-child(2) > div:nth-child(2)").get(i).text().split(" ");
                bankExchangeRates = BankExchangeRates.builder()
                        .bankName(bank)
                        .buyUSD(Double.parseDouble(usd[0]))
                        .sellUSD(Double.parseDouble(usd[1]))
                        .buyEUR(Double.parseDouble(eur[0]))
                        .sellEUR(Double.parseDouble(eur[1]))
                        .build();
                ratesList.add(bankExchangeRates);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error parsing rates in banks: {}", e);
        } return ratesList;
    }

}


