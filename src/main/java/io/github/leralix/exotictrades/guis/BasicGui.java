package io.github.leralix.exotictrades.guis;

import dev.triumphteam.gui.guis.Gui;
import org.bukkit.entity.Player;

public abstract class BasicGui {

    protected final Gui gui;
    protected final Player player;

    protected BasicGui(Player player, String title, int rows){
        gui = GuiUtil.createChestGui(title, rows);
        this.player = player;
    }

    public void open(){
        gui.open(player);
    }
}
