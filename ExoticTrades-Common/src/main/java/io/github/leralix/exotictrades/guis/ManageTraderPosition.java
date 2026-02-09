package io.github.leralix.exotictrades.guis;


import io.github.leralix.exotictrades.storage.StorageForGui;
import io.github.leralix.exotictrades.traders.Trader;
import io.github.leralix.exotictrades.traders.position.FixedPosition;
import org.bukkit.entity.Player;

public class ManageTraderPosition extends BasicGui {
    Trader trader;

    public ManageTraderPosition(Player player, Trader trader, StorageForGui storage) {
        this(player, trader, 0, storage);
    }

    public ManageTraderPosition(Player player, Trader trader, int page, StorageForGui storage) {
        super(player, "Manage Trader Position", 3, storage);
        gui.setDefaultClickAction(event -> event.setCancelled(true));
        this.trader = trader;
        FixedPosition randomRandomPosition = trader.getPosition();

        randomRandomPosition.addGuiItems(gui, this, player, trader, page, storage);


        gui.setItem(3, 1, GuiUtil.createBackArrow(player, event -> new ManageTrader(player, trader, storage).open()));
    }

    public void reload() {
        new ManageTraderPosition(player, trader, storage).open();
    }

}
