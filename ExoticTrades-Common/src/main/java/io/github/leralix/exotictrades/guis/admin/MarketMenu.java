package io.github.leralix.exotictrades.guis.admin;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import io.github.leralix.exotictrades.guis.BasicGui;
import io.github.leralix.exotictrades.guis.GuiUtil;
import io.github.leralix.exotictrades.market.StockMarket;
import io.github.leralix.exotictrades.storage.StorageForGui;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public class MarketMenu extends BasicGui {
    public MarketMenu(Player player, StorageForGui storage) {
        super(player, "Market Menu", 3, storage);
        gui.setDefaultClickAction(event -> event.setCancelled(true));

        for (StockMarket stock : storage.stockMarketManager().getAllStocks()) {
            ItemStack itemStack = stock.getMarketInfo();
            gui.addItem(ItemBuilder.from(itemStack).asGuiItem());
        }

        gui.setItem(3, 1, GuiUtil.createBackArrow(player, p -> new MainMenu(p, storage)));
    }
}
