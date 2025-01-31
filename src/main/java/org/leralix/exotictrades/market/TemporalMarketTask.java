package org.leralix.exotictrades.market;

import org.bukkit.scheduler.BukkitRunnable;
import org.leralix.exotictrades.ExoticTrades;


public class TemporalMarketTask {

    public static void scheduleMovingAverageTask(){
        new BukkitRunnable() {
            @Override
            public void run() {
                StockMarketManager.updateMovingAverage();
            }
        }.runTaskTimer(ExoticTrades.getPlugin(), 0L, 1200L * 60); // Exécute toutes heures
    }
}
