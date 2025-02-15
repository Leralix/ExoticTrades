package org.leralix.exotictrades.item;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.leralix.exotictrades.storage.MarketItemStorage;
import org.leralix.lib.utils.RandomUtil;

public class FishProbability implements LootProbability {

    private final double dropChance;
    private final double luckOfTheSeaBonus;
    private final int rareItemID;

    public FishProbability(double baseChance,double luckOfTheSeaBonus, int id) {
        this.dropChance = baseChance;
        this.luckOfTheSeaBonus = luckOfTheSeaBonus;
        this.rareItemID = id;
    }

    @Override
    public RareItem shouldDrop(ItemStack itemUsed) {
        int randInt = RandomUtil.getRandom().nextInt(1, 100);

        if(itemUsed == null || !itemUsed.hasItemMeta()){
            if(randInt <= dropChance){
                return MarketItemStorage.getRareItem(rareItemID);
            }
            return null;
        }

        int lootingLevel = itemUsed.getItemMeta().getEnchantLevel(Enchantment.LUCK);
        double chanceCap = dropChance + (luckOfTheSeaBonus * lootingLevel);

        if(randInt <= chanceCap){
            return MarketItemStorage.getRareItem(rareItemID);
        }
        return null;
    }
}
