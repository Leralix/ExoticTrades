package io.github.leralix.exotictrades.commands.admin;

import org.bukkit.command.CommandSender;
import io.github.leralix.exotictrades.lang.Lang;
import io.github.leralix.exotictrades.traders.DailyTasks;
import io.github.leralix.exotictrades.util.StringUtil;
import org.leralix.lib.commands.SubCommand;

import java.util.List;

public class UpdateTradersPosition extends SubCommand {


    @Override
    public String getName() {
        return "updateTradersPosition";
    }

    @Override
    public String getDescription() {
        return "Update the position of all traders";
    }

    @Override
    public int getArguments() {
        return 0;
    }

    @Override
    public String getSyntax() {
        return "/extrade updateTradersPosition";
    }

    @Override
    public List<String> getTabCompleteSuggestions(CommandSender commandSender, String s, String[] strings) {
        return List.of();
    }

    @Override
    public void perform(CommandSender commandSender, String[] strings) {
        DailyTasks.update();
        commandSender.sendMessage(StringUtil.getPluginString() + Lang.COMMAND_GENERIC_SUCCESS.get());
    }
}
