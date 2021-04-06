package AwNFC;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class AwNBot extends TelegramLongPollingBot {
    @Override
    public String getBotUsername() {
        return "HidJavaBot";
    }

    @Override
    public String getBotToken() {
        return "1733326202:AAHlecig9hexed9pLZUi_eG-kwxr3rEXM_g";
    }

    @Override
    public void onUpdateReceived(Update update) {


    }

    public void sendMessage(String msg){
        SendMessage message = new SendMessage();
        message.setText(msg);
        message.setChatId("-1001472017987");
        message.setParseMode("MARKDOWN");
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
