package org.leralix.exotictrades.guis;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.GuiItem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.leralix.exotictrades.lang.Lang;
import org.leralix.exotictrades.traders.Trader;
import org.leralix.exotictrades.traders.TraderWork;
import org.leralix.lib.utils.HeadUtils;

public class SelectTraderProfessionMenu extends basicGUI {
    public SelectTraderProfessionMenu(Player player, Trader trader) {
        super(player, "Select Profession", 3);

        for(TraderWork work : TraderWork.values()){
            ItemStack item = work.getIcon(work.getName(), Lang.CLICK_TO_SELECT);

            GuiItem guiItem = ItemBuilder.from(item).asGuiItem(event -> {
                trader.setWorkType(work);
                new ManageTrader(player, trader).open();
            });
            gui.addItem(guiItem);
        }

        gui.setItem(3,1, GuiUtil.createBackArrow(player, event -> new ManageTrader(player, trader).open()));
    }
}
