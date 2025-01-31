package org.leralix.exotictrades.commands.admin;

import org.bukkit.entity.Player;
import org.leralix.exotictrades.guis.MainMenu;
import org.leralix.exotictrades.guis.TradersMenu;
import org.leralix.exotictrades.lang.Lang;
import org.leralix.lib.commands.PlayerSubCommand;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OpenGui extends PlayerSubCommand {

    @Override
    public String getName() {
        return "gui";
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
        return "/extrade gui";
    }
    public List<String> getTabCompleteSuggestions(Player player, String lowerCase, String[] args){

        List<String> suggestions = new ArrayList<>();
        if (args.length == 2) {
            return Collections.singletonList("gui");
        }
        return suggestions;
    }
    @Override
    public void perform(Player player, String[] args) {
        new MainMenu(player).open();
    }
}