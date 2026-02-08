package io.github.leralix.exotictrades.guis;


import org.bukkit.entity.Player;
import io.github.leralix.exotictrades.traders.Trader;
import io.github.leralix.exotictrades.traders.position.FixedPosition;

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
