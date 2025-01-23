package org.leralix.exotictrades.traders;

import org.bukkit.Material;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;

public enum TraderBiome {

    PLAINS(Villager.Type.PLAINS, Material.GRASS_BLOCK),
    DESERT(Villager.Type.DESERT, Material.SAND),
    SAVANNA(Villager.Type.SAVANNA, Material.ACACIA_LOG),
    SNOW(Villager.Type.SNOW, Material.SNOW_BLOCK),
    TAIGA(Villager.Type.TAIGA, Material.SPRUCE_LOG),
    JUNGLE(Villager.Type.JUNGLE, Material.JUNGLE_LOG),
    SWAMP(Villager.Type.SWAMP, Material.MUD);

    private final Material icon;
    private final Villager.Type type;

    TraderBiome(Villager.Type type, Material icon) {
        this.type = type;
        this.icon = icon;
    }

    public Villager.Type getType() {
        return type;
    }

    public ItemStack getIcon() {
        return new ItemStack(icon);
    }
}
