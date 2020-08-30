package ua.kpi.mc.mctotg;

import com.pengrad.telegrambot.Callback;
import com.pengrad.telegrambot.model.*;
import com.pengrad.telegrambot.request.GetChat;
import com.pengrad.telegrambot.request.SetChatDescription;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.GetChatResponse;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.util.ArrayList;

class Core {

    static void TgToMc(Update update) {
        String name = update.message().from().firstName();
        if (update.message().from().lastName() != null)
            name += " " + update.message().from().lastName();
        name = name.replaceAll("[^\\x00-\\x7Fа-яА-ЯёЁіІїЇ ]", "✭");

        String message_text = "";
        String media = null;
        if (update.message().text() != null)
            message_text = update.message().text();
        else {
            if      (update.message().audio()       != null) media = "аудио";
            else if (update.message().sticker()     != null) media = "стикер";
            else if (update.message().animation()   != null) media = "гиф";
            else if (update.message().photo()       != null) media = "фото";
            else if (update.message().video()       != null) media = "видео";
            else if (update.message().videoNote()   != null) media = "кругляш";
            else if (update.message().voice()       != null) media = "войс";
            else if (update.message().document()    != null) media = "документ";
            else media = "медиа";

            if (update.message().caption() != null) message_text = update.message().caption();
        }

        message_text = message_text.replaceAll("[^\\x00-\\x7Fа-яА-ЯёЁіІїЇ]", "✭");
        message_text = message_text.replaceAll("\n", "   ");

        int msg_id = update.message().messageId();
        String link = "https://t.me/" + update.message().chat().username() + "/" + msg_id;

        String cmd =
                "{\"text\":\"[" + name + "] \",\"color\":\"aqua\",\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"/tg " + msg_id + " \"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"Нажми, что бы ответить\"}}," +
                (media == null ? "" : "{\"text\":\"[" + media + "] \",\"color\":\"gold\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"" + link + "\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"Нажми, что бы открыть телегу\"}},") +
                "{\"text\":\"" + message_text + "\",\"color\":\"white\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"" + link + "\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"Нажми, что бы открыть телегу\"}}]";

        new BukkitRunnable() {
            @Override
            public void run() {Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "tellraw @a [" + cmd + "]");}
        }.runTask(Main.instance);
    }

    static void McToTg(String name, String text, Integer replyTo, String world) {
        if (text.equals(""))
            return;
        world = Main.config.worldNamesDict.getOrDefault(world, world);

        String msg = world + "<b>" + name + "</b>" + ": " + text;
        Main.bot.send_msg(msg, replyTo);
    }

    static void GetOnline() {
        ArrayList<String> players = new ArrayList<>();
        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            players.add(p.getName());
        }
        String text = players.size() + " тел. \n" + String.join(", ", players);
        Main.bot.send_msg(text);
    }

    static void UpdateDescription(String online) {
        GetChat request = new GetChat(Main.bot.chatId);
        Main.bot.bot.execute(request, new Callback<GetChat, GetChatResponse>() {
            @Override
            public void onResponse(GetChat getChat, GetChatResponse response) {
                String description = response.chat().description();

                if (description.contains("Online"))
                    description = description.split("Online")[0];
                description += "Online: " + online;

                SetChatDescription request = new SetChatDescription(Main.bot.chatId, description);
                Main.bot.bot.execute(request, new Callback<SetChatDescription, BaseResponse>() {

                    @Override
                    public void onResponse(SetChatDescription setChatDescription, BaseResponse baseResponse) {
                    }

                    @Override
                    public void onFailure(SetChatDescription setChatDescription, IOException e) {
                        e.printStackTrace();
                    }
                });
            }

            @Override
            public void onFailure(GetChat getChat, IOException e) {
                e.printStackTrace();
            }
        });
    }

    static void SendJoinText(String name) {
        Main.bot.send_msg("➡️" + name);
    }

    static void SendLeaveText(String name) {
        Main.bot.send_msg("⬅️" + name);
    }

}
