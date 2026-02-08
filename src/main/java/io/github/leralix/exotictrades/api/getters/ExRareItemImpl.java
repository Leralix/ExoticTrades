package io.github.leralix.exotictrades.api.getters;

import io.github.leralix.exotictrades.item.MarketItem;
import io.github.leralix.exotictrades.market.StockMarket;
import io.github.leralix.interfaces.ExRareItem;

public class ExRareItemImpl implements ExRareItem {

    private final MarketItem marketItem;
    private final StockMarket stockMarket;

    private ExRareItemImpl(MarketItem marketItem, StockMarket stockMarket) {
        this.marketItem = marketItem;
        this.stockMarket = stockMarket;
    }

    public static ExRareItemImpl of(MarketItem marketItem, StockMarket stockMarket) {
        return new ExRareItemImpl(marketItem, stockMarket);
    }


    @Override
    public String getName() {
        return marketItem.getName();
    }

    @Override
    public double getPrice() {
        return stockMarket.getCurrentPrice();
    }

    @Override
    public double getNextPrice() {
        return stockMarket.getPriceNextHour();
    }

    @Override
    public double getMinPrice() {
        return stockMarket.getMinPrice();
    }

    @Override
    public double getMaxPrice() {
        return stockMarket.getMaxPrice();
    }

    @Override
    public double getDemand() {
        return stockMarket.getDemand();
    }

    @Override
    public double getOffer() {
        return stockMarket.getNumberSold();
    }
}
