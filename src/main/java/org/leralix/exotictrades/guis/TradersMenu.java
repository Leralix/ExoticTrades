package org.leralix.exotictrades.guis;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.leralix.exotictrades.lang.Lang;
import org.leralix.exotictrades.storage.TraderStorage;
import org.leralix.exotictrades.traders.Trader;
import org.leralix.lib.utils.HeadUtils;

public class TradersMenu {

    private final Gui gui;
    private final Player player;

    public TradersMenu(Player player){
        gui = IGUI.createChestGui("Traders", 6);
        this.player = player;

        for(Trader trader : TraderStorage.getAll()){
            ItemStack item = HeadUtils.makeSkullURL(trader.getID(), "https://textures.minecraft.net/texture/ebba69f6ee3e128bc2feec78c247b2a2f00c3aea11d8906c728de92c60a542ed",
                    Lang.CLICK_TO_CREATE.get());

            GuiItem guiItem = ItemBuilder.from(item).asGuiItem(event -> {

            });

            gui.addItem(guiItem);


        }

        ItemStack addItem = HeadUtils.makeSkullURL(Lang.NEW_TRADER.get(), "https://textures.minecraft.net/texture/ebba69f6ee3e128bc2feec78c247b2a2f00c3aea11d8906c728de92c60a542ed",
                Lang.CLICK_TO_CREATE.get());

        GuiItem item = ItemBuilder.from(addItem).asGuiItem(event -> TraderStorage.register(player.getLocation()));

        gui.setItem(6,3, item);
        gui.setItem(0, IGUI.createBackArrow(player, HumanEntity::closeInventory));
    }

    public void open(){
        gui.open(player);
    }

}
