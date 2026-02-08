package io.github.leralix.exotictrades.traders.position;

import dev.triumphteam.gui.guis.Gui;
import org.bukkit.entity.Player;
import io.github.leralix.exotictrades.guis.ManageTraderPosition;
import io.github.leralix.exotictrades.traders.Trader;
import org.leralix.lib.position.Vector3D;

public interface TraderPosition {

    Vector3D getPositionNextHour();
    String getSpawnInfo();
    void addGuiItems(Gui gui, ManageTraderPosition manageTraderPosition, Player player, Trader trader, int page);

}
