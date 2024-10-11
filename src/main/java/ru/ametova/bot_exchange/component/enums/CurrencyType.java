package ru.ametova.bot_exchange.component.enums;


import lombok.Getter;

@Getter
public enum CurrencyType {
    USD("доллар"),
    EUR("евро");

    private final String name;

    CurrencyType(String name) {
        this.name = name;
    }

}

