package ua.kpi.mc.mctotg;

import com.pengrad.telegrambot.model.ChatMember;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.GetChatMember;
import org.bukkit.Bukkit;
import org.bukkit.World;
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
        if (text == null) return "";
        return text
                .replaceAll("[^\\x00-\\x7Fа-яА-ЯёЁіІїЇ]", "✭")
                .replaceAll("\n", "   ")
                .replaceAll("\"", "'");
    }


    public static String getWorldIcon(World world) {
        String worldName = world.getName();
        return Main.config.worldNamesDict.getOrDefault(worldName,
                Main.config.worldNamesDict.getOrDefault("default", worldName)
        );
    }


}


class MaxSizeHashMap<K, V> extends LinkedHashMap<K, V> {
    private final int MAXSIZE;

    public MaxSizeHashMap(int max_size) { this.MAXSIZE = max_size; }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) { return size() > MAXSIZE; }
}

class MyMessage {

    public Message originalMessage;

    public String text = "";
    public String senderName;
    public String media;
    public String reply;

    public MyMessage(Message message) {
        originalMessage = message;
        senderName = getSenderName(message.from());

        if (message.text() != null) {
            text = Utils.replaceNonMinecraftSymbols(message.text());
        } else {
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

        if (message.replyToMessage() != null) {
            MyMessage replyMessage = new MyMessage(message.replyToMessage());

            if (Main.bot.isMe(replyMessage.originalMessage.from()))
                reply = replyMessage.text;
            else
                reply = replyMessage.senderName + ": " +
                        (replyMessage.media == null ? "" : "[" + replyMessage.media + "] ") +
                        replyMessage.text;
        }

    }

    public String getLink() { return "https://t.me/" + originalMessage.chat().username() + "/" + originalMessage.messageId(); }

    public boolean isCommand() { return text != null && text.startsWith("/"); }

    public String getCommand() { return text.substring(1); }

    public boolean isAdmin() {
        ChatMember.Status status = Main.bot.bot.execute(
                new GetChatMember(originalMessage.chat().id(), originalMessage.from().id())
        ).chatMember().status();
        return status == ChatMember.Status.administrator || status == ChatMember.Status.creator;
    }

    private String getSenderName(User user) {
        String name = user.firstName();
        if (user.lastName() != null) name += " " + user.lastName();
        return Utils.replaceNonMinecraftSymbols(name);
    }
}