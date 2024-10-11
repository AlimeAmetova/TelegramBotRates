package ru.ametova.bot_exchange.component;

import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import java.util.List;

public interface BotCommands {
    String START = "/start";
    String USD = "/usd";
    String EUR = "/eur";
    String HELP = "/help";
    String BANK = "/bank";
    String USD5Percent = "Курс USD c наценкой +5%";
    String USD3Percent = "Курс USD c наценкой +3%";
    String CRYPTO = "/crypto";
    String SEND_BY_OWNER = "/send22";
    String CBR_VAL = "/cbr";


    List<BotCommand> LIST_OF_COMMANDS = List.of(  // меню сбоку бота
            new BotCommand("/start", "start bot"),
            new BotCommand("/help", "bot info"),
            new BotCommand("/bank", "Курсы в банках"),
            new BotCommand("/usd", "USD ЦБ"),
            new BotCommand("/eur", "EUR ЦБ"),
            new BotCommand("/crypto", "Крипта"),
            new BotCommand("/cbr", "Курсы ЦБ")

    );


    String txtStartCommand = """
                 \s
                 Добро пожаловать в бот, %s!
                 \s
                 Здесь Вы сможете узнать официальные курсы валют на сегодня, установленные ЦБ РФ, а также курс купли/продажи доллара и евро в банках Москвы
                 \s
                 Для этого воспользуйтесь командами:
                 \s
                      /usd - курс доллара ЦБ
                      /eur - курс евро ЦБ
                      /bank - курс купли/продажи доллара и евро в банках Москвы
                      /crypto - курс криптовалют
                      /cbr - курс EUR, USD по ЦБ РФ
                      /help - получение справки
                 \s

            """;

    String txtHelpCommand = """
            Справочная информация по боту
             \s
            Для получения текущих курсов валют воспользуйтесь командами:
                      /usd - курс доллара ЦБ
                      /eur - курс евро ЦБ
                      /bank - курс купли/продажи доллара и евро в банках Москвы
                      /crypto - курс криптовалют
                      /cbr - курс EUR, USD по ЦБ РФ
            \s""";

    String txtUnknownCommand = """
                 Не удалось распознать команду!
                 \s
                 Для этого воспользуйтесь командами:
                 /help - получение справки
                 Если вы не хотите пользоваться этим ботом, то покиньте бот:
                 "Нажмите кнопку блокировать вверху главной страницы".
            
            """;

}


