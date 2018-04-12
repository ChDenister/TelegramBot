import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Bot extends TelegramLongPollingBot {

    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new Bot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    /**
     * Метод возвращает имя бота, указанное при регистрации.
     *
     * @return имя бота
     */
    @Override
    public String getBotUsername() {
        return "myTelegramBot";
    }

    /**
     * Метод возвращает token бота для связи с сервером Telegram
     *
     * @return token для бота
     */
    @Override
    public String getBotToken() {
        return "572949832:AAGGsIoYpWV4f-QEZCFgFXtayDbJbXpYBVQ";
    }

    /**
     * Метод для приема сообщений.
     * Этот метод используется для получения обновлений через long polling (wiki). Ответ возвращается в виде массива объектов Update.
     *
     * @param update Содержит сообщение от пользователя.
     */
    @Override
    public void onUpdateReceived(Update update) {
        Model model = new Model();
        Message message = update.getMessage();
        System.out.println(message);
        if (message != null && message.hasText()) {
            switch (message.getText()) {
                case "/help":
                    sendMsg(message, "Чем могу помочь?");
                    break;
                case "/setting":
                    sendMsg(message, "Что будем настраивать?");
                    break;
                default:
                    try {
                        sendMsg(message, Weather.getWeather(message.getText(), model));

                        //Второй параметр id чата
//                       sendImageFromUrl(Weather.getWeather(),message.getChat().getId());

                    }
//                  Произошло какое-либо исключение ввода-вывода.
                    catch (IOException e) {
                        sendMsg(message, "Город не найден или существует в параллельной вселенной");
                    }
                    break;
            }
        }
    }

    /**
     * Метод для отправки картинки.
     *
     * @param chatId Содержит id чата.
     */
    private void sendImageFromUrl(Long chatId) {
        SendPhoto sendPhotoRequest = new SendPhoto();
        sendPhotoRequest.setChatId(chatId);
        sendPhotoRequest.setPhoto("http://openweathermap.org/img/w/10d.png");
        try {
            sendPhoto(sendPhotoRequest);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    /**
     * Метод для отправки сообщений.
     *
     * @param message Сообщение боту.
     * @param text    Текст ответа
     */
    private void sendMsg(Message message, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(false);
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setText(text);
        try {
            //Устанавливаем для сообщения клавиатуру
            setButtons(sendMessage);
            sendMessage(sendMessage);

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    /**
     * Клавиатура под текстовой панелью.
     *
     * @param sendMessage Объект класса SendMessage, т.е. новое сообщение.
     */
    private void setButtons(SendMessage sendMessage) {
        // Создаем клавиатуру
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        //selective  Этот параметр нужен, чтобы показывать клавиатуру только определённым пользователям
        replyKeyboardMarkup.setSelective(true);
        //resize_keyboard  Указывает клиенту подогнать высоту клавиатуры под количество кнопок (сделать её меньше, если кнопок мало).
        replyKeyboardMarkup.setResizeKeyboard(true);
        // one_time_keyboard  Указывает клиенту скрыть клавиатуру после использования (после нажатия на кнопку)
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        // Создаем список строк клавиатуры
        List<KeyboardRow> keyboard = new ArrayList<>();

        // Первая строчка клавиатуры
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        // Добавляем кнопки в первую строчку клавиатуры
        keyboardFirstRow.add(new KeyboardButton("/help"));
        keyboardFirstRow.add(new KeyboardButton("/setting"));

        // Вторая строчка клавиатуры
//        KeyboardRow keyboardSecondRow = new KeyboardRow();
        // Добавляем кнопки во вторую строчку клавиатуры
//        keyboardSecondRow.add(new KeyboardButton("Nice to meet you"));

        // Добавляем все строчки клавиатуры в список
        keyboard.add(keyboardFirstRow);
//        keyboard.add(keyboardSecondRow);
        // и устанваливаем этот список нашей клавиатуре
        replyKeyboardMarkup.setKeyboard(keyboard);
    }
}