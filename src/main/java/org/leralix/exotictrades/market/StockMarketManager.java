package org.leralix.exotictrades.market;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.leralix.exotictrades.ExoticTrades;
import org.leralix.exotictrades.item.MarketItem;
import org.leralix.exotictrades.item.MarketItemStack;
import org.leralix.exotictrades.storage.MarketItemKey;
import org.leralix.exotictrades.storage.Adapters.MarketItemKeyAdapter;

import java.io.*;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class StockMarketManager{

    private StockMarketManager() {
    }

    private static HashMap<MarketItemKey, StockMarket> marketItems = new HashMap<>();

    public static void updateMovingAverage() {
        for(StockMarket stockMarket : marketItems.values()){
            stockMarket.updateMovingAverage();
        }
    }

    public static List<StockMarket> getAllStocks() {
        return Collections.list(Collections.enumeration(marketItems.values()));
    }


    public static double sellMarketItems(List<MarketItemStack> marketItemStackList) {
        double total = 0;
        for(MarketItemStack marketItemStack : marketItemStackList){
            StockMarket specificMarket = getStockMarket(MarketItemKey.of(marketItemStack));
            total += specificMarket.sell(marketItemStack.getQuantity());
        }

        return total;
    }

    private static StockMarket getStockMarket(MarketItemKey marketItemKey) {
        return marketItems.get(marketItemKey);
    }

    public static void registerOrUpdateMarketItem(MarketItem marketItem, int timeLength, double maxPrice, double minPrice, double volatility,double demandMultiplier, double basePrice) {
        MarketItemKey marketItemKey = MarketItemKey.of(marketItem);
        marketItems.computeIfAbsent(marketItemKey, k -> new StockMarket(marketItem, maxPrice, minPrice, demandMultiplier, volatility, basePrice, timeLength));
    }


    public static void save() {

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
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

    public static void load(){

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(MarketItemKey.class, new MarketItemKeyAdapter())
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

    public static StockMarket getMarketFor(MarketItemKey marketItemKey) {
        return marketItems.get(marketItemKey);
    }
}
