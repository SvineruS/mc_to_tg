package ua.kpi.mc.mctotg.utils;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;
import ua.kpi.mc.mctotg.Main;

public class Utils {

    public static MaxSizeHashMap<Integer, MyMessage> CACHE = new MaxSizeHashMap<>(100);


    public static void runCommand(String cmd) {
        new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), cmd);
            }
        }.runTask(Main.instance);

    }

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


