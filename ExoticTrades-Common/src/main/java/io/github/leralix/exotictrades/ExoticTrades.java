package io.github.leralix.exotictrades;

import io.github.leralix.ExtradeAPI;
import io.github.leralix.exotictrades.api.ExtradeImpl;
import io.github.leralix.exotictrades.commands.admin.AdminCommandManager;
import io.github.leralix.exotictrades.lang.Lang;
import io.github.leralix.exotictrades.listener.*;
import io.github.leralix.exotictrades.listener.chat.ChatListener;
import io.github.leralix.exotictrades.market.PlayerConnectionStorage;
import io.github.leralix.exotictrades.market.StockMarketManager;
import io.github.leralix.exotictrades.storage.EconomyManager;
import io.github.leralix.exotictrades.storage.MarketItemStorage;
import io.github.leralix.exotictrades.storage.TraderStorage;
import io.github.leralix.exotictrades.storage.VillagerHeadStorage;
import io.github.leralix.exotictrades.traders.DailyTasks;
import io.github.leralix.exotictrades.traders.HourlyTasks;
import io.github.leralix.exotictrades.util.HeadUtils;
import io.github.leralix.exotictrades.util.NumberUtil;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.leralix.lib.data.PluginVersion;
import org.leralix.lib.utils.config.ConfigUtil;

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

    private MarketItemStorage marketItemStorage;
    private VillagerHeadStorage villagerHeadStorage;

    private TraderStorage traderStorage;
    private PlayerConnectionStorage playerConnectionStorage;
    private StockMarketManager stockMarketManager;

    @Override
    public void onEnable() {
        plugin = this;
        Logger logger = getLogger();
        logger.info("\u001B[33m---------------- ExoticTrade ------------------\u001B[0m");
        logger.log(Level.INFO, "[ExoticTrade] -Loading plugin");
        logger.log(Level.INFO, "[ExoticTrade] -Loading language");

        var langConfigFile = ConfigUtil.saveAndUpdateResource(this, "lang.yml", Collections.emptyList());
        String lang = langConfigFile.getString("language", "en");


        Lang.loadTranslations(lang);
        logger.log(Level.INFO, Lang.LANGUAGE_SUCCESSFULLY_LOADED.get());

        logger.log(Level.INFO, "[ExoticTrade] -Loading data");
        List<String> blackListedWords = List.of(
                "rareRessources",
                "stockMarket",
                "marketItem"
        );

        var configFile = ConfigUtil.saveAndUpdateResource(this, "config.yml", blackListedWords);
        EconomyManager.setupEconomy();

        this.marketItemStorage = new MarketItemStorage(configFile);
        this.villagerHeadStorage = new VillagerHeadStorage(configFile);

        this.traderStorage = new TraderStorage();
        traderStorage.load();
        PlayerConnectionStorage.load();

        this.stockMarketManager = new StockMarketManager(configFile, marketItemStorage);
        this.stockMarketManager.load();
        NumberUtil.init(configFile.getInt("nbDigits", 2));

        getServer().getPluginManager().registerEvents(new InteractWithTrader(
                        traderStorage,
                        marketItemStorage,
                        stockMarketManager,
                        configFile.getBoolean("removeVanillaVillagerInteractions", false)
                ), this
        );
        getServer().getPluginManager().registerEvents(new EconomyInitialiser(), this);
        getServer().getPluginManager().registerEvents(new RareItemDrops(marketItemStorage), this);
        getServer().getPluginManager().registerEvents(new SpawnTraders(traderStorage), this);
        getServer().getPluginManager().registerEvents(new PlayerCounter(playerConnectionStorage), this);
        getServer().getPluginManager().registerEvents(new ChatListener(), this);


        HeadUtils.init(villagerHeadStorage);

        DailyTasks dailyTasks = new DailyTasks(
                traderStorage,
                configFile.getInt("traderUpdatePositionHour",0),
                configFile.getInt("traderUpdatePositionMinute",0)

        );
        dailyTasks.scheduleTasks();
        HourlyTasks hourlyTasks = new HourlyTasks(
                traderStorage,
                stockMarketManager,
                configFile.getInt("traderUpdatePositionMinute",0)
        );
        hourlyTasks.scheduleTasks();


        logger.warning("[ExoticTrade] -Loading listeners");
        getCommand("extrade").setExecutor(new AdminCommandManager(traderStorage, marketItemStorage, stockMarketManager, hourlyTasks, dailyTasks));

        logger.warning("[ExoticTrade] -Registering API");
        ExtradeAPI.register(new ExtradeImpl(plugin, traderStorage));
        logger.warning("[ExoticTrade] -Registering BStat");
        initBStats();

        logger.log(Level.INFO, "[ExoticTrade] -Plugin loaded successfully");
        pluginLoadedSuccessfully = true;
        getLogger().info("\u001B[33m---------------- ExoticTrade ------------------\u001B[0m");
    }

    @Override
    public void onDisable() {
        if (!pluginLoadedSuccessfully) {
            getLogger().info("[ExoticTrade] Not saving data because plugin failed during setup");
            getLogger().info("[ExoticTrade] Plugin disabled");
            return;
        }
        traderStorage.save();
        stockMarketManager.save();
        PlayerConnectionStorage.save();
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

    public StockMarketManager getStockMarketManager() {
        return stockMarketManager;
    }

    public PlayerConnectionStorage getPlayerConnectionStorage() {
        return playerConnectionStorage;
    }

    public TraderStorage getTraderStorage() {
        return traderStorage;
    }

    public VillagerHeadStorage getVillagerHeadStorage() {
        return villagerHeadStorage;
    }

    public MarketItemStorage getMarketItemStorage() {
        return marketItemStorage;
    }
}
