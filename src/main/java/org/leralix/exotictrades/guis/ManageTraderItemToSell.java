package org.leralix.exotictrades.guis;

import dev.triumphteam.gui.components.GuiAction;
import dev.triumphteam.gui.guis.GuiItem;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;
import org.leralix.exotictrades.ExoticTrades;
import org.leralix.exotictrades.item.SellableItem;
import org.leralix.exotictrades.traders.Trader;

import java.util.ArrayList;
import java.util.List;

public class ManageTraderItemToSell extends BasicGui{

    private final Trader trader;
    private int page;

    public ManageTraderItemToSell(Player player, Trader trader) {
        super(player, "Manage Trader Item To Sell", 4);
        this.trader = trader;
        this.page = 0;

        GuiAction<InventoryDragEvent> actionDrag = event -> {
            event.setCancelled(false);
            Bukkit.getScheduler().runTask(ExoticTrades.getPlugin(), this::updateItems);
        };

        GuiAction<InventoryClickEvent> action = event -> {
            event.setCancelled(false);
            Bukkit.getScheduler().runTask(ExoticTrades.getPlugin(), this::updateItems);
        };

        gui.setDefaultClickAction(action);
        gui.setDragAction(actionDrag);
    }

    private void updateItems() {
        List<SellableItem> allItems = new ArrayList<>();
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 8; j++){
                ItemStack item = gui.getInventory().getItem(i * 9 + j);
                if (item != null) {
                    SellableItem sellableItem = new SellableItem(item, 10);
                    allItems.add(sellableItem);
                }
            }
        }
        trader.updateItemSold(allItems);
    }

    @Override
    public void open() {
        openGui();
    }

    private void openGui() {
        List<GuiItem> guiItems = new ArrayList<>();

        for(SellableItem itemStack : trader.getItemSold()){
            guiItems.add(new GuiItem(itemStack.getItemStack()));
        }

        GuiUtil.createIterator(gui, guiItems, page, player,
                p -> new ManageTrader(player, trader).open(),
                p -> {this.page++; open();},
                p -> {this.page--; open();
        });

        gui.open(player);
    }
}
