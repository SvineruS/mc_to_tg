package ua.kpi.mc.mctotg;

import com.pengrad.telegrambot.Callback;
import com.pengrad.telegrambot.request.GetChat;
import com.pengrad.telegrambot.request.SetChatDescription;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.GetChatResponse;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.Collection;
import java.util.stream.Collectors;

class Core {

    static void TgToMc(MyMessage message) {
        String cmd =
                "{\"text\":\"[" + message.senderName + "] \",\"color\":\"aqua\"," +
                        "\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"/tg " + message.id + " \"}," +
                        "\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"Нажми, что бы ответить\"}}," +
                (message.media == null ? "" : "{\"text\":\"[" + message.media + "] \",\"color\":\"gold\"," +
                        "\"clickEvent\":{\"action\":\"open_url\",\"value\":\"" + message.link + "\"}," +
                        "\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"Нажми, что бы открыть телегу\"}},") +
                "{\"text\":\"" + message.text + "\",\"color\":\"white\"," +
                        "\"clickEvent\":{\"action\":\"open_url\",\"value\":\"" + message.link + "\"}," +
                        "\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"Нажми, что бы открыть телегу\"}}]";

        Utils.tellraw(cmd);
        Utils.CACHE.put(message.id, message);
    }


    static void McToTg(Player sender, String text, Integer replyTo) {
        if (text.equals(""))
            return;

        String senderName = sender.getName();
        String world = sender.getWorld().getName();
        world = Main.config.worldNamesDict.getOrDefault(world, world);

        String msg = world + "<b>" + senderName + "</b>" + ": " + text;
        Main.bot.send_msg(msg, replyTo);
    }

    static void McToTgReplay(Player sender, String text, Integer replyTo) {
        Core.McToTg(sender, text, replyTo);

        MyMessage cachedMessage = Utils.CACHE.get(replyTo);
        String replyToName = cachedMessage == null ? "" : " " + cachedMessage.senderName;

        String cmd =
                "{\"text\":\"" + sender.getDisplayName() + " \",\"color\":\"white\"}," +
                "{\"text\":\"[в ответ" + replyToName + "] \",\"color\":\"gold\"" +
                (cachedMessage == null ? "" : ",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"" + cachedMessage.link + "\"}," +
                                              "\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"" + cachedMessage.text +"\"}") + "}," +
                "{\"text\":\"" + text + "\",\"color\":\"white\"}]";

        Utils.tellraw(cmd);

    }



    static String getOnline() {
        Collection<? extends Player> players = Bukkit.getServer().getOnlinePlayers();
        if (players.size() == 0)
            return "Никого..";

        return players.size() + " тел. \n" + players.stream().map(HumanEntity::getName).collect(Collectors.joining(", "));
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
