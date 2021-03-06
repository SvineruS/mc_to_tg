package ua.kpi.mc.mctotg;

import com.pengrad.telegrambot.Callback;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.GetMe;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.GetMeResponse;
import com.pengrad.telegrambot.response.SendResponse;
import ua.kpi.mc.mctotg.utils.MyMessage;
import ua.kpi.mc.mctotg.utils.Utils;

import java.io.IOException;

public class Bot {
    private final Integer botId;
    public TelegramBot bot;
    Long chatId;

    public Bot(String token, Long chatId) {
        bot = new TelegramBot(token);
        this.chatId = chatId;
        this.botId = Integer.parseInt(token.split(":")[0]);

        bot.setUpdatesListener(updates -> {
            for (Update update: updates) {
                try { processUpdate(update); }
                catch (Exception e) { e.printStackTrace(); }
            }
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

    public void stop() {
        bot.removeGetUpdatesListener();
    }

    public void send_msg(String text, Integer replyTo) {
        SendMessage request = new SendMessage(chatId, text).parseMode(ParseMode.HTML);
        if (replyTo != null)
            request = request.replyToMessageId(replyTo);

        bot.execute(request, new Callback<SendMessage, SendResponse>() {
            @Override
            public void onResponse(SendMessage sendMessage, SendResponse sendResponse) {}

            @Override
            public void onFailure(SendMessage sendMessage, IOException e) {e.printStackTrace();}
        });
    }
    public void send_msg(String text) {
        send_msg(text, null);
    }


    public GetMeResponse getMe() {
        return bot.execute(new GetMe());
    }

    public boolean isMe(User user) { return user.id().equals(botId); }


    void processUpdate(Update update) {
        if (update.message() == null)
            return;
        if (!update.message().chat().id().equals(chatId))
            return;

        MyMessage message = new MyMessage(update.message());
        processMessage(message);
    }

    void processMessage(MyMessage message) {
        if (message.isCommand())
            processCommands(message);
        else
            Core.TgToMc(message);
    }

    void processCommands(MyMessage message) {
        String command = message.getCommand();

        if (command.startsWith("online"))
            send_msg(Core.getOnline());
        else if (message.isAdmin())
            Utils.runCommand(command);
        else
            send_msg("Только для админов");
    }

}
