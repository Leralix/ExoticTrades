package org.leralix.exotictrades.guis;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.GuiItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.leralix.exotictrades.lang.Lang;
import org.leralix.exotictrades.listener.chat.PlayerChatListenerStorage;
import org.leralix.exotictrades.traders.Trader;
import org.leralix.exotictrades.traders.position.FixedPosition;
import org.leralix.exotictrades.traders.position.TraderPosition;
import org.leralix.lib.utils.HeadUtils;

public class ManageTraderPosition extends BasicGui {
    Trader trader;

    public ManageTraderPosition(Player player, Trader trader) {
        this(player, trader, 0);
    }

    public ManageTraderPosition(Player player, Trader trader, int page) {
        super(player, "Manage Trader Position", 3);
        gui.setDefaultClickAction(event -> event.setCancelled(true));
        this.trader = trader;
        FixedPosition randomRandomPosition = trader.getPosition();

        randomRandomPosition.addGuiItems(gui, this, player, trader, page);


        gui.setItem(3, 1, GuiUtil.createBackArrow(player, event -> new ManageTrader(player, trader).open()));
    }

    public void reload() {
        new ManageTraderPosition(player, trader).open();
    }

}
