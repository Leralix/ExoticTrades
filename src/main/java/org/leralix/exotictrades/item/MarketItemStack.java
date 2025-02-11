package org.leralix.exotictrades.item;

import org.leralix.exotictrades.lang.Lang;
import org.leralix.exotictrades.market.StockMarketManager;
import org.leralix.exotictrades.storage.MarketItemKey;
import org.leralix.exotictrades.util.NumberUtil;

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

    public String getDescription() {
        double price = StockMarketManager.getMarketFor(MarketItemKey.of(item)).getCurrentPrice();
        return Lang.QUANTITY_ITEM_TO_SELL.get(item.getName(), NumberUtil.roundWithDigits(quantity * price), quantity, price);
    }
}
