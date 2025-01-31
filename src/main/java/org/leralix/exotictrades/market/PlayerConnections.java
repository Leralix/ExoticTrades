package org.leralix.exotictrades.market;

import org.bukkit.entity.Player;

import java.util.UUID;

public class PlayerConnections {

    private final UUID playerID;
    private long connectionTime;

    public PlayerConnections(UUID playerID){
        this.playerID = playerID;
        this.connectionTime = System.currentTimeMillis();
    }

    public boolean isPlayer(Player player){
        return player.getUniqueId().equals(playerID);
    }

    public long getConnectionTime(){
        return connectionTime;
    }

    public void resetConnectionTime(){
        this.connectionTime = System.currentTimeMillis();
    }


}
