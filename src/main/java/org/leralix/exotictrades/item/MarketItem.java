package org.leralix.exotictrades.item;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MarketItem {

    String id;
    String name;
    Material material;
    int modelData;
    int basePrice;

    public MarketItem(String id, String name, Material material, int modelData, int basePrice) {
        this.id = id;
        this.name = name;
        this.material = material;
        this.modelData = modelData;
        this.basePrice = basePrice;
    }


    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getBasePrice() {
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
