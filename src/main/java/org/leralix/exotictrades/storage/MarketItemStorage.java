package org.leralix.exotictrades.storage;

import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.leralix.exotictrades.item.DropProbability;
import org.leralix.exotictrades.item.KillProbability;
import org.leralix.exotictrades.item.MarketItem;
import org.leralix.exotictrades.item.RareItem;
import org.leralix.lib.utils.config.ConfigTag;
import org.leralix.lib.utils.config.ConfigUtil;

import java.util.*;

public class MarketItemStorage {

    private MarketItemStorage() {
    }



    private static final Map<Material, List<DropProbability>> blockDropProbability = new EnumMap<>(Material.class);
    private static final Map<EntityType, List<KillProbability>> entityDropProbability = new EnumMap<>(EntityType.class);


    private static final Map<String, RareItem> rareItemsByName = new HashMap<>();
    private static final Map<MarketItemKey, MarketItem> marketItemByKey = new HashMap<>();
    private static final Map<Integer, RareItem> rareItems = new HashMap<>();
    private static final Map<Integer, MarketItem> marketItems = new HashMap<>();



    public static void init(){
        marketItemByKey.clear();
        blockDropProbability.clear();
        entityDropProbability.clear();


        Configuration defaultConfiguration = ConfigUtil.getCustomConfig(ConfigTag.MAIN);

        ConfigurationSection section = defaultConfiguration.getConfigurationSection("rareRessources");
        for (String resourceKey : section.getKeys(false)) {
            ConfigurationSection resourceSection = section.getConfigurationSection(resourceKey);

            if (resourceSection == null) continue;
            if(!resourceSection.getBoolean("enabled")) continue;

            resourceSection.addDefault("movingAverage", defaultConfiguration.getInt("defaultMovingAverage", 24));
            resourceSection.addDefault("minPrice", defaultConfiguration.getDouble("defaultMinPrice", 0));
            resourceSection.addDefault("maxPrice", defaultConfiguration.getDouble("defaultMaxPrice", 500));
            resourceSection.addDefault("basePrice", defaultConfiguration.getDouble("defaultBasePrice", 50));
            resourceSection.addDefault("volatility", defaultConfiguration.getDouble("defaultVolatility", 1));
            resourceSection.addDefault("demandMultiplier", defaultConfiguration.getDouble("defaultDemandMultiplier", 1));

            int id = resourceKey.hashCode();
            String name = resourceSection.getString("name");
            Material material = Material.matchMaterial(resourceSection.getString("material"));
            if(material == null){
                throw new IllegalStateException("Material " + resourceSection.getString("material") + " not found.");
            }
            int customModelData = resourceSection.getInt("customModelData");

            int movingAverage = resourceSection.getInt("movingAverage");
            double maxPrice = resourceSection.getDouble("maxPrice");
            double minPrice = resourceSection.getDouble("minPrice");
            double volatility = resourceSection.getDouble("volatility");
            double basePrice = resourceSection.getDouble("basePrice");
            double demandMultiplier = resourceSection.getDouble("demandMultiplier");

            RareItem rareItem = new RareItem(id, name, material, customModelData);

            ConfigurationSection dropChanceSection = resourceSection.getConfigurationSection("dropChance");
            if (dropChanceSection != null) {
                // Vérifier si ce sont des blocs
                if (dropChanceSection.contains("blocks")) {
                    ConfigurationSection blocksSection = dropChanceSection.getConfigurationSection("blocks");
                    blocksSection.getKeys(false).forEach(blockName -> {
                        ConfigurationSection blockSection = blocksSection.getConfigurationSection(blockName);
                        if (blockSection == null) return;

                        Material blockType = Material.matchMaterial(blockName);
                        if (blockType == null) return;

                        double baseChance = blockSection.getDouble("baseChance", 0);
                        double fortuneModifier = blockSection.getDouble("fortuneModifier", 0);
                        boolean allowSilkTouch = blockSection.getBoolean("allowSilkTouch", true);

                        if (!blockDropProbability.containsKey(blockType)) {
                            blockDropProbability.put(blockType, new ArrayList<>());
                        }
                        blockDropProbability.get(blockType).add(new DropProbability(baseChance, fortuneModifier, allowSilkTouch, id));
                    });
                }

                // Vérifier si ce sont des entités
                if (dropChanceSection.contains("entities")) {
                    ConfigurationSection entitiesSection = dropChanceSection.getConfigurationSection("entities");
                    entitiesSection.getKeys(false).forEach(entityName -> {
                        ConfigurationSection entitySection = entitiesSection.getConfigurationSection(entityName);
                        if (entitySection == null) return;

                        EntityType entityType = EntityType.valueOf(entityName);

                        double baseChance = entitySection.getDouble("baseChance", 0);
                        double lootingModifier = entitySection.getDouble("lootingModifier", 0);

                        if (!entityDropProbability.containsKey(entityType)) {
                            entityDropProbability.put(entityType, new ArrayList<>());
                        }
                        entityDropProbability.get(entityType).add(new KillProbability(baseChance, lootingModifier, id));
                    });
                }
            }


            marketItemByKey.put(MarketItemKey.of(rareItem), rareItem);
            rareItemsByName.put(name, rareItem);
            rareItems.put(id, rareItem);
            marketItems.put(id, rareItem);
        }


        ConfigurationSection marketItemSection = defaultConfiguration.getConfigurationSection("marketItem");

        if (marketItemSection != null) {
            for (String resourceKey : marketItemSection.getKeys(false)) {
                Material material = Material.valueOf(marketItemSection.getString(resourceKey));
                MarketItem marketItem = new MarketItem(resourceKey.hashCode(),material);
                marketItems.put(resourceKey.hashCode(), marketItem);
                marketItemByKey.put(MarketItemKey.of(marketItem), marketItem);
            }
        }

    }

    public static RareItem getRareItem(int rareItemID) {
        return rareItems.get(rareItemID);
    }

    public static List<RareItem> getRareItemsDropped(Material blockType, ItemStack itemUsed){
        List<RareItem> items = new ArrayList<>();
        if(blockDropProbability.containsKey(blockType)){
            blockDropProbability.get(blockType).forEach(dropProbability -> {
                RareItem item = dropProbability.shouldDrop(itemUsed);
                if(item != null){
                    items.add(item);
                }
            });
        }
        return items;
    }

    public static List<RareItem> getRareItemsDropped(EntityType entityType, ItemStack itemUsed){
        List<RareItem> items = new ArrayList<>();
        if(entityDropProbability.containsKey(entityType)){
            entityDropProbability.get(entityType).forEach(killProbability -> {
                RareItem item = killProbability.shouldDrop(itemUsed);
                if(item != null){
                    items.add(item);
                }
            });
        }
        return items;
    }

    public static MarketItem getMarketItem(MarketItemKey key) {
        return marketItemByKey.get(key);
    }

    public static List<RareItem> getAllRareItems() {
        return new ArrayList<>(rareItems.values());
    }

    public static List<MarketItem> getAllMarketItems() {
        return new ArrayList<>(marketItems.values());
    }
    public static MarketItem getMarketItem(String name) {
        return rareItemsByName.get(name);
    }

    public static MarketItem getMarketItem(int marketID){
        if(rareItems.containsKey(marketID)) {
            return rareItems.get(marketID);
        }
        if(marketItems.containsKey(marketID)){
            return marketItems.get(marketID);
        }
        return null;
    }
}
