package org.leralix.exotictrades.traders;

import org.bukkit.entity.Villager;
import org.leralix.exotictrades.storage.TraderStorage;
import org.leralix.lib.data.position.Vector3D;

public class Trader {

    private final String id;
    private String name;
    private final Vector3D position;
    private Villager.Type type;
    private Villager entity;


    public Trader(String id, Vector3D position){
        this.id = id;
        this.position = position;
        this.entity = position.getLocation().getWorld().spawn(position.getLocation(), Villager.class);


        updateTrader();
    }

    public void setName(String newName){
        this.name = newName;
        updateTrader();
    }

    public void updateTrader(){
        entity.setVillagerType(type);
        if(name != null){
            entity.setCustomName(name);
            this.entity.setCustomNameVisible(true);
        }
        this.entity.setAI(false);
        this.entity.setInvulnerable(true);
    }

    public void spawnTrader(){
        entity = position.getLocation().getWorld().spawn(position.getLocation(), Villager.class);
        updateTrader();
    }
    public void removeTrader(){
        entity.remove();
    }


    public String getID() {
        return id;
    }
}
