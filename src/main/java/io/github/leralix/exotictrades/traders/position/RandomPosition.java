package io.github.leralix.exotictrades.traders.position;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import io.github.leralix.exotictrades.guis.ManageTraderPosition;
import io.github.leralix.exotictrades.guis.OpenBlockAllowedToSpawnTrader;
import io.github.leralix.exotictrades.lang.Lang;
import io.github.leralix.exotictrades.listener.chat.PlayerChatListenerStorage;
import io.github.leralix.exotictrades.listener.chat.events.RegisterZoneListener;
import io.github.leralix.exotictrades.traders.Trader;
import org.leralix.lib.position.Vector3D;
import org.leralix.lib.position.Zone2D;
import org.leralix.lib.utils.RandomUtil;
import org.leralix.lib.utils.HeadUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class RandomPosition implements TraderPosition {

    private Zone2D zone;

    private final List<Material> blocksAllowed;

    private boolean allowUnderwater;

    RandomPosition() {
        this.zone = null;
        this.blocksAllowed = new ArrayList<>();
        this.allowUnderwater = false;
    }

    public void setZone(Zone2D zone) {
        this.zone = zone;
    }

    public void removeAllowedBlock(Material block){
        blocksAllowed.remove(block);
    }


    public Vector3D getValidRandomPosition(){

        if(zone == null){
            return null;
        }

        for(int i = 0; i < 100; i++){
            int x1 = zone.getMin().getX();
            int x2 = zone.getMax().getX();
            int xMin = Math.min(x1, x2);
            int deltaX = Math.abs(x1 - x2);

            int z1 = zone.getMin().getZ();
            int z2 = zone.getMax().getZ();
            int zMin = Math.min(z1, z2);
            int deltaZ = Math.abs(z1 - z2);


            int randomX = (xMin + RandomUtil.getRandom().nextInt(deltaX));
            int randomZ = (zMin + RandomUtil.getRandom().nextInt(deltaZ));

            World world = zone.getMin().getWorld();

            if(world == null){
                continue;
            }

            Block highestBlock = world.getHighestBlockAt(randomX, randomZ);

            if(!blocksAllowed.contains(highestBlock.getType())){
                continue;
            }

            if(highestBlock.getLocation().add(0,1,0).getBlock().getType() != Material.WATER && allowUnderwater){
                continue;
            }

            return new Vector3D(randomX, highestBlock.getY() + 1, randomZ, world.getUID().toString());
        }
        return null;
    }

    public boolean isWatterAllowed() {
        return allowUnderwater;
    }

    public void switchWaterAllowed() {
        allowUnderwater = !allowUnderwater;
    }

    public void setAuthorizedBlocks(List<Material> authorizedBlocks) {
        blocksAllowed.clear();
        blocksAllowed.addAll(authorizedBlocks);
    }

    public Collection<Material> getAuthorizedBlocks() {
        return Collections.unmodifiableList(blocksAllowed);
    }

    public boolean isSpawnRandom(){
        return zone != null;
    }

    public Zone2D getZone() {
        return zone;
    }

    @Override
    public Vector3D getPositionNextHour() {
        return null;
    }

    @Override
    public String getSpawnInfo() {
        return Lang.TRADER_RANDOM_POSITION.get();
    }

    @Override
    public void addGuiItems(Gui gui, ManageTraderPosition manageTraderPosition, Player player, Trader trader, int page) {
        ItemStack spawnZoneItem = HeadUtils.createCustomItemStack(Material.GRASS_BLOCK, "Spawn Zone", "Click to manage");
        gui.setItem(2, 2, ItemBuilder.from(spawnZoneItem).asGuiItem(event -> PlayerChatListenerStorage.register(player, new RegisterZoneListener(trader, this))));

        ItemStack blockAllowedItem = HeadUtils.createCustomItemStack(Material.GRASS_BLOCK, "Block Allowed", "Click to choose");
        gui.setItem(2, 4, ItemBuilder.from(blockAllowedItem).asGuiItem(event -> new OpenBlockAllowedToSpawnTrader(player, trader, this).open()));



        Material allowWaterMaterial = isWatterAllowed() ? Material.WATER_BUCKET : Material.BUCKET;
        ItemStack allowWaterItem = HeadUtils.createCustomItemStack(allowWaterMaterial, "Allow Water", "Click to toggle");
        gui.setItem(2, 6, ItemBuilder.from(allowWaterItem).asGuiItem(event -> {
            switchWaterAllowed();
            manageTraderPosition.reload();
        }));
    }
}
