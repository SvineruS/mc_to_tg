package ua.kpi.mc.mctotg;

import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {


    @Override
    public void onEnable() {

        TelegramBot.Start();
        Core.UpdateDescription("да");
        getServer().getPluginManager().registerEvents(new EventListener(), this);

    }

    @Override
    public void onDisable() {
        Core.UpdateDescription("оффлайн нахуй");
        TelegramBot.Stop();
    }


}
