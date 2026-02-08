package io.github.leralix.exotictrades.guis;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.GuiItem;
import io.github.leralix.exotictrades.util.HeadUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import io.github.leralix.exotictrades.ExoticTrades;
import io.github.leralix.exotictrades.traders.position.RandomPosition;
import io.github.leralix.exotictrades.traders.Trader;

import java.util.ArrayList;
import java.util.List;

public class OpenBlockAllowedToSpawnTrader extends BasicGui {

    private final RandomPosition randomPosition;

    public OpenBlockAllowedToSpawnTrader(Player player, Trader trader, RandomPosition randomPosition) {
        super(player, "Block Allowed", 6);
        this.randomPosition = randomPosition;

        gui.setDefaultClickAction(event ->
                Bukkit.getScheduler().runTask(ExoticTrades.getPlugin(), this::reloadAuthorizedBlocks)
        );

        for(Material material : randomPosition.getAuthorizedBlocks()){
            GuiItem guiItem = ItemBuilder.from(HeadUtils.createCustomItemStack(material, " ")).asGuiItem(event -> new OpenBlockAllowedToSpawnTrader(player, trader, randomPosition).open());
            gui.addItem(guiItem);
        }

        GuiItem fillerItem = ItemBuilder.from(HeadUtils.createCustomItemStack(Material.GRAY_STAINED_GLASS_PANE, " ")).asGuiItem(e -> e.setCancelled(true));
        gui.getFiller().fillBottom(fillerItem);




        gui.setItem(6,1, GuiUtil.createBackArrow(player, event -> new ManageTraderPosition(player, trader).open()));
    }

    private void reloadAuthorizedBlocks() {
        List<Material> authorizedBlocks = new ArrayList<>();
        for(int i = 0; i < 45; i++){
            ItemStack item = gui.getInventory().getItem(i);
            if(item != null)
                authorizedBlocks.add(item.getType());
        }
        randomPosition.setAuthorizedBlocks(authorizedBlocks);
    }
}
