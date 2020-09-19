package ua.kpi.mc.mctotg.utils;

import com.pengrad.telegrambot.model.ChatMember;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.GetChatMember;
import ua.kpi.mc.mctotg.Main;

public class MyMessage {

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
            if (message.caption() != null) text = Utils.replaceNonMinecraftSymbols(message.caption());

            if (message.audio() != null) media = "аудио";
            else if (message.sticker() != null) media = "стикер";
            else if (message.animation() != null) media = "гиф";
            else if (message.photo() != null) media = "фото";
            else if (message.video() != null) media = "видео";
            else if (message.videoNote() != null) media = "кругляш";
            else if (message.voice() != null) media = "войс";
            else if (message.document() != null) media = "документ";
            else media = "медиа";
        }

        if (message.replyToMessage() != null) {
            MyMessage replyMessage = new MyMessage(message.replyToMessage());
            reply = replyMessage.isFromBot() ? replyMessage.text :
                    replyMessage.senderName + ": " + replyMessage.getMediaAndText();
        }

    }

    public String getMediaAndText() {
        return (media == null ? "" : "[" + media + "] ") + text;
    }

    public String getLink() {
        return "https://t.me/" + originalMessage.chat().username() + "/" + originalMessage.messageId();
    }

    public boolean isCommand() {
        return text != null && text.startsWith("/");
    }

    public String getCommand() {
        return text.substring(1);
    }

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

    private boolean isFromBot() {
        return Main.bot.isMe(originalMessage.from());
    }
}
