package org.leralix.exotictrades.commands.admin;

import org.bukkit.entity.Player;
import org.leralix.exotictrades.item.MarketItem;
import org.leralix.exotictrades.item.RareItem;
import org.leralix.exotictrades.lang.Lang;
import org.leralix.exotictrades.storage.RareItemStorage;
import org.leralix.exotictrades.util.StringUtil;
import org.leralix.lib.commands.PlayerSubCommand;

import java.util.ArrayList;
import java.util.List;

public class GetRareItem extends PlayerSubCommand {

    @Override
    public String getName() {
        return "get";
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
        return "/exotictradeadmin get";
    }
    public List<String> getTabCompleteSuggestions(Player player, String lowerCase, String[] args){

        List<String> suggestions = new ArrayList<>();
        if (args.length == 2) {
            List<RareItem> rareItemList = RareItemStorage.getAllRareItems();
            for (RareItem rareItem : rareItemList) {
                suggestions.add(rareItem.getName().replace(" ", "_"));
            }
        }
        if(args.length == 3){
            suggestions.add("1");
            suggestions.add("64");
        }
        return suggestions;
    }
    @Override
    public void perform(Player player, String[] args) {

        if(args.length < 2 || args.length > 3) {
            return;
        }

        String itemName = args[1].replace("_", " ");
        MarketItem marketItem = RareItemStorage.getMarketItem(itemName);

        if (marketItem == null) {
            player.sendMessage(StringUtil.getPluginString() + Lang.ITEM_NOT_FOUND.get());
            return;
        }

        if (args.length == 3) {
            int amount = Integer.parseInt(args[2]);
            player.getInventory().addItem(marketItem.getItemStack(amount));
        } else {
            player.getInventory().addItem(marketItem.getItemStack(1));
        }
        player.sendMessage(StringUtil.getPluginString() + Lang.COMMAND_GENERIC_SUCCESS.get());
    }
}