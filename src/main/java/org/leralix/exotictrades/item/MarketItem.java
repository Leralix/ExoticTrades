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

    protected String getPriceEvolutionString(double price, double estimatedPrice) {
        if(estimatedPrice > price){
            return ChatColor.GREEN + "▲" + price;
        }
        else if(estimatedPrice < price){
            return ChatColor.RED + "▼" + price;
        }
        else{
            return ChatColor.WHITE + "->" + price;
        }
    }

    public int getModelData() {
        return 0;
    }

    public ItemStack getMarketInfoForPlayer() {
        StockMarket stockMarket = StockMarketManager.getMarketFor(MarketItemKey.of(this));

        double price = stockMarket.getCurrentPrice();
        double estimatedPrice = stockMarket.getPriceNextHour();

        String priceEvolutionString = getPriceEvolutionString(price, estimatedPrice);
        List<String> description = new ArrayList<>();
        description.add(ChatColor.WHITE + "Current price : " + priceEvolutionString);

        ItemStack item = getItemStack(1);
        ItemMeta meta = item.getItemMeta();
        if(this instanceof RareItem rareItem){
            meta.setDisplayName(ChatColor.GREEN + rareItem.getName());
        }
        meta.setLore(description);
        item.setItemMeta(meta);
        return item;
    }

    public String getName() {
        String name = material.toString().toLowerCase().replace("_", " "); //Spigot cannot deal with TranslatableComponent in item lore
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }
}
