package io.github.leralix.exotictrades.guis.admin;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.GuiItem;
import io.github.leralix.exotictrades.guis.IteratorGUI;
import io.github.leralix.exotictrades.item.MarketItem;
import io.github.leralix.exotictrades.item.RareItem;
import io.github.leralix.exotictrades.lang.Lang;
import io.github.leralix.exotictrades.storage.StorageForGui;
import io.github.leralix.exotictrades.traders.Trader;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ManageTraderAuthorized extends IteratorGUI {

    private final Trader trader;

    public ManageTraderAuthorized(Player player, Trader trader, StorageForGui storage) {
        super(player, "Authorized items", 6, storage);
        this.trader = trader;
        gui.setDefaultClickAction(event -> event.setCancelled(false));

        iterator(getAllMarketItems(), p -> new ManageTrader(player, trader, storage).open());
    }

    private List<GuiItem> getAllMarketItems() {
        List<GuiItem> allMarketItems = new ArrayList<>();
        for(MarketItem marketItem : storage.marketItemStorage().getAllMarketItems()){
            ItemStack itemStack = marketItem.getItemStack(1);

            ItemMeta itemMeta = itemStack.getItemMeta();
            String currentState = trader.canTradeMarketItem(marketItem) ? Lang.CURRENT_STATE_ENABLE.get() : Lang.CURRENT_STATE_DISABLE.get();
            List<String> description = new ArrayList<>();
            description.add(currentState);
            description.add(Lang.CLICK_TO_SWAP.get());
            if(marketItem instanceof RareItem){
                itemMeta.setDisplayName(ChatColor.GREEN + itemMeta.getDisplayName());
            }
            itemMeta.setLore(description);
            itemStack.setItemMeta(itemMeta);
            GuiItem item = ItemBuilder.from(itemStack).asGuiItem(event -> {
                if(trader.canTradeMarketItem(marketItem)){
                    trader.removeMarketItem(marketItem);
                }
                else {
                    trader.addMarketItem(marketItem);
                }
                new ManageTraderAuthorized(player, trader, storage).open();
            });
            allMarketItems.add(item);
        }
        return allMarketItems;
    }
}
