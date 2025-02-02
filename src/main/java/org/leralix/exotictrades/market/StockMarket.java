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
    private final double demandMultiplier; // Coefficient for the demand of the item based on the amount of players
    private final double volatility;

    // Internal private variables
    private double currentPrice;

    private final SellHistory sellHistory;

    // Constructor
    public StockMarket(MarketItem marketItem, double maxPrice, double minPrice, double demandMultiplier, double volatility, double basePrice, int timeLength) {
        this.marketItem = marketItem;
        this.maxPrice = maxPrice;
        this.minPrice = minPrice;
        this.midPrice = maxPrice - minPrice;
        this.demandMultiplier = demandMultiplier;
        this.volatility = volatility;

        this.currentPrice = basePrice;
        this.sellHistory = new SellHistory(timeLength);
    }

    public void updateToNextCursor() {
        sellHistory.updateToNextCursor();
    }

    // Method to add an amount to the amount of sells at the current cursor position
    public void addAmountOfSells(int sells) {
        sellHistory.addSell(sells);
    }

    // Updating the expected selling price mainly based on the amount of players that joined the server
    public double getDemand() {
        int uniquePlayerCount = PlayerConnectionStorage.getNumberOfConnections();
        return uniquePlayerCount * demandMultiplier;
    }


    public double getNextPriceEstimation() {
        double deltaSold = sellHistory.getAmount() - getDemand();
        return currentPrice + getSigmoid(deltaSold, 10, volatility);
    }

    // Core price update logic
    public void updatePrice() {
        this.currentPrice = getNextPriceEstimation();
    }

    private double getSigmoid(double deltaSold, int maxValue, double volatility) {
        return (maxValue*2) * (1 / (1 + Math.exp(volatility * deltaSold))) - maxValue;
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
                "Current sells: " + sellHistory.getAmount(),
                "Expected Sells: " + getDemand(),
                "Expected next price: " + getNextPriceEstimation()
        ));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
