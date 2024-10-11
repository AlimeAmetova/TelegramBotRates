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
    private static final String BANK_ROW_SELECTOR = "#app > div.dTSkA6xB.commercial-branding > div.AuRBdDZg " +
                                                    "> div.CG7gMlX9 > div.FsqCLeET > div.jOWso09k > table > tbody > tr";

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
            Document document = Jsoup.connect(bankCurrencyRatesJsoupUrl).get();
            for (int i = 1; i <= 5; i++) {
                Element bankRow = document.select(BANK_ROW_SELECTOR).get(i);
                String[] usd = parseUsdRates(bankRow);
                String[] eur = parseEurRates(bankRow);
                bankExchangeRates = BankExchangeRates.builder()
                        .bankName(parseBankName(bankRow))
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


    private static String parseBankName(Element bankRow) {
        return bankRow.select("td > div:nth-child(1) > a").text();
    }

    private String [] parseUsdRates(Element bankRow) {
        return bankRow.select("td > div:nth-child(2) > div:nth-child(1)").text().split(" ");
    }

    private String [] parseEurRates(Element bankRow) {
        return bankRow.select("td > div:nth-child(2) > div:nth-child(2)").text().split(" ");
    }

}
