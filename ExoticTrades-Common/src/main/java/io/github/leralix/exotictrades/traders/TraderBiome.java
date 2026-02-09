package io.github.leralix.exotictrades.traders;

import org.bukkit.Material;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import io.github.leralix.exotictrades.lang.Lang;

import java.util.Collections;

public enum TraderBiome {

    PLAINS(Villager.Type.PLAINS, Material.GRASS_BLOCK, Lang.PLAINS),
    DESERT(Villager.Type.DESERT, Material.SAND, Lang.DESERT),
    SAVANNA(Villager.Type.SAVANNA, Material.ACACIA_LOG, Lang.SAVANNA),
    SNOW(Villager.Type.SNOW, Material.SNOW_BLOCK, Lang.SNOW),
    TAIGA(Villager.Type.TAIGA, Material.SPRUCE_LOG, Lang.TAIGA),
    JUNGLE(Villager.Type.JUNGLE, Material.JUNGLE_LOG, Lang.JUNGLE),
    SWAMP(Villager.Type.SWAMP, Material.MUD, Lang.SWAMP),;

    private final Material icon;
    private final Villager.Type type;
    private final Lang name;

    TraderBiome(Villager.Type type, Material icon, Lang name) {
        this.type = type;
        this.icon = icon;
        this.name = name;
    }

    public Villager.Type getType() {
        return type;
    }

    public ItemStack getIcon(String title, Lang desc) {
        ItemStack item = new ItemStack(icon);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(title);
        meta.setLore(Collections.singletonList(desc.get()));
        item.setItemMeta(meta);
        return item;
    }

    public String getName() {
        return name.get();
    }
}
