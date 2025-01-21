package org.leralix.exotictrades.commands.admin;

import org.leralix.lib.commands.CommandManager;
import org.leralix.lib.commands.MainHelpCommand;

public class AdminCommandManager extends CommandManager {

    public AdminCommandManager(){
        addSubCommand(new GetRareItem());
        addSubCommand(new OpenGui());

        addSubCommand(new MainHelpCommand(this));
    }

    @Override
    public String getName() {
        return "exotictradeadmin";
    }


}
