package org.leralix.exotictrades.guis;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.GuiItem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.leralix.exotictrades.lang.Lang;
import org.leralix.exotictrades.traders.Trader;
import org.leralix.exotictrades.traders.TraderBiome;
import org.leralix.lib.utils.HeadUtils;

public class SelectTraderBiomeMenu extends BasicGui {
    public SelectTraderBiomeMenu(Player player, Trader trader) {
        super(player, "Select Biome", 3);
        gui.setDefaultClickAction(event -> event.setCancelled(true));

        for(TraderBiome biome : TraderBiome.values()){
            ItemStack item = biome.getIcon(biome.getName(), Lang.CLICK_TO_SELECT);

            GuiItem guiItem = ItemBuilder.from(item).asGuiItem(event -> {
                trader.setBiomeType(biome);
                new ManageTrader(player, trader).open();
            });

            gui.addItem(guiItem);
        }

        gui.setItem(3,1, GuiUtil.createBackArrow(player, event -> new ManageTrader(player, trader).open()));




    }


}
