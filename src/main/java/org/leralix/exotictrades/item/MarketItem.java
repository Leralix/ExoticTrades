package org.leralix.exotictrades.item;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.leralix.exotictrades.market.StockMarket;
import org.leralix.exotictrades.market.StockMarketManager;
import org.leralix.exotictrades.storage.MarketItemKey;

import java.util.ArrayList;
import java.util.List;

public class MarketItem {

    private final int id;
    private final String name;
    private final Material material;
    private final int modelData;
    private final double basePrice;

    public MarketItem(int id, String name, Material material, int modelData, double basePrice) {
        this.id = id;
        this.name = name;
        this.material = material;
        this.modelData = modelData;
        this.basePrice = basePrice;
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
        meta.setCustomModelData(modelData);
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack getMarketInfoForPlayer() {

        StockMarket stockMarket = StockMarketManager.getMarketFor(MarketItemKey.of(this));
        double price = stockMarket.getCurrentPrice();
        double estimaedPrice = stockMarket.getNextPriceEstimation();

        String priceEvolutionString = getPriceEvolutionString(price, estimaedPrice);
        List<String> description = new ArrayList<>();
        description.add(ChatColor.WHITE + "Current price : " + priceEvolutionString);

        ItemStack item = getItemStack(1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + name);
        meta.setLore(description);
        item.setItemMeta(meta);
        return item;
    }

    private String getPriceEvolutionString(double price, double estimatedPrice) {
        if(estimatedPrice > price){
            return ChatColor.GREEN + "▲ " + ChatColor.WHITE + price;
        }
        else if(estimatedPrice < price){
            return ChatColor.RED + "▼ " + ChatColor.WHITE + price;
        }
        else{
            return ChatColor.WHITE + "->" + price;
        }
    }
}
