package org.leralix.exotictrades.guis;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.components.GuiAction;
import dev.triumphteam.gui.guis.GuiItem;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;
import org.leralix.exotictrades.item.SellableItem;
import org.leralix.exotictrades.lang.Lang;
import org.leralix.exotictrades.listener.chat.PlayerChatListenerStorage;
import org.leralix.exotictrades.listener.chat.events.ChangeItemPrice;
import org.leralix.exotictrades.traders.Trader;
import org.leralix.lib.utils.HeadUtils;

import java.util.ArrayList;
import java.util.List;

public class ManageTraderItemToSellPrice extends BasicGui{

    private final Trader trader;
    private int page;

    public ManageTraderItemToSellPrice(Player player, Trader trader) {
        super(player, "Manage Trader Item To Sell price", 4);
        this.trader = trader;
        this.page = 0;

        GuiAction<InventoryDragEvent> actionDrag = event -> event.setCancelled(false);

        GuiAction<InventoryClickEvent> action = event -> event.setCancelled(false);

        gui.setDefaultClickAction(action);
        gui.setDragAction(actionDrag);
    }

    @Override
    public void open() {
        openGui();
    }

    private void openGui() {
        List<GuiItem> guiItems = new ArrayList<>();

        for(SellableItem sellableItem : trader.getItemSold()){

            ItemStack item = sellableItem.getItemStack();

            HeadUtils.addLore(item, Lang.PRICE.get(sellableItem.getPrice()), Lang.CLICK_TO_MODIFY.get());

            GuiItem guiItem = ItemBuilder.from(item).asGuiItem(event ->
                    PlayerChatListenerStorage.register(player, new ChangeItemPrice(trader, sellableItem)));
            guiItems.add(guiItem);
        }

        GuiUtil.createIterator(gui, guiItems, page, player,
                p -> new ManageTrader(player, trader).open(),
                p -> {this.page++; open();},
                p -> {this.page--; open();
        });

        gui.open(player);
    }
}
