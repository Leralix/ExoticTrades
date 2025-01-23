package org.leralix.exotictrades.guis;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.GuiItem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.leralix.exotictrades.traders.Trader;

public class ManageTraderMenu extends GUImenu {


    public ManageTraderMenu(Player player, Trader trader) {
        super(player, "Manage Trader", 3);

        gui.setDefaultClickAction(event -> event.setCancelled(true));


        ItemStack villagerItem = trader.getIcon();
        GuiItem villagerGuiItem = ItemBuilder.from(villagerItem).asGuiItem();


        ItemStack biomeItem = trader.getBiomeType().getIcon();
        GuiItem biomeGuiItem = ItemBuilder.from(biomeItem).asGuiItem(event -> {
            new SelectTraderBiomeMenu(player, trader).open();
        });

        ItemStack workItem = trader.getWorkType().getIcon();
        GuiItem workGuiItem = ItemBuilder.from(workItem).asGuiItem(event -> {
            new SelectTraderProfessionMenu(player, trader).open();
        });



        gui.setItem(1, 5, villagerGuiItem);

        gui.setItem(1, 2, biomeGuiItem);
        gui.setItem(1, 4, workGuiItem);

        gui.setItem(3, 1, IGUI.createBackArrow(player, event -> new TradersMenu(player).open()));
    }

}
