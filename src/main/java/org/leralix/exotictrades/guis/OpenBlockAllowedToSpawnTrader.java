package org.leralix.exotictrades.guis;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.GuiItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.leralix.exotictrades.ExoticTrades;
import org.leralix.exotictrades.traders.SpawnZone;
import org.leralix.exotictrades.traders.Trader;
import org.leralix.lib.utils.HeadUtils;

import java.util.ArrayList;
import java.util.List;

public class OpenBlockAllowedToSpawnTrader extends basicGUI {

    Trader trader;

    public OpenBlockAllowedToSpawnTrader(Player player, Trader trader) {
        super(player, "Block Allowed", 6);
        this.trader = trader;

        gui.setDefaultClickAction(event ->
                Bukkit.getScheduler().runTask(ExoticTrades.getPlugin(), this::reloadAuthorizedBlocks)
        );

        for(Material material : trader.getRandomSpawnZone().getAuthorizedBlocks()){
            GuiItem guiItem = ItemBuilder.from(HeadUtils.createCustomItemStack(material, " ")).asGuiItem(event -> {
                new OpenBlockAllowedToSpawnTrader(player, trader).open();
            });
            gui.addItem(guiItem);
        }

        GuiItem fillerItem = ItemBuilder.from(HeadUtils.createCustomItemStack(Material.GRAY_STAINED_GLASS_PANE, " ")).asGuiItem( e -> e.setCancelled(true));
        gui.getFiller().fillBottom(fillerItem);




        gui.setItem(6,1, GuiUtil.createBackArrow(player, event -> new ManageTraderPosition(player, trader).open()));
    }

    private void reloadAuthorizedBlocks() {
        SpawnZone spawnZone = trader.getRandomSpawnZone();
        List<Material> authorizedBlocks = new ArrayList<>();
        for(int i = 0; i < 45; i++){
            ItemStack item = gui.getInventory().getItem(i);
            if(item != null)
                authorizedBlocks.add(item.getType());
        }
        spawnZone.setAuthorizedBlocks(authorizedBlocks);
    }
}
