package org.leralix.exotictrades.market;

import org.leralix.exotictrades.item.MarketItemStack;
import org.leralix.exotictrades.storage.MarketItemKey;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class StockMarketManager{

    private static HashMap<UUID, Long> playersConnections = new HashMap<>();

    private static HashMap<MarketItemKey, StockMarket> marketItems = new HashMap<>();




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


    public double sellMarketItems(List<MarketItemStack> marketItemStackList) {
        double total = 0;
        for(MarketItemStack marketItemStack : marketItemStackList){
            StockMarket specificMarket = marketItems.get(new MarketItemKey(marketItemStack));
            total += specificMarket.sell(marketItemStack.getQuantity());
        }

        return total;
    }
}
