package io.github.leralix.exotictrades.listener;

import org.bukkit.Chunk;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import io.github.leralix.exotictrades.storage.TraderStorage;
import io.github.leralix.exotictrades.traders.Trader;

import java.util.Arrays;
import java.util.List;

public class SpawnTraders implements Listener {

    private final TraderStorage traderStorage;

    public SpawnTraders(TraderStorage traderStorage){
        this.traderStorage = traderStorage;
    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
        Chunk chunk = event.getChunk();

        List<Trader> traderInChunk = traderStorage.getTradersInChunk(chunk);

        for(Trader trader : traderInChunk){
            String traderID = trader.getID();
            boolean isAlreadySpawned = Arrays.stream(chunk.getEntities()).anyMatch(entity -> entity.getScoreboardTags().contains("exoticTrade_" + traderID));

            if(!isAlreadySpawned){
                trader.spawnTrader();
            }
        }
    }

}