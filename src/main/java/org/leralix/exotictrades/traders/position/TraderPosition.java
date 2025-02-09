package org.leralix.exotictrades.traders.position;

import dev.triumphteam.gui.guis.Gui;
import org.bukkit.entity.Player;
import org.leralix.exotictrades.guis.ManageTraderPosition;
import org.leralix.exotictrades.traders.Trader;
import org.leralix.lib.position.Vector3D;

public interface TraderPosition {

    Vector3D getNextPosition();
    String getSpawnInfo();
    void addGuiItems(Gui gui, ManageTraderPosition manageTraderPosition, Player player, Trader trader, int page);

}
