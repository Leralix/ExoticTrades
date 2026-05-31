package io.github.leralix.exotictrades.commands.admin;

import io.github.leralix.exotictrades.ExoticTrades;
import io.github.leralix.exotictrades.lang.Lang;
import org.bukkit.entity.Player;
import org.leralix.lib.commands.PlayerSubCommand;

import java.util.List;

public class Reload extends PlayerSubCommand {
    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public String getDescription() {
        return "Reload the config.";
    }

    @Override
    public int getArguments() {
        return 1;
    }

    @Override
    public String getSyntax() {
        return "/extrade reload";
    }

    @Override
    public List<String> getTabCompleteSuggestions(Player player, String currentMessage, String[] args) {
        return List.of();
    }

    @Override
    public void perform(Player player, String[] args) {
        ExoticTrades.getPlugin().reload();
        player.sendMessage(Lang.RELOAD_SUCCESS.get());
    }
}
