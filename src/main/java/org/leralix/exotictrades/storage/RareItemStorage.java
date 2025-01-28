package org.leralix.exotictrades.storage;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.leralix.exotictrades.item.MarketItem;
import org.leralix.lib.utils.config.ConfigTag;
import org.leralix.lib.utils.config.ConfigUtil;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class RareItemStorage {


    private static final Map<MarketItemKey, String> marketItemById = new HashMap<>();
    private static final Map<Material, String> marketItemByMaterial = new EnumMap<>(Material.class);
    private static final Map<EntityType, String> marketItemByEntity = new EnumMap<>(EntityType.class);

    private static final Map<String, MarketItem> marketItems = new HashMap<>();



    public static void init(){
        marketItemById.clear();
        marketItemByMaterial.clear();
        marketItemByEntity.clear();

        ConfigUtil.getCustomConfig(ConfigTag.MAIN).getMapList("rareItems").forEach(entry -> {
            String id = (String) entry.get("id");
            String name = (String) entry.get("name");
            int basePrice = (int) entry.get("basePrice");
            int modelID = (int) entry.get("modelID");
            Material material = Material.valueOf((String) entry.get("material"));

            MarketItem item = new MarketItem(id, name, material, basePrice, modelID);

            Map<String, Object> dropChanceBlocks = (Map<String, Object>) entry.get("dropChance.blocks");

            dropChanceBlocks.forEach((blockName, blockData) -> {
                Material blockType = Material.valueOf(blockName);
                Map<String, Object> blockProperties = (Map<String, Object>) blockData;

                double baseChance = (double) blockProperties.get("baseChance");
                int fortuneModifier = (int) blockProperties.get("FortuneModifier");
                boolean allowSilkTouch = (boolean) blockProperties.get("allowSilkTouch");
            });



            marketItems.put(id, item);
            marketItemById.put(new MarketItemKey(item), item.getId());
        });
    }


}
