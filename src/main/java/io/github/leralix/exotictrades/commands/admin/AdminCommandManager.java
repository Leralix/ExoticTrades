package io.github.leralix.exotictrades.commands.admin;

import io.github.leralix.exotictrades.storage.MarketItemStorage;
import io.github.leralix.exotictrades.traders.DailyTasks;
import io.github.leralix.exotictrades.traders.HourlyTasks;
import org.leralix.lib.commands.CommandManager;
import org.leralix.lib.commands.MainHelpCommand;

public class AdminCommandManager extends CommandManager {

    public AdminCommandManager(
            HourlyTasks hourlyTasks,
            DailyTasks dailyTasks,
            MarketItemStorage marketItemStorage
    ){
        super("exotictrades.admin.commands");
        addSubCommand(new GetRareItem(marketItemStorage));
        addSubCommand(new DropRareItem(marketItemStorage));
        addSubCommand(new OpenGui());
        addSubCommand(new NextDay(dailyTasks));
        addSubCommand(new NextHour(hourlyTasks));
        addSubCommand(new UpdateTradersPosition(dailyTasks));
        addSubCommand(new MainHelpCommand(this));
    }

    @Override
    public String getName() {
        return "extrade";
    }


}
