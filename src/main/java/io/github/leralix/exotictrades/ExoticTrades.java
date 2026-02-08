package io.github.leralix.exotictrades;

import io.github.leralix.ExtradeAPI;
import io.github.leralix.exotictrades.listener.*;
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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class ExoticTrades extends JavaPlugin {

    private static ExoticTrades plugin;
    long dateOfStart = System.currentTimeMillis();
    private final PluginVersion pluginVersion = new PluginVersion(0, 4, 0);
    private final PluginVersion minimumSupportingMapPlugin = new PluginVersion(0, 1, 0);


    @Override
    public void onEnable() {
        plugin = this;
        Logger logger = getLogger();
        getLogger().info("\u001B[33m---------------- ExoticTrade ------------------\u001B[0m");
        logger.log(Level.INFO, "[ExoticTrade] -Loading plugin");

        ConfigUtil.saveAndUpdateResource(this, "lang.yml");
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
        ExtradeAPI.register(new ExtradeImpl());


        logger.log(Level.INFO, "[ExoticTrade] -Plugin loaded successfully");
        getLogger().info("\u001B[33m---------------- ExoticTrade ------------------\u001B[0m");
    }

    private void initTasks() {
        DailyTasks.scheduleTasks();
        HourlyTasks.scheduleTasks();

    }

    @Override
    public void onDisable() {
        if(System.currentTimeMillis() - dateOfStart < 30000){
            getLogger().info("[TaN] Not saving data because plugin was closed less than 30s after launch");
            getLogger().info("[TaN] Plugin disabled");
            return;
        }
        TraderStorage.save();
        PlayerConnectionStorage.save();
        StockMarketManager.save();
    }

    public static ExoticTrades getPlugin() {
        return plugin;
    }

    public static String getNameString() {
        return Lang.EXOTIC_TRADE_STRING.get();
    }

    public static PluginVersion getPluginVersion() {
        return plugin.pluginVersion;
    }

    public static PluginVersion getMinimumSupportingMapPlugin() {
        return plugin.minimumSupportingMapPlugin;
    }
}
