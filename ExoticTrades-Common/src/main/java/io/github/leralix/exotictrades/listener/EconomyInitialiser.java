package io.github.leralix.exotictrades.listener;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServiceRegisterEvent;
import io.github.leralix.exotictrades.storage.EconomyManager;

public class EconomyInitialiser implements Listener {

    @EventHandler
    public void onServiceRegister(ServiceRegisterEvent event) {
        if (event.getProvider().getProvider() instanceof Economy economy) {
            EconomyManager.registerEconomy(economy);
        }
    }

}
