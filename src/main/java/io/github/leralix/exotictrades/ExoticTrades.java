package io.github.leralix.exotictrades;

import io.github.leralix.ExtradeAPI;
import io.github.leralix.exotictrades.listener.*;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import io.github.leralix.exotictrades.api.ExtradeImpl;
import io.github.leralix.exotictrades.commands.admin.AdminCommandManager;
import io.github.leralix.exotictrades.lang.Lang;
import io.github.leralix.exotictrades.listener.chat.ChatListener;
import io.github.leralix.exotictrades.market.PlayerConnectionStorage;
import io.github.leralix.exotictrades.market.StockMarketManager;
import io.github.leralix.exotictrades.storage.EconomyManager;
import io.github.leralix.exotictrades.storage.MarketItemStorage;
import io.github.leralix.exotictrades.storage.TraderStorage;
import io.github.leralix.exotictrades.storage.VillagerHeadStorage;
import io.github.leralix.exotictrades.traders.DailyTasks;
import io.github.leralix.exotictrades.traders.HourlyTasks;
import io.github.leralix.exotictrades.util.NumberUtil;
import org.leralix.lib.data.PluginVersion;
import org.leralix.lib.utils.config.ConfigTag;
import org.leralix.lib.utils.config.ConfigUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class ExoticTrades extends JavaPlugin {

    /**
     * Singleton of the plugin
     */
    private static ExoticTrades plugin;
    /**
     * Defines if the plugin loaded without any issues. Used to check if {@link Plugin#onDisable()} should proceed
     */
    boolean pluginLoadedSuccessfully = false;
    /**
     * The plugin current version
     */
    private final PluginVersion pluginVersion = new PluginVersion(0, 4, 0);
    /**
     * Defines the minimum ExoticTrades-map version needed
     */
    private final PluginVersion minimumSupportingMapPlugin = new PluginVersion(0, 1, 0);


    @Override
    public void onEnable() {
        plugin = this;
        Logger logger = getLogger();
        getLogger().info("\u001B[33m---------------- ExoticTrade ------------------\u001B[0m");
        logger.log(Level.INFO, "[ExoticTrade] -Loading plugin");

        var file = ConfigUtil.saveAndUpdateResource(this, "lang.yml", Collections.emptyList());
        ConfigUtil.addCustomConfig(this, "lang.yml", ConfigTag.LANG);
        String lang = ConfigUtil.getCustomConfig(ConfigTag.LANG).getString("language");

        EconomyManager.setupEconomy();

        Lang.loadTranslations(lang);
        getLogger().info(Lang.LANGUAGE_SUCCESSFULLY_LOADED.get());

        logger.warning("[ExoticTrade] -Loading data");
        List<String> blackListedWords = new ArrayList<>();
        blackListedWords.add("rareRessources");
        blackListedWords.add("stockMarket");
        blackListedWords.add("marketItem");

        ConfigUtil.saveAndUpdateResource(this, "config.yml", blackListedWords);
        ConfigUtil.addCustomConfig(this, "config.yml", ConfigTag.MAIN);

        MarketItemStorage.init();
        VillagerHeadStorage.init();

        TraderStorage.load();
        PlayerConnectionStorage.load();

        StockMarketManager.load();
        StockMarketManager.init();
        NumberUtil.init();

        getServer().getPluginManager().registerEvents(new InteractWithTrader(), this);
        getServer().getPluginManager().registerEvents(new EconomyInitialiser(), this);
        getServer().getPluginManager().registerEvents(new RareItemDrops(), this);
        getServer().getPluginManager().registerEvents(new SpawnTraders(), this);
        getServer().getPluginManager().registerEvents(new PlayerCounter(), this);
        getServer().getPluginManager().registerEvents(new ChatListener(), this);


        initTasks();

        logger.warning("[ExoticTrade] -Loading listeners");
        getCommand("extrade").setExecutor(new AdminCommandManager());

        logger.warning("[ExoticTrade] -Registering API");
        ExtradeAPI.register(new ExtradeImpl(plugin));
        logger.warning("[ExoticTrade] -Registering BStat");
        initBStats();

        logger.log(Level.INFO, "[ExoticTrade] -Plugin loaded successfully");
        pluginLoadedSuccessfully = true;
        getLogger().info("\u001B[33m---------------- ExoticTrade ------------------\u001B[0m");
    }

    private void initTasks() {
        DailyTasks.scheduleTasks();
        HourlyTasks.scheduleTasks();

    }

    @Override
    public void onDisable() {
        if(!pluginLoadedSuccessfully){
            getLogger().info("[ExoticTrade] Not saving data because plugin failed during setup");
            getLogger().info("[ExoticTrade] Plugin disabled");
            return;
        }
        TraderStorage.save();
        PlayerConnectionStorage.save();
        StockMarketManager.save();
        getLogger().info("[ExoticTrade] Plugin disabled");
    }

    private void initBStats() {
        try {
            new Metrics(this, 29409);
        } catch (IllegalStateException e) {
            getLogger().log(Level.WARNING, "[ExoticTrade] Failed to submit stats to bStats", e);
        }
    }

    public static ExoticTrades getPlugin() {
        return plugin;
    }

    public static String getNameString() {
        return Lang.EXOTIC_TRADE_STRING.get();
    }

    public PluginVersion getPluginVersion() {
        return pluginVersion;
    }

    public PluginVersion getMinimumSupportingMapPlugin() {
        return minimumSupportingMapPlugin;
    }
}
