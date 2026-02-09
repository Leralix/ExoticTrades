package io.github.leralix.exotictrades.storage;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import io.github.leralix.exotictrades.item.MarketItem;
import io.github.leralix.exotictrades.item.MarketItemStack;

public class MarketItemKey {
    private final Material material;
    private final int modelData;

    public static MarketItemKey of(String material, int modelData) {
        return new MarketItemKey(Material.valueOf(material), modelData);
    }

    public static MarketItemKey of(MarketItem item) {
        return new MarketItemKey(item);
    }

    public static MarketItemKey of(MarketItemStack item) {
        return new MarketItemKey(item);
    }

    public static MarketItemKey of(ItemStack item) {
        return new MarketItemKey(item);
    }

    private MarketItemKey(Material material, int modelData) {
        this.material = material;
        this.modelData = modelData;
    }

    private MarketItemKey(MarketItem item) {
        this.material = item.getMaterial();
        this.modelData = item.getModelData();
    }

    private MarketItemKey(MarketItemStack item) {
        this(item.getItem());
    }

    private MarketItemKey(ItemStack item) {
        this.material = item.getType();
        this.modelData = item.getItemMeta().hasCustomModelData() ? item.getItemMeta().getCustomModelData() : 0;
    }
    @Override
    public String toString() {
        return material + ":" + modelData;
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
