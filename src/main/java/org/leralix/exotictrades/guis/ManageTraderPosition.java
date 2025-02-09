package org.leralix.exotictrades.guis;

import org.bukkit.entity.Player;
import org.leralix.exotictrades.traders.Trader;
import org.leralix.exotictrades.traders.position.TraderPosition;

public class ManageTraderPosition extends basicGUI {
    Trader trader;

    public ManageTraderPosition(Player player, Trader trader) {
        this(player, trader, 0);
    }

    public ManageTraderPosition(Player player, Trader trader, int page) {
        super(player, "Manage Trader Position", 3);
        gui.setDefaultClickAction(event -> event.setCancelled(true));
        this.trader = trader;
        TraderPosition randomRandomPosition = trader.getPosition();

        randomRandomPosition.addGuiItems(gui, this, player, trader, page);




        gui.setItem(3, 1, GuiUtil.createBackArrow(player, event -> new ManageTrader(player, trader).open()));
    }

    public void reload() {
        new ManageTraderPosition(player, trader).open();
    }
}
