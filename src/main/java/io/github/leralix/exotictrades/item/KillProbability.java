package io.github.leralix.exotictrades.item;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import io.github.leralix.exotictrades.storage.MarketItemStorage;
import org.leralix.lib.utils.RandomUtil;

public class KillProbability implements LootProbability {

    private final double dropChance;
    private final double fortuneModifier;
    private final int rareItemID;


    public KillProbability(double dropChance, double fortuneModifier, int rareItemID) {
        this.dropChance = dropChance;
        this.fortuneModifier = fortuneModifier;
        this.rareItemID = rareItemID;
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

        int lootingLevel = itemUsed.getItemMeta().getEnchantLevel(Enchantment.LOOT_BONUS_MOBS);
        double chanceCap = dropChance + (fortuneModifier * lootingLevel);

        if(randInt <= chanceCap){
            return MarketItemStorage.getRareItem(rareItemID);
        }
        return null;
    }
}
