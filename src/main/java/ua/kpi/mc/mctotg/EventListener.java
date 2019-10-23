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
        String name = event.getPlayer().getName();
        String text = event.getMessage();
        Core.McToTg(name, text);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        int online = Bukkit.getServer().getOnlinePlayers().size();
        Core.UpdateDescription(Integer.toString(online));

        Core.SendJoinText(event.getPlayer().getName());
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        int online = Bukkit.getServer().getOnlinePlayers().size() - 1;
        Core.UpdateDescription(Integer.toString(online));

        Core.SendLeaveText(event.getPlayer().getName());
    }

}
