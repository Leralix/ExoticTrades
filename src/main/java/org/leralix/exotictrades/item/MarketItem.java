package org.leralix.exotictrades.item;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MarketItem {

    private final int id;
    private final String name;
    private final Material material;
    private final int modelData;
    private final double basePrice;
    private final double volatility;

    public MarketItem(int id, String name, Material material, int modelData, double basePrice, double volatility) {
        this.id = id;
        this.name = name;
        this.material = material;
        this.modelData = modelData;
        this.basePrice = basePrice;
        this.volatility = volatility;
    }


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public Material getMaterial() {
        return material;
    }

    public int getModelData() {
        return modelData;
    }

    public ItemStack getItemStack(int quantity) {
        ItemStack item = new ItemStack(material);
        item.setAmount(quantity);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        return item;
    }
}
