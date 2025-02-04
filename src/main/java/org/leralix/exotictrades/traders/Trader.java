package org.leralix.exotictrades.traders;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.leralix.exotictrades.ExoticTrades;
import org.leralix.exotictrades.storage.TraderStorage;
import org.leralix.exotictrades.storage.VillagerHeadStorage;
import org.leralix.lib.position.Vector2D;
import org.leralix.lib.position.Vector3D;
import org.leralix.lib.position.Vector3DWithOrientation;
import org.leralix.lib.utils.HeadUtils;

import java.util.Optional;

public class Trader {

    private final String id;
    private String name;
    private Vector3D position;
    private SpawnZone randomSpawnZone;
    private TraderBiome biomeType;
    private TraderWork workType;


    public Trader(String id, Location position){
        this.id = id;
        this.position = new Vector3DWithOrientation(position);
        this.biomeType = TraderBiome.PLAINS;
        this.workType = TraderWork.FARMER;
        this.randomSpawnZone = new SpawnZone();
        spawnTrader();
    }

    public void setName(String newName){
        this.name = newName;
        updateTrader();
    }

    public String getName() {
        return name;
    }

    public void updateTrader() {

        new BukkitRunnable() {
            @Override
            public void run() {
                Optional<Villager> optionalEntity = getVillager();
                if (optionalEntity.isEmpty()) {
                    spawnTrader();
                    return;
                }
                updateTrader(optionalEntity.get());
            }
        }.runTask(ExoticTrades.getPlugin());
    }

    public void setBiomeType(TraderBiome biomeType) {
        this.biomeType = biomeType;
        updateTrader();
    }
    public void setWorkType(TraderWork workType) {
        this.workType = workType;
        updateTrader();
    }

    public void updateTrader(Villager villager){
        villager.setVillagerType(biomeType.getType());
        villager.setProfession(workType.getProfession());
        if(name != null){
            villager.setCustomName(name);
        }
        villager.setInvulnerable(true);
        villager.setPersistent(false);
        villager.setCollidable(false);
        villager.setVillagerLevel(5);
        villager.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, Integer.MAX_VALUE, false, false));
    }



    private Optional<Villager> getVillager() {
        for(Villager villager : Bukkit.getWorld("world").getEntitiesByClass(Villager.class)){
            if (villager.getScoreboardTags().contains("exoticTrade_" + id)){
                return Optional.of(villager);
            }
        }
        return Optional.empty();
    }

    public void spawnTrader(){
        Villager villager = position.getLocation().getWorld().spawn(position.getLocation(), Villager.class);
        villager.addScoreboardTag("exoticTrade_" + id);
        updateTrader(villager);
    }

    public ItemStack getIcon(){
        String url = VillagerHeadStorage.getURL(biomeType, workType);
        return HeadUtils.makeSkullURL(id, url);
    }

    public String getID() {
        return id;
    }

    public Location getLocation() {
        return position.getLocation();
    }

    public Vector2D getChunkPosition() {
        int x = position.getLocation().getBlockX() >> 4;
        int z = position.getLocation().getBlockZ() >> 4;
        return new Vector2D(x, z, position.getWorldID().toString());
    }

    public TraderBiome getBiomeType() {
        return biomeType;
    }

    public TraderWork getWorkType() {
        return workType;
    }

    public void delete() {
       getVillager().ifPresent(Entity::remove);
       TraderStorage.delete(this);
    }


    public void updatePosition() {
        Vector3D vector3D = randomSpawnZone.getValidRandomPosition();
        if(vector3D == null){
            return;
        }
        position = vector3D;

        getVillager().ifPresentOrElse(
                villager -> villager.teleport(position.getLocation()),
                this::spawnTrader);
    }

    public boolean isStatic() {
        return randomSpawnZone == null;
    }

    public SpawnZone getRandomSpawnZone() {
        return randomSpawnZone;
    }
}
