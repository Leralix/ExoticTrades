package org.leralix.exotictrades.guis;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.GuiItem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.leralix.exotictrades.item.SellableItem;
import org.leralix.exotictrades.storage.EconomyManager;
import org.leralix.exotictrades.traders.Trader;
import org.leralix.lib.utils.HeadUtils;

import java.util.ArrayList;
import java.util.List;

public class BuyItemMenu extends BasicGui {


    private Trader trader;

    protected BuyItemMenu(Player player, Trader trader, int page) {
        super(player, "Items", 4);
        this.trader = trader;


        gui.setDefaultClickAction(event -> event.setCancelled(true));
        gui.setDragAction(event -> event.setCancelled(true));

        List<GuiItem> items = new ArrayList<>();

        for(SellableItem item : trader.getTodaySellableItems()){
            int price = item.getPrice();
            ItemStack itemStack = item.getItemStack();
            HeadUtils.addLore(itemStack, "Price: " + price);
            HeadUtils.addLore(itemStack, "Click to buy");

            GuiItem guiItem = ItemBuilder.from(itemStack).asGuiItem(
                event -> {
                    if(EconomyManager.getEconomy().has(player, price)){
                        EconomyManager.getEconomy().withdrawPlayer(player, price);
                        player.getInventory().addItem(itemStack);
                        player.sendMessage("You bought " + itemStack.getType() + " for " + price + " coins.");
                        trader.removeTodaySellableItem(item);
                        new BuyItemMenu(player, trader, page).open();
                    } else {
                        player.sendMessage("You don't have enough coins to buy this item.");
                    }
                }
            );
            items.add(guiItem);
        }


        GuiUtil.createIterator(gui, items, page, player,
                p -> new SellItemMenu(player, trader).open(),
                p -> new BuyItemMenu(player, trader, page + 1).open(),
                p -> new BuyItemMenu(player, trader, page - 1).open());
    }
}
