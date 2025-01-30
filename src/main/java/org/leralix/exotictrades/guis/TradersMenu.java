package org.leralix.exotictrades.guis;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.GuiItem;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.leralix.exotictrades.lang.Lang;
import org.leralix.exotictrades.storage.TraderStorage;
import org.leralix.exotictrades.traders.Trader;
import org.leralix.lib.utils.HeadUtils;

public class TradersMenu extends GUImenu {


    public TradersMenu(Player player) {
        super(player, "Traders", 6);
        gui.setDefaultClickAction(event -> event.setCancelled(true));

        for(Trader trader : TraderStorage.getAll()){
            ItemStack item = trader.getIcon();
            HeadUtils.addLore(item, Lang.LEFT_CLICK_TO_MANAGE.get(), Lang.RIGHT_CLICK_TO_TELEPORT.get());

            GuiItem guiItem = ItemBuilder.from(item).asGuiItem(event -> {
                if(event.isLeftClick()){
                    new ManageTraderMenu(player, trader).open();
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
            TraderStorage.register(player.getLocation());
            new TradersMenu(player).open();
        });

        gui.setItem(6,3, addButton);
        gui.setItem(6,1, GuiUtil.createBackArrow(player, HumanEntity::closeInventory));
    }

}
