package org.leralix.exotictrades.guis;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.GuiItem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.leralix.exotictrades.item.MarketItem;
import org.leralix.exotictrades.traders.Trader;

import java.util.ArrayList;
import java.util.List;

public class MarketInfoMenu extends BasicGui {
    public MarketInfoMenu(Player player, Trader trader, int page) {
        super(player, "Market Info", 4);
        gui.setDefaultClickAction(event -> event.setCancelled(true));
        List<GuiItem> guiItems = new ArrayList<>();

        for(MarketItem marketItem : trader.getMarketItems()){
            ItemStack info = marketItem.getMarketInfoForPlayer();
            GuiItem item = ItemBuilder.from(info).asGuiItem();
            guiItems.add(item);
        }

        GuiUtil.createIterator(gui, guiItems, page, player,
                p -> new TradeMenu(player, trader).open(),
                p -> new MarketInfoMenu(player, trader, page + 1).open(),
                p -> new MarketInfoMenu(player, trader, page - 1).open());

    }
}
