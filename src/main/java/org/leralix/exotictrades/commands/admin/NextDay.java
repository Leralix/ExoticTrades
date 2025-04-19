package org.leralix.exotictrades.commands.admin;

import org.bukkit.command.CommandSender;
import org.leralix.exotictrades.lang.Lang;
import org.leralix.exotictrades.traders.DailyTasks;
import org.leralix.exotictrades.traders.HourlyTasks;
import org.leralix.exotictrades.util.StringUtil;
import org.leralix.lib.commands.SubCommand;

import java.util.Collections;
import java.util.List;

public class NextDay extends SubCommand {
    @Override
    public String getName() {
        return "nextday";
    }

    @Override
    public String getDescription() {
        return "skip an hour in the stock market";
    }

    @Override
    public int getArguments() {
        return 0;
    }

    @Override
    public String getSyntax() {
        return "/extrade nextday";
    }

    @Override
    public List<String> getTabCompleteSuggestions(CommandSender commandSender, String s, String[] strings) {
        return Collections.emptyList();
    }

    @Override
    public void perform(CommandSender commandSender, String[] strings) {
        DailyTasks.update();
        commandSender.sendMessage(StringUtil.getPluginString() + Lang.COMMAND_GENERIC_SUCCESS.get());
    }
}
