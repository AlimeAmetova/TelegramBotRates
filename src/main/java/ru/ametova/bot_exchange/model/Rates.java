package ru.ametova.bot_exchange.model;

import lombok.Data;
import ru.ametova.bot_exchange.component.enums.CurrencyType;

@Data
public abstract class Rates {
    private double usd;
    private double eur;

    abstract public String formatCurrencyCbrRate(double rate, CurrencyType type);


}
