package ua.kpi.mc.mctotg;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class EventListener implements Listener {

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerMessageEvent(AsyncPlayerChatEvent event) {
        String text = event.getMessage();

        if (!text.startsWith(Main.config.msgStartWith)) return;
        text = text.substring(Main.config.msgStartWith.length());

        Core.McToTg(event.getPlayer(), text, null);
    }

    @EventHandler(priority = EventPriority.LOWEST)
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
