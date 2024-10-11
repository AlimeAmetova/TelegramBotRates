package ru.ametova.bot_exchange.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.ChatMemberUpdated;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

public interface TelegramBotService {
    
    List<SendMessage> sendNotificationsForUsersBigText() throws TelegramApiException;

    void registerUser(Update update);

    void deleteUserIfBanned(ChatMemberUpdated chatMember);

    List<SendMessage> sendMessageByOwnerForUsers(String message, Long chatId) throws TelegramApiException;
}
