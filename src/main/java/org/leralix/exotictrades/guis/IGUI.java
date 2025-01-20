package org.leralix.exotictrades.guis;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.leralix.exotictrades.lang.Lang;
import org.leralix.lib.gui.builder.item.ItemBuilder;
import org.leralix.lib.gui.components.GuiType;
import org.leralix.lib.gui.guis.Gui;
import org.leralix.lib.gui.guis.GuiItem;
import org.leralix.lib.utils.HeadUtils;

import java.util.function.Consumer;

public class IGUI {


    static Gui createChestGui(String name, int nRow) {
        return Gui.gui()
                .title(Component.text(name))
                .type(GuiType.CHEST)
                .rows(nRow)
                .create();
    }
    static GuiItem createBackArrow(Player player, Consumer<Player> openMenuAction) {
        ItemStack getBackArrow = HeadUtils.createCustomItemStack(Material.ARROW, Lang.GUI_BACK_ARROW.get());
        return ItemBuilder.from(getBackArrow).asGuiItem(event -> {
            event.setCancelled(true);
            openMenuAction.accept(player);
        });
    }

    static GuiItem getUnnamedItem(Material material) {
        ItemStack item = new ItemStack(material);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName("");
        item.setItemMeta(itemMeta);
        return ItemBuilder.from(item).asGuiItem(event -> event.setCancelled(true));
    }

}