package ru.ametova.bot_exchange.client;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.Optional;

@Component
@Slf4j
public class CbrClient {

    @Autowired
    private OkHttpClient okHttpClient;

    @Value("${cbr.currency.rates.xml.url}")
    private String cbrCurrencyRatesXmlUrl;

    public Optional<String> getCurrencyRatesXML()  {
        // Создаём HTTP-запрос для получения XML от ЦБ РФ
        Request currencyRatesRequest = new Request.Builder()
                .url(cbrCurrencyRatesXmlUrl)
                .build();

        try (Response response = okHttpClient.newCall(currencyRatesRequest).execute()) {
            if (response.body() != null) {
                return Optional.of(response.body().string());
            }
            else {
                return Optional.empty();
            }
        }
        catch (IOException e) {
            throw new RuntimeException("Do not receive XML currencies from the Central Bank", e); // Если ВПН включен может возникать эта ошибка
        }
    }
}
