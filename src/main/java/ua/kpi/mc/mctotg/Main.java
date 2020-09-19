package ua.kpi.mc.mctotg;

import com.pengrad.telegrambot.response.GetMeResponse;
import org.bukkit.plugin.java.JavaPlugin;
import ua.kpi.mc.mctotg.listeners.CommandListener;
import ua.kpi.mc.mctotg.listeners.EventListener;
import ua.kpi.mc.mctotg.listeners.TabCompleteListener;
import ua.kpi.mc.mctotg.utils.MyConfig;

public final class Main extends JavaPlugin {
    public static MyConfig config;
    public static Bot bot;
    public static Main instance;

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

        getCommand("tg").setExecutor(new CommandListener());
        getCommand("tg").setTabCompleter(new TabCompleteListener());

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
        config = new MyConfig(getConfig());
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


