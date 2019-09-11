package ua.kpi.mc.mctotg;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.BotSession;

public class TelegramBot extends TelegramLongPollingBot {

    static TelegramBot Bot;
    private static BotSession telegramBotSession;


    static void Start() {
        ApiContextInitializer.init();
        TelegramBotsApi botsApi = new TelegramBotsApi();
        if (Bot == null) {  // скорее всего юзлесс
            Bot = new TelegramBot();

            try {
                telegramBotSession = botsApi.registerBot(Bot);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    static void Stop() {
        for (Thread t : Thread.getAllStackTraces().keySet()) {
            if (t.getName().equals(Constants.BOT_USERNAME + " Telegram Connection") ||
                t.getName().equals(Constants.BOT_USERNAME + " Telegram Executor"))
                    t.stop();
        }
//        telegramBotSession.stop();   для слабаков
    }


    @Override
    public void onUpdateReceived(Update update) {
        if (!(update.hasMessage() && update.getMessage().hasText()))
            return;
        if (!update.getMessage().getChat().getId().toString().equals(Constants.CHAT_ID)) {
            return;
        }

        String msg_text = update.getMessage().getText();

        if (msg_text.equals("/online")) {
            Core.GetOnline();
        } else
            Core.TgToMc(update);
    }


    void MySendMessage(String text) {
        SendMessage message = new SendMessage().setChatId(Constants.CHAT_ID).setText(text).setParseMode(ParseMode.HTML);
        try {
            execute(message); // Call method to send the message
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    @Override
    public String getBotUsername() {
        return Constants.BOT_USERNAME;
    }

    @Override
    public String getBotToken() {
        return Constants.BOT_TOKEN;
    }

}
