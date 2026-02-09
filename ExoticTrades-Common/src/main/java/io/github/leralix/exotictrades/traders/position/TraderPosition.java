package io.github.leralix.exotictrades.traders.position;

import dev.triumphteam.gui.guis.Gui;
import io.github.leralix.exotictrades.guis.admin.ManageTraderPosition;
import io.github.leralix.exotictrades.storage.StorageForGui;
import io.github.leralix.exotictrades.traders.Trader;
import org.bukkit.entity.Player;
import org.leralix.lib.position.Vector3D;

public interface TraderPosition {

    Vector3D getPositionNextHour();
    String getSpawnInfo();
    void addGuiItems(Gui gui, ManageTraderPosition manageTraderPosition, Player player, Trader trader, int page, StorageForGui storage);

}
