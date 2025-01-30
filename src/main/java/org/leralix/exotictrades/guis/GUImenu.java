package org.leralix.exotictrades.guis;

import dev.triumphteam.gui.guis.Gui;
import org.bukkit.entity.Player;

public abstract class GUImenu {

    protected final Gui gui;
    protected final Player player;

    protected GUImenu(Player player, String title, int rows){
        gui = GuiUtil.createChestGui(title, rows);
        this.player = player;
    }

    public void open(){
        gui.open(player);
    }
}
