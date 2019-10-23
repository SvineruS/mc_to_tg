package ua.kpi.mc.mctotg;

import emoji4j.EmojiUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChat;
import org.telegram.telegrambots.meta.api.methods.groupadministration.SetChatDescription;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;


class Core {

    static void TgToMc(Update update) {
        String name = update.getMessage().getFrom().getFirstName();
        if (update.getMessage().getFrom().getLastName() != null)
            name += " " + update.getMessage().getFrom().getLastName();
            name = EmojiUtils.shortCodify(name).replaceAll("[^\\x00-\\x7Fа-яА-ЯёЁіІїЇ]", "");

            String message_text = update.getMessage().getText();
            message_text = EmojiUtils.shortCodify(message_text)
                    .replaceAll("[^\\x00-\\x7Fа-яА-ЯёЁіІїЇ]", "");

        String text = ChatColor.AQUA + "[" + name + "] " + ChatColor.WHITE + message_text;
        Bukkit.broadcastMessage(text);
    }

    static void McToTg(String name, String text) {
        String msg = "<b>" + name + "</b>: " + text;
        TelegramBot.Bot.MySendMessage(msg);
    }

    static void GetOnline() {
        ArrayList<String> players = new ArrayList<>();
        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            players.add(p.getName());
        }
        String text = players.size() + " тел. \n" + String.join(", ", players);
        TelegramBot.Bot.MySendMessage(text);
    }

    static void UpdateDescription(String online) {
        try {
            Chat chat = TelegramBot.Bot.execute(new GetChat(Constants.CHAT_ID));
            String description = chat.getDescription();

            if (description.contains("Online"))
                description = description.split("Online")[0];
            description += "Online: " + online;
            TelegramBot.Bot.execute(new SetChatDescription(Constants.CHAT_ID, description));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    static void SendJoinText(String name) {
        TelegramBot.Bot.MySendMessage("➡️" + name);
    }

    static void SendLeaveText(String name) {
        TelegramBot.Bot.MySendMessage("⬅️" + name);
    }

}
