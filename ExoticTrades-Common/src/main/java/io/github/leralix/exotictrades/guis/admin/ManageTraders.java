package io.github.leralix.exotictrades.guis.admin;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.GuiItem;
import io.github.leralix.exotictrades.guis.BasicGui;
import io.github.leralix.exotictrades.guis.GuiUtil;
import io.github.leralix.exotictrades.lang.Lang;
import io.github.leralix.exotictrades.storage.StorageForGui;
import io.github.leralix.exotictrades.traders.Trader;
import io.github.leralix.exotictrades.util.HeadUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ManageTraders extends BasicGui {


    public ManageTraders(Player player, StorageForGui storage) {
        super(player, "Traders", 6, storage);
        gui.setDefaultClickAction(event -> event.setCancelled(true));

        for(Trader trader : storage.traderStorage().getAll()){
            ItemStack item = trader.getIcon();
            HeadUtils.addLore(item, Lang.LEFT_CLICK_TO_MANAGE.get(), Lang.RIGHT_CLICK_TO_TELEPORT.get());

            GuiItem guiItem = ItemBuilder.from(item).asGuiItem(event -> {
                if(event.isLeftClick()){
                    new ManageTrader(player, trader, storage).open();
                }
                else if(event.isRightClick()){
                    player.teleport(trader.getLocation());
                    player.closeInventory();
                }
            });

            gui.addItem(guiItem);
        }

        ItemStack addItem = HeadUtils.makeSkullURL(Lang.NEW_TRADER.get(), "https://textures.minecraft.net/texture/ebba69f6ee3e128bc2feec78c247b2a2f00c3aea11d8906c728de92c60a542ed",
                Lang.CLICK_TO_CREATE.get());

        GuiItem addButton = ItemBuilder.from(addItem).asGuiItem(event -> {
            storage.traderStorage().register(player.getLocation(), storage.marketItemStorage().getAllMarketItemsKey());
            new ManageTraders(player, storage).open();
        });

        gui.setItem(6,3, addButton);
        gui.setItem(6,1, GuiUtil.createBackArrow(player, p -> new MainMenu(player, storage).open()));
    }

}
