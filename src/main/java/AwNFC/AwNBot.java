package AwNFC;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class AwNBot extends TelegramLongPollingBot {
    @Override
    public String getBotUsername() {
        return "AwNFC_bot";
    }

    @Override
    public String getBotToken() {
        return "1781978308:AAGIOJqs-fQQ6aFU_Z0LsT9W9pQwso8PfXw";
    }

    @Override
    public void onUpdateReceived(Update update) {


    }

    public void sendMessage(String msg){
        SendMessage message = new SendMessage();
        message.setText(msg);
        message.setChatId("1761486952");
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
