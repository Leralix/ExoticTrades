package org.leralix.exotictrades.commands.admin;

import org.bukkit.command.CommandSender;
import org.leralix.exotictrades.market.StockMarketManager;
import org.leralix.exotictrades.market.TemporalMarketTask;
import org.leralix.exotictrades.traders.TemporalUpdateTraderPosition;
import org.leralix.lib.commands.SubCommand;

import java.util.Collections;
import java.util.List;

public class SkipOneHour extends SubCommand {
    @Override
    public String getName() {
        return "skipHour";
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
        return "/extrade skipHour";
    }

    @Override
    public List<String> getTabCompleteSuggestions(CommandSender commandSender, String s, String[] strings) {
        return Collections.emptyList();
    }

    @Override
    public void perform(CommandSender commandSender, String[] strings) {
        StockMarketManager.updateMovingAverage();
    }
}
