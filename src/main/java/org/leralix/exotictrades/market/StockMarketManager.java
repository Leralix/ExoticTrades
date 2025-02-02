package org.leralix.exotictrades.market;

import org.bukkit.entity.Player;
import org.leralix.exotictrades.item.MarketItem;
import org.leralix.exotictrades.item.MarketItemStack;
import org.leralix.exotictrades.storage.MarketItemKey;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class StockMarketManager{

    private StockMarketManager() {
    }

    private static final HashMap<UUID, Long> playersConnections = new HashMap<>();

    private static final HashMap<MarketItemKey, StockMarket> marketItems = new HashMap<>();

    public static void newConnection(UUID playerID){
        if(playersConnections.containsKey(playerID)){
            playersConnections.replace(playerID, System.currentTimeMillis());
        } else {
            playersConnections.put(playerID, System.currentTimeMillis());
        }
    }

    public static int getNumberOfConnections(){
        updateConnections();
        return playersConnections.size();
    }

    private static void updateConnections() {
        long timeBeforeRemoval = (long) 7 * 24 * 60 * 60 * 1000;
        playersConnections.keySet().removeIf(playerID -> System.currentTimeMillis() - playersConnections.get(playerID) > timeBeforeRemoval);
    }

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
            StockMarket specificMarket = getStockMarket(new MarketItemKey(marketItemStack));
            total += specificMarket.sell(marketItemStack.getQuantity());
        }

        return total;
    }

    private static StockMarket getStockMarket(MarketItemKey marketItemKey) {
        return marketItems.get(marketItemKey);
    }

    public static void registerOrUpdateMarketItem(MarketItem marketItem, int timeLength, double maxPrice, double minPrice, double volatility, double basePrice) {
        MarketItemKey marketItemKey = new MarketItemKey(marketItem);
        marketItems.computeIfAbsent(marketItemKey, k -> new StockMarket(marketItem, timeLength, maxPrice, minPrice, volatility, basePrice));
    }

    public static void registerPlayer(Player player) {
        newConnection(player.getUniqueId());
    }
}
