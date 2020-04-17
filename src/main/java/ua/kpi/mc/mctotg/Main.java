package ua.kpi.mc.mctotg;

import com.pengrad.telegrambot.response.GetMeResponse;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {
    static Myconfig config;
    static Bot bot;
    static Main instance;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        try {
            initBot();
        } catch (Exception e) {
            e.printStackTrace();
        }


        Core.UpdateDescription("да");

        getCommand("tg").setExecutor(new CommandTg());
        getServer().getPluginManager().registerEvents(new EventListener(), this);
    }

    @Override
    public void onDisable() {
        Core.UpdateDescription("оффлайн нахуй");
        bot.stop();
    }

    public void reload() throws Exception {
        reloadConfig();
        bot.stop();
        initBot();
    }

    private void initBot() throws Exception {
        config = new Myconfig(getConfig());
        bot = new Bot(config.token, config.chatId);
        GetMeResponse r = bot.getMe();
        if (!r.isOk()) {
            bot.stop();
            throw new Exception("can't getMe(). Plugin will shut down. /tg reload for another try. Error message: " + r.description());
        } else {
            getLogger().fine("working as " + r.user().firstName() + " on chat " + config.chatId);
        }
    }
}


class Myconfig {
    String token;
    Long chatId;
    String msgStartWith;

    public Myconfig(FileConfiguration config) {
        this.token = config.getString("token");
        this.chatId = config.getLong("chatId");
        this.msgStartWith = config.getString("msgStartWith");
    }
}