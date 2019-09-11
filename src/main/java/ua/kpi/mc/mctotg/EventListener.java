package ua.kpi.mc.mctotg;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class EventListener implements Listener {

    @EventHandler
    public void onPlayerMessageEvent(AsyncPlayerChatEvent event) {
        Core.McToTg(event);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        int online = Bukkit.getServer().getOnlinePlayers().size();
        Core.UpdateDescription(Integer.toString(online));
    }
    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        int online = Bukkit.getServer().getOnlinePlayers().size() - 1;
        Core.UpdateDescription(Integer.toString(online));
    }


}
