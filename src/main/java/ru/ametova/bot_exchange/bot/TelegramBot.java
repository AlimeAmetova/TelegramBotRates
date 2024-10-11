package ru.ametova.bot_exchange.bot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.ChatMemberUpdated;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMemberBanned;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import ru.ametova.bot_exchange.component.BotCommands;
import ru.ametova.bot_exchange.exception.NotBeParsedAndRetrieved;
import ru.ametova.bot_exchange.exception.TelegramRequestErrorUserBlock;
import ru.ametova.bot_exchange.service.TelegramBotService;
import java.util.List;

@Component
@Slf4j
public class TelegramBot extends TelegramLongPollingBot implements BotCommands {
    private final TelegramBotService telegramBotService;
    private final ManagerCommandsBot managerCommandsBot;

    public TelegramBot(@Value("${bot.token}")
                       String botToken,
                       TelegramBotService telegramBotService,
                       ManagerCommandsBot managerCommandsBot) {
        super(botToken);
        this.telegramBotService = telegramBotService;
        this.managerCommandsBot = managerCommandsBot;
        try {
            execute(new SetMyCommands(LIST_OF_COMMANDS, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }


    @Value("${bot.name}")
    String name;
    @Override
    public String getBotUsername() {
        log.info("Name bot: {}", name);
        return name;
    }

    @Override
    public void onUpdateReceived(Update update) {

        try {
            if (update.hasMessage() && update.getMessage().hasText()) {
                handlerMessage(update);
            } else if (update.hasCallbackQuery()) {
              handlerCallback(update);
            } else if (update.hasMyChatMember()) {
                var chat = update.getMyChatMember().getChat();
                var chatMember = update.getMyChatMember();
                extractedMemberChat(chatMember, chat);
            }
        } catch (Exception e) {
            log.error("Error processing commands: {}", e.getMessage(), e);
        }
    }

    private void handlerMessage(Update update) throws NotBeParsedAndRetrieved, TelegramApiException, TelegramRequestErrorUserBlock {
        var message = update.getMessage().getText();
        var chatId = update.getMessage().getChatId();
        var userName = getUserName(update);
        if (message.contains(SEND_BY_OWNER)) { // если сообщение вводимое в тг содержит специльную комманду админа
            sendMessageForUserInBot(message, chatId);
        }
        else {
            botCommands(message, chatId, userName, update);
        }
    }

    private void handlerCallback(Update update) throws NotBeParsedAndRetrieved {
        var chatId = update.getCallbackQuery().getMessage().getChatId();
        var userName = update.getCallbackQuery().getFrom().getFirstName();
        var message = update.getCallbackQuery().getData();
        botCommands(message, chatId, userName, update);
    }


    private String getUserName(Update update) {
        return update.getMessage().getChat().getUserName() != null
                ? update.getMessage().getChat().getUserName() : update.getMessage().getChat().getFirstName() ;
    }

    private void extractedMemberChat(ChatMemberUpdated chatMember, Chat chat) {
        if (chatMember != null && chatMember.getOldChatMember().getStatus().equals(ChatMemberBanned.STATUS)) {
            log.info("User ID ({}) has been joined after being a kicked. UserName: {}", chat.getId(), chat.getFirstName());
        } else if (chatMember != null && chatMember.getNewChatMember().getStatus().equals(ChatMemberBanned.STATUS)) {
            log.warn("User ID ({}) kicked the chat. UserName: {}", chat.getId(), chat.getFirstName());
            telegramBotService.deleteUserIfBanned(chatMember); // удаляем пользователя из базы
            }
        }


    private void botCommands(String message, Long chatId, String userName, Update update) throws NotBeParsedAndRetrieved {
        switch (message) {
            case START -> {
                telegramBotService.registerUser(update);
                executeMessage(managerCommandsBot.startCommandBot(chatId, userName));
            }
            case USD -> executeMessage(managerCommandsBot.usdCbrCommand(chatId));
            case EUR -> executeMessage(managerCommandsBot.eurCbrCommand(chatId));
            case HELP -> executeMessage(managerCommandsBot.helpCommandBot(chatId));
            case BANK -> executeMessage(managerCommandsBot.bankExchangeCommandBot(chatId));
            case USD5Percent -> executeMessage(managerCommandsBot.cbrRateWith5PercentCommand(chatId));
            case USD3Percent -> executeMessage(managerCommandsBot.cbrRateWith3PercentCommand(chatId));
            case CRYPTO -> executeMessage(managerCommandsBot.cryptoValCommandBot(chatId));
            case CBR_VAL -> executeMessage(managerCommandsBot.valCbrUsdEurCommand(chatId));
            default -> executeMessage(managerCommandsBot.unknownCommandBot(chatId));
        }
    }

    //отправка сообщений пользователям из базы по времени
    @Scheduled(cron = "${cron_txt.scheduler}")
    private void sendMessageForUser() throws TelegramApiException, TelegramRequestErrorUserBlock {
        executeMessage(telegramBotService.sendNotificationsForUsersBigText());
    }

    // отправка сообщений пользователя при введении команды админом
    private void sendMessageForUserInBot(String message, Long chatId) throws TelegramApiException, TelegramRequestErrorUserBlock {
        executeMessage(telegramBotService.sendMessageByOwnerForUsers(message, chatId));
    }

    private void executeMessage(SendMessage message) {
        try {
            execute(message);
            log.info("Response ChatId: {} text: {}", message.getChatId(), message.getText());
        } catch (TelegramApiException e) {
            log.error("Error sending the message: {}", e.getMessage());
        }
    }

    private void executeMessage(List<SendMessage> messages) throws TelegramRequestErrorUserBlock {
        if (messages != null) {
            for (SendMessage message : messages) {
                try {
                    execute(message);
                    log.info("Response message ChatId: {} text: {}", message.getChatId(), message.getText());
                } catch (TelegramApiRequestException e){
                    throw new TelegramRequestErrorUserBlock("Сообщение не доставлено, возможно пользователь заблокирован", e);
                } catch (TelegramApiException e ) {
                    log.error("Error message sending : {}", e.getMessage());
                }
            }
        } else {
            log.info("No messages to send");
        }
    }
}








