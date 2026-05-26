package io.github.leralix.exotictrades.listener;

import io.github.leralix.exotictrades.market.PlayerConnectionStorage;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerCounter implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(PlayerJoinEvent event) {
        PlayerConnectionStorage.newConnection(event.getPlayer().getUniqueId());
    }

}
