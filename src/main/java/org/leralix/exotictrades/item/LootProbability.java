package org.leralix.exotictrades.item;

import org.bukkit.inventory.ItemStack;

public interface LootProbability {
    RareItem shouldDrop(ItemStack itemUsed);
}
