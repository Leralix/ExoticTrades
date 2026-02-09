package io.github.leralix.exotictrades.guis;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.GuiItem;
import io.github.leralix.exotictrades.lang.Lang;
import io.github.leralix.exotictrades.storage.StorageForGui;
import io.github.leralix.exotictrades.util.HeadUtils;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MainMenu extends BasicGui {

    public MainMenu(Player player, StorageForGui storage) {
        super(player, "Main Menu", 3, storage);
        gui.setDefaultClickAction(event -> event.setCancelled(true));
        gui.setDragAction(event -> event.setCancelled(true));

        ItemStack tradersMenu = HeadUtils.createCustomItemStack(Material.EMERALD, Lang.TRADER_MENU.get(), Lang.CLICK_TO_OPEN.get());
        GuiItem traderMenu = ItemBuilder.from(tradersMenu).asGuiItem(p -> new ManageTraders(player, storage).open());
        gui.setItem(2,3, traderMenu);

        ItemStack marketMenu = HeadUtils.createCustomItemStack(Material.GOLD_INGOT, Lang.MARKET_MENU.get(), Lang.CLICK_TO_OPEN.get());
        GuiItem marketMenuButton = ItemBuilder.from(marketMenu).asGuiItem(p -> new MarketMenu(player, storage).open());
        gui.setItem(2,7, marketMenuButton);

        gui.setItem(3,1, GuiUtil.createBackArrow(player, HumanEntity::closeInventory));
        gui.open(player);
    }
}
