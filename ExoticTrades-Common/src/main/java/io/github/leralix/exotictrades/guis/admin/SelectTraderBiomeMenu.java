package io.github.leralix.exotictrades.guis.admin;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.GuiItem;
import io.github.leralix.exotictrades.guis.BasicGui;
import io.github.leralix.exotictrades.guis.GuiUtil;
import io.github.leralix.exotictrades.lang.Lang;
import io.github.leralix.exotictrades.storage.StorageForGui;
import io.github.leralix.exotictrades.traders.Trader;
import io.github.leralix.exotictrades.traders.TraderBiome;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SelectTraderBiomeMenu extends BasicGui {
    public SelectTraderBiomeMenu(Player player, Trader trader, StorageForGui storage) {
        super(player, "Select Biome", 3, storage);
        gui.setDefaultClickAction(event -> event.setCancelled(true));

        for(TraderBiome biome : TraderBiome.values()){
            ItemStack item = biome.getIcon(biome.getName(), Lang.CLICK_TO_SELECT);

            GuiItem guiItem = ItemBuilder.from(item).asGuiItem(event -> {
                trader.setBiomeType(biome);
                new ManageTrader(player, trader, storage).open();
            });

            gui.addItem(guiItem);
        }

        gui.setItem(3,1, GuiUtil.createBackArrow(player, event -> new ManageTrader(player, trader, storage).open()));
    }
}
