package org.leralix.exotictrades.storage;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.units.qual.A;
import org.leralix.exotictrades.item.DropProbability;
import org.leralix.exotictrades.item.KillProbability;
import org.leralix.exotictrades.item.LootProbability;
import org.leralix.exotictrades.item.RareItem;
import org.leralix.lib.utils.config.ConfigTag;
import org.leralix.lib.utils.config.ConfigUtil;

import java.util.*;

public class RareItemStorage {


    private static final Map<MarketItemKey, String> marketItemById = new HashMap<>();

    private static final Map<Material, List<DropProbability>> blockDropProbability = new EnumMap<>(Material.class);
    private static final Map<EntityType, List<KillProbability>> entityDropProbability = new EnumMap<>(EntityType.class);
    private static final Map<Integer, RareItem> rareItems = new HashMap<>();



    public static void init(){
        marketItemById.clear();
        blockDropProbability.clear();
        entityDropProbability.clear();

        ConfigurationSection section = ConfigUtil.getCustomConfig(ConfigTag.MAIN).getConfigurationSection("rareRessources");
        for (String resourceKey : section.getKeys(false)) {
            ConfigurationSection resourceSection = section.getConfigurationSection(resourceKey);
            if (resourceSection == null) continue;

            if(!resourceSection.getBoolean("enabled")) continue;

            int id = resourceSection.getInt("id");
            String name = resourceSection.getString("name");
            Material material = Material.matchMaterial(resourceSection.getString("material"));
            int customModelData = resourceSection.getInt("customModelData");
            double basePrice = resourceSection.getDouble("basePrice");
            double volatility = resourceSection.getDouble("volatility");

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
}
