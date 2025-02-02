package org.leralix.exotictrades.market;


import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.leralix.exotictrades.item.MarketItem;
import org.leralix.exotictrades.lang.Lang;

import java.util.*;

public class StockMarket {
    // Initialization parameters
    private final MarketItem marketItem;
    private final double maxPrice;
    private final double minPrice;
    private final double midPrice;
    private final double Kprice; // Coefficient for the price increase after sells
    private final float KexpectedSells; // Coefficient for

    // Internal private variables
    private int expectedSells;
    private double currentPrice;
    private final SellHistory sellHistory;

    // Constructor
    public StockMarket(MarketItem marketItem, int timeLength, double maxPrice, double minPrice, double midPrice, double Kprice) {
        this.marketItem = marketItem;
        this.maxPrice = maxPrice;
        this.minPrice = minPrice;
        this.midPrice = midPrice;
        this.Kprice = Kprice;
        this.KexpectedSells = 1;
        this.sellHistory = new SellHistory(timeLength);

        // Initialize internal variables
        this.expectedSells = 0;
        this.currentPrice = 0;
    }

    public void updateToNextCursor() {
        sellHistory.updateToNextCursor();
    }

    // Method to add an amount to the amount of sells at the current cursor position
    public void addAmountOfSells(int sells) {
        sellHistory.addSell(sells);
    }

    // Updating the expected selling price mainly based on the amount of players that joined the server
    public void updateExpectedSells() {
        int uniquePlayerCount = StockMarketManager.getNumberOfConnections();
        this.expectedSells = (int) (uniquePlayerCount * midPrice * KexpectedSells);
    }
    
    // Core price update logic
    public void updatePrice() {
        // Get sells data from current cursor position
        int soldAmount = sellHistory.getAmount();
        // Update the expected sells value
        updateExpectedSells();

        System.out.printf("currentPrice : " + currentPrice);

        // 1. Calculate normalized price
        double cPrice;
        double normalizedPrice;

        if (currentPrice <= midPrice) {
            // Normalize between minPrice and midPrice
            normalizedPrice = (currentPrice - minPrice) / (midPrice - minPrice);
            // Scale to range [0.5, 1]
            cPrice = 0.5f + 0.5f * normalizedPrice;
        } else {
            // Normalize between midPrice and maxPrice
            normalizedPrice = (currentPrice - midPrice) / (maxPrice - midPrice);
            // Scale to range [1, 0.5]
            cPrice = 1.0f - 0.5f * normalizedPrice;
        }

        // 3. Calculate sales difference
        double difference = Math.abs(expectedSells - soldAmount);

        System.out.println("difference : " + difference);

        // 4. Determine price change direction
        double deltaPrice;
        if (soldAmount != expectedSells) {
            if (soldAmount < expectedSells) {
                deltaPrice = currentPrice > midPrice ?
                        (difference / expectedSells) * cPrice * Kprice :
                        (difference / expectedSells) * (2 - cPrice) * Kprice;
                currentPrice = Math.min(currentPrice + deltaPrice, maxPrice);
            } else {
                deltaPrice = currentPrice > midPrice ?
                        (difference / expectedSells) * (2 - cPrice) * Kprice :
                        (difference / expectedSells) * cPrice * Kprice;
                currentPrice = Math.max(currentPrice - deltaPrice, minPrice);
            }
            System.out.printf("new currentPrice : " + currentPrice);
        }
    }

    public double sell(int amount){
        double total = 0;
        for(int i = 0; i < amount; i++){
            total += currentPrice;
        }

        addAmountOfSells(amount);

        return total;
    }


    public void updateMovingAverage() {
        updatePrice();
        updateToNextCursor();
    }


    public ItemStack getMarketInfo() {
        ItemStack itemStack = new ItemStack(Material.GOLD_INGOT);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(marketItem.getName());
        itemMeta.setLore(Arrays.asList(
                Lang.CURRENT_PRICE.get(currentPrice),
                "Expected Sells: " + expectedSells,
                "Sold Items: " + sellHistory.getAmount()
        ));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
