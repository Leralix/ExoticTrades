package io.github.leralix.exotictrades.guis;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.GuiItem;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import io.github.leralix.exotictrades.lang.Lang;
import org.leralix.lib.utils.HeadUtils;

public class MainMenu extends BasicGui {

    public MainMenu(Player player) {
        super(player, "Main Menu", 3);
        gui.setDefaultClickAction(event -> event.setCancelled(true));
        gui.setDragAction(event -> event.setCancelled(true));

        ItemStack tradersMenu = HeadUtils.createCustomItemStack(Material.EMERALD, Lang.TRADER_MENU.get(), Lang.CLICK_TO_OPEN.get());
        GuiItem traderMenu = ItemBuilder.from(tradersMenu).asGuiItem(p -> new ManageTraders(player).open());
        gui.setItem(2,3, traderMenu);

        ItemStack marketMenu = HeadUtils.createCustomItemStack(Material.GOLD_INGOT, Lang.MARKET_MENU.get(), Lang.CLICK_TO_OPEN.get());
        GuiItem marketMenuButton = ItemBuilder.from(marketMenu).asGuiItem(p -> new MarketMenu(player).open());
        gui.setItem(2,7, marketMenuButton);

        gui.setItem(3,1, GuiUtil.createBackArrow(player, HumanEntity::closeInventory));
        gui.open(player);
    }
}
