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

    /**
     * The current price of the item.
     */
    private double currentPrice;

    private MarketConstants constants;

    /**
     * The history of every item sold in a period of days.
     * Used to evaluate the market demand
     */
    private final SellHistory sellHistory;

    public StockMarket(
            MarketItem marketItem,
            double maxPrice,
            double percentForMaxPrice,
            double minPrice,
            double percentForMinPrice,
            double demandMultiplier,
            double volatility,
            double basePrice,
            int timeLength
    ) {
        this.marketItem = marketItem;
        this.constants = new MarketConstants(maxPrice, percentForMaxPrice, minPrice, percentForMinPrice, demandMultiplier, volatility);

        this.currentPrice = basePrice;
        this.sellHistory = new SellHistory(timeLength);
    }


    public void updateHistory(int newQuantitySold) {
        sellHistory.recordSale(newQuantitySold);
    }

    /**
     * Get the expected demand of the ressource.
     * @return the quantity of item to be sold
     */
    public double getDemand() {
        int uniquePlayerCount = PlayerConnectionStorage.getNumberOfConnections();
        return uniquePlayerCount * constants.demandMultiplier();
    }

    public double getPercentSold(){

        double demand = getDemand();
        //Avoid dividing by 0
        if(demand < 0.1)
            demand = 0.1;

        return sellHistory.getTotalSales() / demand;
    }

    /**
     * Compute the price at the next update
     * @return the estimated new price
     */
    public double getEstimatedPrice() {

        double percent = getPercentSold();
        if(percent > 1){
            return getNextIncrease(percent);
        }
        return getNextDecrease(percent);
    }

    private double getNextDecrease(double percent) {
        double ratio = Math.max(1, constants.percentForMinPrice() / percent);
        return constants.minPrice() * ratio;
    }

    private double getNextIncrease(double percent) {
        double ratio = Math.min(percent - 1.0, constants.percentForMaxPrice()) / constants.percentForMaxPrice();
        return constants.maxPrice() * ratio;
    }

    public double sell(int amount){
        updateHistory(amount);
        return NumberUtil.roundWithDigits(currentPrice * amount);
    }


    public void updateMovingAverage() {
        updatePrice();
        sellHistory.nextRecord();
    }

    private void updatePrice() {
        double difference = getEstimatedPrice() - currentPrice;
        this.currentPrice = this.currentPrice + difference * constants.volatility();
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
                Lang.EXPECTED_NEXT_PRICE.get(getEstimatedPrice()),
                Lang.MIN_PRICE.get(constants.minPrice()),
                Lang.MAX_PRICE.get(constants.maxPrice()),
                Lang.CURRENT_SELLS.get(sellHistory.getTotalSales()),
                Lang.CURRENT_BUYS.get(getDemand())
        ));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }


    public void updateConstants(double maxPrice, double percentForMaxPrice, double minPrice, double percentForMinPrice, double demandMultiplier, double volatility, int timeLength) {
        this.constants = new MarketConstants(maxPrice, percentForMaxPrice, minPrice, percentForMinPrice, demandMultiplier, volatility);
        this.sellHistory.updateTimeLength(timeLength);
    }
}
