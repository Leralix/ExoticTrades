package io.github.leralix.exotictrades.guis.admin;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.GuiItem;
import io.github.leralix.exotictrades.guis.BasicGui;
import io.github.leralix.exotictrades.guis.GuiUtil;
import io.github.leralix.exotictrades.item.SellableItem;
import io.github.leralix.exotictrades.lang.Lang;
import io.github.leralix.exotictrades.listener.chat.PlayerChatListenerStorage;
import io.github.leralix.exotictrades.listener.chat.events.ChangeItemPrice;
import io.github.leralix.exotictrades.listener.chat.events.RegisterTraderNumberOfSellableItem;
import io.github.leralix.exotictrades.storage.StorageForGui;
import io.github.leralix.exotictrades.traders.Trader;
import io.github.leralix.exotictrades.util.HeadUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ManageTraderItemToSell extends BasicGui {

    private final Trader trader;
    private int page;

    public ManageTraderItemToSell(Player player, Trader trader, StorageForGui storage) {
        super(player, "Manage Trader Item To Sell", 4, storage);
        this.trader = trader;
        this.page = 0;

        gui.setDefaultClickAction(event -> {
            if (event.getClickedInventory() == null) return;
            if (event.getClickedInventory().getType() != InventoryType.PLAYER) {
                event.setCancelled(true);
            }
        });
        gui.setDragAction(event -> event.setCancelled(true));
    }

    @Override
    public void open() {
        openGui();
    }

    private void openGui() {
        List<GuiItem> guiItems = new ArrayList<>();

        for (SellableItem sellableItem : trader.getItemSold()) {
            ItemStack itemStack = sellableItem.getItemStack();

            HeadUtils.addLore(itemStack,
                    Lang.PRICE.get(sellableItem.getPrice()),
                    Lang.CLICK_TO_MODIFY.get(),
                    Lang.RIGHT_CLICK_TO_DELETE.get());

            GuiItem guiItem = ItemBuilder.from(itemStack).asGuiItem(event -> {
                if (event.isLeftClick()) {
                    PlayerChatListenerStorage.register(player, new ChangeItemPrice(trader, sellableItem, storage));
                } else if (event.isRightClick()) {
                    trader.removeSellableItem(sellableItem);
                    new ManageTraderItemToSell(player, trader, storage).open();
                }
            });
            guiItems.add(guiItem);
        }

        GuiUtil.createIterator(gui, guiItems, page, player,
                p -> new ManageTrader(player, trader, storage).open(),
                p -> {
                    this.page++;
                    open();
                },
                p -> {
                    this.page--;
                    open();
                });

        ItemStack addNewSellableItem = HeadUtils.makeSkullURL(Lang.ADD_NEW_SELLABLE_ITEM.get(), "https://textures.minecraft.net/texture/5ff31431d64587ff6ef98c0675810681f8c13bf96f51d9cb07ed7852b2ffd1",
                Lang.DRAG_AND_DROP.get());

        gui.setItem(4, 3, ItemBuilder.from(addNewSellableItem).asGuiItem(event -> {
            ItemStack newItem = event.getCursor();
            if (newItem == null) {
                player.sendMessage(Lang.NO_ITEM_OR_WRONG.get());
                return;
            }
            trader.addSellableItem(new SellableItem(newItem, 100));
            new ManageTraderItemToSell(player, trader, storage).open();
        }));

        ItemStack setNumberOfSellableItemPerDay = HeadUtils.createCustomItemStack(
                Material.PAPER,
                Lang.SET_NUMBER_OF_ITEMS_TO_SELL.get(),
                Lang.SET_NUMBER_OF_ITEMS_TO_SELL_DESC.get(trader.getSellableItemManager().getNbDailySellableItems()),
                Lang.CLICK_TO_MODIFY.get()
        );

        gui.setItem(4, 5, ItemBuilder.from(setNumberOfSellableItemPerDay).asGuiItem(event ->
                PlayerChatListenerStorage.register(player, new RegisterTraderNumberOfSellableItem(trader, storage)))
        );



        gui.open(player);
    }
}
