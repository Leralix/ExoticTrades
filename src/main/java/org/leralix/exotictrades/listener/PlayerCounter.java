package org.leralix.exotictrades.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.leralix.exotictrades.market.StockMarketManager;

public class PlayerCounter implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        StockMarketManager.registerPlayer(event.getPlayer());
    }

}
