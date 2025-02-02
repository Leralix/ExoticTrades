package org.leralix.exotictrades.item;

import org.leralix.exotictrades.lang.Lang;

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
        return Lang.QUANTITY_ITEM_TO_SELL.get(item.getName(), quantity, item.getName());
    }
}
