package de.avpod.lambda.telegram;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Created by apodznoev
 * date 20.06.2019.
 */
public class MyAmazingBot extends TelegramLongPollingBot {


    @Override
    public void onUpdateReceived(Update update) {
    }

    @Override
    public String getBotUsername() {
        return "AVPod-Bot";
    }

    @Override
    public String getBotToken() {
        return System.getenv("TELEGRAM_BOT_TOKEN");
    }
}
