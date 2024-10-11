package ru.ametova.bot_exchange.bot;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.ametova.bot_exchange.exception.NotBeParsedAndRetrieved;

import java.io.IOException;

public interface ManagerCommandsBot {

    SendMessage startCommandBot(Long id, String username);

    SendMessage unknownCommandBot(Long chatId);

    SendMessage helpCommandBot(Long chatId);

    SendMessage bankExchangeCommandBot(Long chatId);

    SendMessage cryptoValCommandBot(Long chatId);

    SendMessage eurCbrCommand(Long chatId) throws NotBeParsedAndRetrieved;

    SendMessage usdCbrCommand(Long chatId) throws NotBeParsedAndRetrieved;

    SendMessage valCbrUsdEurCommand(Long chatId) throws NotBeParsedAndRetrieved;

    SendMessage cbrRateWith5PercentCommand(Long chatID) throws NotBeParsedAndRetrieved;

    SendMessage cbrRateWith3PercentCommand(Long chatID) throws NotBeParsedAndRetrieved;
}
