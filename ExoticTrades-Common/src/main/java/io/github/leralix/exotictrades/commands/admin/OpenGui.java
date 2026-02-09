package io.github.leralix.exotictrades.commands.admin;

import io.github.leralix.exotictrades.guis.MainMenu;
import io.github.leralix.exotictrades.lang.Lang;
import io.github.leralix.exotictrades.market.StockMarketManager;
import io.github.leralix.exotictrades.storage.MarketItemStorage;
import io.github.leralix.exotictrades.storage.StorageForGui;
import io.github.leralix.exotictrades.storage.TraderStorage;
import org.bukkit.entity.Player;
import org.leralix.lib.commands.PlayerSubCommand;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OpenGui extends PlayerSubCommand {

    private final StorageForGui storage;

    public OpenGui(TraderStorage traderStorage, MarketItemStorage marketItemStorage, StockMarketManager stockMarketManager){

        this.storage = new StorageForGui(traderStorage, marketItemStorage, stockMarketManager);
    }
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
        new MainMenu(player, storage);
    }
}