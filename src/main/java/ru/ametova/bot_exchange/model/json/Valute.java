package ru.ametova.bot_exchange.model.json;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.Locale;


@Data
public class Valute  {

    @SerializedName("USD")
    private Usd usd;
    @SerializedName("EUR")
    private Eur eur;


    private static String getDecimalFormat(double rate) {
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getNumberInstance(new Locale("ru", "RU"));
        formatter.setMinimumFractionDigits(4);
        formatter.setMaximumFractionDigits(4);
        return formatter.format(rate);
    }

    @Override
    public String toString() {
        return String.format("Курс валют по ЦБ на %s составляет: USD  %s руб.\nEUR  %s руб. ",
                LocalDate.now(), getDecimalFormat(usd.getRate()), getDecimalFormat(eur.getRate()));
    }
}