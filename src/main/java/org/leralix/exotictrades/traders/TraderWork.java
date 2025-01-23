package org.leralix.exotictrades.traders;

import org.bukkit.Material;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;

public enum TraderWork {

    NONE(Villager.Profession.NONE, Material.BARRIER),
    FARMER(Villager.Profession.FARMER, Material.WHEAT),
    LIBRARIAN(Villager.Profession.LIBRARIAN, Material.BOOK),
    BUTCHER(Villager.Profession.BUTCHER, Material.COOKED_BEEF),
    NITWIT(Villager.Profession.NITWIT, Material.VILLAGER_SPAWN_EGG),
    ARMORER(Villager.Profession.ARMORER, Material.IRON_CHESTPLATE),
    CARTOGRAPHER(Villager.Profession.CARTOGRAPHER, Material.MAP),
    CLERIC(Villager.Profession.CLERIC, Material.ENCHANTED_BOOK),
    FISHERMAN(Villager.Profession.FISHERMAN, Material.FISHING_ROD),
    FLETCHER(Villager.Profession.FLETCHER, Material.ARROW),
    LEATHERWORKER(Villager.Profession.LEATHERWORKER, Material.LEATHER),
    MASON(Villager.Profession.MASON, Material.BRICK),
    SHEPHERD(Villager.Profession.SHEPHERD, Material.SHEARS),
    TOOLSMITH(Villager.Profession.TOOLSMITH, Material.IRON_PICKAXE),
    WEAPONSMITH(Villager.Profession.WEAPONSMITH, Material.IRON_SWORD);

    private final Villager.Profession profession;
    private final Material icon;

    TraderWork(Villager.Profession profession, Material icon) {
        this.profession = profession;
        this.icon = icon;
    }

    public Villager.Profession getProfession(){
        return profession;
    }

    public ItemStack getIcon() {
        return new ItemStack(icon);
    }
}
