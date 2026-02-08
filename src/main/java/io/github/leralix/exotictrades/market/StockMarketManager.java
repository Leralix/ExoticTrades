package io.github.leralix.exotictrades.market;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import io.github.leralix.exotictrades.ExoticTrades;
import io.github.leralix.exotictrades.item.MarketItem;
import io.github.leralix.exotictrades.item.MarketItemStack;
import io.github.leralix.exotictrades.item.RareItem;
import io.github.leralix.exotictrades.storage.MarketItemKey;
import io.github.leralix.exotictrades.storage.adapters.MarketItemKeyAdapter;
import io.github.leralix.exotictrades.storage.MarketItemStorage;
import io.github.leralix.exotictrades.storage.adapters.RuntimeTypeAdapterFactory;
import org.bukkit.configuration.file.FileConfiguration;
import org.leralix.lib.utils.config.ConfigTag;
import org.leralix.lib.utils.config.ConfigUtil;

import java.io.*;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class StockMarketManager{

    private HashMap<MarketItemKey, StockMarket> marketItems;

    public StockMarketManager(FileConfiguration fileConfiguration, MarketItemStorage marketItemStorage) {
        marketItems = new HashMap<>();

        ConfigurationSection section = fileConfiguration.getConfigurationSection("stockMarket");
        if(section == null)
            return;
        for (String resourceKey : section.getKeys(false)) {
            ConfigurationSection resourceSection = section.getConfigurationSection(resourceKey);

            if (resourceSection == null) continue;

            resourceSection.addDefault("movingAverage", fileConfiguration.getInt("defaultMovingAverage", 24));
            resourceSection.addDefault("minPrice", fileConfiguration.getDouble("defaultMinPrice", 0));
            resourceSection.addDefault("percentForMinPrice", fileConfiguration.getDouble("defaultPercentForMinPrice", 0));
            resourceSection.addDefault("maxPrice", fileConfiguration.getDouble("defaultMaxPrice", 500));
            resourceSection.addDefault("percentForMaxPrice", fileConfiguration.getDouble("defaultPercentForMaxPrice", 0));
            resourceSection.addDefault("basePrice", fileConfiguration.getDouble("defaultBasePrice", 50));
            resourceSection.addDefault("volatility", fileConfiguration.getDouble("defaultVolatility", 1));
            resourceSection.addDefault("demandMultiplier", fileConfiguration.getDouble("defaultDemandMultiplier", 1));

            MarketItem marketItem = marketItemStorage.getMarketItem(resourceKey.hashCode());

            if(marketItem == null)
                continue;

            int movingAverage = resourceSection.getInt("movingAverage");
            double maxPrice = resourceSection.getDouble("maxPrice");
            double percentForMaxPrice = resourceSection.getDouble("percentForMaxPrice");
            double minPrice = resourceSection.getDouble("minPrice");
            double percentForMinPrice = resourceSection.getDouble("percentForMinPrice");
            double volatility = resourceSection.getDouble("volatility");
            double basePrice = resourceSection.getDouble("basePrice");
            double demandMultiplier = resourceSection.getDouble("demandMultiplier");

            registerOrUpdateMarketItem(marketItem, movingAverage, maxPrice, percentForMaxPrice, minPrice, percentForMinPrice, volatility, demandMultiplier, basePrice);
        }
    }

    public void updateMovingAverage() {
        for(StockMarket stockMarket : marketItems.values()){
            stockMarket.updateMovingAverage();
        }
    }

    public List<StockMarket> getAllStocks() {
        return Collections.list(Collections.enumeration(marketItems.values()));
    }


    public double sellMarketItems(List<MarketItemStack> marketItemStackList) {
        double total = 0;
        for(MarketItemStack marketItemStack : marketItemStackList){
            StockMarket specificMarket = getStockMarket(MarketItemKey.of(marketItemStack));
            total += specificMarket.sell(marketItemStack.getQuantity());
        }

        return total;
    }

    private StockMarket getStockMarket(MarketItemKey marketItemKey) {
        return marketItems.get(marketItemKey);
    }

    public void registerOrUpdateMarketItem(MarketItem marketItem, int timeLength, double maxPrice,double percentForMaxPrice, double minPrice, double percentForMinPrice, double volatility,double demandMultiplier, double basePrice) {
        MarketItemKey marketItemKey = MarketItemKey.of(marketItem);
        if(marketItems.containsKey(marketItemKey)){
            marketItems.get(marketItemKey).updateConstants(basePrice, maxPrice, percentForMaxPrice, minPrice, percentForMinPrice, demandMultiplier, volatility, timeLength);
        }
        else {
            marketItems.put(marketItemKey,new StockMarket(marketItem, maxPrice, percentForMaxPrice, minPrice, percentForMinPrice, demandMultiplier, volatility, basePrice, timeLength));
        }
    }


    public void save() {

        Gson gson = new GsonBuilder().setPrettyPrinting()
        .registerTypeAdapterFactory(
                RuntimeTypeAdapterFactory.of(MarketItem.class)
                        .registerSubtype(MarketItem.class, "MarketItem")
                        .registerSubtype(RareItem.class, "RareItem")
        )
        .create();
        File storageFolder = new File(ExoticTrades.getPlugin().getDataFolder().getAbsolutePath() + "/storage");
        storageFolder.mkdir();
        File jsonFile = new File(storageFolder.getAbsolutePath() + "/json");
        jsonFile.mkdir();
        File file = new File(jsonFile.getAbsolutePath() + "/markets.json");

        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Writer writer;
        try {
            writer = new FileWriter(file, false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        gson.toJson(marketItems, writer);
        try {
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void load(){

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(MarketItemKey.class, new MarketItemKeyAdapter())
                .registerTypeAdapterFactory(
                        RuntimeTypeAdapterFactory.of(MarketItem.class)
                                .registerSubtype(MarketItem.class, "MarketItem")
                                .registerSubtype(RareItem.class, "RareItem")
                )
                .setPrettyPrinting().create();
        File file = new File(ExoticTrades.getPlugin().getDataFolder().getAbsolutePath() + "/storage/json/markets.json");
        if (!file.exists())
            return;

        Reader reader;
        try {
            reader = new FileReader(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        Type type = new TypeToken<HashMap<MarketItemKey, StockMarket>>(){}.getType();
        marketItems = gson.fromJson(reader, type);
    }

    public StockMarket getMarketFor(MarketItemKey marketItemKey) {
        return marketItems.get(marketItemKey);
    }
}
