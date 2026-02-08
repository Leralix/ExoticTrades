package io.github.leralix.exotictrades.storage;

import io.github.leralix.exotictrades.item.*;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class MarketItemStorage {

    private final Map<Material, List<DropProbability>> blockDropProbability;
    private final Map<EntityType, List<KillProbability>> entityDropProbability;
    private final Map<Material, List<FishProbability>> entityFishProbability;

    private final Map<String, RareItem> rareItemsByName;
    private final Map<MarketItemKey, MarketItem> marketItemByKey;
    private final Map<Integer, RareItem> rareItems;
    private final Map<Integer, MarketItem> marketItems;

    public MarketItemStorage(FileConfiguration fileConfiguration){
        this.blockDropProbability = new EnumMap<>(Material.class);
        this.entityDropProbability = new EnumMap<>(EntityType.class);
        this.entityFishProbability = new EnumMap<>(Material.class);

        this.rareItemsByName = new HashMap<>();
        this.marketItemByKey = new HashMap<>();
        this.rareItems = new HashMap<>();
        this.marketItems = new HashMap<>();



        ConfigurationSection section = fileConfiguration.getConfigurationSection("rareRessources");
        if(section != null){
            for (String resourceKey : section.getKeys(false)) {
                ConfigurationSection resourceSection = section.getConfigurationSection(resourceKey);

                if (resourceSection == null){
                    continue;
                }

                if(!resourceSection.getBoolean("enabled")){
                    continue;
                }

                int id = resourceKey.hashCode();
                String name = resourceSection.getString("name");
                Material material = Material.matchMaterial(resourceSection.getString("material"));
                if(material == null){
                    throw new IllegalStateException("Material " + resourceSection.getString("material") + " not found.");
                }
                int customModelData = resourceSection.getInt("customModelData");

                RareItem rareItem = new RareItem(id, name, material, customModelData);

                ConfigurationSection dropChanceSection = resourceSection.getConfigurationSection("dropChance");
                if (dropChanceSection != null) {
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

                    if (dropChanceSection.contains("fishing")) {
                        ConfigurationSection entitiesSection = dropChanceSection.getConfigurationSection("fishing");
                        entitiesSection.getKeys(false).forEach(entityName -> {
                            ConfigurationSection entitySection = entitiesSection.getConfigurationSection(entityName);
                            if (entitySection == null) return;

                            Material fishedMaterial = Material.valueOf(entityName);

                            double baseChance = entitySection.getDouble("baseChance", 0);
                            double luckOfTheSeaModifier = entitySection.getDouble("luckOfTheSeaModifier", 0);
                            boolean replaceReward = entitySection.getBoolean("replaceVanilla", false);

                            if (!entityFishProbability.containsKey(fishedMaterial)) {
                                entityFishProbability.put(fishedMaterial, new ArrayList<>());
                            }
                            entityFishProbability.get(fishedMaterial).add(new FishProbability(baseChance,luckOfTheSeaModifier, replaceReward, id));
                        });
                    }
                }


                marketItemByKey.put(MarketItemKey.of(rareItem), rareItem);
                rareItemsByName.put(name, rareItem);
                rareItems.put(id, rareItem);
                marketItems.put(id, rareItem);
            }
        }


        ConfigurationSection marketItemSection = fileConfiguration.getConfigurationSection("marketItem");
        if (marketItemSection != null) {
            for (String resourceKey : marketItemSection.getKeys(false)) {
                Material material = Material.valueOf(marketItemSection.getString(resourceKey));
                MarketItem marketItem = new MarketItem(resourceKey.hashCode(),material);
                marketItems.put(resourceKey.hashCode(), marketItem);
                marketItemByKey.put(MarketItemKey.of(marketItem), marketItem);
            }
        }

    }

    public RareItem getRareItem(int rareItemID) {
        return rareItems.get(rareItemID);
    }

    public List<RareItem> getRareItemsDropped(Material blockType, ItemStack itemUsed){
        List<RareItem> items = new ArrayList<>();
        if(blockDropProbability.containsKey(blockType)){
            blockDropProbability.get(blockType).forEach(dropProbability -> {
                Integer itemID = dropProbability.shouldDrop(itemUsed);
                if(itemID != null){
                    items.add(getRareItem(itemID));
                }
            });
        }
        return items;
    }

    public List<RareItem> getRareItemsDropped(EntityType entityType, ItemStack itemUsed){
        List<RareItem> items = new ArrayList<>();
        if(entityDropProbability.containsKey(entityType)){
            entityDropProbability.get(entityType).forEach(killProbability -> {
                Integer itemID = killProbability.shouldDrop(itemUsed);
                if(itemID != null){
                    items.add(getRareItem(itemID));
                }
            });
        }
        return items;
    }

    public MarketItem getMarketItem(MarketItemKey key) {
        return marketItemByKey.get(key);
    }

    public List<RareItem> getAllRareItems() {
        return new ArrayList<>(rareItems.values());
    }

    public List<MarketItem> getAllMarketItems() {
        return new ArrayList<>(marketItems.values());
    }
    public MarketItem getMarketItem(String name) {
        return rareItemsByName.get(name);
    }

    public MarketItem getMarketItem(int marketID){
        if(marketItems.containsKey(marketID)){
            return marketItems.get(marketID);
        }
        return null;
    }

    public Collection<MarketItemKey> getAllMarketItemsKey() {
        return marketItemByKey.keySet();
    }

    public List<RareItem> getRareItemFished(Material type, PlayerFishEvent event) {
        List<RareItem> items = new ArrayList<>();
        if(entityFishProbability.containsKey(type)){
            entityFishProbability.get(type).forEach(fishProbability -> {
                Integer item = fishProbability.shouldDrop(event.getPlayer().getInventory().getItemInMainHand());
                if(item != null){
                    items.add(getRareItem(item));
                    if(fishProbability.shouldReplaceReward()){
                        Entity entityType = event.getCaught();
                        if(entityType != null){
                            event.getCaught().remove();
                        }
                    }
                }
            });
        }
        return items;
    }
}
