package org.leralix.exotictrades.guis;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.leralix.exotictrades.listener.chat.PlayerChatListenerStorage;
import org.leralix.exotictrades.listener.chat.events.RegisterZoneListener;
import org.leralix.exotictrades.traders.SpawnZone;
import org.leralix.exotictrades.traders.Trader;
import org.leralix.lib.utils.HeadUtils;

public class ManageTraderPosition extends basicGUI {
    public ManageTraderPosition(Player player, Trader trader) {
        super(player, "Manage Trader Position", 3);
        gui.setDefaultClickAction(event -> event.setCancelled(true));

        SpawnZone randomSpawnZone = trader.getRandomSpawnZone();

        ItemStack spawnZoneItem = HeadUtils.createCustomItemStack(Material.GRASS_BLOCK, "Spawn Zone", "Click to manage");
        gui.setItem(2, 2, ItemBuilder.from(spawnZoneItem).asGuiItem(event -> PlayerChatListenerStorage.register(player, new RegisterZoneListener(trader))));

        ItemStack blockAllowedItem = HeadUtils.createCustomItemStack(Material.GRASS_BLOCK, "Block Allowed", "Click to choose");
        gui.setItem(2, 4, ItemBuilder.from(blockAllowedItem).asGuiItem(event -> new OpenBlockAllowedToSpawnTrader(player, trader).open()));



        Material allowWaterMaterial = randomSpawnZone.isWatterAllowed() ? Material.WATER_BUCKET : Material.BUCKET;
        ItemStack allowWaterItem = HeadUtils.createCustomItemStack(allowWaterMaterial, "Allow Water", "Click to toggle");
        gui.setItem(2, 6, ItemBuilder.from(allowWaterItem).asGuiItem(event -> {
            randomSpawnZone.switchWaterAllowed();
            new ManageTraderPosition(player, trader).open();
        }));




        gui.setItem(3, 1, GuiUtil.createBackArrow(player, event -> new ManageTrader(player, trader).open()));
    }
}
