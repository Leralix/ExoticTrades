package org.leralix.exotictrades.guis;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.GuiItem;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.leralix.exotictrades.item.MarketItem;
import org.leralix.exotictrades.item.RareItem;
import org.leralix.exotictrades.lang.Lang;
import org.leralix.exotictrades.storage.MarketItemStorage;
import org.leralix.exotictrades.traders.Trader;

import java.util.ArrayList;
import java.util.List;

public class ManageTraderAuthorized extends BasicGui {

    public ManageTraderAuthorized(Player player, Trader trader, int page) {
        super(player, "Authorized items", 6);
        gui.setDefaultClickAction(event -> event.setCancelled(false));


        List<GuiItem> allMarketItems = new ArrayList<>();
        for(MarketItem marketItem : MarketItemStorage.getAllMarketItems()){
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
                new ManageTraderAuthorized(player, trader, page).open();
            });
            allMarketItems.add(item);
        }

        GuiUtil.createIterator(gui, allMarketItems, page, player,
                p -> new ManageTrader(player, trader).open(),
                p -> new ManageTraderAuthorized(player, trader, page + 1).open(),
                p -> new ManageTraderAuthorized(player, trader, page - 1).open());


    }
}
