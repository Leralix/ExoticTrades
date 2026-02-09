package io.github.leralix.exotictrades.guis;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.GuiItem;
import io.github.leralix.exotictrades.item.SellableItem;
import io.github.leralix.exotictrades.lang.Lang;
import io.github.leralix.exotictrades.storage.EconomyManager;
import io.github.leralix.exotictrades.storage.StorageForGui;
import io.github.leralix.exotictrades.traders.Trader;
import io.github.leralix.exotictrades.util.HeadUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.leralix.lib.data.SoundEnum;
import org.leralix.lib.utils.SoundUtil;

import java.util.ArrayList;
import java.util.List;

public class BuyItemMenu extends IteratorGUI {



    protected BuyItemMenu(Player player, Trader trader, int page, StorageForGui storage) {
        super(player, "Items", 4, storage);


        gui.setDefaultClickAction(event -> event.setCancelled(true));
        gui.setDragAction(event -> event.setCancelled(true));

        List<GuiItem> items = getTodaySellableItems(player, trader, page);
        iterator(getTodaySellableItems(player, trader, page), p -> new SellItemMenu(player, trader, storage).open());
        gui.open(player);
    }

    private @NotNull List<GuiItem> getTodaySellableItems(Player player, Trader trader, int page) {
        List<GuiItem> items = new ArrayList<>();

        for(SellableItem item : trader.getTodaySellableItems()){
            int price = item.getPrice();
            ItemStack itemStack = item.getItemStack();

            HeadUtils.addLore(itemStack, Lang.PRICE.get(price));
            HeadUtils.addLore(itemStack, Lang.CLICK_TO_BUY.get());

            GuiItem guiItem = ItemBuilder.from(itemStack).asGuiItem(
                event -> {
                    if(EconomyManager.getEconomy().has(player, price)){
                        EconomyManager.getEconomy().withdrawPlayer(player, price);
                        player.getInventory().addItem(item.getItemStack());
                        String itemName = itemStack.getType().toString().replace("_", " ").toLowerCase();
                        trader.removeTodaySellableItem(item);
                        player.sendMessage(Lang.TRANSACTION_SUCCESS.get(itemName, price));
                        SoundUtil.playSound(player, SoundEnum.MINOR_GOOD);
                        new BuyItemMenu(player, trader, page, storage).open();
                    } else {
                        player.sendMessage(Lang.NOT_ENOUGH_MONEY.get(price));
                        SoundUtil.playSound(player, SoundEnum.MINOR_BAD);
                    }
                }
            );
            items.add(guiItem);
        }
        return items;
    }
}
