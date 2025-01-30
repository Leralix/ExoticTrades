package org.leralix.exotictrades.item;

import org.bukkit.Material;

public class RareItem extends MarketItem {
    public RareItem(int id, String name, Material material, int modelData, double basePrice, double volatility) {
        super(id, name, material, modelData, basePrice, volatility);
    }
}
