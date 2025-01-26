package org.leralix.exotictrades.listener;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServiceRegisterEvent;
import org.leralix.exotictrades.ExoticTrades;
import org.leralix.exotictrades.storage.EconomyManager;

public class EconomyInitialiser implements Listener {

    @EventHandler
    public void onServiceRegister(ServiceRegisterEvent event) {
        ExoticTrades.getPlugin().getLogger().info("[ExoticTrade] -Economy loaded");
        if (event.getProvider().getProvider() instanceof Economy economy) {
            EconomyManager.registerEconomy(economy);
        }
    }

}
