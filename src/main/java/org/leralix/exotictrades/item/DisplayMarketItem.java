package org.leralix.exotictrades.item;

import net.md_5.bungee.api.ChatColor;
import org.leralix.exotictrades.lang.Lang;

public class DisplayMarketItem {

    String name;
    double price;
    int quantity;

    public DisplayMarketItem(MarketItem item, int quantity) {
        this.name = item.getName();
        this.price = item.getBasePrice();
        this.quantity = quantity;
    }

    public String getLine(){
        return ChatColor.RESET + Lang.QUANTITY_ITEM_TO_SELL.get(name, quantity * price, quantity, price);
    }
    @Override
    public String toString() {
        return name + " : " + price + " : " + quantity;
    }
}
