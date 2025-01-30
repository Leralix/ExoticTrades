package org.leralix.exotictrades.item;

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

}
