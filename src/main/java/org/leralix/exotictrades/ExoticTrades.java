package org.leralix.exotictrades;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;
import java.util.logging.Logger;

public final class ExoticTrades extends JavaPlugin {

    public static ExoticTrades plugin;

    @Override
    public void onEnable() {
        plugin = this;
        Logger logger = getLogger();
        getLogger().info("\u001B[33m---------------- ExoticTrade ------------------\u001B[0m");
        logger.log(Level.INFO, "[ExoticTrade] -Loading plugin");



        DropChances.load();



        logger.log(Level.INFO, "[ExoticTrade] -Plugin loaded successfully");
        getLogger().info("\u001B[33m---------------- ExoticTrade ------------------\u001B[0m");

    }

    @Override
    public void onDisable() {

    }

    public static ExoticTrades getPlugin() {
        return plugin;
    }
}
