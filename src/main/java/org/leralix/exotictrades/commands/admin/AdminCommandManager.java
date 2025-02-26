package org.leralix.exotictrades.commands.admin;

import org.leralix.lib.commands.CommandManager;
import org.leralix.lib.commands.MainHelpCommand;

public class AdminCommandManager extends CommandManager {

    public AdminCommandManager(){
        super("exotictrades.admin.commands");
        addSubCommand(new GetRareItem());
        addSubCommand(new OpenGui());
        addSubCommand(new SkipOneHour());
        addSubCommand(new UpdateTradersPosition());
        addSubCommand(new MainHelpCommand(this));
    }

    @Override
    public String getName() {
        return "extrade";
    }


}
