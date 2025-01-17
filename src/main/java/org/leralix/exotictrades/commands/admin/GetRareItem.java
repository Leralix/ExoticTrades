package org.leralix.exotictrades.commands.admin;

import org.bukkit.entity.Player;
import org.leralix.exotictrades.util.DropChances;
import org.leralix.lib.commands.SubCommand;
import org.leralix.lib.lang.Lang;

import java.util.ArrayList;
import java.util.List;

public class GetRareItem extends SubCommand {

    @Override
    public String getName() {
        return "getrareitem";
    }

    @Override
    public String getDescription() {
        return Lang.ADMIN_SPAWN_RARE_ITEM.get();
    }

    @Override
    public int getArguments() {
        return 1;
    }

    @Override
    public String getSyntax() {
        return "/tanadmin getrareitem";
    }
    public List<String> getTabCompleteSuggestions(Player player, String lowerCase, String[] args){

        List<String> suggestions = new ArrayList<>();
        if (args.length == 2) {
            suggestions.add("rarestone");
            suggestions.add("rarewood");
            suggestions.add("rarecrop");
            suggestions.add("raresoul");
            suggestions.add("rarefish");
        }
        return suggestions;
    }
    @Override
    public void perform(Player player, String[] args) {
        switch (args[1]) {
            case "rarestone" -> player.getInventory().addItem(DropChances.getRareStone());
            case "rarewood" ->  player.getInventory().addItem(DropChances.getRareWood());
            case "rarecrop" ->  player.getInventory().addItem(DropChances.getRareCrops());
            case "raresoul" ->  player.getInventory().addItem(DropChances.getRareSoul());
            case "rarefish" ->  player.getInventory().addItem(DropChances.getRareFish());
            default -> {
                player.sendMessage(TanChatUtils.getTANString() + Lang.SYNTAX_ERROR.get());
                return;
            }
        }
        player.sendMessage(TanChatUtils.getTANString() + Lang.COMMAND_GENERIC_SUCCESS.get());
    }
}