package io.github.leralix.exotictrades.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import io.github.leralix.exotictrades.market.PlayerConnectionStorage;

public class PlayerCounter implements Listener {

    private final PlayerConnectionStorage playerConnectionStorage;

    public PlayerCounter(PlayerConnectionStorage playerConnectionStorage){
        this.playerConnectionStorage = playerConnectionStorage;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(PlayerJoinEvent event) {
        playerConnectionStorage.newConnection(event.getPlayer().getUniqueId());
    }

}
