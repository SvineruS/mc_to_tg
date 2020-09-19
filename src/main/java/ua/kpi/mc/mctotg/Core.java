package ua.kpi.mc.mctotg;

import com.pengrad.telegrambot.Callback;
import com.pengrad.telegrambot.request.GetChat;
import com.pengrad.telegrambot.request.SetChatDescription;
import com.pengrad.telegrambot.response.GetChatResponse;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import ua.kpi.mc.mctotg.utils.MyMessage;
import ua.kpi.mc.mctotg.utils.tellraw.Tellraw;
import ua.kpi.mc.mctotg.utils.tellraw.Text;
import ua.kpi.mc.mctotg.utils.Utils;

import java.io.IOException;
import java.util.Collection;
import java.util.stream.Collectors;


public class Core {

    public static void TgToMc(MyMessage message) {
        Tellraw tellraw = new Tellraw();
        tellraw.add(new Text("[" + message.senderName + "] ").color("aqua").showText("Нажми, что бы ответить").suggestCommand("/tg " + message.originalMessage.messageId().toString() + " "));
        if (message.reply != null) tellraw.add(new Text("[реплай] ").color("gold").showText(message.reply).openUrl(new MyMessage(message.originalMessage.replyToMessage()).getLink()));
        if (message.media != null) tellraw.add(new Text("[" + message.media + "] ").color("gold").showText("Нажми, что бы открыть телегу").openUrl(message.getLink()));
        tellraw.add(new Text(message.text).color("white").showText("Нажми, что бы открыть телегу").openUrl(message.getLink()));
        tellraw.send();
        Utils.CACHE.put(message.originalMessage.messageId(), message);
    }


    public static void McToTg(Player sender, String text, Integer replyTo) {
        if (text.equals(""))
            return;

        String senderName = sender.getName();
        String world = Utils.getWorldIcon(sender.getWorld());

        String msg = world + "<b>" + senderName + "</b>" + ": " + text;
        Main.bot.send_msg(msg, replyTo);
    }

    public static void McToTgReplay(Player sender, String text, Integer replyTo) {
        Core.McToTg(sender, text, replyTo);

        MyMessage cachedMessage = Utils.CACHE.get(replyTo);
        Text rText = cachedMessage == null ? new Text(" [в ответ] ") :
                                             new Text(" [в ответ " + cachedMessage.senderName + "] ")
                                                     .showText(cachedMessage.getMediaAndText()).openUrl(cachedMessage.getLink());

        new Tellraw(
                new Text(sender.getDisplayName()).color("white"),
                rText.color("gold"),
                new Text(text).color("white")
        ).send();
    }



    public static String getOnline() {
        Collection<? extends Player> players = Bukkit.getServer().getOnlinePlayers();
        if (players.size() == 0)
            return "Никого..";

        return players.size() + " тел. \n" + players.stream()
                .map(p -> Utils.getWorldIcon(p.getWorld()) + p.getName())
                .collect(Collectors.joining("\n"));
    }

    public static void UpdateDescription(String online) {
        GetChat request = new GetChat(Main.bot.chatId);
        Main.bot.bot.execute(request, new Callback<GetChat, GetChatResponse>() {
            @Override
            public void onResponse(GetChat getChat, GetChatResponse response) {
                String description = response.chat().description();

                if (description.contains("Online"))
                    description = description.split("Online")[0];
                description += "Online: " + online;

                Main.bot.bot.execute(new SetChatDescription(Main.bot.chatId, description));
            }

            @Override
            public void onFailure(GetChat getChat, IOException e) { e.printStackTrace(); }
        });
    }

    public static void SendJoinText(String name) { Main.bot.send_msg("➡️" + name); }

    public static void SendLeaveText(String name) { Main.bot.send_msg("⬅️" + name); }

}
