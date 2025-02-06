package org.leralix.exotictrades.guis;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.GuiItem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.leralix.exotictrades.item.MarketItem;
import org.leralix.exotictrades.traders.Trader;

public class MarketInfoMenu extends basicGUI {
    public MarketInfoMenu(Player player, Trader trader) {
        super(player, "Market Info", 6);
        gui.setDefaultClickAction(event -> event.setCancelled(true));

        for(MarketItem marketItem : trader.getMarketItems()){
            ItemStack info = marketItem.getMarketInfoForPlayer();
            GuiItem item = ItemBuilder.from(info).asGuiItem();
            gui.addItem(item);
        }
        gui.setItem(6,1,GuiUtil.createBackArrow(player, event -> new TradeMenu(player, trader)));
    }
}
