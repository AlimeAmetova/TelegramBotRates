package ru.ametova.bot_exchange.client.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import ru.ametova.bot_exchange.exception.NotBeParsedAndRetrieved;
import ru.ametova.bot_exchange.model.json.Eur;
import ru.ametova.bot_exchange.model.json.Usd;
import ru.ametova.bot_exchange.model.json.Valute;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

@Component
@Slf4j
public class CbrJsonRateClient {

    static String url = "https://www.cbr-xml-daily.ru/daily_json.js";


    public String getCurrencyBankRatesJson() {
        try {
            Gson gson = getGson();
            Valute valute = createValute(gson);
            return valute.toString();
        } catch (IOException e){
            throw new RuntimeException("Error parsing json rates in CBR: {}", e);
        }
    }


    private Valute createValute(Gson gson) throws IOException {
        return gson.fromJson(String.valueOf(getValuteObject()), Valute.class);
    }

    private Eur createEur(Gson gson) throws IOException {
       return gson.fromJson(String.valueOf(getEURObject()), Eur.class);
    }

    private Usd createUsd(Gson gson) throws IOException {
        return gson.fromJson(String.valueOf(getUSDObject()), Usd.class);
    }

    @NotNull
    private Gson getGson() {
        return new GsonBuilder()
                .setPrettyPrinting()
                .create();
    }

    private JSONObject getUSDObject() throws IOException {
        return getValuteObject().getJSONObject("USD");
    }

    private JSONObject getEURObject() throws IOException {
        return getValuteObject().getJSONObject("EUR");
    }

    private JSONObject getValuteObject() throws IOException {
        JSONObject jsonObject = new JSONObject(getUrl(new URL(url)));
        return jsonObject.getJSONObject("Valute");
    }

    private String getUrl(URL url) throws IOException {
        try (InputStream input = url.openStream()) {
            InputStreamReader isr = new InputStreamReader(input);
            BufferedReader reader = new BufferedReader(isr);
            StringBuilder json = new StringBuilder();
            int c;
            while ((c = reader.read()) != -1) {
                json.append((char) c);
            }
            return json.toString();
        }
    }
}
