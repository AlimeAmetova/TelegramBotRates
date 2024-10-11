package ru.ametova.bot_exchange.component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import java.util.List;

public class Buttons {
    private static final InlineKeyboardButton START_BUTTON = new InlineKeyboardButton("Start");
    private static final InlineKeyboardButton HELP_BUTTON = new InlineKeyboardButton("Help");
    private static final InlineKeyboardButton BANK_USD_EUR_BUTTON = new InlineKeyboardButton("Курсы банков");
    private static final InlineKeyboardButton BANK_USD_CBR = new InlineKeyboardButton("Курс USD ЦБ");
    private static final InlineKeyboardButton BANK_EUR_CBR = new InlineKeyboardButton("Курс EUR ЦБ");
    private static final InlineKeyboardButton CRYPTO = new InlineKeyboardButton("Крипта");
    private static final InlineKeyboardButton CBR_VAL = new InlineKeyboardButton("ЦБ РФ");





    // Такая клавиатура привязывается к определенному сообщению и существует только для него.
    public static InlineKeyboardMarkup inlineMarkup() {

        START_BUTTON.setCallbackData("/start");
        HELP_BUTTON.setCallbackData("/help");
        BANK_USD_EUR_BUTTON.setCallbackData("/bank");
        BANK_EUR_CBR.setCallbackData("/eur");
        BANK_USD_CBR.setCallbackData("/usd");
        CRYPTO.setCallbackData("/crypto");
        CBR_VAL.setCallbackData("/cbr");


        List<InlineKeyboardButton> rowInline = List.of(BANK_USD_EUR_BUTTON, BANK_USD_CBR, BANK_EUR_CBR); // тут менять, чтобы добавить новые кнопки под сообщением в ряд
        List<InlineKeyboardButton> rowInline2 = List.of(CRYPTO, CBR_VAL, HELP_BUTTON); // 2 ряд кнопок
        List<List<InlineKeyboardButton>> rowsInLine = List.of(rowInline, rowInline2);

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        markupInline.setKeyboard(rowsInLine);

        return markupInline;
    }



    public static ReplyKeyboardMarkup keyboardMarkup () {
        // Создаем клавиуатуру
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true); // клавиатура будет отображаться только для конкретного пользователя или сообщения
        replyKeyboardMarkup.setResizeKeyboard(true); // клавиатура масштабируется по ширине
        replyKeyboardMarkup.setOneTimeKeyboard(true); // клавиатура не висит псоле отправки текста
        // Первая строчка клавиатуры
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add(new KeyboardButton("Курс USD c наценкой +5%"));

        // Вторая строчка клавиатуры
        KeyboardRow keyboardSecondRow = new KeyboardRow();
        keyboardSecondRow.add(new KeyboardButton("Курс USD c наценкой +3%"));

        List<KeyboardRow> keyboard = List.of(keyboardFirstRow, keyboardSecondRow);
        replyKeyboardMarkup.setKeyboard(keyboard);

        return replyKeyboardMarkup;
    }



}
