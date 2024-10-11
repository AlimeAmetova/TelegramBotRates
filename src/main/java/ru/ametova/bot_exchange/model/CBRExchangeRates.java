package ru.ametova.bot_exchange.model;

import lombok.*;
import org.springframework.stereotype.Component;
import ru.ametova.bot_exchange.component.enums.CurrencyType;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.Locale;

@NoArgsConstructor
@Getter
@Setter
@Component
public class CBRExchangeRates extends Rates {

    public String getFormattedUsdRate() {
       return formatCurrencyCbrRate(getUsd(), CurrencyType.USD);
    }

    public String getFormattedEurRate() {
        return formatCurrencyCbrRate(getEur(), CurrencyType.EUR);

    }
    @Override
    public String formatCurrencyCbrRate(double rate, CurrencyType type) {
        try {
            // Создаем DecimalFormat, чтобы одинаково отображался курс (%.4f) не подходит для этой цели
            String formattedRate = getDecimalFormat(rate);
            return String.format("Курс ЦБ РФ %s на %s составляет %s рублей", type, LocalDate.now(), formattedRate);
        } catch (Exception e) {
            return "Не удалось получить текущий курс. Попробуйте позже.";
        }
    }

    private static String getDecimalFormat(double rate) {
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getNumberInstance(new Locale("ru", "RU"));
        formatter.setMinimumFractionDigits(4);
        formatter.setMaximumFractionDigits(4);
        return formatter.format(rate);
    }

    @Override
    public String toString() {
        return String.format("Курс валют по ЦБ на %s составляет: USD %s руб.\nEUR %s руб. ",
                LocalDate.now(), getDecimalFormat(super.getUsd()), getDecimalFormat(super.getEur()));
    }
}
