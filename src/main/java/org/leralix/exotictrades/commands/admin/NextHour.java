package org.leralix.exotictrades.commands.admin;

import org.bukkit.command.CommandSender;
import org.leralix.exotictrades.lang.Lang;
import org.leralix.exotictrades.traders.DailyTasks;
import org.leralix.exotictrades.traders.HourlyTasks;
import org.leralix.exotictrades.util.StringUtil;
import org.leralix.lib.commands.SubCommand;

import java.util.Collections;
import java.util.List;

public class NextHour extends SubCommand {
    @Override
    public String getName() {
        return "nexthour";
    }

    @Override
    public String getDescription() {
        return "skip a day in the stock market";
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
        HourlyTasks.update();
        commandSender.sendMessage(StringUtil.getPluginString() + Lang.COMMAND_GENERIC_SUCCESS.get());
    }
}
