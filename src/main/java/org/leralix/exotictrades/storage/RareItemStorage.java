package org.leralix.exotictrades.storage;

import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.leralix.exotictrades.ExoticTrades;
import org.leralix.exotictrades.item.DropProbability;
import org.leralix.exotictrades.item.KillProbability;
import org.leralix.exotictrades.item.MarketItem;
import org.leralix.exotictrades.item.RareItem;
import org.leralix.exotictrades.market.StockMarketManager;
import org.leralix.lib.utils.config.ConfigTag;
import org.leralix.lib.utils.config.ConfigUtil;

import java.util.*;

public class RareItemStorage {

    private RareItemStorage() {

    }

    private static final Map<Material, List<DropProbability>> blockDropProbability = new EnumMap<>(Material.class);
    private static final Map<EntityType, List<KillProbability>> entityDropProbability = new EnumMap<>(EntityType.class);


    private static final Map<String, Integer> marketItemByName = new HashMap<>();
    private static final Map<MarketItemKey, Integer> marketItemById = new HashMap<>();
    private static final Map<Integer, RareItem> rareItems = new HashMap<>();



    public static void init(){
        marketItemById.clear();
        blockDropProbability.clear();
        entityDropProbability.clear();


        Configuration defaultConfiguration = new MemoryConfiguration();

        ConfigUtil.getCustomConfig(ConfigTag.MAIN).setDefaults(defaultConfiguration);

        ConfigurationSection section = ConfigUtil.getCustomConfig(ConfigTag.MAIN).getConfigurationSection("rareRessources");
        for (String resourceKey : section.getKeys(false)) {
            ConfigurationSection resourceSection = section.getConfigurationSection(resourceKey);

            if (resourceSection == null) continue;
            if(!resourceSection.getBoolean("enabled")) continue;

            resourceSection.addDefault("movingAverage", defaultConfiguration.getInt("defaultMovingAverage", 24));
            resourceSection.addDefault("minPrice", defaultConfiguration.getDouble("defaultMinPrice", 0));
            resourceSection.addDefault("maxPrice", defaultConfiguration.getDouble("defaultMaxPrice", 1000));
            resourceSection.addDefault("basePrice", defaultConfiguration.getDouble("defaultBasePrice", 100));
            resourceSection.addDefault("volatility", defaultConfiguration.getDouble("defaultVolatility", 1));

            int id = resourceSection.getInt("id");
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

            RareItem rareItem = new RareItem(id, name, material, customModelData, basePrice, volatility);



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
                        if (entityType == null) return;

                        double baseChance = entitySection.getDouble("baseChance", 0);
                        double lootingModifier = entitySection.getDouble("lootingModifier", 0);

                        if (!entityDropProbability.containsKey(entityType)) {
                            entityDropProbability.put(entityType, new ArrayList<>());
                        }
                        entityDropProbability.get(entityType).add(new KillProbability(baseChance, lootingModifier, id));
                    });
                }
            }

            StockMarketManager.registerOrUpdateMarketItem(rareItem, movingAverage, maxPrice, minPrice, volatility, basePrice);

            marketItemById.put(new MarketItemKey(rareItem), id);
            marketItemByName.put(name, id);
            rareItems.put(id, rareItem);
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
        Integer id = marketItemById.get(key);
        if (id == null) return null;
        return getRareItem(id);
    }

    public static List<RareItem> getAllRareItems() {
        return new ArrayList<>(rareItems.values());
    }
    public static MarketItem getMarketItem(String name) {
        Integer id = marketItemByName.get(name);
        if (id == null) return null;
        return getRareItem(id);
    }
}
