package io.github.leralix.exotictrades.traders;

import org.bukkit.scheduler.BukkitRunnable;
import io.github.leralix.exotictrades.ExoticTrades;
import io.github.leralix.exotictrades.storage.TraderStorage;
import org.leralix.lib.utils.config.ConfigTag;
import org.leralix.lib.utils.config.ConfigUtil;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class DailyTasks {

    private final TraderStorage traderStorage;

    public DailyTasks(TraderStorage traderStorage) {
        this.traderStorage = traderStorage;
    }

    public void scheduleTasks() {
        new BukkitRunnable() {
            @Override
            public void run() {
                Calendar calendar = new GregorianCalendar();

                int hour = ConfigUtil.getCustomConfig(ConfigTag.MAIN).getInt("traderUpdatePositionHour",0);
                int minute = ConfigUtil.getCustomConfig(ConfigTag.MAIN).getInt("traderUpdatePositionMinute",0);

                if (calendar.get(Calendar.HOUR_OF_DAY) == hour && calendar.get(Calendar.MINUTE) == minute) {
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
