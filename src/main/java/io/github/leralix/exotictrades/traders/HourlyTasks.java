package io.github.leralix.exotictrades.traders;

import org.bukkit.scheduler.BukkitRunnable;
import io.github.leralix.exotictrades.ExoticTrades;
import io.github.leralix.exotictrades.market.StockMarketManager;
import io.github.leralix.exotictrades.storage.TraderStorage;
import org.leralix.lib.utils.config.ConfigTag;
import org.leralix.lib.utils.config.ConfigUtil;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class HourlyTasks {

    private HourlyTasks() {

    }

    public static void scheduleTasks() {
        new BukkitRunnable() {
            @Override
            public void run() {
                Calendar calendar = new GregorianCalendar();

                int minute = ConfigUtil.getCustomConfig(ConfigTag.MAIN).getInt("traderUpdatePositionMinute",0);

                if (calendar.get(Calendar.MINUTE) == minute) {
                    update();
                }
            }
        }.runTaskTimer(ExoticTrades.getPlugin(), 0L, 1200L);
    }

    public static void update() {
        for(Trader trader : TraderStorage.getAll()){
            trader.nextHour();
        }
        TraderStorage.updateTraderPosition();
        StockMarketManager.updateMovingAverage();
    }
}
