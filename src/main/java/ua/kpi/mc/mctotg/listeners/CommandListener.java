package ua.kpi.mc.mctotg.listeners;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ua.kpi.mc.mctotg.Core;
import ua.kpi.mc.mctotg.Main;


import java.util.Arrays;

public class CommandListener implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length >= 1 && args[0].equals("reload")) {
            if (sender.hasPermission("tg.reload")) {
                try {
                    Main.instance.reload();
                    sender.sendMessage("ok");
                } catch (Exception e) {
                    sender.sendMessage(e.getMessage());
                }
                return true;
            }
        }

        if (args.length >= 2 && sender instanceof Player) {
            try {
                int replyTo = Integer.parseInt(args[0]);
                String text = String.join(" ", Arrays.copyOfRange(args, 1, args.length));

                Core.McToTgReplay((Player) sender, text, replyTo);
                return true;
            }
            catch (NumberFormatException e) { return false; }
        }

        return false;
    }


}