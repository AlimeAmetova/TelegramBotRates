package ru.ametova.bot_exchange.exception;

public class TelegramRequestErrorUserBlock extends Exception{

    public TelegramRequestErrorUserBlock(String message) {
        super(message);
    }

    public TelegramRequestErrorUserBlock(String message, Throwable cause) {
        super(message, cause);
    }
}
