package io.github.leralix.exotictrades.traders;

import io.github.leralix.exotictrades.ExoticTrades;
import io.github.leralix.exotictrades.storage.TraderStorage;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class DailyTasks {

    private final TraderStorage traderStorage;
    private final int traderUpdatePositionHour;
    private final int traderUpdatePositionMinute;

    public DailyTasks(
            TraderStorage traderStorage,
            int traderUpdatePositionHour,
            int traderUpdatePositionMinute
    ) {
        this.traderStorage = traderStorage;
        this.traderUpdatePositionHour = traderUpdatePositionHour;
        this.traderUpdatePositionMinute = traderUpdatePositionMinute;
    }

    public void scheduleTasks() {
        new BukkitRunnable() {
            @Override
            public void run() {
                Calendar calendar = new GregorianCalendar();

                if (calendar.get(Calendar.HOUR_OF_DAY) == traderUpdatePositionHour && calendar.get(Calendar.MINUTE) == traderUpdatePositionMinute) {
                    update();
                }
            }
        }.runTaskTimer(ExoticTrades.getPlugin(), 0L, 1200L); // Exécute toutes les 1200 ticks (1 minute en temps Minecraft)
    }

    public void update() {
        for(Trader trader : traderStorage.getAll()){
            trader.nextDay();
        }
    }
}
