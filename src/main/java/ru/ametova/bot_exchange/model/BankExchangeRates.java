package ru.ametova.bot_exchange.model;

import lombok.*;


@Data
@Builder
public class BankExchangeRates {
    private String bankName;
    private double buyEUR;
    private double sellEUR;
    private double buyUSD;
    private double sellUSD;

}
