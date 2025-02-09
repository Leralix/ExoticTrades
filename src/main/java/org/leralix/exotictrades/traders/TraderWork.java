package org.leralix.exotictrades.traders;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.leralix.exotictrades.lang.Lang;

import java.util.Collections;

public enum TraderWork {

    NONE(Villager.Profession.NONE, Material.BARRIER, Lang.NONE),
    FARMER(Villager.Profession.FARMER, Material.WHEAT, Lang.FARMER),
    LIBRARIAN(Villager.Profession.LIBRARIAN, Material.BOOK, Lang.LIBRARIAN),
    BUTCHER(Villager.Profession.BUTCHER, Material.COOKED_BEEF, Lang.BUTCHER),
    NITWIT(Villager.Profession.NITWIT, Material.VILLAGER_SPAWN_EGG, Lang.NITWIT),
    ARMORER(Villager.Profession.ARMORER, Material.IRON_CHESTPLATE, Lang.ARMORER),
    CARTOGRAPHER(Villager.Profession.CARTOGRAPHER, Material.MAP, Lang.CARTOGRAPHER),
    CLERIC(Villager.Profession.CLERIC, Material.ENCHANTED_BOOK, Lang.CLERIC),
    FISHERMAN(Villager.Profession.FISHERMAN, Material.FISHING_ROD, Lang.FISHERMAN),
    FLETCHER(Villager.Profession.FLETCHER, Material.ARROW, Lang.FLETCHER),
    LEATHERWORKER(Villager.Profession.LEATHERWORKER, Material.LEATHER, Lang.LEATHERWORKER),
    MASON(Villager.Profession.MASON, Material.BRICK, Lang.MASON),
    SHEPHERD(Villager.Profession.SHEPHERD, Material.SHEARS, Lang.SHEPHERD),
    TOOLSMITH(Villager.Profession.TOOLSMITH, Material.IRON_PICKAXE, Lang.TOOLSMITH),
    WEAPONSMITH(Villager.Profession.WEAPONSMITH, Material.IRON_SWORD, Lang.WEAPONSMITH);

    private final Villager.Profession profession;
    private final Material icon;
    private final Lang lang;

    TraderWork(Villager.Profession profession, Material icon, Lang lang) {
        this.profession = profession;
        this.icon = icon;
        this.lang = lang;
    }

    public Villager.Profession getProfession(){
        return profession;
    }

    public ItemStack getIcon(String title, Lang desc){
        ItemStack item = new ItemStack(icon);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + title);
        meta.setLore(Collections.singletonList(desc.get()));
        item.setItemMeta(meta);
        return item;
    }

    public String getName() {
        return lang.get();
    }
}
