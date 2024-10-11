package ru.ametova.bot_exchange.bot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

public class TelegramMessageBuilder  {
    private Long chatId;
    private String text;
    private ReplyKeyboard replyKeyboard;

    public TelegramMessageBuilder chatId(Long chatId) {
        this.chatId = chatId;
        return this;
    }

    public TelegramMessageBuilder text(String text) {
        this.text = text;
        return this;
    }

    public TelegramMessageBuilder replyKeyboard(ReplyKeyboard replyKeyboard) {
        this.replyKeyboard = replyKeyboard;
        return this;
    }


    public SendMessage build() {
        validate();
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        message.setReplyMarkup(replyKeyboard);
        return message;
    }


    public void validate() {
        if (chatId == null || text == null ) {
            throw new IllegalArgumentException("Invalid or empty ID or text message");
        }
    }

}


