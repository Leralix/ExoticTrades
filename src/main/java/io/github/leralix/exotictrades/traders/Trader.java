package io.github.leralix.exotictrades.traders;

import io.github.leralix.exotictrades.util.HeadUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import io.github.leralix.exotictrades.ExoticTrades;
import io.github.leralix.exotictrades.item.MarketItem;
import io.github.leralix.exotictrades.item.SellableItem;
import io.github.leralix.exotictrades.lang.Lang;
import io.github.leralix.exotictrades.storage.MarketItemKey;
import io.github.leralix.exotictrades.storage.MarketItemStorage;
import io.github.leralix.exotictrades.storage.TraderStorage;
import io.github.leralix.exotictrades.storage.VillagerHeadStorage;
import io.github.leralix.exotictrades.traders.position.FixedPosition;
import org.leralix.lib.position.Vector2D;
import org.leralix.lib.position.Vector3D;
import org.leralix.lib.position.Vector3DWithOrientation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Trader {

    private final String id;
    private String name;
    private Vector3D position;
    private final FixedPosition positionHandler;
    private TraderBiome biomeType;
    private TraderWork workType;
    private final List<MarketItemKey> acceptedMarketItem;
    private SellableItemManager sellableItemManager;



    public Trader(String id, Location position){
        this.id = id;
        this.position = new Vector3DWithOrientation(position);
        this.name = Lang.TRADER_BASE_NAME.get();
        this.biomeType = TraderBiome.PLAINS;
        this.workType = TraderWork.FARMER;
        this.positionHandler = new FixedPosition(this.position);
        this.acceptedMarketItem = new ArrayList<>(MarketItemStorage.getAllMarketItemsKey());
        this.sellableItemManager = new SellableItemManager(2);
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
        villager.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.0);
    }



    private Optional<Villager> getVillager() {

        World world = Bukkit.getWorld(getPosition().getCurrentPosition().getWorld().getName());
        if(world == null){
            return Optional.empty();
        }

        for(Villager villager : world.getEntitiesByClass(Villager.class)){
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
        Vector3D vector3D = positionHandler.getPositionNextHour();
        if(vector3D == null){
            return;
        }
        position = vector3D;

        getVillager().ifPresentOrElse(
                villager -> villager.teleport(position.getLocation()),
                this::spawnTrader);
    }

    public boolean isStatic() {
        return positionHandler == null;
    }

    public List<MarketItem> getMarketItems() {
        List<MarketItem> result = new ArrayList<>();
        for(MarketItemKey key : acceptedMarketItem){
            result.add(MarketItemStorage.getMarketItem(key));
        }
        result.remove(null);
        return result;
    }

    public void addMarketItem(MarketItem marketItem){
        acceptedMarketItem.add(MarketItemKey.of(marketItem));
    }

    public void removeMarketItem(MarketItem marketItem){
        acceptedMarketItem.remove(MarketItemKey.of(marketItem));
    }

    public boolean canTradeMarketItem(MarketItem marketItem){
        return acceptedMarketItem.contains(MarketItemKey.of(marketItem));
    }

    public FixedPosition getPosition() {
        return positionHandler;
    }

    private SellableItemManager getItemManager() {
        if(sellableItemManager == null){
            sellableItemManager = new SellableItemManager(2);
        }
        return sellableItemManager;
    }

    public List<SellableItem> getItemSold() {
        return getItemManager().getAllSellableItems();
    }

    public List<SellableItem> getTodaySellableItems() {
        return getItemManager().getTodaySellableItems();
    }

    public void setNumberOfDailySellableItems(int number){
        getItemManager().setNbDailySellableItems(number);
    }

    public void nextHour() {
        updatePosition();
    }

    public void nextDay() {
        getItemManager().updateSellableItems();
    }

    public void removeTodaySellableItem(SellableItem item) {
        getItemManager().removeTodaySellableItem(item);
    }

    public void removeSellableItem(SellableItem sellableItem) {
        getItemManager().removeSellableItem(sellableItem);
    }

    public void addSellableItem(SellableItem sellableItem) {
        getItemManager().addSellableItem(sellableItem);
    }
}
