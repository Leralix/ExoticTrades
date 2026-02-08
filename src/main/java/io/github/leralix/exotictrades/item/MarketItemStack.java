package io.github.leralix.exotictrades.item;

import io.github.leralix.exotictrades.lang.Lang;
import io.github.leralix.exotictrades.market.StockMarketManager;
import io.github.leralix.exotictrades.storage.MarketItemKey;
import io.github.leralix.exotictrades.util.NumberUtil;

public class MarketItemStack {

    MarketItem item;
    int quantity;

    public MarketItemStack(MarketItem item, int quantity){
        this.item = item;
        this.quantity = quantity;
    }

    public MarketItem getItem(){
        return item;
    }

    public int getQuantity(){
        return quantity;
    }

    public String getDescription(StockMarketManager stockMarketManager) {
        double price = stockMarketManager.getMarketFor(MarketItemKey.of(item)).getCurrentPrice();
        return Lang.QUANTITY_ITEM_TO_SELL.get(item.getName(), NumberUtil.roundWithDigits(quantity * price), quantity, price);
    }
}
