package io.github.leralix.exotictrades.listener;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import io.github.leralix.exotictrades.item.RareItem;
import io.github.leralix.exotictrades.storage.MarketItemStorage;

import java.util.List;

public class RareItemDrops implements Listener {

    private final MarketItemStorage marketItemStorage;

    public RareItemDrops(MarketItemStorage marketItemStorage){
        this.marketItemStorage = marketItemStorage;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBreakBlock(BlockBreakEvent event){

        if(event.isCancelled()){
            return;
        }

        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if(player.getGameMode() != GameMode.SURVIVAL)
            return;

        Block block = event.getBlock();
        Material type = block.getType();

        if(type == Material.WHEAT || type == Material.BEETROOTS || type == Material.POTATOES || type == Material.CARROTS) { //TODO : Remove if check to take account all ageable blocks
            BlockData data = block.getBlockData();
            if(data instanceof Ageable ageable && ageable.getAge() < ageable.getMaximumAge()) {
                return;
            }
        }


        List<RareItem> rareItems = marketItemStorage.getRareItemsDropped(type,item);
        for(RareItem rareItem : rareItems){
            block.getWorld().dropItem(block.getLocation(), rareItem.getItemStack(1));
        }

    }
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onKillingMobs(EntityDeathEvent event){

        Player killer = event.getEntity().getKiller();

        if(killer == null)
            return;

        EntityType type = event.getEntityType();
        ItemStack item = killer.getInventory().getItem(killer.getInventory().getHeldItemSlot());
        List<RareItem> rareItems = marketItemStorage.getRareItemsDropped(type,item);
        for(RareItem rareItem : rareItems){
            event.getEntity().getWorld().dropItem(event.getEntity().getLocation(), rareItem.getItemStack(1));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onFishing(PlayerFishEvent event){

        if(event.isCancelled()){
            return;
        }

        Player player = event.getPlayer();

        if(player.getGameMode() != GameMode.SURVIVAL)
            return;
        if(!event.getState().equals(PlayerFishEvent.State.CAUGHT_FISH))
            return;
        Entity caughtEntity = event.getCaught();
        if(caughtEntity == null)
            return;
        if(caughtEntity instanceof LivingEntity)
            return;
        Item fish = (Item)event.getCaught();

        List<RareItem> rareItems = marketItemStorage.getRareItemFished(fish.getItemStack().getType(),event);

        for(RareItem rareItem : rareItems){

            Item rareFish = event.getCaught().getWorld().dropItem(event.getCaught().getLocation(), rareItem.getItemStack(1));

            Vector playerPos = player.getLocation().toVector();
            Vector caughtItemPos = caughtEntity.getLocation().toVector();

            Vector velocity = playerPos.subtract(caughtItemPos).multiply(0.1);
            velocity.add(new Vector(0, 0.15, 0));

            rareFish.setVelocity(velocity);
        }
    }
}
