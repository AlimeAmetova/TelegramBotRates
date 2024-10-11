package ru.ametova.bot_exchange.service.impl;

import com.vdurmont.emoji.EmojiParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.ChatMemberUpdated;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMemberBanned;
import ru.ametova.bot_exchange.bot.TelegramMessageBuilder;
import ru.ametova.bot_exchange.model.Users;
import ru.ametova.bot_exchange.repository.NotificationsRepository;
import ru.ametova.bot_exchange.repository.UserRepository;
import ru.ametova.bot_exchange.service.TelegramBotService;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class TelegramBotServiceImpl implements TelegramBotService {
    @Value("${id.owner}")
    Long idOwner;
    private final UserRepository userRepository;
    private final NotificationsRepository notificationsRepository;

    public TelegramBotServiceImpl(UserRepository userRepository, NotificationsRepository notificationsRepository) {
        this.userRepository = userRepository;
        this.notificationsRepository = notificationsRepository;
    }

    @Override
    public void registerUser(Update update) {
        Optional<Users> existingUser = userRepository.findById(update.getMessage().getChatId());
        if (existingUser.isEmpty()) {
            Users user = createUser(update);
            userRepository.save(user);
            log.info("User saved: {}", user);
        } else {
            log.info("User already exists with chatId: {}", update.getMessage().getChatId());
        }
    }

    private Users createUser(Update update){
        Users user = new Users();
        user.setUserNameUser(update.getMessage().getChat().getUserName());
        user.setFirstNameUser(update.getMessage().getChat().getFirstName());
        user.setLastNameUser(update.getMessage().getChat().getLastName());
        user.setChatIdUser(update.getMessage().getChatId());
        user.setRegisteredAtUser(new Timestamp(System.currentTimeMillis()));
        return user;
    }

    @Override
    public void deleteUserIfBanned(ChatMemberUpdated chatMember) {
        var userId = chatMember.getChat().getId();
        var chat = chatMember.getChat();
        if (isUserBanned(chatMember)) {
            Optional<Users> optionalUser = userRepository.findById(userId);
            optionalUser.ifPresent(user -> {
                userRepository.delete(user);
                log.info("User ID ({}) delete in BD UserName: {}", chat.getId(), chat.getFirstName());
            });
        } else {
            throw new RuntimeException("Error delete User");
        }
    }

    public boolean isUserBanned(ChatMemberUpdated chatMember) {
        return chatMember != null && chatMember.getNewChatMember().getStatus().equals(ChatMemberBanned.STATUS);
    }

    //insert into telegrambots.notifications values(1, 'test')
    //либо update
    @Override
    public List<SendMessage> sendNotificationsForUsersBigText() {
        var userAll = userRepository.findAll();
        Long lastNotificationId = notificationsRepository.getLastNotificationId();
        var notifications = notificationsRepository.findById(lastNotificationId);
        List<SendMessage> list = new ArrayList<>();
        if (notifications.isPresent()) {
            for (Users user : userAll) {
                SendMessage sendMessage = new TelegramMessageBuilder()
                        .text(notifications.get().getNotice())
                        .chatId(user.getChatIdUser())
                        .build();
                list.add(sendMessage);
            }
        } else {
            SendMessage sendMessage = new TelegramMessageBuilder()
                    .text("Есть ошибки при отправке сообщений пользователям из базы данных")
                    .chatId(idOwner)
                    .build();
            list.add(sendMessage);
            log.info("Not found notifications in the database");
        }
        return list;
    }


    @Override
    public List<SendMessage> sendMessageByOwnerForUsers(String message, Long chatId) {
        SendMessage sendMessage;
        var users = userRepository.findAll();
        List<SendMessage> list = new ArrayList<>();
        // Проверяем, содержит ли сообщение пробел после команды
        // Проверяем ялвялется ли пользователь idOwner по id (админстратором)
        if (message.contains(" ") && chatId.equals(idOwner)) {
            String textToSend = EmojiParser.parseToUnicode(message.substring(message.indexOf(" ")));
            for (Users user : users) {
                sendMessage = new TelegramMessageBuilder()
                        .text(textToSend)
                        .chatId(user.getChatIdUser())
                        .build();
                list.add(sendMessage);
            }
        } else {
            sendMessage = new TelegramMessageBuilder()
                    .text("Эту комманду может использовать администратор чата или Вы вводите команду неправильно")
                    .chatId(chatId)
                    .build();
            list.add(sendMessage);
        }
        return list;
    }
}
