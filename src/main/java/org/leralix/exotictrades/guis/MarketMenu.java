package org.leralix.exotictrades.guis;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.leralix.exotictrades.market.StockMarket;
import org.leralix.exotictrades.market.StockMarketManager;


public class MarketMenu extends basicGUI {
    public MarketMenu(Player player) {
        super(player, "Market Menu", 3);


        for(StockMarket stock : StockMarketManager.getAllStocks()){
            int numberOfSoldItems = stock.getNumberOfSoldItems();
            ItemStack itemStack = new ItemStack(Material.GOLD_INGOT);
            itemStack.setAmount(numberOfSoldItems);
            gui.addItem(ItemBuilder.from(itemStack).asGuiItem());

        }

    }


}
