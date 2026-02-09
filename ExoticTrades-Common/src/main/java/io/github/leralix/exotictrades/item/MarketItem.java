package io.github.leralix.exotictrades.item;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class MarketItem {

    protected final Material material;
    protected int id;

    public MarketItem(int id, Material material) {
        this.material = material;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public Material getMaterial() {
        return material;
    }



    public ItemStack getItemStack(int quantity) {
        ItemStack item = new ItemStack(material);
        item.setAmount(quantity);
        return item;
    }

    public int getModelData() {
        return 0;
    }

    public String getName() {
        String name = material.toString().toLowerCase().replace("_", " "); //Spigot cannot deal with TranslatableComponent in item lore
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }
}
