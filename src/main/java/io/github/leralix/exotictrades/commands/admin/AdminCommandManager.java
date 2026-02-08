package io.github.leralix.exotictrades.commands.admin;

import org.leralix.lib.commands.CommandManager;
import org.leralix.lib.commands.MainHelpCommand;

public class AdminCommandManager extends CommandManager {

    public AdminCommandManager(){
        super("exotictrades.admin.commands");
        addSubCommand(new GetRareItem());
        addSubCommand(new DropRareItem());
        addSubCommand(new OpenGui());
        addSubCommand(new NextDay());
        addSubCommand(new NextHour());
        addSubCommand(new UpdateTradersPosition());
        addSubCommand(new MainHelpCommand(this));
    }

    @Override
    public String getName() {
        return "extrade";
    }


}
