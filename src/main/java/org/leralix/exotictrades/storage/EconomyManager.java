package org.leralix.exotictrades.storage;

import net.milkbowl.vault.economy.Economy;

public class EconomyManager {

    private EconomyManager() {
        throw new IllegalStateException("Utility class");
    }


    private static Economy economy;

    public static void registerEconomy(Economy registeredEconomy) {
        economy = registeredEconomy;
    }

    public static Economy getEconomy() {
        return economy;
    }
}
