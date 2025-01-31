package org.leralix.exotictrades;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.java.JavaPlugin;
import org.leralix.exotictrades.commands.admin.AdminCommandManager;
import org.leralix.exotictrades.lang.Lang;
import org.leralix.exotictrades.listener.EconomyInitialiser;
import org.leralix.exotictrades.listener.InteractWithTrader;
import org.leralix.exotictrades.listener.RareItemDrops;
import org.leralix.exotictrades.listener.SpawnTraders;
import org.leralix.exotictrades.storage.RareItemStorage;
import org.leralix.exotictrades.storage.TraderStorage;
import org.leralix.exotictrades.storage.VillagerHeadStorage;
import org.leralix.lib.utils.config.ConfigTag;
import org.leralix.lib.utils.config.ConfigUtil;

import java.util.logging.Level;
import java.util.logging.Logger;

public final class ExoticTrades extends JavaPlugin {

    private static ExoticTrades plugin;
    long dateOfStart = System.currentTimeMillis();

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

        RareItemStorage.init();
        TraderStorage.load();
        VillagerHeadStorage.init();

        getServer().getPluginManager().registerEvents(new InteractWithTrader(), this);
        getServer().getPluginManager().registerEvents(new EconomyInitialiser(), this);
        getServer().getPluginManager().registerEvents(new RareItemDrops(), this);
        getServer().getPluginManager().registerEvents(new SpawnTraders(), this);


        initTasks();

        logger.warning("[ExoticTrade] -Loading listeners");
        getCommand("extrade").setExecutor(new AdminCommandManager());


        logger.log(Level.INFO, "[ExoticTrade] -Plugin loaded successfully");
        getLogger().info("\u001B[33m---------------- ExoticTrade ------------------\u001B[0m");
    }

    private void initTasks() {
        int movingSlider = 20 * 60 * 5;

    }

    @Override
    public void onDisable() {
        if(System.currentTimeMillis() - dateOfStart < 30000){
            getLogger().info("[TaN] Not saving data because plugin was closed less than 30s after launch");
            getLogger().info("[TaN] Plugin disabled");
            return;
        }
        TraderStorage.save();
    }

    public static ExoticTrades getPlugin() {
        return plugin;
    }
}
