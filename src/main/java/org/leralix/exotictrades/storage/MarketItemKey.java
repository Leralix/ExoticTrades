package org.leralix.exotictrades.storage;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.leralix.exotictrades.item.MarketItem;

public class MarketItemKey {
    private final Material material;
    private final int modelData;

    public MarketItemKey(MarketItem item) {
        this.material = item.getMaterial();
        this.modelData = item.getModelData();
    }

    public MarketItemKey(ItemStack item) {
        this.material = item.getType();
        this.modelData = item.getItemMeta().hasCustomModelData() ? item.getItemMeta().getCustomModelData() : 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MarketItemKey key) {
            return key.material == material && key.modelData == modelData;
        }
        return false;
    }
    @Override
    public int hashCode() {
        return material.hashCode() + modelData;
    }

}
