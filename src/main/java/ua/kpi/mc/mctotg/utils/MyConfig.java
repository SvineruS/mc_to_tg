package ua.kpi.mc.mctotg.utils;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;

public class MyConfig {
    public String token;
    public Long chatId;
    public String msgStartWith;
    public HashMap<String, String> worldNamesDict = new HashMap<>();

    public MyConfig(FileConfiguration config) {
        this.token = config.getString("token");
        this.chatId = config.getLong("chatId");
        this.msgStartWith = config.getString("msgStartWith");
        if (config.getConfigurationSection("worldNamesDict") != null)
            this.worldNamesDict = (HashMap) config.getConfigurationSection("worldNamesDict").getValues(false);
    }
}
