package ru.ametova.bot_exchange.exception;

public class NotBeParsedAndRetrieved extends Exception{
    public NotBeParsedAndRetrieved(String message) {
        super(message);
    }

    public NotBeParsedAndRetrieved(String message, Throwable cause) {
        super(message, cause);
    }
}
