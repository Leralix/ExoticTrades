package io.github.leralix.exotictrades.market;


import io.github.leralix.exotictrades.item.MarketItem;
import io.github.leralix.exotictrades.item.RareItem;
import io.github.leralix.exotictrades.lang.Lang;
import io.github.leralix.exotictrades.util.NumberUtil;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

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
        this.constants = new MarketConstants(basePrice, maxPrice, percentForMaxPrice, minPrice, percentForMinPrice, demandMultiplier, volatility);

        this.currentPrice = basePrice;
        this.sellHistory = new SellHistory(timeLength);
    }


    public void updateHistory(int newQuantitySold) {
        sellHistory.recordSale(newQuantitySold);
    }

    /**
     * Get the expected demand of the ressource.
     *
     * @return the quantity of item to be sold
     */
    public double getDemand() {
        int uniquePlayerCount = PlayerConnectionStorage.getNumberOfConnections();
        return uniquePlayerCount * constants.demandMultiplier();
    }

    public double getPercentSold() {
        double demand = getDemand();
        return sellHistory.getTotalSales() / demand;
    }

    /**
     * Compute the price at the next update
     *
     * @return the estimated new price
     */
    public double getPriceNextHour() {
        double targetPrice = getTargetPrice();
        double difference = targetPrice - currentPrice;
        return NumberUtil.roundWithDigits(currentPrice + difference * constants.volatility());

    }

    double getTargetPrice() {
        double percent = getPercentSold();
        return NumberUtil.roundWithDigits(percent > 1 ? getEstimatedPriceDown(percent) : getEstimatedPriceUp(percent));
    }

    private double getEstimatedPriceDown(double percent) {
        double ratio = (percent - 1.0) / (constants.percentForMinPrice() - 1.0);
        ratio = Math.min(1.0, ratio);
        return constants.basePrice() - (constants.basePrice() - constants.minPrice()) * ratio;
    }

    private double getEstimatedPriceUp(double percent) {
        double ratio = (1.0 - percent) / (1.0 - constants.percentForMaxPrice());
        ratio = Math.min(1.0, ratio);
        return constants.basePrice() + (constants.maxPrice() - constants.basePrice()) * ratio;
    }


    public double sell(int amount) {
        updateHistory(amount);
        return NumberUtil.roundWithDigits(currentPrice * amount);
    }


    public void updateMovingAverage() {
        updatePrice();
        sellHistory.nextRecord();
    }

    private void updatePrice() {
        this.currentPrice = getPriceNextHour();
    }

    public ItemStack getItemStack() {
        return marketItem.getItemStack(1);
    }

    public ItemStack getMarketInfo() {
        ItemStack itemStack = getItemStack();
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (marketItem instanceof RareItem) {
            itemMeta.setCustomModelData(marketItem.getModelData());
            itemMeta.setDisplayName(ChatColor.GREEN + marketItem.getName());
        }
        itemMeta.setLore(Arrays.asList(
                Lang.CURRENT_PRICE.get(getCurrentPrice()),
                Lang.TARGET_PRICE.get(getTargetPrice()),
                Lang.EXPECTED_NEXT_PRICE.get(getPriceNextHour()),
                Lang.MIN_PRICE.get(constants.minPrice()),
                Lang.MAX_PRICE.get(constants.maxPrice()),
                Lang.CURRENT_SELLS.get(sellHistory.getTotalSales()),
                Lang.CURRENT_BUYS.get(getDemand())
        ));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }


    public void updateConstants(double basePrice, double maxPrice, double percentForMaxPrice, double minPrice, double percentForMinPrice, double demandMultiplier, double volatility, int timeLength) {
        this.constants = new MarketConstants(basePrice, maxPrice, percentForMaxPrice, minPrice, percentForMinPrice, demandMultiplier, volatility);
        this.sellHistory.updateTimeLength(timeLength);
    }

    public double getCurrentPrice() {
        return NumberUtil.roundWithDigits(currentPrice);
    }

    public double getMinPrice() {
        return constants.minPrice();
    }

    public double getMaxPrice() {
        return constants.maxPrice();
    }

    public double getNumberSold() {
        return sellHistory.getTotalSales();
    }
}
