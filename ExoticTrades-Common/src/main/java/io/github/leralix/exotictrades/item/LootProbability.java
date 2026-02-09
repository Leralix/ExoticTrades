package io.github.leralix.exotictrades.item;

import org.bukkit.inventory.ItemStack;

public interface LootProbability {
    Integer shouldDrop(ItemStack itemUsed);
}
