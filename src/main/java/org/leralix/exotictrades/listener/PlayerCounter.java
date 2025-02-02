package org.leralix.exotictrades.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.leralix.exotictrades.market.PlayerConnectionStorage;

public class PlayerCounter implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(PlayerJoinEvent event) {
        PlayerConnectionStorage.newConnection(event.getPlayer().getUniqueId());
    }

}
