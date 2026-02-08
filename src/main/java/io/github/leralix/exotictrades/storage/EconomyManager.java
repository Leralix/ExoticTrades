package io.github.leralix.exotictrades.storage;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import io.github.leralix.exotictrades.ExoticTrades;


public class EconomyManager {

    private EconomyManager() {
        throw new IllegalStateException("Utility class");
    }


    private static Economy economy;

    public static void setupEconomy() {
        RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return;
        }
        registerEconomy(rsp.getProvider());
    }

    public static void registerEconomy(Economy registeredEconomy) {
        ExoticTrades.getPlugin().getLogger().info("[ExoticTrade] -Economy loaded");
        economy = registeredEconomy;
    }

    public static Economy getEconomy() {
        return economy;
    }

    public static String getCurrencySymbol() {
        return economy.currencyNameSingular();
    }
}
