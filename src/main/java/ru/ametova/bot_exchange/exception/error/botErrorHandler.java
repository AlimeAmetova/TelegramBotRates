package ru.ametova.bot_exchange.exception.error;

import org.jetbrains.annotations.NotNull;

public class botErrorHandler {
    public static @NotNull String getErrorCommand() {
        return "Не удалось получить текущий курс. Попробуйте позже";
    }
}
