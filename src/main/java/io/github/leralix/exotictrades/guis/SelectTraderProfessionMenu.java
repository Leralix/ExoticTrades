package io.github.leralix.exotictrades.guis;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.GuiItem;
import io.github.leralix.exotictrades.lang.Lang;
import io.github.leralix.exotictrades.storage.StorageForGui;
import io.github.leralix.exotictrades.traders.Trader;
import io.github.leralix.exotictrades.traders.TraderWork;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SelectTraderProfessionMenu extends BasicGui {
    public SelectTraderProfessionMenu(Player player, Trader trader, StorageForGui storage) {
        super(player, "Select Profession", 3, storage);

        for(TraderWork work : TraderWork.values()){
            ItemStack item = work.getIcon(work.getName(), Lang.CLICK_TO_SELECT);

            GuiItem guiItem = ItemBuilder.from(item).asGuiItem(event -> {
                trader.setWorkType(work);
                new ManageTrader(player, trader, storage).open();
            });
            gui.addItem(guiItem);
        }

        gui.setItem(3,1, GuiUtil.createBackArrow(player, event -> new ManageTrader(player, trader, storage).open()));
    }
}
