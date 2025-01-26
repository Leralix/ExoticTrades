package org.leralix.exotictrades;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.leralix.exotictrades.commands.admin.AdminCommandManager;
import org.leralix.exotictrades.lang.Lang;
import org.leralix.exotictrades.listener.EconomyInitialiser;
import org.leralix.exotictrades.listener.InteractWithTrader;
import org.leralix.exotictrades.listener.SpawnTraders;
import org.leralix.exotictrades.storage.TraderStorage;
import org.leralix.exotictrades.storage.VillagerHeadStorage;
import org.leralix.exotictrades.util.DropChances;
import org.leralix.lib.utils.config.ConfigTag;
import org.leralix.lib.utils.config.ConfigUtil;

import java.util.logging.Level;
import java.util.logging.Logger;

public final class ExoticTrades extends JavaPlugin {

    private static ExoticTrades plugin;
    private Economy economy;

    @Override
    public void onEnable() {
        plugin = this;
        Logger logger = getLogger();
        getLogger().info("\u001B[33m---------------- ExoticTrade ------------------\u001B[0m");
        logger.log(Level.INFO, "[ExoticTrade] -Loading plugin");

        ConfigUtil.saveAndUpdateResource(this, "lang.yml");
        ConfigUtil.addCustomConfig(this, "lang.yml", ConfigTag.LANG);
        String lang = ConfigUtil.getCustomConfig(ConfigTag.LANG).getString("language");


        Lang.loadTranslations(lang);
        getLogger().info(Lang.LANGUAGE_SUCCESSFULLY_LOADED.get());

        logger.warning("[ExoticTrade] -Loading data");
        ConfigUtil.saveAndUpdateResource(this, "config.yml");
        ConfigUtil.addCustomConfig(this, "config.yml", ConfigTag.MAIN);

        DropChances.load();
        TraderStorage.load();
        VillagerHeadStorage.init();

        getServer().getPluginManager().registerEvents(new SpawnTraders(), this);
        getServer().getPluginManager().registerEvents(new EconomyInitialiser(), this);


        logger.warning("[ExoticTrade] -Loading listeners");
        getCommand("exotictradeadmin").setExecutor(new AdminCommandManager());


        logger.log(Level.INFO, "[ExoticTrade] -Plugin loaded successfully");
        getLogger().info("\u001B[33m---------------- ExoticTrade ------------------\u001B[0m");
    }

    @Override
    public void onDisable() {
        TraderStorage.save();
    }

    public static ExoticTrades getPlugin() {
        return plugin;
    }
}
