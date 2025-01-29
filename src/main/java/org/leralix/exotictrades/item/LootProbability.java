package org.leralix.exotictrades.item;

import org.bukkit.inventory.ItemStack;

public interface LootProbability {

    public RareItem shouldDrop(ItemStack itemUsed);
}
