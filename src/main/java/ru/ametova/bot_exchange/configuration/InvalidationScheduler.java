package ru.ametova.bot_exchange.configuration;

import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.ametova.bot_exchange.service.CbrExchangeRatesService;
import ru.ametova.bot_exchange.service.TelegramBotService;

@Component
@AllArgsConstructor
public class InvalidationScheduler {

    private final CbrExchangeRatesService service;
    private final TelegramBotService telegramBotService;


    //@Scheduled - аннотация, которая используется для расписания задач

    @Scheduled(cron = "* 0 0 * * ?")
    public void invalidateCache() {
        service.clearUSDCache();
        service.clearEURCache();

    }

    @Scheduled(cron = "${cron_txt.scheduler}")
    public void invalidateNotice() throws TelegramApiException {
        telegramBotService.sendNotificationsForUsersBigText();
    }
}
