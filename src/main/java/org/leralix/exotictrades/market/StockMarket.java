package org.leralix.exotictrades.market;


import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.leralix.exotictrades.item.MarketItem;
import org.leralix.exotictrades.item.RareItem;
import org.leralix.exotictrades.lang.Lang;
import org.leralix.exotictrades.util.NumberUtil;

import java.util.*;

public class StockMarket {
    private final MarketItem marketItem;
    private final double demandMultiplier; // Coefficient for the demand of the item based on the amount of players
    private final double volatility;

    private double currentPrice;
    private final double maxPrice;
    private final double minPrice;
    private final double maxIncreasePercent;
    private final SellHistory sellHistory;

    // Constructor
    public StockMarket(MarketItem marketItem, double maxPrice, double minPrice, double demandMultiplier, double volatility, double basePrice, int timeLength) {
        this.marketItem = marketItem;
        this.demandMultiplier = demandMultiplier;
        this.volatility = volatility;

        this.maxPrice = NumberUtil.roundWithDigits(maxPrice);
        this.minPrice = NumberUtil.roundWithDigits(minPrice);
        this.maxIncreasePercent = 0.2;

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
        double maxIncrease = currentPrice * maxIncreasePercent;
        double newPrice = currentPrice + getSigmoid(deltaSold, maxIncrease, volatility);
        return NumberUtil.roundWithDigits(Math.min(Math.max(newPrice,minPrice),maxPrice));
    }

    public void updatePrice() {
        this.currentPrice = getNextPriceEstimation();
    }

    private double getSigmoid(double deltaSold, double maxValue, double volatility) {
        return (maxValue*2) * (1 / (1 + Math.exp(volatility * deltaSold))) - maxValue;
    }

    public double sell(int amount){
        double total = 0;
        for(int i = 0; i < amount; i++){
            total += currentPrice;
        }

        addAmountOfSells(amount);

        return NumberUtil.roundWithDigits(total);
    }


    public void updateMovingAverage() {
        updatePrice();
        updateToNextCursor();
    }

    public ItemStack getItemStack(){
        return marketItem.getItemStack(1);
    }

    public ItemStack getMarketInfo() {
        ItemStack itemStack = getItemStack();
        ItemMeta itemMeta = itemStack.getItemMeta();
        if(marketItem instanceof RareItem){
            itemMeta.setCustomModelData(marketItem.getModelData());
            itemMeta.setDisplayName(ChatColor.GREEN + marketItem.getName());
        }
        itemMeta.setLore(Arrays.asList(
                Lang.CURRENT_PRICE.get(currentPrice),
                Lang.EXPECTED_NEXT_PRICE.get(getNextPriceEstimation()),
                Lang.MIN_PRICE.get(minPrice),
                Lang.MAX_PRICE.get(maxPrice),
                Lang.CURRENT_SELLS.get(sellHistory.getAmount()),
                Lang.CURRENT_BUYS.get(getDemand())
        ));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }
}
