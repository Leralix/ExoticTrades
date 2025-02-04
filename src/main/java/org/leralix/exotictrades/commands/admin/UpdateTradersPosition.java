package org.leralix.exotictrades.commands.admin;

import org.bukkit.command.CommandSender;
import org.leralix.exotictrades.traders.TemporalUpdateTraderPosition;
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
        TemporalUpdateTraderPosition.updateTraderPosition();
    }
}
