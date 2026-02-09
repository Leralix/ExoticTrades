package io.github.leralix.exotictrades.commands.admin;

import io.github.leralix.exotictrades.market.StockMarketManager;
import io.github.leralix.exotictrades.storage.MarketItemStorage;
import io.github.leralix.exotictrades.storage.TraderStorage;
import io.github.leralix.exotictrades.traders.DailyTasks;
import io.github.leralix.exotictrades.traders.HourlyTasks;
import org.leralix.lib.commands.CommandManager;
import org.leralix.lib.commands.MainHelpCommand;

public class AdminCommandManager extends CommandManager {

    public AdminCommandManager(
            TraderStorage traderStorage,
            MarketItemStorage marketItemStorage,
            StockMarketManager stockMarketManager,
            HourlyTasks hourlyTasks,
            DailyTasks dailyTasks
    ){
        super("exotictrades.admin.commands");
        addSubCommand(new GetRareItem(marketItemStorage));
        addSubCommand(new DropRareItem(marketItemStorage));
        addSubCommand(new OpenGui(traderStorage, marketItemStorage, stockMarketManager));
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
