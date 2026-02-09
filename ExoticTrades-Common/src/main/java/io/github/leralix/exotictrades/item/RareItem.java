package io.github.leralix.exotictrades.item;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


public class RareItem extends MarketItem {

    private final String name;
    private final int modelData;

    public RareItem(int id, String name, Material material, int modelData) {
        super(id, material);
        this.name = name;
        this.modelData = modelData;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getModelData() {
        return modelData;
    }
    @Override
    public ItemStack getItemStack(int quantity) {
        ItemStack item = new ItemStack(material);
        item.setAmount(quantity);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.AQUA + name);
        meta.setCustomModelData(modelData);
        item.setItemMeta(meta);
        return item;
    }

}
