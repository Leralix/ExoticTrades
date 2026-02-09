package io.github.leralix.exotictrades.guis.player;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.GuiItem;
import io.github.leralix.exotictrades.guis.IteratorGUI;
import io.github.leralix.exotictrades.item.MarketItem;
import io.github.leralix.exotictrades.item.RareItem;
import io.github.leralix.exotictrades.market.StockMarket;
import io.github.leralix.exotictrades.storage.MarketItemKey;
import io.github.leralix.exotictrades.storage.StorageForGui;
import io.github.leralix.exotictrades.traders.Trader;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MarketInfoMenu extends IteratorGUI {
    public MarketInfoMenu(Player player, Trader trader, int page, StorageForGui storage) {
        super(player, "Market Info", 4, storage);
        gui.setDefaultClickAction(event -> event.setCancelled(true));

        iterator(getGuiItems(trader, storage), p -> new SellItemMenu(player, trader, storage).open());
    }

    private @NotNull List<GuiItem> getGuiItems(Trader trader, StorageForGui storage) {
        List<GuiItem> guiItems = new ArrayList<>();

        for (MarketItemKey marketItemkey : trader.getMarketItemsKey()) {

            MarketItem marketItem = storage.marketItemStorage().getMarketItem(marketItemkey);
            StockMarket stockMarket = storage.stockMarketManager().getMarketFor(MarketItemKey.of(marketItem));

            double price = stockMarket.getCurrentPrice();
            double estimatedPrice = stockMarket.getPriceNextHour();

            String priceEvolutionString = getPriceEvolutionString(price, estimatedPrice);
            List<String> description = new ArrayList<>();
            description.add(ChatColor.WHITE + "Current price : " + priceEvolutionString);

            ItemStack item = marketItem.getItemStack(1);
            ItemMeta meta = item.getItemMeta();
            if (marketItem instanceof RareItem rareItem) {
                meta.setDisplayName(ChatColor.GREEN + rareItem.getName());
            }
            meta.setLore(description);
            item.setItemMeta(meta);

            GuiItem guiItem = ItemBuilder.from(item).asGuiItem();
            guiItems.add(guiItem);
        }
        return guiItems;
    }

    protected String getPriceEvolutionString(double price, double estimatedPrice) {
        if (estimatedPrice > price) {
            return ChatColor.GREEN + "▲" + price;
        } else if (estimatedPrice < price) {
            return ChatColor.RED + "▼" + price;
        } else {
            return ChatColor.GRAY + "▶" + price;
        }
    }
}
