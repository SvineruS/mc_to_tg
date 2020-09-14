package ua.kpi.mc.mctotg;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.ChatMember;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.GetChatMember;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.LinkedHashMap;
import java.util.Map;

public class Utils {

    static MaxSizeHashMap<Integer, MyMessage> CACHE = new MaxSizeHashMap<>(100);


    public static void runCommand(String cmd) {
        new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), cmd);
            }
        }.runTask(Main.instance);

    }

    public static void tellraw(String cmd) { runCommand("tellraw @a [" + cmd + "]"); }


    public static String replaceNonMinecraftSymbols(String text) {
        return text
                .replaceAll("[^\\x00-\\x7Fа-яА-ЯёЁіІїЇ]", "✭")
                .replaceAll("\n", "   ")
                .replaceAll("\"", "'");
    }

}


class MaxSizeHashMap<K, V> extends LinkedHashMap<K, V> {
    private final int MAXSIZE;

    public MaxSizeHashMap(int max_size) { this.MAXSIZE = max_size; }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) { return size() > MAXSIZE; }
}

class MyMessage {

    public Integer id;
    public User sender;
    public Chat chat;
    public String text = "";
    public String senderName;
    public String media;
    public String link;

    public MyMessage(Message message) {
        id = message.messageId();
        sender = message.from();
        chat = message.chat();


        senderName = getSenderName(sender);
        link = "https://t.me/" + chat.username() + "/" + id;

        if (message.text() != null)
            text = Utils.replaceNonMinecraftSymbols(message.text());
        else {
            if      (message.caption()          != null) text = Utils.replaceNonMinecraftSymbols(message.caption());

            if      (message.audio()            != null) media = "аудио";
            else if (message.sticker()          != null) media = "стикер";
            else if (message.animation()        != null) media = "гиф";
            else if (message.photo()            != null) media = "фото";
            else if (message.video()            != null) media = "видео";
            else if (message.videoNote()        != null) media = "кругляш";
            else if (message.voice()            != null) media = "войс";
            else if (message.document()         != null) media = "документ";
            else                                         media = "медиа";
        }

    }

    public boolean isCommand() { return text != null && text.startsWith("/"); }

    public String getCommand() { return text.substring(1); }

    public boolean isAdmin() {
        ChatMember.Status status = Main.bot.bot.execute(
                new GetChatMember(chat.id(), sender.id())
        ).chatMember().status();
        return status == ChatMember.Status.administrator || status == ChatMember.Status.creator;
    }

    private String getSenderName(User user) {
        String name = user.firstName();
        if (user.lastName() != null) name += " " + user.lastName();
        return Utils.replaceNonMinecraftSymbols(name);
    }
}