package io.github.leralix.exotictrades.traders;

import io.github.leralix.exotictrades.ExoticTrades;
import io.github.leralix.exotictrades.market.StockMarketManager;
import io.github.leralix.exotictrades.storage.TraderStorage;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class HourlyTasks {

    private final TraderStorage traderStorage;

    private final StockMarketManager stockMarketManager;

    private final int traderUpdatePositionMinute;



    public HourlyTasks(
            TraderStorage traderStorage,
            StockMarketManager stockMarketManager,
            int traderUpdatePositionMinute

    ) {
        this.traderStorage = traderStorage;
        this.stockMarketManager = stockMarketManager;
        this.traderUpdatePositionMinute = traderUpdatePositionMinute;
    }

    public void scheduleTasks() {
        new BukkitRunnable() {
            @Override
            public void run() {
                Calendar calendar = new GregorianCalendar();

                if (calendar.get(Calendar.MINUTE) == traderUpdatePositionMinute) {
                    update();
                }
            }
        }.runTaskTimer(ExoticTrades.getPlugin(), 0L, 1200L);
    }

    public void update() {
        for(Trader trader : traderStorage.getAll()){
            trader.nextHour();
        }
        traderStorage.updateTraderPosition();
        stockMarketManager.updateMovingAverage();
        traderStorage.save();
    }
}
