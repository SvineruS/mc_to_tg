package ua.kpi.mc.mctotg.listeners;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class TabCompleteListener implements TabCompleter {

    static final ArrayList<String> SUGGESTION_1 = new ArrayList<>();
    static final ArrayList<String> SUGGESTION_2 = new ArrayList<>();
    static final ArrayList<String> SUGGESTION_3 = new ArrayList<>();
    static {
        SUGGESTION_1.add("чел тут должны были быть цифры, типа куда отвечать, забей короче");
        SUGGESTION_2.add("ответ");
    }


    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length == 1) return SUGGESTION_1;
        if (strings.length == 2) return SUGGESTION_2;
                                 return SUGGESTION_3;
    }
}
