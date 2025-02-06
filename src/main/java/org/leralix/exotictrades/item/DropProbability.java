package org.leralix.exotictrades.item;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.leralix.exotictrades.storage.MarketItemStorage;
import org.leralix.lib.utils.RandomUtil;

public class DropProbability implements LootProbability {

    private final double baseChance;
    private final double fortuneModifier;
    private final boolean allowSilkTouch;
    private final int rareItemID;

    public DropProbability(double baseChance, double fortuneModifier, boolean allowSilkTouch, int rareItemID) {
        this.baseChance = baseChance;
        this.fortuneModifier = fortuneModifier;
        this.allowSilkTouch = allowSilkTouch;
        this.rareItemID = rareItemID;
    }

    public RareItem shouldDrop(ItemStack itemUsed){
        if(!allowSilkTouch && itemUsed.containsEnchantment(Enchantment.SILK_TOUCH)){
            return null;
        }

        int fortuneLevel = itemUsed.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);
        double chanceCap = baseChance + (fortuneModifier * fortuneLevel);
        int chanceRolled = RandomUtil.getRandom().nextInt() * 100;

        if(chanceRolled <= chanceCap){
            return MarketItemStorage.getRareItem(rareItemID);
        }
        return null;
    }
}
