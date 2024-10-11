package ru.ametova.bot_exchange.bot;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.ametova.bot_exchange.component.BotCommands;
import ru.ametova.bot_exchange.component.Buttons;
import ru.ametova.bot_exchange.exception.NotBeParsedAndRetrieved;
import ru.ametova.bot_exchange.service.BankExchangeRatesService;
import ru.ametova.bot_exchange.service.CbrExchangeRatesService;
import ru.ametova.bot_exchange.service.CryptoRatesService;

import java.io.IOException;

import static ru.ametova.bot_exchange.exception.error.botErrorHandler.getErrorCommand;

@Slf4j
@Component
public class ManagerCommandsBotBotImpl implements ManagerCommandsBot {
    private final BankExchangeRatesService bankRatesService;
    private final CryptoRatesService cryptoRatesService;
    private final CbrExchangeRatesService cbrExchangeRatesService;

    public ManagerCommandsBotBotImpl(BankExchangeRatesService bankRatesService, CryptoRatesService cryptoRatesService, CbrExchangeRatesService cbrExchangeRatesService) {
        this.bankRatesService = bankRatesService;
        this.cryptoRatesService = cryptoRatesService;
        this.cbrExchangeRatesService = cbrExchangeRatesService;
    }


    @Override
    public SendMessage startCommandBot(Long id, String username) {
        var formattedText = String.format(BotCommands.txtStartCommand, username);
        return new TelegramMessageBuilder()
                .text(formattedText)
                .chatId(id)
                .replyKeyboard(Buttons.inlineMarkup())
                .build();
    }

    @Override
    public SendMessage unknownCommandBot(Long chatId) {
        return new TelegramMessageBuilder()
                .text(BotCommands.txtUnknownCommand)
                .chatId(chatId)
                .build();
    }

    @Override
    public SendMessage helpCommandBot(Long chatId) {
        return new TelegramMessageBuilder()
                .text(BotCommands.txtHelpCommand)
                .chatId(chatId)
                .build();
    }

    @Override
    public SendMessage bankExchangeCommandBot(Long chatId) {
        var bankRate = bankRatesService.getCurrencyExchangeRateBank();
        return new TelegramMessageBuilder()
                .text(bankRate)
                .chatId(chatId)
                .build();
    }


    @Override
    public SendMessage cryptoValCommandBot(Long chatId) {
        var cryptoRate = cryptoRatesService.getCurrencyCryptoRate();
        return new TelegramMessageBuilder()
                .text(cryptoRate)
                .chatId(chatId)
                .build();
    }

    @Override
    public SendMessage eurCbrCommand(Long chatId) throws NotBeParsedAndRetrieved {
        var eur = cbrExchangeRatesService.getEURExchangeRateCbrFormatted();
        return new TelegramMessageBuilder()
                .text((eur == null) ? getErrorCommand() : eur)
                .chatId(chatId)
                .build();
    }

    @Override
    public SendMessage usdCbrCommand(Long chatId) throws NotBeParsedAndRetrieved {
        var usd = cbrExchangeRatesService.getUSDExchangeRateCbrFormatted();
        return new TelegramMessageBuilder()
                .text((usd == null) ? getErrorCommand() : usd)
                .chatId(chatId)
                .replyKeyboard(Buttons.keyboardMarkup())
                .build();
    }

    @SneakyThrows
    @Override
    public SendMessage valCbrUsdEurCommand(Long chatId) {
        var valute = cbrExchangeRatesService.getValutesCBR();
        return new TelegramMessageBuilder()
                .text((valute == null) ? getErrorCommand() : valute)
                .chatId(chatId)
                .build();
    }

    @Override
    public SendMessage cbrRateWith5PercentCommand(Long chatID)  {
        var usd5Percent = cbrExchangeRatesService.getCbrUSDRateWith5Percent();
        return new TelegramMessageBuilder()
                .text(usd5Percent)
                .chatId(chatID)
                .build();

    }

    @Override
    public SendMessage cbrRateWith3PercentCommand(Long chatID) {
        var usd3Percent = cbrExchangeRatesService.getCbrEURRateWith3Percent();
        return new TelegramMessageBuilder()
                .text(usd3Percent)
                .chatId(chatID)
                .build();

    }

}
