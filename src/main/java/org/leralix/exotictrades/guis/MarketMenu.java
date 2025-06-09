package org.leralix.exotictrades.guis;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.leralix.exotictrades.market.StockMarket;
import org.leralix.exotictrades.market.StockMarketManager;


public class MarketMenu extends BasicGui {
    public MarketMenu(Player player) {
        super(player, "Market Menu", 3);
        gui.setDefaultClickAction(event -> event.setCancelled(true));

        for(StockMarket stock : StockMarketManager.getAllStocks()){
            ItemStack itemStack = stock.getMarketInfo();
            gui.addItem(ItemBuilder.from(itemStack).asGuiItem());
        }

        gui.setItem(3,1, GuiUtil.createBackArrow(player, p -> new MainMenu(p)));
    }
}
