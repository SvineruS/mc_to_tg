package ua.kpi.mc.mctotg;

import com.pengrad.telegrambot.Callback;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.GetMe;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.GetMeResponse;
import com.pengrad.telegrambot.response.SendResponse;

import java.io.IOException;

public class Bot {
    TelegramBot bot;
    Long chatId;

    public Bot(String token, Long chatId) {
        bot = new TelegramBot(token);
        this.chatId = chatId;

        bot.setUpdatesListener(updates -> {
            for (Update update: updates) {
                try {
                    processUpdate(update);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

    public void stop() {
        bot.removeGetUpdatesListener();
    }

    public void send_msg(String text) {
        send_msg(text, null);
    }

    public void send_msg(String text, Integer replyTo) {
        SendMessage request = new SendMessage(chatId, text).parseMode(ParseMode.HTML);
        if (replyTo != null)
            request = request.replyToMessageId(replyTo);

        bot.execute(request, new Callback<SendMessage, SendResponse>() {
            @Override
            public void onResponse(SendMessage sendMessage, SendResponse sendResponse) {}

            @Override
            public void onFailure(SendMessage sendMessage, IOException e) {                        e.printStackTrace();
            }
        });
    }

    public GetMeResponse getMe() {
        return bot.execute(new GetMe());
    }

    void processUpdate(Update update) {
        if (update.message() == null)
            return;
        if (!update.message().chat().id().equals(chatId))
            return;

        if (update.message().text() != null && update.message().text().startsWith("/online")) {
            Core.GetOnline();
            return;
        }
        Core.TgToMc(update);
    }


}
